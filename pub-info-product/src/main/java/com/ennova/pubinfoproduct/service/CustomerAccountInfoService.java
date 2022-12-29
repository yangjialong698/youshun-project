package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfoproduct.daos.CustomerAccountFileMapper;
import com.ennova.pubinfoproduct.daos.CustomerAccountInfoMapper;
import com.ennova.pubinfoproduct.dto.FileDelDTO;
import com.ennova.pubinfoproduct.entity.CustomerAccountFile;
import com.ennova.pubinfoproduct.entity.CustomerAccountInfo;
import com.ennova.pubinfoproduct.vo.CustomerAccountInfoVO;
import com.ennova.pubinfoproduct.vo.FileVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-12-28
 */
@Slf4j
@Service
public class CustomerAccountInfoService {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CustomerAccountFileMapper customerAccountFileMapper;
    @Autowired
    private CustomerAccountInfoMapper customerAccountInfoMapper;

    /** 本地路径 */
    @Value("${spring.upload.local.path}")
    private String localPath;

    /** 访问的url */
    @Value("${spring.upload.local.url}")
    private String localUrl;

    /** 支持文件 */
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
        CustomerAccountFile customerAccountFile = CustomerAccountFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(new Date()).build();
        int count = customerAccountFileMapper.insertSelective(customerAccountFile);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(customerAccountFile.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "上传失败!");
    }

    public Callback deleteFile(FileDelDTO fileDelDTO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        fileDelDTO.getFileVos().forEach(fileVo -> {
            String path = localPath + "/" + fileVo.getNewfileName();
            // 如果是本人上传的，才能执行删除操作
            assert userVo != null;
            List<CustomerAccountFile> files = customerAccountFileMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = customerAccountFileMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count == 1) {
                        file.delete();
                    }
                    customerAccountFileMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success("附件删除成功");
    }

    public Callback insertOrUpdate(CustomerAccountInfoVO customerAccountInfoVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
        BeanUtils.copyProperties(customerAccountInfoVO,customerAccountInfo);
        if (customerAccountInfoVO.getId() != null){
            //更新
            CustomerAccountInfo customerAccInfo = customerAccountInfoMapper.selectByPrimaryKey(customerAccountInfoVO.getId());
            if (null != customerAccInfo){
                customerAccountInfo.setUpdateTime(new Date());
                customerAccountInfo.setCreateTime(customerAccInfo.getCreateTime());
                customerAccountInfo.setId(customerAccInfo.getId());
                customerAccountInfoMapper.updateByPrimaryKey(customerAccountInfo);
                if (CollectionUtil.isNotEmpty(customerAccountInfoVO.getFileVOList())){
                    for (FileVO fileVO : customerAccountInfoVO.getFileVOList()) {
                        CustomerAccountFile customerAccountFile = customerAccountFileMapper.selectByPrimaryKey(fileVO.getId());
                        Optional.ofNullable(customerAccountFile).ifPresent(cgPurchaseFile1 -> {
                            CustomerAccountFile caFile = customerAccountFile.builder().id(fileVO.getId()).updateTime(new Date()).customerAccountId(customerAccountInfo.getId()).build();
                            customerAccountFileMapper.updateByPrimaryKeySelective(caFile);
                        });
                    }
                }
                return Callback.success(true);
            }

        }else {
            //新增
            customerAccountInfo.setCreateTime(new Date());
            customerAccountInfoMapper.insertSelective(customerAccountInfo);
            if (CollectionUtil.isNotEmpty(customerAccountInfoVO.getFileVOList())){
                for (FileVO fileVO : customerAccountInfoVO.getFileVOList()) {
                    CustomerAccountFile customerAccountFile = customerAccountFileMapper.selectByPrimaryKey(fileVO.getId());
                    Optional.ofNullable(customerAccountFile).ifPresent(cgPurchaseFile1 -> {
                        CustomerAccountFile caFile = customerAccountFile.builder().id(fileVO.getId()).updateTime(new Date()).customerAccountId(customerAccountInfo.getId()).build();
                        customerAccountFileMapper.updateByPrimaryKeySelective(caFile);
                    });
                }
            }
            return Callback.success(true);
        }
        return Callback.error(2, "数据处理失败!");
    }

    public Callback delete(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CustomerAccountInfo customerAccountInfo = customerAccountInfoMapper.selectByPrimaryKey(id);
        List<CustomerAccountFile> customerAccountFiles = customerAccountFileMapper.selectAllByCustomerAccountId(id);
        if (customerAccountInfo != null) {
            int i = customerAccountInfoMapper.deleteByPrimaryKey(id);
            if (CollectionUtil.isNotEmpty(customerAccountFiles)){
                customerAccountFiles.forEach(customerAccountFile -> {
                    customerAccountFileMapper.deleteByPrimaryKey(customerAccountFile.getId());
                });
            }
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

    public Callback<CustomerAccountInfoVO> getDetail(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        List<CustomerAccountFile> customerAccountFiles = customerAccountFileMapper.selectAllByCustomerAccountId(id);
        CustomerAccountInfo customerAccountInfo = customerAccountInfoMapper.selectByPrimaryKey(id);
        CustomerAccountInfoVO customerAccountInfoVOFin = new CustomerAccountInfoVO();
        if (null != customerAccountInfo){
            BeanUtils.copyProperties(customerAccountInfo, customerAccountInfoVOFin);
            customerAccountInfoVOFin.setFileList(customerAccountFiles);
        }
        return Callback.success(customerAccountInfoVOFin);
    }

    public Callback<BaseVO<CustomerAccountInfoVO>> selectCustomerAccountInfoList(Integer page, Integer pageSize, Integer monthNum, String key) {
        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
        List<CustomerAccountInfoVO> customerAccountInfoVOList = customerAccountInfoMapper.selectByMonthNumAndKey(monthNum,key);
        if (CollectionUtil.isNotEmpty(customerAccountInfoVOList)){
            customerAccountInfoVOList.forEach(customerAccountInfoVO->{
                List<CustomerAccountFile> customerAccountFiles = customerAccountFileMapper.selectAllByCustomerAccountId(customerAccountInfoVO.getId());
                if (CollectionUtil.isNotEmpty(customerAccountFiles)){
                    customerAccountInfoVO.setFileList(customerAccountFiles);
                }else {
                    customerAccountInfoVO.setFileList(null);
                }
            });
        }
        BaseVO<CustomerAccountInfoVO> baseVO = new BaseVO<>(customerAccountInfoVOList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }
}
