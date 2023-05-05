package com.ennova.pubinfopurchase.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.*;
import com.ennova.pubinfopurchase.dto.FileDelDTO;
import com.ennova.pubinfopurchase.dto.OaBathRejectsDeleteDTO;
import com.ennova.pubinfopurchase.dto.OaPressRejectsDTO;
import com.ennova.pubinfopurchase.entity.*;
import com.ennova.pubinfopurchase.utils.BeanConvertUtils;
import com.ennova.pubinfopurchase.utils.OaUtils;
import com.ennova.pubinfopurchase.vo.*;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OaRejectsService {

    private final OaRejectsMapper oaRejectsMapper;
    private final OaRejectsDetailMapper oaRejectsDetailMapper;
    private final OaRejectsDetailFileMapper oaRejectsDetailFileMapper;
    private final OaRejectsOpinionMapper oaRejectsOpinionMapper;
    private final OaSystemManageMapper oaSystemManageMapper;
    private final OaRejectsFileMapper oaRejectsFileMapper;
    private final HttpServletRequest request;
    private final UserMapper userMapper;
    private final OaMailMapper mailMapper;
    private final JavaMailSender mailSender;
    private final OaOpinionUserMailMapper oaOpinionUserMailMapper;
    private final OaOperationLogMapper oaOperationLogMapper;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${spring.mail.mailFromNick}")
    private String mailFromNick;

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

    public Callback insertOrUpdateRejects(OaRejectsVO oaRejectsVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = new OaRejects();
        BeanConvertUtils.copyProperties(oaRejectsVO, oaRejects);

        if (ObjectUtils.isNotEmpty(oaRejects) && oaRejectsVO.getId() != null) {
            oaRejects.setHeadline(oaRejectsVO.getWorkCenter() + "不合格评审单");
            oaRejectsMapper.updateByPrimaryKeySelective(oaRejects);

            if (CollectionUtils.isNotEmpty(oaRejectsVO.getOaRejectsDetails())) {
                for (OaRejectsDetailVO oaRejectsDetailVO : oaRejectsVO.getOaRejectsDetails()) {
                    OaRejectsDetail oaRejectsDetail = new OaRejectsDetail();
                    BeanConvertUtils.copyProperties(oaRejectsDetailVO, oaRejectsDetail);
                    if (oaRejectsDetail.getId() != null) {
                        oaRejectsDetailMapper.updateByPrimaryKeySelective(oaRejectsDetail);
                    } else {
                        oaRejectsDetail.setRejectsId(oaRejects.getId());
                        oaRejectsDetail.setCreateTime(LocalDateTime.now());
                        oaRejectsDetail.setDelFlag(0);
                        oaRejectsDetailMapper.insertSelective(oaRejectsDetail);
                    }
                    if (oaRejectsDetailVO.getOaRejectsDetailFiles() != null && !oaRejectsDetailVO.getOaRejectsDetailFiles().isEmpty()) {
                        for (OaRejectsDetailFileVO fileVO : oaRejectsDetailVO.getOaRejectsDetailFiles()) {
                            OaRejectsDetailFile oaRejectsDetailFile = oaRejectsDetailFileMapper.selectByPrimaryKey(fileVO.getId());
                            Optional.ofNullable(oaRejectsDetailFile).ifPresent(v -> {
                                OaRejectsDetailFile build = OaRejectsDetailFile.builder().id(fileVO.getId()).updateTime(LocalDateTime.now()).rejectsDetailId(oaRejectsDetail.getId()).build();
                                oaRejectsDetailFileMapper.updateByPrimaryKeySelective(build);
                            });
                        }
                    }
                }
            }

            OaOperationLog oaOperationLog = OaOperationLog.builder().rejectsId(oaRejects.getId()).userId(userVo.getId()).userName(userMapper.selectById(userVo.getId()).getUserName()).createTime(LocalDateTime.now()).description("修改表单").build();
            oaOperationLogMapper.insertSelective(oaOperationLog);

            return Callback.success(true);
        } else {
            String serialNumber = oaRejectsMapper.selectLastSerialNumber();
            if (StringUtils.isEmpty(serialNumber)) {
                serialNumber = "";
            }
            oaRejects.setSerialNumber(OaUtils.getSerialNumber(serialNumber));
            oaRejects.setHeadline(oaRejectsVO.getWorkCenter() + "不合格处理单");
            oaRejects.setSetpStaus(oaRejectsVO.getSetpStaus());
            oaRejects.setUserId(userVo.getId());
            oaRejects.setUserName(userMapper.selectById(userVo.getId()).getUserName());
            oaRejects.setSchedule("进行中");
            oaRejects.setDelFlag(0);
            oaRejectsMapper.insertSelective(oaRejects);

            if (CollectionUtils.isNotEmpty(oaRejectsVO.getOaRejectsDetails())) {
                for (OaRejectsDetailVO oaRejectsDetailVO : oaRejectsVO.getOaRejectsDetails()) {
                    OaRejectsDetail oaRejectsDetail = new OaRejectsDetail();
                    BeanConvertUtils.copyProperties(oaRejectsDetailVO, oaRejectsDetail);
                    if (ObjectUtils.isNotEmpty(oaRejectsDetail)) {
                        oaRejectsDetail.setRejectsId(oaRejects.getId());
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
                    }
                }
            }

            OaOperationLog oaOperationLog = OaOperationLog.builder().rejectsId(oaRejects.getId()).userId(userVo.getId()).userName(userMapper.selectById(userVo.getId()).getUserName()).createTime(LocalDateTime.now()).description("新建表单").build();
            oaOperationLogMapper.insertSelective(oaOperationLog);

            return Callback.success(true);
        }
    }

    public Callback<PageInfo<OaRejectsVO>> selectRejectsInfo(Integer page, Integer pageSize, String startTime, String endTime, String workCenter, String userName, String schedule, String headline) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        PageMethod.startPage(page, pageSize);
        List<OaRejects> oaRejects = oaRejectsMapper.selectRejectsInfo(startTime, endTime, workCenter, userName, schedule, headline);
        List<OaRejectsVO> rejectsInfos = new ArrayList<>();
        oaRejects.forEach(v -> {
            OaRejectsVO oaRejectsVO = BeanConvertUtils.convertTo(v, OaRejectsVO::new);
            List<OaRejectsDetailVO> oaRejectsDetailVOS = oaRejectsDetailMapper.selectByRejectsId(v.getId());
            if (CollectionUtils.isNotEmpty(oaRejectsDetailVOS)) {
                oaRejectsDetailVOS.forEach(oaRejectsDetailVO -> {
                    List<OaRejectsDetailFileVO> oaRejectsDetailFileVOS = oaRejectsDetailFileMapper.selectAllByRejectsDetailId(oaRejectsDetailVO.getId());
                    oaRejectsDetailVO.setOaRejectsDetailFiles(oaRejectsDetailFileVOS);
                });
            }
            List<OaRejectsFileVO> oaRejectsFileVOS = oaRejectsFileMapper.selectByRejectsId(v.getId());
            oaRejectsVO.setOaRejectsFileVOS(oaRejectsFileVOS);
            oaRejectsVO.setOaRejectsDetails(oaRejectsDetailVOS);
            List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(v.getId(), v.getSetpStaus());
            List<OaRejectsOpinionVO> oaRejectsOpinionss = oaRejectsOpinionMapper.selectByRejectsId(v.getId());
            if (CollectionUtils.isNotEmpty(oaRejectsOpinions)) {
                oaRejectsVO.setOaRejectsOpinionVOS(oaRejectsOpinions);
            }
            List<Integer> opinionUserIds = oaRejectsOpinions.stream().map(o -> o.getOpinionUserId()).collect(Collectors.toList());
            List<Integer> opinionUserIdss = oaRejectsOpinionss.stream().map(o -> o.getOpinionUserId()).collect(Collectors.toList());
            List<LocalDateTime> publishTimes = oaRejectsOpinions.stream().sorted(Comparator.comparing(o -> o.getCreateTime())).map(o -> o.getPublishTime()).collect(Collectors.toList());
            if (publishTimes.size() > 0) {
                oaRejectsVO.setPublishTime(publishTimes.get(0));
            }
            if (opinionUserIds.contains(userVo.getId()) && v.getSetpStaus() > 1 && StringUtils.isEmpty(v.getQualityConfirm())) {
                oaRejectsVO.setBackStatus(1);
            }
            if (opinionUserIdss.contains(userVo.getId())) {
                oaRejectsVO.setShowStatus(1);
            }
            if (StringUtils.isEmpty(v.getTransactor()) || v.getTransactor().equals(userMapper.selectById(userVo.getId()).getUserName())) {
                oaRejectsVO.setOpenStatus(1);
            }
            if (StringUtils.isEmpty(v.getTransactor()) && v.getSetpStaus().equals(5)) {
                oaRejectsVO.setFinishStatus(1);
                oaRejectsMapper.updateByPrimaryKeySelective(OaRejects.builder().id(v.getId()).schedule("已完成").build());
            }
            if (StringUtils.isEmpty(v.getTransactor()) && !v.getSetpStaus().equals(5) && !v.getSetpStaus().equals(1)) {
                String opinionUsers = oaRejectsOpinions.stream().filter(o -> StringUtils.isEmpty(o.getOpinionContent())).map(o -> o.getOpinionUser()).collect(Collectors.joining(","));
                oaRejectsMapper.updateByPrimaryKeySelective(OaRejects.builder().id(v.getId()).transactor(opinionUsers).build());
            }
            rejectsInfos.add(oaRejectsVO);
        });
        List<Integer> collect = oaSystemManageMapper.selectByAll().stream().map(v -> v.getUserId()).collect(Collectors.toList());
        if (collect.size() > 0 && collect.contains(userVo.getId())) {
            return Callback.success(new PageInfo<>(rejectsInfos));
        }
        List<OaRejectsVO> list = rejectsInfos.stream().filter(o -> o.getUserId().equals(userVo.getId()) || ObjectUtils.isNotEmpty(o.getShowStatus()) && o.getShowStatus().equals(1)).collect(Collectors.toList());
        PageInfo<OaRejectsVO> oaRejectsVOPageInfo = new PageInfo<>(list);
        return Callback.success(oaRejectsVOPageInfo);
    }

    public Callback<PageInfo<OaRejectsVO>> selectRejectsInfos(Integer page, Integer pageSize, String startTime, String endTime, String workCenter, String userName, String opinionUser, String transactor, String schedule, String headline) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        PageMethod.startPage(page, pageSize);
        List<OaRejects> oaRejects = oaRejectsMapper.selectRejectsInfos(startTime, endTime, workCenter, userName, transactor, schedule, headline);
        List<OaRejectsVO> rejectsInfos = new ArrayList<>();
        oaRejects.forEach(v -> {
            OaRejectsVO oaRejectsVO = BeanConvertUtils.convertTo(v, OaRejectsVO::new);
            List<OaRejectsDetailVO> oaRejectsDetailVOS = oaRejectsDetailMapper.selectByRejectsId(v.getId());
            if (CollectionUtils.isNotEmpty(oaRejectsDetailVOS)) {
                oaRejectsDetailVOS.forEach(oaRejectsDetailVO -> {
                    List<OaRejectsDetailFileVO> oaRejectsDetailFileVOS = oaRejectsDetailFileMapper.selectAllByRejectsDetailId(oaRejectsDetailVO.getId());
                    oaRejectsDetailVO.setOaRejectsDetailFiles(oaRejectsDetailFileVOS);
                });
            }
            List<OaRejectsFileVO> oaRejectsFileVOS = oaRejectsFileMapper.selectByRejectsId(v.getId());
            oaRejectsVO.setOaRejectsFileVOS(oaRejectsFileVOS);
            oaRejectsVO.setOaRejectsDetails(oaRejectsDetailVOS);
            List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(v.getId(), v.getSetpStaus());
            List<OaRejectsOpinionVO> oaRejectsOpinionss = oaRejectsOpinionMapper.selectByRejectsId(v.getId());
            if (CollectionUtils.isNotEmpty(oaRejectsOpinions)) {
                oaRejectsVO.setOaRejectsOpinionVOS(oaRejectsOpinions);
            }
            List<Integer> opinionUserIds = oaRejectsOpinions.stream().map(o -> o.getOpinionUserId()).collect(Collectors.toList());
            List<String> opinionUserss = oaRejectsOpinionss.stream().map(o -> o.getOpinionUser()).collect(Collectors.toList());
            List<LocalDateTime> publishTimes = oaRejectsOpinions.stream().sorted(Comparator.comparing(o -> o.getCreateTime())).map(o -> o.getPublishTime()).collect(Collectors.toList());
            if (publishTimes.size() > 0) {
                oaRejectsVO.setPublishTime(publishTimes.get(0));
            }
            if (opinionUserIds.contains(userVo.getId()) && v.getSetpStaus() > 1 && StringUtils.isEmpty(v.getQualityConfirm())) {
                oaRejectsVO.setBackStatus(1);
            }
            if (StringUtils.isNotEmpty(opinionUser) && opinionUserss.contains(opinionUser)) {
                oaRejectsVO.setShowStatus(1);
            }
            if (StringUtils.isEmpty(v.getTransactor()) || v.getTransactor().equals(userMapper.selectById(userVo.getId()).getUserName())) {
                oaRejectsVO.setOpenStatus(1);
            }
            if (StringUtils.isEmpty(v.getTransactor()) && v.getSetpStaus().equals(5)) {
                oaRejectsVO.setFinishStatus(1);
                oaRejectsMapper.updateByPrimaryKeySelective(OaRejects.builder().id(v.getId()).schedule("已完成").build());
            }
            if (StringUtils.isEmpty(v.getTransactor()) && !v.getSetpStaus().equals(5) && !v.getSetpStaus().equals(1)) {
                String opinionUsers = oaRejectsOpinions.stream().filter(o -> StringUtils.isEmpty(o.getOpinionContent())).map(o -> o.getOpinionUser()).collect(Collectors.joining(","));
                oaRejectsMapper.updateByPrimaryKeySelective(OaRejects.builder().id(v.getId()).transactor(opinionUsers).build());
            }
            rejectsInfos.add(oaRejectsVO);
        });
        List<OaRejectsVO> collect = rejectsInfos.stream().filter(o -> ObjectUtils.isNotEmpty(o.getShowStatus()) && o.getShowStatus().equals(1)).collect(Collectors.toList());
        return Callback.success(new PageInfo<>(collect));
    }

    public Callback<OaRejectsVO> selectRejectsInfoDetail(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = oaRejectsMapper.selectByPrimaryKey(id);
        OaRejectsVO oaRejectsVO = BeanConvertUtils.convertTo(oaRejects, OaRejectsVO::new);
        List<OaRejectsDetailVO> oaRejectsDetailVOS = oaRejectsDetailMapper.selectByRejectsId(oaRejects.getId());
        if (CollectionUtils.isNotEmpty(oaRejectsDetailVOS)) {
            oaRejectsDetailVOS.forEach(oaRejectsDetailVO -> {
                List<OaRejectsDetailFileVO> oaRejectsDetailFileVOS = oaRejectsDetailFileMapper.selectAllByRejectsDetailId(oaRejectsDetailVO.getId());
                oaRejectsDetailVO.setOaRejectsDetailFiles(oaRejectsDetailFileVOS);
            });
        }
        oaRejectsVO.setOaRejectsDetails(oaRejectsDetailVOS);
        List<OaRejectsFileVO> oaRejectsFileVOS = oaRejectsFileMapper.selectByRejectsId(oaRejects.getId());
        oaRejectsVO.setOaRejectsFileVOS(oaRejectsFileVOS);
        List<OaRejectsOpinionVO> oaRejectsOpinionVOList = oaRejectsOpinionMapper.selectByRejectsId(oaRejects.getId());
        List<OaRejectsOpinionVO> oaRejectsOpinionVOS = oaRejectsOpinionVOList.stream().filter(o -> StringUtils.isNotEmpty(o.getOpinionContent())).collect(Collectors.toList());
        List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(oaRejects.getId(), oaRejects.getSetpStaus());
        if (CollectionUtils.isNotEmpty(oaRejectsOpinions)) {
            oaRejectsVO.setOaRejectsOpinionVOS(oaRejectsOpinionVOS);
        }
        List<Integer> opinionUserIds = oaRejectsOpinions.stream().map(o -> o.getOpinionUserId()).collect(Collectors.toList());
        if (opinionUserIds.contains(userVo.getId()) && oaRejects.getSetpStaus() > 1) {
            oaRejectsVO.setBackStatus(1);
        }
        if (StringUtils.isEmpty(oaRejects.getTransactor()) || oaRejects.getTransactor().equals(userMapper.selectById(userVo.getId()).getUserName())) {
            oaRejectsVO.setOpenStatus(1);
        }
        /*是否可以会签*/
        List<Integer> opinionUserId = oaRejectsOpinions.stream().filter(v -> StringUtils.isEmpty(v.getOpinionContent())).map(v -> v.getOpinionUserId()).collect(Collectors.toList());
        if (opinionUserId.size() > 0 && opinionUserId.contains(userVo.getId())) {
            oaRejectsVO.setOpinionStatus(1);
        }
        return Callback.success(oaRejectsVO);
    }

    public Callback backRejects(Integer id, Integer setpStaus, String backReason) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = oaRejectsMapper.selectByPrimaryKey(id);
        List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(id, setpStaus);

        if (CollectionUtils.isNotEmpty(oaRejectsOpinions)) {
            for (OaRejectsOpinionVO oaRejectsOpinionVO : oaRejectsOpinions) {
                OaRejectsOpinion oaRejectsOpinion = new OaRejectsOpinion();
                oaRejectsOpinion.setId(oaRejectsOpinionVO.getId());
                oaRejectsOpinion.setDelFlag(1);
                oaRejectsOpinionMapper.updateByPrimaryKeySelective(oaRejectsOpinion);
            }
        }

        List<OaRejectsOpinionVO> oaRejectsOpinionss = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(id, setpStaus - 1);
        if (CollectionUtils.isNotEmpty(oaRejectsOpinionss)) {
            for (OaRejectsOpinionVO oaRejectsOpinionVO : oaRejectsOpinionss) {
                OaRejectsOpinion oaRejectsOpinion = BeanConvertUtils.convertTo(oaRejectsOpinionVO, OaRejectsOpinion::new);
                oaRejectsOpinion.setOpinionContent(null);
                oaRejectsOpinion.setPublishTime(null);
                oaRejectsOpinionMapper.updateByPrimaryKey(oaRejectsOpinion);
            }
        }

        List<OaRejectsOpinionVO> oaRejectsOpinionsss = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(id, setpStaus - 1);

        if (oaRejects.getSetpStaus().equals(5) || oaRejects.getSetpStaus().equals(4)) {
            oaRejects.setQualityConfirm(null);
            List<OaRejectsFileVO> oaRejectsFileVOS = oaRejectsFileMapper.selectByRejectsId(id);
            if (CollectionUtils.isNotEmpty(oaRejectsFileVOS)) {
                for (OaRejectsFileVO oaRejectsFileVO : oaRejectsFileVOS) {
                    oaRejectsFileMapper.deleteByPrimaryKey(oaRejectsFileVO.getId());
                }
            }
            oaRejectsMapper.updateByPrimaryKey(oaRejects);
        }

        if (oaRejects.getSetpStaus() > 1) {
            oaRejects.setId(id);
            oaRejects.setSetpStaus(setpStaus - 1);
            String opinionUsers = oaRejectsOpinionsss.stream().filter(v -> StringUtils.isEmpty(v.getOpinionContent())).map(v -> v.getOpinionUser()).collect(Collectors.joining(","));
            oaRejects.setTransactor(opinionUsers);
            oaRejects.setSchedule("进行中");
            oaRejectsMapper.updateByPrimaryKeySelective(oaRejects);

            OaOperationLog oaOperationLog = OaOperationLog.builder().rejectsId(oaRejects.getId()).userId(userVo.getId()).userName(userMapper.selectById(userVo.getId()).getUserName()).backReason(backReason).createTime(LocalDateTime.now()).description("回退表单").build();
            oaOperationLogMapper.insertSelective(oaOperationLog);

            return Callback.success(true);
        } else {
            return Callback.error(2, "当前不能回退!");
        }
    }

    public Callback pressRejects(OaPressRejectsVO oaPressRejectsVO) throws MessagingException {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        List<OaPressRejectsDTO> oaPressRejectsDTOList = oaPressRejectsVO.getOaPressRejectsDTOList();
        CompletableFuture.runAsync(() -> {
            for (OaPressRejectsDTO oaPressRejectsDTO : oaPressRejectsDTOList) {
                OaRejects oaRejects = oaRejectsMapper.selectByPrimaryKey(oaPressRejectsDTO.getRejectsId());
                List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(oaPressRejectsDTO.getRejectsId(), oaPressRejectsDTO.getSetpStaus());
                List<String> opinionUsers = oaRejectsOpinions.stream().filter(v -> StringUtils.isEmpty(v.getOpinionContent())).map(v -> v.getOpinionUser()).collect(Collectors.toList());

                List<OaOpinionUserMailVO> mailList = new ArrayList<>();
                for (String opinionUser : opinionUsers) {
                    OaOpinionUserMailVO oaOpinionUserMailVO = oaOpinionUserMailMapper.selectByName(opinionUser);
                    mailList.add(oaOpinionUserMailVO);
                }
                //抄送人
                String cc = null;
                //主题
                String subject = "流程催办";
                //收件人
                String mailTo = mailList.stream().filter(v -> StringUtils.isNotEmpty(v.getMail())).map(v -> v.getMail()).collect(Collectors.joining(","));
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                try {
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                    mimeMessageHelper.setFrom(new InternetAddress(mailFromNick + " <" + mailFrom + ">"));
                    // 设置多个收件人
                    String[] toAddress = mailTo.split(",");
                    mimeMessageHelper.setTo(toAddress);
                    if (!StringUtils.isEmpty(cc)) {
                        mimeMessageHelper.setCc(cc);
                    }
                    mimeMessageHelper.setSubject(subject);
                    mimeMessageHelper.setText("流程：" + oaRejects.getHeadline() + oaRejects.getSerialNumber() + "到达你处，请尽快办理，谢谢！");
                    mailSender.send(mimeMessage);
                } catch (MessagingException e) {
                    log.info("发送邮件失败", e);
                }
            }
        });
        return Callback.success(true);
    }

    public Callback batchRejectsDelete(OaBathRejectsDeleteDTO oaBathRejectsDeleteDTO) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        List<OaSystemManage> oaSystemManages = oaSystemManageMapper.selectByAll();
        Set<Integer> userIds = oaSystemManages.stream().map(v -> v.getUserId()).collect(Collectors.toSet());

        if (!userIds.contains(userVo.getId())) {
            return Callback.error("暂无权限删除");
        }

        // 判断ID是不是数字
        for (Integer id : oaBathRejectsDeleteDTO.getRejectsIds()) {
            if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
                return Callback.error(2, "ID不能为空");
            }
            OaRejects oaRejects = oaRejectsMapper.selectByPrimaryKey(id);
            if (ObjectUtils.isNotEmpty(oaRejects)) {
                List<OaRejectsDetailVO> oaRejectsDetailVOS = oaRejectsDetailMapper.selectByRejectsId(oaRejects.getId());
                List<OaRejectsDetailVO> oaRejectsDetailVOList = oaRejectsDetailVOS.stream().filter(o -> o.getId() != null).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(oaRejectsDetailVOList)) {
                    for (OaRejectsDetailVO oaRejectsDetailVO : oaRejectsDetailVOList) {
                        oaRejectsDetailMapper.deleteByPrimaryKey(oaRejectsDetailVO.getId());
                        List<OaRejectsDetailFileVO> oaRejectsDetailFileVOS = oaRejectsDetailFileMapper.selectAllByRejectsDetailId(oaRejectsDetailVO.getId());
                        List<OaRejectsDetailFileVO> oaRejectsDetailFileVOList = oaRejectsDetailFileVOS.stream().filter(o -> o.getId() != null).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(oaRejectsDetailFileVOList)) {
                            for (OaRejectsDetailFileVO oaRejectsDetailFileVO : oaRejectsDetailFileVOList) {
                                oaRejectsDetailFileMapper.deleteByPrimaryKey(oaRejectsDetailFileVO.getId());
                            }
                        }
                    }
                }
                List<OaRejectsOpinionVO> oaRejectsOpinionVOS = oaRejectsOpinionMapper.selectByRejectsId(oaRejects.getId());
                List<OaRejectsOpinionVO> oaRejectsOpinionVOList = oaRejectsOpinionVOS.stream().filter(o -> o.getId() != null).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(oaRejectsOpinionVOList)) {
                    for (OaRejectsOpinionVO oaRejectsOpinionVO : oaRejectsOpinionVOList) {
                        oaRejectsOpinionMapper.deleteByPrimaryKey(oaRejectsOpinionVO.getId());
                    }
                }
                oaRejectsMapper.deleteByPrimaryKey(oaRejects.getId());
            }
        }
        return Callback.success(true);
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

        OaRejectsFile build = OaRejectsFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(LocalDateTime.now()).build();
        int count = oaRejectsFileMapper.insertSelective(build);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(build.getId()).fileName(map.get("fileName")).newfileName(subname).build();
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
            List<OaRejectsFile> oaRejectsFiles = oaRejectsFileMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (oaRejectsFiles != null && !oaRejectsFiles.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = oaRejectsFileMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count == 1) {
                        file.delete();
                    }
                    oaRejectsFileMapper.deleteByPrimaryKey(fileVo.getId());
                }
            }
        });
        return Callback.success("附件删除成功");
    }

    public Callback selectBatchDeletes() {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        Set<Integer> userIds = oaSystemManageMapper.selectByAll().stream().map(oaSystemManage -> oaSystemManage.getUserId()).collect(Collectors.toSet());
        if (userIds.contains(userVo.getId())) {
            OaRejectsVO build = OaRejectsVO.builder().batchDeletes(1).build();
            return Callback.success(build);
        }
        return Callback.success("暂无权限");
    }

    public Callback insertProcessingDetails(OaRejectsVO oaRejectsVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = new OaRejects();
        BeanConvertUtils.copyProperties(oaRejectsVO, oaRejects);

        if (ObjectUtils.isNotEmpty(oaRejects) && oaRejectsVO.getId() != null) {
            oaRejectsMapper.updateByPrimaryKeySelective(oaRejects);

            if (CollectionUtils.isNotEmpty(oaRejectsVO.getOaRejectsFileVOS())) {
                for (OaRejectsFileVO oaRejectsFileVO : oaRejectsVO.getOaRejectsFileVOS()) {
                    OaRejectsFile oaRejectsFile = oaRejectsFileMapper.selectByPrimaryKey(oaRejectsFileVO.getId());
                    Optional.ofNullable(oaRejectsFile).ifPresent(v -> {
                        OaRejectsFile build = OaRejectsFile.builder().id(oaRejectsFileVO.getId()).rejectsId(oaRejects.getId()).updateTime(LocalDateTime.now()).build();
                        oaRejectsFileMapper.updateByPrimaryKeySelective(build);
                    });
                }
            }

            return Callback.success(true);
        }
        return Callback.error(2, "新增数据失败!");
    }

}

