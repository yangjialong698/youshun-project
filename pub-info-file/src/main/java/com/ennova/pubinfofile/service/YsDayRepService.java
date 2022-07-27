package com.ennova.pubinfofile.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfofile.dao.*;
import com.ennova.pubinfofile.entity.*;
import com.ennova.pubinfofile.service.feign.PubInfoTaskClient;
import com.ennova.pubinfofile.service.feign.PubInfoUserClient;
import com.ennova.pubinfofile.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YsDayRepService {

    @Autowired
    private YsDayRepMapper ysDayRepMapper;
    @Autowired
    private YsDayRepFileMapper ysDayRepFileMapper;
    @Autowired
    private PubInfoUserClient pubInfoUserClient;
    @Autowired
    private YsTeamMapper ysTeamMapper;
    @Autowired
    private YsFeedBackMapper ysFeedBackMapper;
    @Autowired
    private PubInfoTaskClient pubInfoTaskClient;
    @Autowired
    private  HttpServletRequest req;
    /**
     * 访问url
     */
    @Value("${spring.upload.local.url}")
    private String localUrl;

    /**
     * 本地路径
     */
    @Value("${spring.upload.local.path}")
    private String localPath;

    public Callback insertOrUpdate(YsDayRepVO ysDayRepVO) {
        if (null != ysDayRepVO.getId()){
            YsDayRep task = ysDayRepMapper.selectByPrimaryKey(ysDayRepVO.getId());
            if (null != task){
                //更新
                YsDayRep ysDayRep = YsDayRep.builder().id(ysDayRepVO.getId())
                        .fileName(ysDayRepVO.getFileName())
                        .ysMasterTaskId(ysDayRepVO.getYsMasterTaskId())
                        .fileContent(ysDayRepVO.getFileContent())
                        .dayrepTime(ysDayRepVO.getDayrepTime())
                        .updateTime(new Date())
                        .build();
                ysDayRepMapper.updateByPrimaryKeySelective(ysDayRep);
                Integer  ysDayRepId = ysDayRep.getId();
                //更新附件表
                List<FileVO> fileVOList = ysDayRepVO.getFileVOList();
                if (CollectionUtil.isNotEmpty(fileVOList)){
                    for (FileVO fileVO : fileVOList) {
                        YsDayRepFile ysDayRepFile = YsDayRepFile.builder()
                                .id(fileVO.getId())
                                .updateTime(new Date())
                                .dayRepId(ysDayRepId)
                                .build();
                        ysDayRepFileMapper.updateByPrimaryKeySelective(ysDayRepFile);
                    }
                }
                return Callback.success("日报修改成功");
            }
        }else {
            //新增
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = req.getHeader("Authorization");
            UserVO userVo = JWTUtil.getUserVOByToken(token);
            YsDayRep ysDayRep = YsDayRep.builder()
                    .fileName(ysDayRepVO.getFileName())
                    .openFile(0)
                    .delFlag(0)
                    .ysMasterTaskId(ysDayRepVO.getYsMasterTaskId())
                    .fileContent(ysDayRepVO.getFileContent())
                    .userId(userVo.getId())
                    .dayrepTime(ysDayRepVO.getDayrepTime())
                    .createTime(new Date())
                    .build();
            ysDayRepMapper.insertSelective(ysDayRep);
            Integer ysDayRepId = ysDayRep.getId();
            //回写日报文件表
            List<FileVO> fileVOList = ysDayRepVO.getFileVOList();
            if (CollectionUtil.isNotEmpty(fileVOList)){
                for (FileVO fileVO : fileVOList) {
                    YsDayRepFile ysDayRepFile = YsDayRepFile.builder().id(fileVO.getId()).updateTime(new Date()).dayRepId(ysDayRepId).build();
                    ysDayRepFileMapper.updateByPrimaryKeySelective(ysDayRepFile);
                }
            }
            return Callback.success("日报新增成功");
        }
        return Callback.error("日报操作失败!");
    }

    public Callback deleteDayRep(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        ysDayRepFileMapper.deleteYsDayRepFile(id);
        int result = ysDayRepMapper.delete(id);
        if (result < 1) {
            return Callback.error("删除失败");
        }
        return Callback.success();
    }

    public Callback<YsDayRepVO> selectByPrimaryKey(Integer id) {
        if (id == null) {
            return Callback.error("日报id不能为空");
        }
        YsDayRepVO ysDayRepVO = ysDayRepMapper.selectDetailOne(id);
        if(null != ysDayRepVO){
            return Callback.success(ysDayRepVO);
        }
        return Callback.error("日报无数据!");
    }

    public Callback addFeedContent(FeedBackVO feedBackVO) {
        YsFeedBack record = YsFeedBack.builder()
                .feedContent(feedBackVO.getFeedContent())
                .dayRepId(feedBackVO.getId())
                .feedStatus(1)
                .createTime(new Date())
                .userId(Integer.parseInt(feedBackVO.getUserId())).build();
        int i = ysFeedBackMapper.insertSelective(record);
        if (i > 0){
            //推送
            Integer drId = feedBackVO.getId();
            YsDayRep ysDayRep = ysDayRepMapper.selectByPrimaryKey(drId);
            Integer userId = ysDayRep.getUserId();//创建日报用户ID
            MessageVO<Object> messageVO = MessageVO.builder().sourceType(2).type(4).content("你的日报被反馈!").title(ysDayRep.getFileName()).userId(userId).backId(ysDayRep.getId()).build();
            pubInfoTaskClient.sendMessByMessageVO(messageVO);
            return Callback.success("反馈成功!");
        }
        return Callback.error("反馈失败!");
    }

    public Callback<BaseVO<DayRepDetailVO>> getDayRepDetails(Integer page, Integer pageSize, Integer ysMasterTaskId, String fileName, String startTime, String endTime, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return Callback.error("无权限token");
        }
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        Callback<UserVO> callback = pubInfoUserClient.getUserById(userVo.getId());
        if (callback.errCode != 0 || callback.data == null) {
            return Callback.error("获取用户异常!");
        }
        UserVO userVO = callback.data;
        Integer userId = userVO.getId();
        String roleCode = userVO.getRoleCode();
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        BaseVO<DayRepDetailVO> baseVO = null ;
//        if (roleCode.equals("task_manage")){
//            //任务管理员 --- 子任务的数据
//            String subRoleCode = "sub_task_manage";
//            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
//            List<DayRepDetailVO> list = ysDayRepMapper.getDayRepsByRoleCode(ysMasterTaskId,fileName,userId,subRoleCode,startTime,endTime);
//            baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
//        }else if
        if (roleCode.equals("sub_task_manage")){
            //子任务管理
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userId);
            List<Integer> maskTaskIdList = null ;
            if (CollectionUtil.isNotEmpty(ysTeams)){
                maskTaskIdList = ysTeams.stream().map(e -> e.getYsMasterTaskId()).distinct().collect(Collectors.toList());
            }
            if (CollectionUtil.isNotEmpty(maskTaskIdList)){
                //1.归属团队数据
                Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
                List<DayRepDetailVO> list = ysDayRepMapper.getDayRepDetailsByStm(maskTaskIdList,ysMasterTaskId,fileName,userId,startTime,endTime);
                baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
            }else {
                //2.自己数据 3.下属数据
                Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
                List<DayRepDetailVO> list = ysDayRepMapper.getDayRepDetails(ysMasterTaskId,fileName,userId,startTime,endTime);
                baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
            }
        }else if (roleCode.equals("executor")){
            //普通用户
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<DayRepDetailVO> list = ysDayRepMapper.getDayRepsBySelf(ysMasterTaskId,fileName,userId,startTime,endTime);
            baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        }else {
            //其他角色
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<DayRepDetailVO> list = ysDayRepMapper.getDayRepAll(ysMasterTaskId,fileName,userId,startTime,endTime);
            baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        }
        return Callback.success(baseVO);
    }


    public Callback<FileVO> uploadFile(MultipartFile file) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            log.info("上传文件大小为空!");
            return Callback.error("上传文件不能为空!");
        }
        HashMap<String, String> map = FileUtils.uploadFile(file, localPath,new String[1]);
        if (StringUtils.isNotBlank(map.get("error"))){
            return Callback.error( map.get("error"));
        }
        String subname =  map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");
        YsDayRepFile record = YsDayRepFile.builder()
                .fileMd5(subname)
                .fileUrl(localUrl + "/file/" + subname)
                .name(map.get("fileName"))
                .fileSize(map.get("fileSize"))
                .openFile(0)
                .delFlag(0)
                .userId(userVo.getId())
                .createTime(new Date()).build();
        int i = ysDayRepFileMapper.insertSelective(record);
        if(i > 0){
            FileVO fileVo = FileVO.builder()
                    .id(record.getId())
                    .fileName(map.get("fileName"))
                    .newfileName(subname)
                    .build();
            return Callback.success(fileVo);
        }
        return Callback.error("附件上传失败!");
    }

    public Callback delFiles(FileRepDelVO fileRepDelVO) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        List<FileVO> fileVOList = fileRepDelVO.getFileVOList();
        fileVOList.forEach(e->{
            String path = localPath +  "/" + e.getNewfileName();
            List<YsDayRepFile> files = ysDayRepFileMapper.selectAllByFileMd5AndUserId(e.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = ysDayRepFileMapper.selectByFileMd5(e.getNewfileName());
                    if(count == 1){
                        file.delete();
                    }
                    ysDayRepFileMapper.deleteByPrimaryKey(e.getId());
                }
            }
        });
        if (null != fileRepDelVO.getDayRepId()){
            int count = ysDayRepFileMapper.selectByDayRepId(fileRepDelVO.getDayRepId());
            if (count == 0){
                // slave 文件中没有对应的 master, 再删除唯一的 master
                ysDayRepMapper.deleteByPrimaryKey(fileRepDelVO.getDayRepId());
            }
        }
        return Callback.success("附件删除成功");
    }


    public Callback<List<FileDownVO>> fileDetail(Integer id) {
        List<FileDownVO> fileDownVOList = ysDayRepMapper.fileDetail(id);
        return Callback.success(fileDownVOList);
    }
}
