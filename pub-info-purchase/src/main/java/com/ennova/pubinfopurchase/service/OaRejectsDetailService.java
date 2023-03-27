package com.ennova.pubinfopurchase.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.OaRejectsDetailFileMapper;
import com.ennova.pubinfopurchase.dao.OaRejectsDetailMapper;
import com.ennova.pubinfopurchase.dto.BadDisposalDTO;
import com.ennova.pubinfopurchase.dto.BadItemDTO;
import com.ennova.pubinfopurchase.dto.PrdInfoDTO;
import com.ennova.pubinfopurchase.entity.OaRejectsDetail;
import com.ennova.pubinfopurchase.entity.OaRejectsDetailFile;
import com.ennova.pubinfopurchase.utils.BeanConvertUtils;
import com.ennova.pubinfopurchase.vo.FileVO;
import com.ennova.pubinfopurchase.vo.OaRejectsDetailFileVO;
import com.ennova.pubinfopurchase.vo.OaRejectsDetailVO;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OaRejectsDetailService {

    private final OaRejectsDetailMapper oaRejectsDetailMapper;
    private final OaRejectsDetailFileMapper oaRejectsDetailFileMapper;
    private final HttpServletRequest request;

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

    public Callback<List<PrdInfoDTO>> getPrdInfo(String workOrderNo) {
        List<PrdInfoDTO> erpPrdInfos = oaRejectsDetailMapper.selectByWorkOrderNo(workOrderNo);
        for (PrdInfoDTO erpPrdInfo : erpPrdInfos) {
            String[] workOrderNos = erpPrdInfo.getWorkOrderNo().split("-");
            erpPrdInfo.setWorkOrderNo(workOrderNos[1]);
        }
        if (ObjectUtils.isEmpty(erpPrdInfos)) {
            return Callback.error("未查询到工单号相关信息");
        }
        return Callback.success(erpPrdInfos);
    }

    public Callback<List<BadItemDTO>> getBadItem() {
        List<BadItemDTO> badItemDTOS = oaRejectsDetailMapper.selectBadItemInfo();
        if (CollectionUtil.isEmpty(badItemDTOS)) {
            return Callback.error("未查询到不良项目信息");
        }
        return Callback.success(badItemDTOS);
    }

    public Callback<List<BadDisposalDTO>> getBadDisposal() {
        List<BadDisposalDTO> badDisposalDTOS = oaRejectsDetailMapper.selectBadDisposalInfo();
        if (CollectionUtil.isEmpty(badDisposalDTOS)) {
            return Callback.error("未查询到不良处置信息");
        }
        return Callback.success(badDisposalDTOS);
    }

    public Callback insertRejectsDetail(OaRejectsDetailVO oaRejectsDetailVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejectsDetail oaRejectsDetail = new OaRejectsDetail();
        BeanConvertUtils.copyProperties(oaRejectsDetailVO, oaRejectsDetail);
        if (ObjectUtils.isNotEmpty(oaRejectsDetail)) {
            oaRejectsDetail.setCreateTime(LocalDateTime.now());
            oaRejectsDetail.setDelFlag(0);
            oaRejectsDetailMapper.insertSelective(oaRejectsDetail);
            if (oaRejectsDetailVO.getOaRejectsDetailFiles() != null && !oaRejectsDetailVO.getOaRejectsDetailFiles().isEmpty()) {
                for (OaRejectsDetailFileVO fileVO : oaRejectsDetailVO.getOaRejectsDetailFiles()) {
                    OaRejectsDetailFile oaRejectsDetailFile = oaRejectsDetailFileMapper.selectByPrimaryKey(fileVO.getId());
                    Optional.ofNullable(oaRejectsDetailFile).ifPresent(v -> {
                        OaRejectsDetailFile build = OaRejectsDetailFile.builder().id(fileVO.getId()).updateTime(LocalDateTime.now()).rejectsDetailId(oaRejectsDetail.getId()).build();
                        oaRejectsDetailFileMapper.updateByPrimaryKeySelective(build);
                    });
                }
            }
            return Callback.success(true);
        }
        return Callback.error(2, "数据处理失败!");
    }

    public Callback<List<OaRejectsDetailVO>> selectRejectsDetail() {
        List<OaRejectsDetailVO> oaRejectsDetailVOS = oaRejectsDetailMapper.selectRejectsDetail();
        if (CollectionUtil.isEmpty(oaRejectsDetailVOS)) {
            return Callback.error("未查询到不良品明细");
        }
        return Callback.success(oaRejectsDetailVOS);
    }

    public Callback deleteRejectsDetail(Integer id) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        // 判断ID是不是数字
        if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
            return Callback.error(2, "ID不能为空");
        }

        OaRejectsDetail oaRejectsDetail = oaRejectsDetailMapper.selectByPrimaryKey(id);
        List<OaRejectsDetailFileVO> oaRejectsDetailFiles = oaRejectsDetailFileMapper.selectAllByRejectsDetailId(oaRejectsDetail.getId());
        if (oaRejectsDetail != null) {
            int i = oaRejectsDetailMapper.deleteByPrimaryKey(oaRejectsDetail.getId());
            if (oaRejectsDetailFiles != null){
                oaRejectsDetailFiles.forEach(v -> {
                    oaRejectsDetailFileMapper.deleteByPrimaryKey(v.getId());
                });
            }
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

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

        OaRejectsDetailFile oaRejectsDetailFile = OaRejectsDetailFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(LocalDateTime.now()).build();
        int count = oaRejectsDetailFileMapper.insertSelective(oaRejectsDetailFile);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(oaRejectsDetailFile.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "上传失败!");
    }


}
