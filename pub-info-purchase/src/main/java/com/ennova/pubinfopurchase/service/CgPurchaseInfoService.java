package com.ennova.pubinfopurchase.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.CgContactInformationMapper;
import com.ennova.pubinfopurchase.dao.CgPurchaseFileMapper;
import com.ennova.pubinfopurchase.dao.CgPurchaseInfoMapper;
import com.ennova.pubinfopurchase.dto.FileDelDTO;
import com.ennova.pubinfopurchase.entity.CgContactInformation;
import com.ennova.pubinfopurchase.entity.CgPurchaseFile;
import com.ennova.pubinfopurchase.entity.CgPurchaseInfo;
import com.ennova.pubinfopurchase.service.fegin.PubInfoTaskClient;
import com.ennova.pubinfopurchase.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class CgPurchaseInfoService {

    private final HttpServletRequest request;
    private final CgPurchaseInfoMapper cgPurchaseInfoMapper;
    private final CgPurchaseFileMapper cgPurchaseFileMapper;
    private final CgContactInformationMapper cgContactInformationMapper;
    private final PubInfoTaskClient pubInfoTaskClient;

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

        CgPurchaseFile purchaseFile = CgPurchaseFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(new Date()).build();
        int count = cgPurchaseFileMapper.insertFileSelective(purchaseFile);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(purchaseFile.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
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
            List<CgPurchaseFile> files = cgPurchaseFileMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = cgPurchaseFileMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count == 1) {
                        file.delete();
                    }
                    cgPurchaseFileMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success("附件删除成功");
    }

    public Callback insertOrUpdate(CgPurchaseInfoVO cgPurchaseInfoVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CgPurchaseInfo cgPurchaseInfo = new CgPurchaseInfo();
        BeanUtils.copyProperties(cgPurchaseInfoVO, cgPurchaseInfo);
        /*CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if (!cgPurchaseInfoVO.getPurchaseName().equals(currentUserVO.getUsername())){
            return Callback.error("采购人填写有误，应与登录用户名一致");
        }*/
        //根据需求任务编号改为非必填项
        /*List<TaskNumber> taskNumbers = cgPurchaseInfoMapper.selectTaskNumberIsExist(Integer.parseInt(cgPurchaseInfoVO.getTaskNumber()));
        if (taskNumbers.isEmpty()){
            return Callback.error("请输入正确的任务编号");
        }*/
        if (cgPurchaseInfoVO.getId() != null) {
            //修改采集信息
            CgPurchaseInfo purchaseInfo = cgPurchaseInfoMapper.selectByPrimaryKey(cgPurchaseInfoVO.getId());
            if (purchaseInfo != null) {
                cgPurchaseInfo.setUpdateTime(new Date());
                cgPurchaseInfo.setSerialNumber(purchaseInfo.getSerialNumber());
                cgPurchaseInfo.setCreateTime(purchaseInfo.getCreateTime());
                cgPurchaseInfo.setId(purchaseInfo.getId());
                cgPurchaseInfoMapper.updateByPrimaryKey(cgPurchaseInfo);
                if (cgPurchaseInfoVO.getFileVOList() != null && !cgPurchaseInfoVO.getFileVOList().isEmpty()) {
                    for (FileVO fileVO : cgPurchaseInfoVO.getFileVOList()) {
                        CgPurchaseFile cgPurchaseFile = cgPurchaseFileMapper.selectByPrimaryKey(fileVO.getId());
                        Optional.ofNullable(cgPurchaseFile).ifPresent(cgPurchaseFile1 -> {
                            CgPurchaseFile cgPurchaseFile2 = CgPurchaseFile.builder().id(fileVO.getId()).updateTime(new Date()).purchaseInfoId(cgPurchaseInfo.getId()).build();
                            cgPurchaseFileMapper.updateByPrimaryKeySelective(cgPurchaseFile2);
                        });
                    }
                }
                return Callback.success(true);
            }
        } else {
            //发布采集信息
            Integer number = cgPurchaseInfoMapper.selectLastSerialNumber();
            Integer serialNumber = getSerialNumber(number);
            cgPurchaseInfo.setSerialNumber(serialNumber);
            cgPurchaseInfo.setCreateTime(new Date());
            cgPurchaseInfo.setIssuerId(userVo.getId());
            cgPurchaseInfoMapper.insertInfoSelective(cgPurchaseInfo);
            if (cgPurchaseInfoVO.getFileVOList() != null && !cgPurchaseInfoVO.getFileVOList().isEmpty()) {
                for (FileVO fileVO : cgPurchaseInfoVO.getFileVOList()) {
                    CgPurchaseFile cgPurchaseFile = cgPurchaseFileMapper.selectByPrimaryKey(fileVO.getId());
                    Optional.ofNullable(cgPurchaseFile).ifPresent(cgPurchaseFile1 -> {
                        CgPurchaseFile cgPurchaseFile2 = CgPurchaseFile.builder().id(fileVO.getId()).updateTime(new Date()).purchaseInfoId(cgPurchaseInfo.getId()).build();
                        cgPurchaseFileMapper.updateByPrimaryKeySelective(cgPurchaseFile2);
                    });
                }
            }
            return Callback.success(true);
        }
        return Callback.error(2, "数据处理失败!");
    }

    public Callback selectTaskNumber(String name) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        List<TaskNumber> taskNumbers = cgPurchaseInfoMapper.selectTaskNumber(name);
        return Callback.success(taskNumbers);
    }

    public Callback delete(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CgPurchaseInfo cgPurchaseInfo = cgPurchaseInfoMapper.selectByPrimaryKey(id);
        List<CgPurchaseFileVO> cgPurchaseFileVOS = cgPurchaseFileMapper.selectAllByPurchaseInfoId(id);
        if (cgPurchaseInfo != null) {
            int i = cgPurchaseInfoMapper.deleteByPrimaryKey(id);
            if (cgPurchaseFileVOS != null){
                cgPurchaseFileVOS.forEach(cgPurchaseFileVO -> {
                    cgPurchaseFileMapper.deleteByPrimaryKey(cgPurchaseFileVO.getId());
                });
            }
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

    private Integer getSerialNumber(Integer serialNumber) {
        String dayStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        String[] dayArr = dayStr.split("-");

        String year = dayArr[0].substring(2, 4);
        String month = dayArr[1];
        if (serialNumber == null || serialNumber.equals(0)) {
            String newNumber = year + month + "001";
            return Integer.parseInt(newNumber);
        }

        String stringNum = String.valueOf(serialNumber);
        String year2 = stringNum.substring(0, 2);
        String month2 = stringNum.substring(2, 4);

        // 如果日期一致，相加
        if (year.equals(year2) && month.equals(month2)) {
            return serialNumber.intValue() + 1;
        }

        // 日期不一致，获取一个新的日期
        String newNumber = year + month + "001";
        return Integer.parseInt(newNumber);
    }

    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(Integer page, Integer pageSize, String name) {
       /* String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;*/
        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<CgPurchaseInfoVO> cgPurchaseInfos = cgPurchaseInfoMapper.selectPurchaseInfo(name);
        cgPurchaseInfos.forEach(cgPurchaseInfoVO -> {
            List<FileVO> fileVOS = cgPurchaseFileMapper.selectByPurchaseInfoId(cgPurchaseInfoVO.getId());
            if (CollectionUtil.isNotEmpty(fileVOS)){
                cgPurchaseInfoVO.setFileVOList(fileVOS);
            }else {
                cgPurchaseInfoVO.setFileVOList(null);
            }
        });
        BaseVO<CgPurchaseInfoVO> baseVO = new BaseVO<>(cgPurchaseInfos, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<List<CgPurchaseInfoVO>> selectAllPurchaseInfo(String name) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        List<CgPurchaseInfoVO> cgPurchaseInfos = cgPurchaseInfoMapper.selectPurchaseInfo(name);
        return Callback.success(cgPurchaseInfos);
    }

    public Callback<CgPurchaseInfoVO> getDetail(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        List<FileVO> cgPurchaseFiles = cgPurchaseFileMapper.selectByPurchaseInfoId(id);
        CgPurchaseInfo cgPurchaseInfo = cgPurchaseInfoMapper.selectByPrimaryKey(id);
        CgPurchaseInfoVO cgPurchaseInfoVO = CgPurchaseInfoVO.builder()
                .id(cgPurchaseInfo.getId())
                .name(cgPurchaseInfo.getName())
                .applyName(cgPurchaseInfo.getApplyName())
                .taskNumber(cgPurchaseInfo.getTaskNumber())
                .createTime(cgPurchaseInfo.getCreateTime())
                .purchaseRequirements(cgPurchaseInfo.getPurchaseRequirements())
                .issuerId(cgPurchaseInfo.getIssuerId())
                .deliveryTime(cgPurchaseInfo.getDeliveryTime())
                .fileVOList(cgPurchaseFiles).build();
        return Callback.success(cgPurchaseInfoVO);
    }

    public Callback<CgContactInformation> contactInformation() {
        CgContactInformation cgContactInformation = cgContactInformationMapper.selectByPrimaryKey(1);
        return Callback.success(cgContactInformation);
    }
}
