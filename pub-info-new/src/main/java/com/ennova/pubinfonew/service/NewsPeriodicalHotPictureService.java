package com.ennova.pubinfonew.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfonew.dao.NewsPeriodicalHotPictureMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalPictureMapper;
import com.ennova.pubinfonew.dto.FileDelDTO;
import com.ennova.pubinfonew.entity.NewsPeriodicalHotPicture;
import com.ennova.pubinfonew.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/22
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class NewsPeriodicalHotPictureService {

    private final HttpServletRequest request;
    private final NewsPeriodicalHotPictureMapper newsPeriodicalHotPictureMapper;
    private final NewsPeriodicalPictureMapper newsPeriodicalPictureMapper;

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

    public Callback<FileVO> selectHotPicture(MultipartFile file) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            return Callback.error(2, "上传文件不能为空!");
        }
        HashMap<String, String> map = FileUtils.uploadFile(file, localPath, fileSuffix);
        if (StringUtils.isNotBlank(map.get("error"))) {
            return Callback.error(2, map.get("error"));
        }
        String subname = map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");

        NewsPeriodicalHotPicture newsPeriodicalHotPicture = NewsPeriodicalHotPicture.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(new Date()).build();
        int count = newsPeriodicalHotPictureMapper.insertSelective(newsPeriodicalHotPicture);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(newsPeriodicalHotPicture.getId()).fileName(map.get("fileName")).fileUrl(newsPeriodicalHotPicture.getFileUrl()).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "选择文件失败!");
    }


    public Callback deleteHotPicture(FileDelDTO fileDelDTO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        fileDelDTO.getFileVos().forEach(fileVo -> {
            String path = localPath + "/" + fileVo.getNewfileName();
            // 如果是本人上传的，才能执行删除操作
            assert userVo != null;
            List<NewsPeriodicalHotPicture> files = newsPeriodicalHotPictureMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = newsPeriodicalHotPictureMapper.selectByFileMd5(fileVo.getNewfileName());
                    int i = newsPeriodicalPictureMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count + i <= 1) {
                        file.delete();
                    }
                    newsPeriodicalHotPictureMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success("附件删除成功");
    }
}
