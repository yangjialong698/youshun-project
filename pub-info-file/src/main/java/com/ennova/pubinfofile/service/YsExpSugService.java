package com.ennova.pubinfofile.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfofile.dao.*;
import com.ennova.pubinfofile.entity.*;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YsExpSugService {

    @Autowired
    private YsExpSugMapper ysExpSugMapper;
    @Autowired
    private YsExpSugFileMapper ysExpSugFileMapper;
    @Autowired
    private YsSugCommentMapper ysSugCommentMapper;
    @Autowired
    private PubInfoUserClient pubInfoUserClient;
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

    public Callback insertOrUpdate(YsExpSugVO ysExpSugVO) {
        if (null != ysExpSugVO.getId()){
            YsExpSug task = ysExpSugMapper.selectByPrimaryKey(ysExpSugVO.getId());
            if (null != task){
                //更新
                YsExpSug ysExpSug = YsExpSug.builder().id(ysExpSugVO.getId())
                        .fileName(ysExpSugVO.getFileName())
                        .ysMasterTaskId(ysExpSugVO.getYsMasterTaskId())
                        .fileContent(ysExpSugVO.getFileContent())
                        .sugTime(ysExpSugVO.getSugTime())
                        .updateTime(new Date())
                        .build();
                ysExpSugMapper.updateByPrimaryKeySelective(ysExpSug);
                Integer  ysExpSugId = ysExpSug.getId();
                //更新附件表
                List<FileVO> fileVOList = ysExpSugVO.getFileVOList();
                if (CollectionUtil.isNotEmpty(fileVOList)){
                    for (FileVO fileVO : fileVOList) {
                        YsExpSugFile ysDayRepFile = YsExpSugFile.builder()
                                .id(fileVO.getId())
                                .updateTime(new Date())
                                .expSugId(ysExpSugId)
                                .build();
                        ysExpSugFileMapper.updateByPrimaryKeySelective(ysDayRepFile);
                    }
                }
                return Callback.success("经验建议修改成功");
            }
        }else {
            //新增
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = req.getHeader("Authorization");
            UserVO userVo = JWTUtil.getUserVOByToken(token);
            YsExpSug ysExpSug = YsExpSug.builder()
                    .fileName(ysExpSugVO.getFileName())
                    .openFile(0)
                    .delFlag(0)
                    .ysMasterTaskId(ysExpSugVO.getYsMasterTaskId())
                    .fileContent(ysExpSugVO.getFileContent())
                    .userId(userVo.getId())
                    .sugTime(ysExpSugVO.getSugTime())
                    .createTime(new Date())
                    .build();
            ysExpSugMapper.insertSelective(ysExpSug);
            Integer ysExpSugId = ysExpSug.getId();
            //回写经验建议文件表
            List<FileVO> fileVOList = ysExpSugVO.getFileVOList();
            if (CollectionUtil.isNotEmpty(fileVOList)){
                for (FileVO fileVO : fileVOList) {
                    YsExpSugFile ysExpSugFile = YsExpSugFile.builder().id(fileVO.getId()).updateTime(new Date()).expSugId(ysExpSugId).build();
                    ysExpSugFileMapper.updateByPrimaryKeySelective(ysExpSugFile);
                }
            }
            return Callback.success("经验建议新增成功");
        }
        return Callback.error("经验建议操作失败!");

    }

    public Callback deleteExpSug(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        ysExpSugFileMapper.deleteExpSugFile(id);
        int result = ysExpSugMapper.delete(id);
        if (result < 1) {
            return Callback.error("删除失败");
        }
        return Callback.success();
    }

    public Callback<YsExpSugVO> selectByPrimaryKey(Integer id) {
        if (id == null) {
            return Callback.error("经验建议id不能为空");
        }
        YsExpSugVO ysExpSugVO = ysExpSugMapper.selectDetailOne(id);
        if(null != ysExpSugVO){
            return Callback.success(ysExpSugVO);
        }
        return Callback.error("经验建议无数据!");
    }

    public Callback addSugComment(SugCommentVO sugCommentVO) {
        YsSugComment record = YsSugComment.builder()
                .sugContent(sugCommentVO.getSugContent())
                .expSugId(sugCommentVO.getId())
                .sugStatus(1)
                .createTime(new Date())
                .userId(Integer.parseInt(sugCommentVO.getUserId())).build();
        int i = ysSugCommentMapper.insertSelective(record);
        if (i > 0){
            return Callback.success("评论成功!");
        }
        return Callback.error("评论失败!");

    }

    public Callback<List<FileDownVO>> fileDetail(Integer id) {
        List<FileDownVO> fileDownVOList = ysExpSugMapper.fileDetail(id);
        return Callback.success(fileDownVOList);
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
        YsExpSugFile record = YsExpSugFile.builder()
                .fileMd5(subname)
                .fileUrl(localUrl + "/file/" + subname)
                .name(map.get("fileName"))
                .fileSize(map.get("fileSize"))
                .openFile(0)
                .delFlag(0)
                .userId(userVo.getId())
                .createTime(new Date()).build();
        int i = ysExpSugFileMapper.insertSelective(record);
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

    public Callback delFiles(ExpSugDelVO expSugDelVO) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        List<FileVO> fileVOList = expSugDelVO.getFileVOList();
        fileVOList.forEach(e->{
            String path = localPath +  "/" + e.getNewfileName();
            List<YsExpSugFile> files = ysExpSugFileMapper.selectAllByFileMd5AndUserId(e.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = ysExpSugFileMapper.selectByFileMd5(e.getNewfileName());
                    if(count == 1){
                        file.delete();
                    }
                    ysExpSugFileMapper.deleteByPrimaryKey(e.getId());
                }
            }
        });
        if (null != expSugDelVO.getExpSugId()){
            int count = ysExpSugFileMapper.selectByExpSugId(expSugDelVO.getExpSugId());
            if (count == 0){
                ysExpSugMapper.deleteByPrimaryKey(expSugDelVO.getExpSugId());
            }
        }
        return Callback.success("附件删除成功");
    }

    public Callback<BaseVO<ExpSugDetailVO>> getExpSugDetails(Integer page, Integer pageSize, Integer ysMasterTaskId, String fileName, HttpServletRequest req) {
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
//        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
//        List<ExpSugDetailVO> list = ysExpSugMapper.getFileDetails(ysMasterTaskId,fileName,userId);
//        BaseVO<ExpSugDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
//        return Callback.success(baseVO);
        if (roleCode.equals("sub_task_manage")){
            //角色是子任务管理
            Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
            List<ExpSugDetailVO> list = ysExpSugMapper.getFileDetailsForZrw(ysMasterTaskId,fileName,userId);
            BaseVO<ExpSugDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
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
                        List<ExpSugDetailVO> list = ysExpSugMapper.getDetailsByMaskTaskIds(maskTaskIdList,ysMasterTaskId,fileName);
                        BaseVO<ExpSugDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
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
            List<ExpSugDetailVO> list = ysExpSugMapper.getFileDetails(ysMasterTaskId,fileName,userId);
            BaseVO<ExpSugDetailVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
            return Callback.success(baseVO);
        }
    }
}
