package com.ennova.pubinfotask.service;

import com.alibaba.fastjson.JSONObject;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.MenuVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.dao.YsBulletinMapper;
import com.ennova.pubinfotask.dao.YsMessageMapper;
import com.ennova.pubinfotask.dto.PublishDTO;
import com.ennova.pubinfotask.entity.YsBulletin;
import com.ennova.pubinfotask.entity.YsFileType;
import com.ennova.pubinfotask.config.ChannelHandlerPool;
import com.ennova.pubinfotask.entity.YsMessage;
import com.ennova.pubinfotask.utils.BeanConvertUtils;
import com.ennova.pubinfotask.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsBulletinService {

    private final YsBulletinMapper ysBulletinMapper;
    private final HttpServletRequest req;
    private final RedisTemplate redisTemplate;
    private final UserMapper userMapper;
    private final YsMessageMapper ysMessageMapper;

    //审核人列表
    public Callback getReviewerList() {
        List<ReviewerVO> list = ysBulletinMapper.getReviewerList();
        return Callback.success(list);
    }

    // task_manage角色可新增
    public Callback insert(PublishDTO publishDTO) {

        log.info("publishDTO: {}", publishDTO);

        String token = req.getHeader("Authorization");
        log.info("token: {}", token);
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        log.info("userVo: {}", userVo);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if (!currentUserVO.getRoleCode().equals("task_manage")) {
            return Callback.error(2, "您没有权限新增");
        }

        YsBulletin ysBulletin = BeanConvertUtils.convertTo(publishDTO, YsBulletin::new);
        ysBulletin.setCreateTime(LocalDateTime.now());
        ysBulletin.setStatus(0);
        ysBulletin.setCreateId(userVo.getId());
        //ysBulletin.setIsDelete(0);
        List<YsBulletin> ysBulletins = ysBulletinMapper.selectByTitleAndContent(ysBulletin.getTitle(), ysBulletin.getContent());
        if (CollectionUtils.isNotEmpty(ysBulletins)) {
            return Callback.error(2, "已存在相同的公告标题和内容!");
        }
        int i = ysBulletinMapper.insert(ysBulletin); // 插入主表
        if (i > 0) {
            //推送:sourceType=0 公告,type=0 新增
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(0).content(ysBulletin).build();
            List<Channel> channelList = getChannelByName(String.valueOf(publishDTO.getCheckUserId()));
            if (channelList.size() <= 0) {
                redisTemplate.opsForList().rightPush("bulletin:add:" + publishDTO.getCheckUserId(), JSONObject.toJSONString(socketVO));
                log.info("用户: " + publishDTO.getCheckUserId()+" 没有登录，添加到redis队列");
                return Callback.success();
            } else {
                //channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame("您有一条编号为" + JSONObject.toJSONString(socketVO) + "的公告需要审批！")));
                channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO))));
            }
            return Callback.success(true);
        }
        return Callback.error(2, "新增失败");
    }

    // 任务管理员可修改，并判断是否存在数据；存在则修改
    public Callback update(PublishDTO publishDTO) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if (!currentUserVO.getRoleCode().equals("task_manage")) {
            return Callback.error(2, "您没有权限修改");
        }

        YsBulletin old = ysBulletinMapper.selectbyIdAndCreateId(publishDTO.getId(), userVo.getId());
        if (old == null) {
            return Callback.error(2, "ID为"+ publishDTO.getId() +"的公告不存在");
        }

        // 排除当前ID，不能修改成已存在的公告标题和内容
        List<YsBulletin> ysBulletins = ysBulletinMapper.selectByTitleAndContentAndIdNot(publishDTO.getTitle(), publishDTO.getContent(), publishDTO.getId());
        if (CollectionUtils.isNotEmpty(ysBulletins)) {
            return Callback.error(2, "已存在相同的公告标题和内容!");
        }

        // 只有待审核、驳回状态才能修改
        if (old.getStatus().equals(1)) {
            return Callback.error(2, "当前公告已审核，不能修改");
        }

        YsBulletin ysBulletin = BeanConvertUtils.convertTo(publishDTO, YsBulletin::new);
        ysBulletin.setStatus(0);
        ysBulletin.setUpdateTime(LocalDateTime.now());
        int i = ysBulletinMapper.updateByPrimaryKeySelective(ysBulletin); // 更新主表
        if (i > 0) {
            //推送:sourceType=0 公告,type=3 修改
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(3).content(ysBulletin).build();
            List<Channel> channelList = getChannelByName(String.valueOf(publishDTO.getCheckUserId()));
            if (channelList.size() <= 0) {
                redisTemplate.opsForList().rightPush("bulletin:add:" + publishDTO.getCheckUserId(), JSONObject.toJSONString(socketVO));
                log.info("用户: " + publishDTO.getCheckUserId()+" 没有登录，添加到redis队列");
                return Callback.success();
            } else {
                channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO))));
            }
            return Callback.success(true);
        }
        return Callback.error(2, "修改失败");

    }

    // 只有管理员可删除
    public Callback delete(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if(!currentUserVO.getRoleCode().equals("ADMIN")){
            return Callback.error(2, "您没有权限删除");
        }
        YsBulletin old = ysBulletinMapper.selectByPrimaryKey(id);
        if (old != null) {
            int i = ysBulletinMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

    // 查询当前用户创建和审核的公告列表
    public Callback<BaseVO<YsBulletinVO>> getBulletinList(Integer page, Integer pageSize, Integer status, String likeTitle) {

        //String token = req.getHeader("Authorization");
        //UserVO userVo = JWTUtil.getUserVOByToken(token);
        //CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());


        // 菜单不展示，可查看人员的角色：task_manage、admin、check_person
        Page<YsFileType> startPage = PageMethod.startPage(page, pageSize);
        List<YsBulletinVO> list = ysBulletinMapper.selectByStatusAndTitleLike(status, likeTitle,null,"id desc");
        //list.stream().forEach(ysBulletin -> {
        //    ysBulletin.setIsEdit(false);
        //    if (currentUserVO.getUserId().equals(ysBulletin.getCreateId())) {
        //        ysBulletin.setIsEdit(true);
        //    }
        //    ysBulletin.setIsDeleteable(false);
        //    if (currentUserVO.getRoleCode().equals("ADMIN")) {
        //        ysBulletin.setIsDeleteable(true);
        //    }
        //    ysBulletin.setIsCheckable(false);
        //    if (currentUserVO.getUserId().equals(ysBulletin.getCheckUserId())) {
        //        ysBulletin.setIsCheckable(true);
        //    }
        //});

        BaseVO<YsBulletinVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }


    //查询公告详情
    public Callback<YsBulletinVO> getBulletinDetail(Integer id) {
        YsBulletin ysBulletin = ysBulletinMapper.selectByPrimaryKey(id);
        YsBulletinVO ysBulletinVO = BeanConvertUtils.convertTo(ysBulletin, YsBulletinVO::new);
        ysBulletinVO.setCreateName(userMapper.selectById(ysBulletinVO.getCreateId()).getUserName());
        ysBulletinVO.setCheckName(userMapper.selectById(ysBulletinVO.getCheckUserId()).getUserName());
        return Callback.success(ysBulletinVO);
    }

    // 公告审核
    public Callback checkBulletin(Integer id, Integer status) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());

        List<String> roleCodes = Arrays.asList("task_manage", "check_person");
        if (!roleCodes.contains(currentUserVO.getRoleCode())) {
            return Callback.error(2, "您没有权限审核");
        }

        // 判断ID是不是数字
        if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
            return Callback.error(2, "ID不能为空");
        }

        YsBulletin ysBulletin = ysBulletinMapper.selectByPrimaryKey(id);
        if (ysBulletin == null) {
            return Callback.error(2, "当前ID为"+ id +"的公告不存在");
        }

        if (ysBulletin.getStatus() != 0) {
            return Callback.error(2, "当前公告已审核，不能重复审核");
        }

        ysBulletin.setStatus(status);
        ysBulletin.setUpdateTime(LocalDateTime.now());
        List<CurrentUserVO> currentUserVOS = userMapper.selectAllUser();
        List<Integer> users = currentUserVOS.stream().map(CurrentUserVO::getUserId).collect(Collectors.toList());

        //审核通过
        if (status == 1) {
            ysBulletin.setStatus(1);
            ysBulletin.setCheckTime(LocalDateTime.now());

            //TODO 审核通过
            //保存到数据库
            users.stream().forEach(user->{
                   YsMessage message = YsMessage.builder().sourceType(0).receiveId(user).ysBulletin(ysBulletin.getId()).status(false).createTime(LocalDateTime.now()).build();
                   ysMessageMapper.insert(message);
            });

            //推送:sourceType=0 公告,type=1 审核通过了
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(1).content(ysBulletin).build();
            if (users.size() > 0) {
              //推送消息给所有用户
               users.forEach(user ->{
                   List<Channel> channelList = getChannelByName(String.valueOf(user));
                   if (channelList.size() <= 0) {
                       redisTemplate.opsForList().rightPush("bulletin:push:" + user, JSONObject.toJSONString(socketVO));
                   } else {
                       channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO))));
                   }
               });
            }

        }
        //审核不通过
        if (status == 2) {
            ysBulletin.setStatus(2);
            ysBulletin.setUpdateTime(LocalDateTime.now());

            //TODO 审核不通过
            //推送:sourceType=0 公告,type=2 审核不通过
             SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(2).content(ysBulletin).build();
            List<Channel> channelList = getChannelByName(String.valueOf(ysBulletin.getCreateId()));
            if (channelList.size() <= 0) {
                redisTemplate.opsForList().rightPush("bulletin:reject:" + ysBulletin.getCreateId(), JSONObject.toJSONString(socketVO));
            } else {
                //channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame("您有一条编号为" + ysBulletin.getId() + "的公告已被驳回！")));
                channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO))));
            }
        }

        int i = ysBulletinMapper.updateByPrimaryKeySelective(ysBulletin); // 更新主表
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error(2, "审核失败");
    }


    // 查询用户
    public List<Channel> getChannelByName(String name) {
        AttributeKey<String> key = AttributeKey.valueOf("user");
        return ChannelHandlerPool.channelGroup.stream().filter(channel -> channel.attr(key).get().equals(name))
                .collect(Collectors.toList());
    }



    /** -----------------------------------------------------   消息  ------------------------------------------------------------------------------   **/

    public Callback<BaseVO<YsMessageVO>> getMessageList(Integer page, Integer pageSize, Boolean status, String likeTitle) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        Page<YsFileType> startPage = PageMethod.startPage(page, pageSize);
        List<YsMessageVO> list = ysMessageMapper.selectByStatusAndYsBulletinLike(status, likeTitle,userVo.getId());
        BaseVO<YsMessageVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

     // 只能修改自己的任务为已读状态
    public Callback updateMessage(Integer id) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        YsMessage ysMessage = ysMessageMapper.selectByIdAndReceiveId(id, userVo.getId());
        if (null != ysMessage){
            if (ysMessage.getStatus().equals(true)){
                return Callback.error(2, "此消息已读,不能重复修改状态!");
            }
            ysMessage.setStatus(true);
            ysMessage.setUpdateTime(LocalDateTime.now());
            int i = ysMessageMapper.updateByPrimaryKey(ysMessage);
            if (i > 0){
                return Callback.success();
            }
        }
        return Callback.error(2, "状态更改失败!");
    }

    public Callback unreadMessageCount(){
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        List<YsMessage> list = ysMessageMapper.selectByReceiveIdAndStatus(userVo.getId(), false);
        return Callback.success(list.size());
    }














}

