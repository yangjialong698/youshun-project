package com.ennova.pubinfofile.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfofile.dao.YsMasterFileMapper;
import com.ennova.pubinfofile.dao.YsSlaveFileMapper;
import com.ennova.pubinfofile.dao.YsSonTaskMapper;
import com.ennova.pubinfofile.dao.YsTeamMapper;
import com.ennova.pubinfofile.entity.YsSlaveFileEntity;
import com.ennova.pubinfofile.entity.YsSonTask;
import com.ennova.pubinfofile.entity.YsTeam;
import com.ennova.pubinfofile.service.feign.PubInfoUserClient;
import com.ennova.pubinfofile.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileDetailService {

    @Autowired
    private YsMasterFileMapper ysMasterFileMapper;
    @Autowired
    private PubInfoUserClient pubInfoUserClient;
    @Autowired
    private YsSlaveFileMapper ysSlaveFileMapper;
    @Autowired
    private YsTeamMapper ysTeamMapper;
    @Autowired
    private YsSonTaskMapper ysSonTaskMapper;

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
    /**
     * 支持文件
     */
    @Value("${file.suffix}")
    private String[] fileSuffix;

    public Callback<BaseVO<FileDetailVO>> getFileDetails(Integer page, Integer pageSize, Integer ysFileTypeId,
                                                         Integer ysMasterTaskId, Integer ysSonTaskId,
                                                         String fileName, String startTime,String endTime,HttpServletRequest req) {
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
        if (roleCode.equals("sub_task_manage")){
            //角色是子任务管理
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<FileDetailVO> list = ysMasterFileMapper.getFileDetailsForZrw(ysFileTypeId,ysMasterTaskId,ysSonTaskId,fileName,userId,startTime,endTime);
            BaseVO<FileDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
            return Callback.success(baseVO);
        }else if(roleCode.equals("executor")){
            //角色是普通用户
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userId);
            if (CollectionUtil.isNotEmpty(ysTeams)){
                List<Integer> teamIds = ysTeams.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<YsSonTask> ysSonTaskList = ysSonTaskMapper.selectAllByYsTeamIds(teamIds);
                if (CollectionUtil.isNotEmpty(ysSonTaskList)){
                    List<Integer> maskTaskIdList = ysSonTaskList.stream().map(e -> e.getYsMasterTaskId()).distinct().collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(maskTaskIdList)){
                        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
                        List<FileDetailVO> list = ysMasterFileMapper.getDetailsByMaskTaskIds(maskTaskIdList,ysFileTypeId,ysMasterTaskId,ysSonTaskId,fileName);
                        BaseVO<FileDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
                        return Callback.success(baseVO);
                    }else {
                        return null;
                    }
                }
                return null;
            }else {
                return null;
            }
        }else {
            //角色不是子任务管理和普通用户
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<FileDetailVO> list = ysMasterFileMapper.getFileDetails(ysFileTypeId,ysMasterTaskId,ysSonTaskId,fileName,userId,startTime,endTime);
            BaseVO<FileDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
            return Callback.success(baseVO);
        }
    }



    public Callback<BaseVO<FileDetailVO>> getMyFileDetails(Integer page, Integer pageSize, Integer ysFileTypeId, Integer ysMasterTaskId, Integer ysSonTaskId, String fileName, HttpServletRequest req) {
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
//        String roleCode = userVO.getRoleCode();
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
        List<FileDetailVO> list = ysMasterFileMapper.getMyFileDetails(ysFileTypeId,ysMasterTaskId,ysSonTaskId,fileName,userId);
        BaseVO<FileDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<List<FileTypeCou>> getFileTypeCou() {
        List<FileTypeCou> fileTypeCouList =  ysMasterFileMapper.getFileTypeCou();
        Integer totalNum = fileTypeCouList.stream().collect(Collectors.summingInt(FileTypeCou::getCount));
        if (totalNum >0 ){
            for (FileTypeCou fileTypeCou : fileTypeCouList) {
                Integer count = fileTypeCou.getCount();
                Integer ratio = getRatio(count, totalNum);
                fileTypeCou.setCount(count);
                fileTypeCou.setFilePercent(ratio);
            }
        }
        return Callback.success(fileTypeCouList);
    }

    public Callback<FileUpdateVO> getFileUpRatio(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return Callback.error("无权限token");
        }
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        if (userVo == null) {
            return Callback.error("获取用户异常!");
        }
        Integer userId = userVo.getId();
        FileUpdateVO fileUpdateVO = new FileUpdateVO();
        Integer fileTotal = ysMasterFileMapper.fileTotal();
        //今日更新统计
        Integer dayUpNum = ysMasterFileMapper.dayUpNum();
        Integer earUpNum = fileTotal - dayUpNum;
        Integer updateRatio = getRatio(dayUpNum, fileTotal);
        //我的文档统计
        Integer myFileNum = ysMasterFileMapper.myFileNum(userId);
        Integer myFileRatio = getRatio(myFileNum, fileTotal);
        //我的建议文档统计
        Integer mySugNum = ysMasterFileMapper.mySugNum(userId);
        Integer mySugTotal = ysMasterFileMapper.mySugTotal();
        Integer mySugRatio = getRatio(mySugNum, mySugTotal);
        fileUpdateVO.setDayUpNum(dayUpNum);
        fileUpdateVO.setEarUpNum(earUpNum);
        fileUpdateVO.setMyFileNum(myFileNum);
        fileUpdateVO.setMySugNum(mySugNum);
        fileUpdateVO.setUpdateRatio(updateRatio);
        fileUpdateVO.setMyFileRatio(myFileRatio);
        fileUpdateVO.setMySugRatio(mySugRatio);
        return Callback.success(fileUpdateVO);
    }

    private Integer getRatio(Integer first, Integer all) {
        double per = (double) (first) / all;
        Integer perratio = new Double(per * 100).intValue();
        return perratio;
    }

    public Callback<List<FileDownVO>> downDetail(Integer id) {
        List<FileDownVO> fileDownVOList = ysMasterFileMapper.downDetail(id);
        return Callback.success(fileDownVOList);
    }

    public Callback<ModifyFileVO> selectDetailOne(Integer id) {
        if (id == null) {
            return Callback.error("主任务id不能为空");
        }
        ModifyFileVO modifyFileVO = ysMasterFileMapper.selectDetailOne(id);
        if(null != modifyFileVO){
            return Callback.success(modifyFileVO);
        }
        return Callback.error("文件夹无数据!");
    }

    public Callback modifyfile(ModifyFileVO modifyFileVO) {
        Integer type = modifyFileVO.getType();
        Integer ysMasterFileId = null ;
        if (null != type && type == 1){
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = req.getHeader("Authorization");
            UserVO userVo = JWTUtil.getUserVOByToken(token);
            String formatTime = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            YsMasterFileVO master = YsMasterFileVO.builder()
                    .serialNumber(modifyFileVO.getFilePrefix() + formatTime)
                    .fileName(modifyFileVO.getFileName())
                    .openFile(0)
                    .delFlag(0)
                    .ysFileTypeId(modifyFileVO.getYsFileTypeId())
                    .ysMasterTaskId(modifyFileVO.getYsMasterTaskId())
                    .ysSonTaskId(modifyFileVO.getYsSonTaskId())
                    .fileContent(modifyFileVO.getFileContent())
                    .versionNo(modifyFileVO.getVersionNo())
                    .userId(userVo.getId())
                    .dayrepTime(modifyFileVO.getDayrepTime())
                    .createTime(new Date())
                    .build();
            ysMasterFileMapper.insertSelective(master);
            ysMasterFileId = master.getId();
        }else {
            //更新封皮表
            YsMasterFileVO ysMasterFileVO = YsMasterFileVO.builder().id(modifyFileVO.getId())
                    .fileName(modifyFileVO.getFileName())
                    .ysMasterTaskId(modifyFileVO.getYsMasterTaskId())
                    .ysSonTaskId(modifyFileVO.getYsSonTaskId())
                    .fileContent(modifyFileVO.getFileContent())
                    .versionNo(modifyFileVO.getVersionNo())
                    .dayrepTime(modifyFileVO.getDayrepTime())
                    .build();
            ysMasterFileMapper.updateByPrimaryKeySelective(ysMasterFileVO);
            ysMasterFileId = ysMasterFileVO.getId();
        }
        //更新附件表
        List<FileVO> fileVOList = modifyFileVO.getFileVOList();
        if (CollectionUtil.isNotEmpty(fileVOList)){
            for (FileVO fileVO : fileVOList) {
                YsSlaveFileVO ysSlaveFile = YsSlaveFileVO.builder()
                        .id(fileVO.getId())
                        .updateTime(new Date())
                        .fileMasterId(ysMasterFileId)
                        .build();
                ysSlaveFileMapper.updateByPrimaryKeySelective(ysSlaveFile);
            }
        }
        return Callback.success("修改成功");
    }

    public Callback<List<LinkedHashMap>> selectMasterTask(HttpServletRequest req) {
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
        List<LinkedHashMap> list = null;
        if (roleCode.equals("sub_task_manage")){
            List<LinkedHashMap> list1 = ysMasterFileMapper.selectMasterTaskByZrw(userId);
            //新增子任务管理归属哪个团队的主任务作为下拉菜单新增+搜索
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userId);
            if (CollectionUtil.isNotEmpty(ysTeams)){
                List<Integer> maskTaskIdList = ysTeams.stream().map(e -> e.getYsMasterTaskId()).distinct().collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(maskTaskIdList)){
                    List<LinkedHashMap> list2 = ysMasterFileMapper.selectMaster(maskTaskIdList);
                    if (CollectionUtil.isNotEmpty(list1)){
                        list1.addAll(list2);
                        list = list1.stream().distinct().collect(Collectors.toList());
                    }
                }
            }
        }else if (roleCode.equals("executor")){
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userId);
            if (CollectionUtil.isNotEmpty(ysTeams)){
                List<Integer> teamIds = ysTeams.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<YsSonTask> ysSonTaskList = ysSonTaskMapper.selectAllByYsTeamIds(teamIds);
                if (CollectionUtil.isNotEmpty(ysSonTaskList)){
                    List<Integer> ysMasterTaskIdList = ysSonTaskList.stream().map(e -> e.getYsMasterTaskId()).collect(Collectors.toList());
                    list = ysMasterFileMapper.selectMaster(ysMasterTaskIdList);
                }
            }
        }else {
            list = ysMasterFileMapper.selectMasterTask();
        }
