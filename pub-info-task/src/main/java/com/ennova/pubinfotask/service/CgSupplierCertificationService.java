package com.ennova.pubinfotask.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.CgSupplierCertificationMapper;
import com.ennova.pubinfotask.dao.CgSupplierFileMapper;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.dao.YsMessageMapper;
import com.ennova.pubinfotask.dto.CgSupplierCertificationDTO;
import com.ennova.pubinfotask.dto.FileDelDTO;
import com.ennova.pubinfotask.entity.CgSupplierCertification;
import com.ennova.pubinfotask.entity.CgSupplierFile;
import com.ennova.pubinfotask.entity.YsMessage;
import com.ennova.pubinfotask.utils.BeanConvertUtils;
import com.ennova.pubinfotask.vo.CgSupplierCertificationVO;
import com.ennova.pubinfotask.vo.CurrentUserVO;
import com.ennova.pubinfotask.vo.FileVO;
import com.ennova.pubinfotask.vo.SocketVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class CgSupplierCertificationService {

    private final HttpServletRequest req;
    private final CgSupplierCertificationMapper cgSupplierCertificationMapper;
    private final YsBulletinService ysBulletinService;
    private final RedisTemplate redisTemplate;
    private final CgSupplierFileMapper cgSupplierFileMapper;
    private final UserMapper userMapper;
    private final YsMessageMapper ysMessageMapper;

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

        CgSupplierFile slave = CgSupplierFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(LocalDateTime.now()).build();
        int count = cgSupplierFileMapper.insertSelective(slave);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(slave.getId()).fileName(map.get("fileName")).newfileName(subname).build();
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
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        fileDelDTO.getFileVos().forEach(fileVo -> {
            String path = localPath + "/" + fileVo.getNewfileName();
            // 如果是本人上传的，才能执行删除操作
            assert userVo != null;
            List<CgSupplierFile> files = cgSupplierFileMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = cgSupplierFileMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count == 1) {
                        file.delete();
                    }
                    cgSupplierFileMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success("附件删除成功");
    }


    public Callback insert(CgSupplierCertificationDTO cgSupplierCertificationDTO) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        CgSupplierCertification cgSupplierCertification = BeanConvertUtils.convertTo(cgSupplierCertificationDTO, CgSupplierCertification::new);
        cgSupplierCertification.setCreateUserId(userVo.getId());
        cgSupplierCertification.setStatus(0);
        cgSupplierCertification.setCreateTime(LocalDateTime.now());
        Integer number = cgSupplierCertificationMapper.selectLastSerialNumber();
        Integer serialNumber = getSerialNumber(number);
        cgSupplierCertification.setSupplierNumber(serialNumber);
        int i = cgSupplierCertificationMapper.insert(cgSupplierCertification);
        if (cgSupplierCertificationDTO.getFileVOList() != null && !cgSupplierCertificationDTO.getFileVOList().isEmpty()) {
            for (FileVO fileVO : cgSupplierCertificationDTO.getFileVOList()) {
                CgSupplierFile cgSupplierFile = cgSupplierFileMapper.selectByPrimaryKey(fileVO.getId());
                Optional.ofNullable(cgSupplierFile).ifPresent(cgSupplierFile1 -> {
                    CgSupplierFile cgSupplierFile2 = cgSupplierFile.builder().id(fileVO.getId()).updateTime(LocalDateTime.now()).cgSupplierId(cgSupplierCertification.getId()).build();
                    cgSupplierFileMapper.updateByPrimaryKeySelective(cgSupplierFile2);
                });
            }
        }
        if (i > 0) {
            SocketVO<Object> content = SocketVO.builder().sourceType(1).type(0).content(cgSupplierCertification).build();
            Channel channel = ysBulletinService.getChannel(String.valueOf(cgSupplierCertificationDTO.getCheckUserId()));

            log.info("新增供应商时的审核人：" + cgSupplierCertificationDTO.getCheckUserId());
            log.info("新增供应商时channel" + channel);

            if (null == channel) {
                redisTemplate.opsForList().rightPush("supplier:add:" + cgSupplierCertificationDTO.getCheckUserId(), JSONObject.toJSONString(content));
                log.info("用户: " + cgSupplierCertificationDTO.getCheckUserId() + " 没有登录，添加到redis队列");
                return Callback.success();
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(content)));
            }
            return Callback.success(true);
        }
        return Callback.error(2, "新增失败");
    }

    public Callback<List<CurrentUserVO>> selectCheckPerson(){
        List<CurrentUserVO> currentUserVOS = userMapper.selectAllUser();
        List<CurrentUserVO> checkPerson = currentUserVOS.stream().filter(v -> v.getUsername().equals("王大祥")).collect(Collectors.toList());
        return Callback.success(checkPerson);
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

    public Callback update(CgSupplierCertificationDTO cgSupplierCertificationDTO){
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CgSupplierCertification old = cgSupplierCertificationMapper.selectbyIdAndCreateId(cgSupplierCertificationDTO.getId(), userVo.getId());
        // 只有待审核、驳回状态才能修改
        if (old.getStatus().equals(1)) {
            return Callback.error(2, "当前供应商认证已审核，不能修改");
        }
        CgSupplierCertification cgSupplierCertification = BeanConvertUtils.convertTo(cgSupplierCertificationDTO, CgSupplierCertification::new);
        cgSupplierCertification.setStatus(0);
        cgSupplierCertification.setCheckUserId(cgSupplierCertificationDTO.getCheckUserId());
        cgSupplierCertification.setUpdateTime(LocalDateTime.now());
        int i = cgSupplierCertificationMapper.updateByPrimaryKeySelective(cgSupplierCertification); // 更新主表
        if (cgSupplierCertificationDTO.getFileVOList() != null && !cgSupplierCertificationDTO.getFileVOList().isEmpty()) {
            for (FileVO fileVO : cgSupplierCertificationDTO.getFileVOList()) {
                CgSupplierFile cgSupplierFile = cgSupplierFileMapper.selectByPrimaryKey(fileVO.getId());
                Optional.ofNullable(cgSupplierFile).ifPresent(cgSupplierFile1 -> {
                    CgSupplierFile cgSupplierFile2 = cgSupplierFile.builder().id(fileVO.getId()).updateTime(LocalDateTime.now()).cgSupplierId(cgSupplierCertification.getId()).build();
                    cgSupplierFileMapper.updateByPrimaryKeySelective(cgSupplierFile2);
                });
            }
        }
        log.info("新增供应商时的审核人: " + cgSupplierCertificationDTO.getCheckUserId() );

        if (i > 0) {
            //推送:sourceType=1 供应商认证,type=3 修改
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(1).type(3).content(cgSupplierCertification).build();
            Channel channel = ysBulletinService.getChannel(String.valueOf(cgSupplierCertificationDTO.getCheckUserId()));

            log.info("修改供应商时channel: " + channel );

            if (null == channel) {
                redisTemplate.opsForList().rightPush("supplier:add:" + cgSupplierCertificationDTO.getCheckUserId(), JSONObject.toJSONString(socketVO));
                log.info("用户: " + cgSupplierCertificationDTO.getCheckUserId() + " 没有登录，添加到redis队列");
                return Callback.success();
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
            }
            return Callback.success(true);
        }
        return Callback.error(2, "修改失败");
    }

    public Callback<BaseVO<CgSupplierCertificationVO>> getSupplierList(Integer page, Integer pageSize, Integer status, String likeTitle) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<CgSupplierCertificationVO> cgSupplierCertificationVOS = cgSupplierCertificationMapper.selectByStatusAndSupplierName(status, likeTitle);
        cgSupplierCertificationVOS.forEach(v -> {
            List<FileVO> fileVOS = cgSupplierFileMapper.selectByCgSupplierId(v.getId());
            if (CollectionUtil.isNotEmpty(fileVOS)){
                v.setFileVOList(fileVOS);
            }else {
                v.setFileVOList(null);
            }
        });

        BaseVO<CgSupplierCertificationVO> baseVO = new BaseVO<>(cgSupplierCertificationVOS, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    // 供应商认证审核
    public Callback checkSupplier(Integer id, Integer status) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());

        if (!"王大祥".equals(currentUserVO.getUsername())) {
            return Callback.error(2, "您没有权限审核,请找王大祥审核");
        }

        // 判断ID是不是数字
        if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
            return Callback.error(2, "ID不能为空");
        }

        CgSupplierCertification cgSupplierCertification = cgSupplierCertificationMapper.selectByPrimaryKey(id);
        if (cgSupplierCertification == null) {
            return Callback.error(2, "当前ID为" + id + "的供应商资质认证不存在");
        }

        if (cgSupplierCertification.getStatus() != 0) {
            return Callback.error(2, "当前供应商资质认证已审核，不能重复审核");
        }

        cgSupplierCertification.setStatus(status);
        cgSupplierCertification.setUpdateTime(LocalDateTime.now());


        //审核通过
        if (status == 1) {
            cgSupplierCertification.setStatus(1);
            cgSupplierCertification.setCheckTime(LocalDateTime.now());

            //TODO 审核通过
            //保存到数据库
            YsMessage message = YsMessage.builder().sourceType(1).receiveId(cgSupplierCertification.getCreateUserId()).CgSupplier(cgSupplierCertification.getId()).status(false).createTime(LocalDateTime.now()).build();
            ysMessageMapper.insert(message);

            //推送:sourceType=1 供应商认证,type=1 审核通过了
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(1).type(1).content(cgSupplierCertification).build();
            Channel channel = ysBulletinService.getChannel(String.valueOf(cgSupplierCertification.getCreateUserId()));
            log.info("审核通过用户：" + cgSupplierCertification.getCreateUserId());
            if (null == channel) {
                redisTemplate.opsForList().rightPush("supplier:check:" + cgSupplierCertification.getCreateUserId(), JSONObject.toJSONString(socketVO));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
                log.info("审核通过的用户："+ cgSupplierCertification.getCreateUserId() + " channel:" + channel);
            }

        }
        //审核不通过
        if (status == 2) {
            cgSupplierCertification.setStatus(2);
            cgSupplierCertification.setUpdateTime(LocalDateTime.now());

            //TODO 审核不通过
            //推送:sourceType=1 供应商认证,type=1 审核不通过
            SocketVO<Object> socketVO = SocketVO.builder().sourceType(1).type(2).content(cgSupplierCertification).build();

            Channel channel = ysBulletinService.getChannel(String.valueOf(cgSupplierCertification.getCreateUserId()));
            log.info("审核不通过用户：" + cgSupplierCertification.getCreateUserId());
            if (null == channel) {
                redisTemplate.opsForList().rightPush("supplier:refuse:" + cgSupplierCertification.getCreateUserId(), JSONObject.toJSONString(socketVO));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketVO)));
                log.info("审核不通过的用户："+ cgSupplierCertification.getCreateUserId() + " channel:" + channel);
            }
        }

        int i = cgSupplierCertificationMapper.updateByPrimaryKeySelective(cgSupplierCertification); // 更新主表
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error(2, "审核失败");
    }

    //查询供应商详情
    public Callback<CgSupplierCertificationVO> getSupplierDetail(Integer id) {
        CgSupplierCertification cgSupplierCertification = cgSupplierCertificationMapper.selectByPrimaryKey(id);
        CgSupplierCertificationVO cgSupplierCertificationVO = BeanConvertUtils.convertTo(cgSupplierCertification, CgSupplierCertificationVO::new);
        cgSupplierCertificationVO.setCreateName(userMapper.selectById(cgSupplierCertificationVO.getCreateUserId()).getUserName());
        cgSupplierCertificationVO.setCheckName(userMapper.selectById(cgSupplierCertificationVO.getCheckUserId()).getUserName());
        return Callback.success(cgSupplierCertificationVO);
    }

    public Callback delete(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CgSupplierCertification cgSupplierCertification = cgSupplierCertificationMapper.selectByPrimaryKey(id);
        if (cgSupplierCertification != null) {
            int i = cgSupplierCertificationMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }



}
