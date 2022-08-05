package com.ennova.pubinfotask.service;

import com.alibaba.fastjson.JSONObject;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.config.ChannelHandlerPool;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.dao.YsBulletinFileMapper;
import com.ennova.pubinfotask.dao.YsBulletinMapper;
import com.ennova.pubinfotask.dao.YsMessageMapper;
import com.ennova.pubinfotask.dto.FileDelDTO;
import com.ennova.pubinfotask.dto.PublishDTO;
import com.ennova.pubinfotask.entity.*;
import com.ennova.pubinfotask.utils.BeanConvertUtils;
import com.ennova.pubinfotask.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
    private final YsBulletinFileMapper ysBulletinFileMapper;

    /**
     * 本地路径
     */
    @Value("${spring.upload.local.path}")
    private String localPath;

    /**
     * 访问url
     */
    @Value("${spring.upload.local.url}")
    private String localUrl;

    /**
     * 支持文件
     */
    @Value("${file.suffix}")
    private String[] fileSuffix;


    public Callback<FileVO> uploadFile(MultipartFile file) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            log.info("上传文件大小为空!");
            return Callback.error(2, "上传文件不能为空!");
        }

        HashMap<String, String> map = FileUtils.uploadFile(file, localPath, fileSuffix);
        if (StringUtils.isNotBlank(map.get("error"))) {
            return Callback.error(2, map.get("error"));
        }
        String subname = map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");

        YsBulletinFile ysBulletinFile = YsBulletinFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(LocalDateTime.now()).build();

        int count = ysBulletinFileMapper.insertSelective(ysBulletinFile);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(ysBulletinFile.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "上传失败!");
    }

    public Callback deleteFile(FileDelDTO fileDelDTO) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        fileDelDTO.getFileVos().forEach(fileVo -> {
            String path = localPath + "/" + fileVo.getNewfileName();
            // 如果是本人上传的，才能执行删除操作
            assert userVo != null;
            List<YsBulletinFile> files = ysBulletinFileMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (CollectionUtils.isNotEmpty(files)) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = ysBulletinFileMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count == 1) {
                        file.delete();
                    }
                    YsBulletinFile ysBulletinFile = ysBulletinFileMapper.selectByPrimaryKey(fileVo.getId());
                    ysBulletinFileMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success(true);
    }

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

        int i = ysBulletinMapper.selectByTitle(publishDTO.getTitle(), null);
        if (i > 0) {
            return Callback.error(2, "标题已存在");
        }

        YsBulletin ysBulletin = BeanConvertUtils.convertTo(publishDTO, YsBulletin::new);
        ysBulletin.setCreateTime(LocalDateTime.now());
        ysBulletin.setStatus(0);
        ysBulletin.setCreateId(userVo.getId());
        int count = ysBulletinMapper.insert(ysBulletin);// 插入主表
        List<FileVO> fileVOList = publishDTO.getFileVOList();
        if (CollectionUtils.isNotEmpty(fileVOList)) {
            fileVOList.forEach(fileVO -> {
                Optional.ofNullable(ysBulletinFileMapper.selectByPrimaryKey(fileVO.getId())).ifPresent(ysBulletinFile -> {
                    ysBulletinFile.setFileMasterId(ysBulletin.getId());
                    ysBulletinFile.setUpdateTime(LocalDateTime.now());
                    ysBulletinFileMapper.updateByPrimaryKeySelective(ysBulletinFile);
                });
            });
        }
        //推送:sourceType=0 公告,type=0 新增
        if (count > 0) {
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(0).content(ysBulletin).build();
            Channel channel = getChannel(String.valueOf(publishDTO.getCheckUserId()));
            log.info("新增公告时的审核人: " + publishDTO.getCheckUserId() );
            log.info("新增公告时channeList: " + channel );
            if (null == channel) {
                redisTemplate.opsForList().rightPush("bulletin:add:" + publishDTO.getCheckUserId(), JSONObject.toJSONString(socketVO));
                log.info("用户: " + publishDTO.getCheckUserId() + " 没有登录，添加到redis队列");
                return Callback.success();
            } else {
                //推送list第一个元素
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
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
            return Callback.error(2, "ID为" + publishDTO.getId() + "的公告不存在");
        }

        int i = ysBulletinMapper.selectByTitle(publishDTO.getTitle(), publishDTO.getId());
        if (i > 0) {
            return Callback.error(2, "标题已存在");
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
        int count = ysBulletinMapper.updateByPrimaryKeySelective(ysBulletin);// 更新主表
        log.info("更新公告时的审核人: " + publishDTO.getCheckUserId() );

        List<FileVO> fileVOList = publishDTO.getFileVOList();
        if (CollectionUtils.isNotEmpty(fileVOList)) {
            fileVOList.forEach(fileVO -> {
                Optional.ofNullable(ysBulletinFileMapper.selectByPrimaryKey(fileVO.getId())).ifPresent(ysBulletinFile -> {
                    ysBulletinFile.setFileMasterId(ysBulletin.getId());
                    ysBulletinFile.setUpdateTime(LocalDateTime.now());
                    ysBulletinFileMapper.updateByPrimaryKeySelective(ysBulletinFile);
                });
            });
        }
        if (count > 0){
            //推送:sourceType=0 公告,type=3 修改
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(3).content(ysBulletin).build();
            //List<Channel> channelList = getChannelByName(String.valueOf(publishDTO.getCheckUserId()));
            Channel channel = getChannel(String.valueOf(publishDTO.getCheckUserId()));
            log.info("更新公告时channeList: " + channel );
            if (null == channel) {
                redisTemplate.opsForList().rightPush("bulletin:add:" + publishDTO.getCheckUserId(), JSONObject.toJSONString(socketVO));
                log.info("用户: " + publishDTO.getCheckUserId() + " 没有登录，添加到redis队列");
                return Callback.success();
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
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
        if (!currentUserVO.getRoleCode().equals("ADMIN")) {
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
        List<YsBulletinVO> list = ysBulletinMapper.selectByStatusAndTitleLike(status, likeTitle, null, "id desc");
        if(CollectionUtils.isNotEmpty(list)){
            list.stream().map(ysBulletinVO -> {
                List<YsBulletinFile> ysbulletinFiles = ysBulletinFileMapper.selectByFileMasterId(ysBulletinVO.getId());
                ysBulletinVO.setFiles(ysbulletinFiles);
                return ysBulletinVO;
            }).collect(Collectors.toList());
        }


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
        if(ysBulletin != null){
           ysBulletin.getId();
            List<YsBulletinFile> files = ysBulletinFileMapper.selectByFileMasterId(ysBulletin.getId());
            ysBulletin.setFiles(files);
        }
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
            return Callback.error(2, "当前ID为" + id + "的公告不存在");
        }

        if (ysBulletin.getStatus() != 0) {
            return Callback.error(2, "当前公告已审核，不能重复审核");
        }

        ysBulletin.setStatus(status);
        ysBulletin.setUpdateTime(LocalDateTime.now());


        //审核通过
        if (status == 1) {
            ysBulletin.setStatus(1);
            ysBulletin.setCheckTime(LocalDateTime.now());
            List<CurrentUserVO> currentUserVOS = userMapper.selectAllUser();
            List<Integer> users = currentUserVOS.stream().map(CurrentUserVO::getUserId).collect(Collectors.toList());

            //TODO 审核通过
            //保存到数据库
            users.stream().forEach(user -> {
                YsMessage message = YsMessage.builder().sourceType(0).receiveId(user).ysBulletin(ysBulletin.getId()).status(false).createTime(LocalDateTime.now()).build();
                ysMessageMapper.insert(message);
            });
            log.info("审核通过获取所有用户"+users);
            //推送:sourceType=0 公告,type=1 审核通过了
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(1).content(ysBulletin).build();
            if (users.size() > 0) {
                //推送消息给所有用户
                users.forEach(user -> {
                    log.info("遍历要发送的用户user："+ user);
                    Channel channel = getChannel(String.valueOf(user));
                    log.info("遍历要发送的用户channeList："+ channel + " =-----------------------------------------------------------" );
                    if (null == channel) {
                         log.info("不在线的用户："+ user);
                        redisTemplate.opsForList().rightPush("bulletin:push:" + user, JSONObject.toJSONString(socketVO));
                    } else {
                        log.info("发送成功的用户："+ user + " channel:" + channel);
                        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
                    }
                });


                //users.forEach(user -> {
                //    Channel channel = getChannelByName2(String.valueOf(user));
                //    if (null == channel) {
                //        if (online(String.valueOf(user))) {
                //            redisTemplate.opsForList().rightPush("bulletin:push:" + user, JSONObject.toJSONString(socketVO));
                //        } else {
                //            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
                //            //channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO))));
                //        }
                //
                //    }
                //
                //});
            }

        }
        //审核不通过
        if (status == 2) {
            ysBulletin.setStatus(2);
            ysBulletin.setUpdateTime(LocalDateTime.now());

            //TODO 审核不通过
            //推送:sourceType=0 公告,type=2 审核不通过
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(0).type(2).content(ysBulletin).build();
            //Channel channel = getChannelByName2(String.valueOf(ysBulletin.getCreateId()));
            //if (null == channel) {
            //    if (online(String.valueOf(ysBulletin.getCreateId()))) {
            //        redisTemplate.opsForList().rightPush("bulletin:push:" + ysBulletin.getCreateId(), JSONObject.toJSONString(socketVO));
            //    } else {
            //        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
            //    }
            //}


            Channel channel = getChannel(String.valueOf(ysBulletin.getCreateId()));
            log.info("审核不通过用户：" + ysBulletin.getCreateId());
            if (null == channel) {
                redisTemplate.opsForList().rightPush("bulletin:reject:" + ysBulletin.getCreateId(), JSONObject.toJSONString(socketVO));
            } else {
                //channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame("您有一条编号为" + ysBulletin.getId() + "的公告已被驳回！")));
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
                log.info("审核不通过的用户："+ ysBulletin.getCreateId() + " channel:" + channel);
            }
        }

        int i = ysBulletinMapper.updateByPrimaryKeySelective(ysBulletin); // 更新主表
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error(2, "审核失败");
    }


    // 查询用户
//    public List<Channel> getChannelByName(String name) {
//        AttributeKey<String> key = AttributeKey.valueOf("user");
//
//        return channelGroup.stream().filter(channel -> channel.attr(key).get().equals(name))
//                .collect(Collectors.toList());
//    }

    // 查询用户
    public Channel getChannel(String userId) {
        // Channel channel = userMap.get(paramMap.get("userId")); //获取用户的channel
        Map<String, Channel> getConnects = ChannelHandlerPool.getConnects;
        Channel channel = getConnects.get(userId);
        return channel;
    }


    //
    //public Channel getChannelByName2(String name) {
    //    return ChannelHandlerPool.channelMap.get(name);
    //}
    //
    //public Boolean online(String userId) {
    //    return ChannelHandlerPool.channelMap.containsKey(userId) && ChannelHandlerPool.channelMap.get(userId) != null;
    //}


    /**
     * -----------------------------------------------------   消息  ------------------------------------------------------------------------------
     **/

    public Callback<BaseVO<YsMessageVO>> getMessageList(Integer page, Integer pageSize, Boolean status, String likeTitle) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        if(page==null || page<1){
            page = 1;
        }
        if(pageSize==null || pageSize<1){
            pageSize = 10;
        }
        int index = (page - 1) * pageSize;
        List<YsMessageVO> list = ysMessageMapper.selectByStatusAndYsBulletinLike(status, likeTitle, userVo.getId());
        List<YsMessageVO> ysMessageVOS = ysMessageMapper.selectByStatusAndSupplierLike(status, likeTitle, userVo.getId());
        List<YsMessageVO> dayRepList =ysMessageMapper.selectByStatusAndDayRepLike(status, likeTitle, userVo.getId());
        List<YsMessageVO> expSugList =ysMessageMapper.selectByStatusAndExpSugLike(status, likeTitle, userVo.getId());
        list.addAll(ysMessageVOS);
        list.addAll(dayRepList);
        list.addAll(expSugList);

        List<YsMessageVO> filterList = list.stream().sorted(Comparator.comparing(YsMessageVO::getId).reversed()).collect(Collectors.toList());
        List<YsMessageVO> list2 = filterList.stream().sorted(Comparator.comparing(YsMessageVO::getStatus)).collect(Collectors.toList());
//        List<YsMessageVO> filterList = list.stream().sorted(Comparator.comparing(YsMessageVO::getId).reversed())
//                .collect(Collectors.toList());
        BaseVO<YsMessageVO> baseVO = new BaseVO<>(getPaging(index,pageSize,list2), new PageUtil(pageSize, filterList.size(), page));
        return Callback.success(baseVO);
    }

    public List<YsMessageVO> getPaging(int index, int pageSize, List<YsMessageVO> list){
        if (index < 0 || index >= list.size() || pageSize <= 0) {
            return null;
        }
        int lastIndex = index + pageSize;
        if (lastIndex > list.size()) {
            lastIndex = list.size();
        }
        list = list.subList(index, lastIndex);
        return list;
    }

    // 只能修改自己的任务为已读状态
    public Callback updateMessageBulletin(Integer bulletin) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        YsMessage ysMessage = ysMessageMapper.selectBybulletinIdAndReceiveId(bulletin, userVo.getId());
        if (null != ysMessage) {
            if (ysMessage.getStatus().equals(true)) {
                return Callback.error(2, "此消息已读,不能重复修改状态!");
            }
            ysMessage.setStatus(true);
            ysMessage.setUpdateTime(LocalDateTime.now());
            int i = ysMessageMapper.updateByPrimaryKey(ysMessage);
            if (i > 0) {
                return Callback.success();
            }
        }
        return Callback.error(2, "状态更改失败!");
    }

    public Callback updateMessage(Integer id) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        YsMessage ysMessage = ysMessageMapper.selectByIdAndReceiveId(id, userVo.getId());
        if (null != ysMessage) {
            if (ysMessage.getStatus().equals(true)) {
                return Callback.error(2, "此消息已读,不能重复修改状态!");
            }
            ysMessage.setStatus(true);
            ysMessage.setUpdateTime(LocalDateTime.now());
            int i = ysMessageMapper.updateByPrimaryKey(ysMessage);
            if (i > 0) {
                return Callback.success();
            }
        }
        return Callback.error(2, "状态更改失败!");
    }

    public Callback unreadMessageCount() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        List<YsMessage> list = ysMessageMapper.selectByReceiveIdAndStatus(userVo.getId(), false);
        return Callback.success(list.size());
    }

    // 查询当前用户创建和审核的公告列表
    public Callback<List<YsBulletinVO>> getAllBulletinList(Integer status) {

        List<YsBulletinVO> list = ysBulletinMapper.selectAll(status, "id desc");

        return Callback.success(list);
    }

    public void sendMessByMessageVO(MessageVO messageVO) {
        Map<String, Channel> getConnects = ChannelHandlerPool.getConnects;
        Channel channel = getConnects.get(messageVO.getUserId());
        YsMessage message = YsMessage.builder().sourceType(messageVO.getSourceType()).receiveId(Integer.parseInt(messageVO.getUserId())).ysBulletin(messageVO.getBackId()).status(false).createTime(LocalDateTime.now()).build();
        ysMessageMapper.insert(message);
        if (null == channel) {
            if (messageVO.getType()==4){
                redisTemplate.opsForList().rightPush("dayrep:send:" + messageVO.getUserId(), JSONObject.toJSONString(messageVO));
            }else if (messageVO.getType()==5){
                redisTemplate.opsForList().rightPush("expsug:send:" + messageVO.getUserId(), JSONObject.toJSONString(messageVO));
            }
        } else {
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(messageVO)));
        }
    }
    public Callback addSupplierPursh(MessageVO messageVO){
        Map<String, Channel> getConnects = ChannelHandlerPool.getConnects;
        Channel channel = getConnects.get(messageVO.getUserId());

        log.info("新增供应商时的审核人：" + String.valueOf(messageVO.getUserId()));
        log.info("新增供应商时channel" + channel);

        if (null == channel) {
            if (messageVO.getType()==0 || messageVO.getType()==3){
                redisTemplate.opsForList().rightPush("supplier:add:" + messageVO.getUserId(), JSONObject.toJSONString(messageVO));
                log.info("用户: " + messageVO.getUserId() + " 没有登录，添加到redis队列");
            }else if (messageVO.getType()==1){
                redisTemplate.opsForList().rightPush("supplier:check:" + messageVO.getUserId(), JSONObject.toJSONString(messageVO));
                log.info("用户: " + messageVO.getUserId() + " 没有登录，添加到redis队列");
            }else if (messageVO.getType()==2){
                redisTemplate.opsForList().rightPush("supplier:refuse:" + messageVO.getUserId(), JSONObject.toJSONString(messageVO));
            }
            return Callback.success();
        } else {
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(messageVO)));
        }
        return Callback.success(true);
    }
}