//        if (!roleCode.equals("sub_task_manage") ){
//            //角色不是子任务管理
//            list = ysMasterFileMapper.selectMasterTask();
//        }else {
//            list = ysMasterFileMapper.selectMasterTaskByZrw(userId);
//        }
        return Callback.success(list);
    }

    public Callback<List<LinkedHashMap>> selectSonTask(Integer ysMasterTaskId) {
        List<LinkedHashMap>  list = ysMasterFileMapper.selectSonTask(ysMasterTaskId);
        return Callback.success(list);
    }

    public Callback deleteFileDetail(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        ysSlaveFileMapper.deleteSlaveFiles(id);
        int result = ysMasterFileMapper.delete(id);
        if (result < 1) {
            return Callback.error("删除失败");
        }
        return Callback.success();
    }


    public Callback delFiles(FileDeleteVO fileDeleteVO) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        List<FileVO> fileVOList = fileDeleteVO.getFileVOList();
        fileVOList.forEach(e->{
            String path = localPath +  "/" + e.getNewfileName();
            List<YsSlaveFileEntity> files = ysSlaveFileMapper.selectAllByFileMd5AndUserId(e.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = ysSlaveFileMapper.selectByFileMd5(e.getNewfileName());
                    if(count == 1){
                        file.delete();
                    }
                    ysSlaveFileMapper.deleteByPrimaryKey(e.getId());
                }
            }
        });
        if (null != fileDeleteVO.getFileMaterId()){
            int masterCount = ysSlaveFileMapper.selectByFileMasterId(fileDeleteVO.getFileMaterId());
            if (masterCount == 0){
                // slave 文件中没有对应的 master, 再删除唯一的 master
                ysMasterFileMapper.deleteByPrimaryKey(fileDeleteVO.getFileMaterId());
            }
        }
        return Callback.success("附件删除成功");
    }

    public Callback<FileVO> uploadFile(MultipartFile file) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            log.info("上传文件大小为空!");
            return Callback.error("上传文件不能为空!");
        }
