package com.ennova.pubinfodaily.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfodaily.dao.*;
import com.ennova.pubinfodaily.entity.*;
import com.ennova.pubinfodaily.vo.*;
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
import javax.xml.ws.Action;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YsDailyRepService {
    /**
     * 访问URL
     */
    @Value("${spring.upload.local.url}")
    private String localUrl;

    /**
     * 本地路径
     */
    @Value("${spring.upload.local.path}")
    private String localPath;

    @Autowired
    private YsDailyRepMapper ysDailyRepMapper;
    @Autowired
    private YsDailyRepFileMapper ysDailyRepFileMapper;
    @Autowired
    private YsDailyFeedBackMapper ysDailyFeedBackMapper;
    @Autowired
    private TDeptMapper tDeptMapper;
    @Autowired
    private TUserMapper tUserMapper;


    public Callback insertOrUpdate(YsDailyRepVO ysDailyRepVO) {
        if (null != ysDailyRepVO.getId()){
            YsDailyRep task = ysDailyRepMapper.selectByPrimaryKey(ysDailyRepVO.getId());
            if (null != task){
                //更新
                YsDailyRep ysDailyRep = YsDailyRep.builder().id(ysDailyRepVO.getId())
                        .fileName(ysDailyRepVO.getFileName())
                        .fileContent(ysDailyRepVO.getFileContent())
                        .dailyRepTime(ysDailyRepVO.getDailyRepTime())
                        .updateTime(new Date())
                        .build();
                ysDailyRepMapper.updateByPrimaryKeySelective(ysDailyRep);
                Integer  ysDayRepId = ysDailyRep.getId();
                //更新附件表
                List<FileVO> fileVOList = ysDailyRepVO.getFileVOList();
                if (CollectionUtil.isNotEmpty(fileVOList)){
                    for (FileVO fileVO : fileVOList) {
                        YsDailyRepFile ysDayRepFile = YsDailyRepFile.builder()
                                .id(fileVO.getId())
                                .updateTime(new Date())
                                .dailyRepId(ysDayRepId)
                                .build();
                        ysDailyRepFileMapper.updateByPrimaryKeySelective(ysDayRepFile);
                    }
                }
                return Callback.success("日报修改成功");
            }
        }else {
            //新增
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = req.getHeader("Authorization");
            UserVO userVo = JWTUtil.getUserVOByToken(token);
            YsDailyRep ysDailyRep = YsDailyRep.builder()
                    .fileName(ysDailyRepVO.getFileName())
                    .openFile(0)
                    .delFlag(0)
                    .fileContent(ysDailyRepVO.getFileContent())
                    .userId(userVo.getId())
                    .dailyRepTime(ysDailyRepVO.getDailyRepTime())
                    .createTime(new Date())
                    .build();
            ysDailyRepMapper.insertSelective(ysDailyRep);
            Integer ysDailyRepId = ysDailyRep.getId();
            //回写日报文件表
            List<FileVO> fileVOList = ysDailyRepVO.getFileVOList();
            if (CollectionUtil.isNotEmpty(fileVOList)){
                for (FileVO fileVO : fileVOList) {
                    YsDailyRepFile ysDayRepFile = YsDailyRepFile.builder().id(fileVO.getId()).updateTime(new Date()).dailyRepId(ysDailyRepId).build();
                    ysDailyRepFileMapper.updateByPrimaryKeySelective(ysDayRepFile);
                }
            }
            return Callback.success("日报新增成功");
        }
        return Callback.error("日报操作失败!");
    }

    public Callback deleteDailyRep(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        ysDailyRepFileMapper.deleteYsDailyRepFile(id);
        int result = ysDailyRepMapper.delete(id);
        if (result < 1) {
            return Callback.error("删除失败");
        }
        return Callback.success();
    }

    public Callback<YsDailyRepVO> selectByPrimaryKey(Integer id) {
        if (id == null) {
            return Callback.error("日报id不能为空");
        }
        YsDailyRepVO ysDayRepVO = ysDailyRepMapper.selectDetailOne(id);
        if(null != ysDayRepVO){
            return Callback.success(ysDayRepVO);
        }
        return Callback.error("日报无数据!");
    }

    public Callback addFeedContent(FeedBackVO feedBackVO) {
        YsDailyFeedBack record = YsDailyFeedBack.builder()
                .feedContent(feedBackVO.getFeedContent())
                .dailyRepId(feedBackVO.getId())
                .feedStatus(1)
                .createTime(new Date())
                .userId(Integer.parseInt(feedBackVO.getUserId())).build();
        int i = ysDailyFeedBackMapper.insertSelective(record);
        if (i > 0){
            //推送
            Integer drId = feedBackVO.getId();
            YsDailyRep ysDailyRep = ysDailyRepMapper.selectByPrimaryKey(drId);
            Integer userId = ysDailyRep.getUserId();//创建日报用户ID
            MessageVO<Object> messageVO = MessageVO.builder().sourceType(2).type(4).content("你的日报被反馈!").title(ysDailyRep.getFileName()).userId(userId).backId(ysDailyRep.getId()).build();
//            pubInfoTaskClient.sendMessByMessageVO(messageVO);
            return Callback.success("反馈成功!");
        }
        return Callback.error("反馈失败!");
    }

    public Callback<List<FileDownVO>> fileDetail(Integer id) {
        List<FileDownVO> fileDownVOList = ysDailyRepMapper.fileDetail(id);
        return Callback.success(fileDownVOList);
    }

    public Callback<BaseVO<DailyRepDetailVO>> getDailyyRepDetails(Integer page, Integer pageSize, String fileName, String startTime, String endTime, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return Callback.error("无权限token");
        }
        UserVO userVO = JWTUtil.getUserVOByToken(token);
        Integer userId = userVO.getId();
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        BaseVO<DailyRepDetailVO> baseVO = null ;
        //查询check_id
        List<TUser> tUsers = new ArrayList<>();
        TUser tUser = tUserMapper.selectByPrimaryKey(userId);
        List<TDept> tDeptsCheck = tDeptMapper.selectByCheckId(tUser.getUserId());
        if (CollectionUtil.isNotEmpty(tDeptsCheck)){
            List<Long> deptIds = tDeptsCheck.stream().map(e -> e.getDeptId()).collect(Collectors.toList());
            tUsers = tUserMapper.selectByDepartment(deptIds);
            //根据用户ID查日报
        }else {
            List<TDept> tDeptsManage = tDeptMapper.selectByManageId(tUser.getUserId());
            if (CollectionUtil.isNotEmpty(tDeptsManage)){
                List<Long> deptIds = tDeptsManage.stream().map(e -> e.getDeptId()).collect(Collectors.toList());
                tUsers = tUserMapper.selectByDepartment(deptIds);
            }else {
                tUsers.add(tUser);
            }
        }
        List<Integer> userIds = tUsers.stream().map(e -> e.getId()).collect(Collectors.toList());
        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
        List<DailyRepDetailVO> list = ysDailyRepMapper.getDayRepByUserIds(fileName,userIds,startTime,endTime);
        baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
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
        YsDailyRepFile record = YsDailyRepFile.builder()
                .fileMd5(subname)
                .fileUrl(localUrl + "/file/" + subname)
                .name(map.get("fileName"))
                .fileSize(map.get("fileSize"))
                .openFile(0)
                .delFlag(0)
                .userId(userVo.getId())
                .createTime(new Date()).build();
        int i = ysDailyRepFileMapper.insertSelective(record);
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
            List<YsDailyRepFile> files = ysDailyRepFileMapper.selectAllByFileMd5AndUserId(e.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = ysDailyRepFileMapper.selectByFileMd5(e.getNewfileName());
                    if(count == 1){
                        file.delete();
                    }
                    ysDailyRepFileMapper.deleteByPrimaryKey(e.getId());
                }
            }
        });
        if (null != fileRepDelVO.getDailyRepId()){
            int count = ysDailyRepFileMapper.selectByDailyRepId(fileRepDelVO.getDailyRepId());
            if (count == 0){
                // slave 文件中没有对应的 master, 再删除唯一的 master
                ysDailyRepMapper.deleteByPrimaryKey(fileRepDelVO.getDailyRepId());
            }
        }
        return Callback.success("附件删除成功");
    }
}
