package com.ennova.pubinfonew.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfonew.dao.NewsPeriodicalFileMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalPictureMapper;
import com.ennova.pubinfonew.dto.FileDelDTO;
import com.ennova.pubinfonew.dto.FileDto;
import com.ennova.pubinfonew.entity.NewsPeriodicalFile;
import com.ennova.pubinfonew.vo.FileVO;
import com.ennova.pubinfonew.vo.NewsVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class NewsPeriodicalFileService {

    private final HttpServletRequest request;
    private final NewsPeriodicalFileMapper newsPeriodicalFileMapper;
    private final NewsPeriodicalMapper newsPeriodicalMapper;
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

    public Callback<FileVO> selectFile(MultipartFile file) {
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

        NewsPeriodicalFile files = NewsPeriodicalFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(new Date()).build();
        int count = newsPeriodicalFileMapper.insertSelective(files);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(files.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "选择文件失败!");
    }

    public Callback upload(FileDto fileDto) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (fileDto.getPeriodicalNum() != null) {
            NewsPeriodicalFile newsPeriodicalFile = newsPeriodicalFileMapper.selectByPrimaryKey(fileDto.getId());
            newsPeriodicalFile.setNewsPeriodicalNum(fileDto.getPeriodicalNum());
            newsPeriodicalFile.setNewsEditionNum(fileDto.getEditionNum());
            int i = newsPeriodicalFileMapper.updateByPrimaryKeySelective(newsPeriodicalFile);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "上传失败!");
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

    public Callback deleteFile(FileDelDTO fileDelDTO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        fileDelDTO.getFileVos().forEach(fileVo -> {
            String path = localPath + "/" + fileVo.getNewfileName();
            // 如果是本人上传的，才能执行删除操作
            assert userVo != null;
            List<NewsPeriodicalFile> files = newsPeriodicalFileMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = newsPeriodicalFileMapper.selectByFileMd5(fileVo.getNewfileName());
                    int i = newsPeriodicalPictureMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count + i <= 1) {
                        file.delete();
                    }
                    newsPeriodicalFileMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success("附件删除成功");
    }

    public Callback<BaseVO<NewsVO>> selectPeriodicalFile(Integer page, Integer pageSize, Integer periodicalNum, Integer editionNum) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<NewsVO> newsVOS = newsPeriodicalFileMapper.selectPeriodicalFile(periodicalNum, editionNum);
        List<NewsVO> collect = newsVOS.stream().filter(newsVO -> newsVO.getPeriodicalNum() != null).collect(Collectors.toList());
        BaseVO<NewsVO> baseVO = new BaseVO<>(collect, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

}