//        String fileMd5 = FileUtils.getFileMd5(file, localPath);
////        List<YsSlaveFileEntity> files = ysSlaveFileMapper.selectAllByFileMd5AndUserId(fileMd5,userVo.getId());
////        if (!files.isEmpty()) {
////            return Callback.error("当前用户已上传该附件！");
////        }
        HashMap<String, String> map = FileUtils.uploadFile(file, localPath, fileSuffix);
        if (StringUtils.isNotBlank(map.get("error"))){
            return Callback.error( map.get("error"));
        }
        String subname =  map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");
        YsSlaveFileVO slave = YsSlaveFileVO.builder()
                .fileMd5(subname)
                .fileUrl(localUrl + "/file/" + subname)
                .name(map.get("fileName"))
                .fileSize(map.get("fileSize"))
                .openFile(0)
                .delFlag(0)
                .userId(userVo.getId())
                .createTime(new Date()).build();
        int count = ysSlaveFileMapper.insertSelective(slave);
        if(count > 0){
            FileVO fileVo = FileVO.builder()
                    .id(slave.getId())
                    .fileName(map.get("fileName"))
                    .newfileName(subname)
                    .build();
            return Callback.success(fileVo);
        }
        return Callback.error("附件上传失败!");
    }

    public Callback addMasterFile(ModifyFileVO modifyFileVO) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        String formatTime = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        YsMasterFileVO master = YsMasterFileVO.builder()
                .serialNumber(modifyFileVO.getFilePrefix() + formatTime)
                .fileName(modifyFileVO.getFileName())
                .openFile(0)
                .delFlag(0)
                .ysFileTypeId(modifyFileVO.getYsFileTypeId())
                .ysMasterTaskId(modifyFileVO.getYsMasterTaskId())
                .ysSonTaskId(modifyFileVO.getYsSonTaskId())
                .fileContent(modifyFileVO.getFileContent())
                .versionNo(modifyFileVO.getVersionNo())
                .userId(userVo.getId())
                .dayrepTime(modifyFileVO.getDayrepTime())
                .createTime(new Date())
                .build();
        ysMasterFileMapper.insertSelective(master);
        Integer masterId = master.getId();
        List<FileVO> fileVOList = modifyFileVO.getFileVOList();
        if (CollectionUtil.isNotEmpty(fileVOList)){
            for (FileVO fileVO : fileVOList) {
                YsSlaveFileVO ysSlaveFile = YsSlaveFileVO.builder().id(fileVO.getId()).updateTime(new Date()).fileMasterId(masterId).build();
                ysSlaveFileMapper.updateByPrimaryKeySelective(ysSlaveFile);
            }
        }
        return Callback.success("保存成功");
    }


    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        URL url;
        URLConnection conn;
        InputStream inputStream = null;
        try {
            // 这里填文件的url地址
            url = new URL(netAddress);
            conn = url.openConnection();
            inputStream = conn.getInputStream();
            response.setContentType(conn.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(filename, "UTF-8"));
            byte[] buffer = new byte[1024];
            int len;
            OutputStream outputStream = response.getOutputStream();
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Callback<List<LinkedHashMap>> queryMasterTask(HttpServletRequest req) {
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
        List<LinkedHashMap> list = null;
        if (roleCode.equals("sub_task_manage")){
            List<LinkedHashMap> list1  = ysMasterFileMapper.queryMasterTaskByUid(userId);
            //新增子任务管理归属哪个团队的主任务作为下拉菜单新增+搜索
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userId);
            if (CollectionUtil.isNotEmpty(ysTeams)){
                List<Integer> maskTaskIdList = ysTeams.stream().map(e -> e.getYsMasterTaskId()).distinct().collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(maskTaskIdList)){
                    List<LinkedHashMap> list2 = ysMasterFileMapper.selectMaster(maskTaskIdList);
                    list1.addAll(list2);
                    list = list1.stream().distinct().collect(Collectors.toList());
                }
            }
        }else if (roleCode.equals("executor")){
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userId);
            if (CollectionUtil.isNotEmpty(ysTeams)){
                List<Integer> teamIds = ysTeams.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<YsSonTask> ysSonTaskList = ysSonTaskMapper.selectAllByYsTeamIds(teamIds);
                if (CollectionUtil.isNotEmpty(ysSonTaskList)){
                    List<Integer> ysMasterTaskIdList = ysSonTaskList.stream().map(e -> e.getYsMasterTaskId()).collect(Collectors.toList());
                    list = ysMasterFileMapper.selectMaster(ysMasterTaskIdList);
                }
            }
        }else {
            list = ysMasterFileMapper.queryMasterTask();
        }
        return Callback.success(list);
