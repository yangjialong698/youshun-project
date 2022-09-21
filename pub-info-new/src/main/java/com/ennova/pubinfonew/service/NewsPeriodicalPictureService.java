package com.ennova.pubinfonew.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfonew.dao.NewsPeriodicalMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalPictureMapper;
import com.ennova.pubinfonew.dto.FileDelDTO;
import com.ennova.pubinfonew.dto.FileDto;
import com.ennova.pubinfonew.entity.NewsPeriodicalPicture;
import com.ennova.pubinfonew.vo.FileVO;
import com.ennova.pubinfonew.vo.NewsPeriodicalVO;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class NewsPeriodicalPictureService {

    private final HttpServletRequest request;
    private final NewsPeriodicalPictureMapper newsPeriodicalPictureMapper;
    private final NewsPeriodicalMapper newsPeriodicalMapper;

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

        NewsPeriodicalPicture periodicalPicture = NewsPeriodicalPicture.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(new Date()).build();
        int count = newsPeriodicalPictureMapper.insertSelective(periodicalPicture);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(periodicalPicture.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "选择文件失败!");
    }

    public Callback<List<Integer>> getEditionNum(Integer periodicalNum) {
        List<NewsVO> newsVOS = newsPeriodicalPictureMapper.selectPeriodicalPicture(periodicalNum, null);
        List<NewsVO> collect = newsVOS.stream().filter(newsVO -> newsVO.getPeriodicalNum() != null).sorted(Comparator.comparing(NewsVO::getEditionNum)).collect(Collectors.toList());
        List<Integer> editionNums = collect.stream().map(newsVO -> newsVO.getEditionNum()).collect(Collectors.toList());
        List<Integer> collect1 = editionNums.stream().distinct().collect(Collectors.toList());
        return Callback.success(collect1);
    }

    public Callback<List<Integer>> getPeriodicalNum() {
        List<NewsVO> newsVOS = newsPeriodicalPictureMapper.selectPeriodicalPicture(null, null);
        List<NewsVO> collect = newsVOS.stream().filter(newsVO -> newsVO.getPeriodicalNum() != null).sorted(Comparator.comparing(NewsVO::getPeriodicalNum).reversed()).collect(Collectors.toList());
        List<Integer> periodicalNums = collect.stream().map(newsVO -> newsVO.getPeriodicalNum()).collect(Collectors.toList());
        List<Integer> collect1 = periodicalNums.stream().distinct().collect(Collectors.toList());
        return Callback.success(collect1);
    }

    public Callback<NewsVO> getPeriodicalAndEditionNum(Integer periodicalNum, Integer editionNum) {
        NewsVO newsVO = newsPeriodicalPictureMapper.selectPeriodicalAndedition(periodicalNum, editionNum);
        if (ObjectUtils.isNotEmpty(newsVO)) {
            List<NewsVO> UpPeriodicals = newsPeriodicalPictureMapper.selectPeriodicalPicture(periodicalNum - 1, null);
            if (UpPeriodicals.size() > 0) {
                newsVO.setUpPeriodical(true);
            } else {
                newsVO.setUpPeriodical(false);
            }
            List<NewsVO> downPeriodicals = newsPeriodicalPictureMapper.selectPeriodicalPicture(periodicalNum + 1, null);
            if (downPeriodicals.size() > 0) {
                newsVO.setDownPeriodical(true);
            } else {
                newsVO.setDownPeriodical(false);
            }
            List<NewsVO> upEditions = newsPeriodicalPictureMapper.selectPeriodicalPicture(periodicalNum, editionNum - 1);
            if (upEditions.size() > 0) {
                newsVO.setUpEdition(true);
            } else {
                newsVO.setUpEdition(false);
            }
            List<NewsVO> downEditions = newsPeriodicalPictureMapper.selectPeriodicalPicture(periodicalNum, editionNum + 1);
            if (downEditions.size() > 0) {
                newsVO.setDownEdition(true);
            } else {
                newsVO.setDownEdition(false);
            }
        }else {
            newsVO = NewsVO.builder().upPeriodical(false).downPeriodical(false).upEdition(false).downEdition(false).build();
        }
        return Callback.success(newsVO);
    }

    public Callback upload(FileDto fileDto) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (fileDto.getPeriodicalNum() != null) {
            List<NewsVO> newsVOS = newsPeriodicalPictureMapper.selectPeriodicalPicture(fileDto.getPeriodicalNum(), fileDto.getEditionNum());
            if (newsVOS.size() > 0) {
                return Callback.error("该期对应版数已存在，请勿重复上传");
            }
            NewsPeriodicalPicture newsPeriodicalPicture = newsPeriodicalPictureMapper.selectByPrimaryKey(fileDto.getId());
            newsPeriodicalPicture.setNewsPeriodicalNum(fileDto.getPeriodicalNum());
            newsPeriodicalPicture.setNewsEditionNum(fileDto.getEditionNum());
            int i = newsPeriodicalPictureMapper.updateByPrimaryKeySelective(newsPeriodicalPicture);
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
            List<NewsPeriodicalPicture> files = newsPeriodicalPictureMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = newsPeriodicalPictureMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count == 1) {
                        file.delete();
                    }
                    newsPeriodicalPictureMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success("附件删除成功");
    }

    public Callback<BaseVO<NewsVO>> selectPeriodicalPicture(Integer page, Integer pageSize, Integer periodicalNum, Integer editionNum) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<NewsVO> newsVOS = newsPeriodicalPictureMapper.selectPeriodicalPicture(periodicalNum, editionNum);
        for (NewsVO newsVO : newsVOS) {
            List<NewsPeriodicalVO> newsPeriodicalVOS = newsPeriodicalMapper.selectAllNewPeriodical(newsVO.getPeriodicalNum(), newsVO.getEditionNum(), null);
            if (CollectionUtil.isNotEmpty(newsPeriodicalVOS)) {
                newsVO.setChangeStatus(false);
            } else {
                newsVO.setChangeStatus(true);
            }
        }
        List<NewsVO> collect = newsVOS.stream().filter(newsVO -> newsVO.getPeriodicalNum() != null).collect(Collectors.toList());
        BaseVO<NewsVO> baseVO = new BaseVO<>(collect, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<NewsVO> selectPeriodicalChangeStatus(Integer periodicalNum, Integer editionNum) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (periodicalNum == null && editionNum == null) {
            return Callback.error(2, "期刊期数和版数不能为空");
        }
        List<NewsPeriodicalVO> newsPeriodicalVOS = newsPeriodicalMapper.selectAllNewPeriodical(periodicalNum, editionNum, null);
        if (CollectionUtil.isNotEmpty(newsPeriodicalVOS)) {
            NewsVO newsVO = newsPeriodicalPictureMapper.selectPeriodicalAndedition(periodicalNum, editionNum);
            newsVO.setChangeStatus(false);
            return Callback.success(newsVO);
        } else {
            return Callback.success(NewsVO.builder().changeStatus(true).build());
        }
    }
}