//        if (!roleCode.equals("sub_task_manage") ){
//            //角色不是子任务管理
//            list = ysMasterFileMapper.queryMasterTask();
//        }else {
//            list = ysMasterFileMapper.queryMasterTaskByUid(userId);
//        }
    }


    public Callback<BaseVO<FileDetailVO>> getDayRepDetails(Integer page, Integer pageSize, Integer ysMasterTaskId,
                                                            String fileName, String startTime, String endTime, HttpServletRequest req) {
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
        BaseVO<FileDetailVO> baseVO = null ;
        String filePrefix = "gzrb";
        //任务管理员
        if (roleCode.equals("task_manage")){
            String subRoleCode = "sub_task_manage";
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<FileDetailVO> list = ysMasterFileMapper.getDayRepsByRoleCode(filePrefix,ysMasterTaskId,fileName,userId,subRoleCode,startTime,endTime);
            baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        }else if (roleCode.equals("sub_task_manage")){
            //1.归属团队数据
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userId);
            if (CollectionUtil.isNotEmpty(ysTeams)){
                List<Integer> maskTaskIdList = ysTeams.stream().map(e -> e.getYsMasterTaskId()).distinct().collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(maskTaskIdList)){
                    Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
                    List<FileDetailVO> list = ysMasterFileMapper.getDayRepDetailsByStm(maskTaskIdList,filePrefix,ysMasterTaskId,fileName,userId,startTime,endTime);
                    baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
                }
            }
            //2.自己数据
            //3.下属数据
        }else if (roleCode.equals("executor")){
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<FileDetailVO> list = ysMasterFileMapper.getDayRepsBySelf(filePrefix,ysMasterTaskId,fileName,userId,startTime,endTime);
            baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        }else {
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<FileDetailVO> list = ysMasterFileMapper.getDayRepAll(filePrefix,ysMasterTaskId,fileName,userId,startTime,endTime);
            baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        }
        return Callback.success(baseVO);
    }
}
