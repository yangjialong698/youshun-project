package com.ennova.pubinfopurchase.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.*;
import com.ennova.pubinfopurchase.entity.*;
import com.ennova.pubinfopurchase.utils.BeanConvertUtils;
import com.ennova.pubinfopurchase.utils.OaUtils;
import com.ennova.pubinfopurchase.vo.OaRejectsDetailFileVO;
import com.ennova.pubinfopurchase.vo.OaRejectsDetailVO;
import com.ennova.pubinfopurchase.vo.OaRejectsOpinionVO;
import com.ennova.pubinfopurchase.vo.OaRejectsVO;
import com.github.pagehelper.Page;
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

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
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
    private final HttpServletRequest request;
    private final UserMapper userMapper;
    private final OaMailMapper mailMapper;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${spring.mail.mailFromNick}")
    private String mailFromNick;

    public Callback insertRejects(OaRejectsVO oaRejectsVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = new OaRejects();
        BeanConvertUtils.copyProperties(oaRejectsVO, oaRejects);

        if (ObjectUtils.isNotEmpty(oaRejects)) {
            String serialNumber = oaRejectsMapper.selectLastSerialNumber();
            if (StringUtils.isEmpty(serialNumber)) {
                serialNumber = "";
            }
            oaRejects.setSerialNumber(OaUtils.getSerialNumber(serialNumber));
            oaRejects.setHeadline(oaRejectsVO.getWorkCenter() + "不合格评审单");
            oaRejects.setSetpStaus(oaRejectsVO.getSetpStaus());
            oaRejects.setUserId(userVo.getId());
            oaRejects.setUserName(userMapper.selectById(userVo.getId()).getUserName());
            oaRejects.setSchedule("进行中");
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
            return Callback.success(true);
        }
        return Callback.error(2, "新增数据失败!");
    }

    public Callback updateRejects(OaRejectsVO oaRejectsVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = new OaRejects();
        BeanConvertUtils.copyProperties(oaRejectsVO, oaRejects);

        if (ObjectUtils.isNotEmpty(oaRejects) && oaRejectsVO.getId() != null) {
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
            return Callback.success(true);
        }
        return Callback.error(2, "修改数据失败!");
    }

    public Callback<BaseVO<OaRejectsVO>> selectRejectsInfo(Integer page, Integer pageSize, String startTime, String endTime, String workCenter, String exigencyStatus, String schedule, String headline) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<OaRejects> oaRejects = oaRejectsMapper.selectRejectsInfo(startTime, endTime, workCenter, exigencyStatus, schedule, headline);
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
            oaRejectsVO.setOaRejectsDetails(oaRejectsDetailVOS);
            List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(v.getId(), v.getSetpStaus());
            if (CollectionUtils.isNotEmpty(oaRejectsOpinions)) {
                oaRejectsVO.setOaRejectsOpinionVOS(oaRejectsOpinions);
            }
            List<Integer> opinionUserIds = oaRejectsOpinions.stream().map(o -> o.getOpinionUserId()).collect(Collectors.toList());
            List<LocalDateTime> publishTimes = oaRejectsOpinions.stream().map(o -> o.getPublishTime()).collect(Collectors.toList());
            LocalDateTime publishTime = null;
            if (publishTimes.size() > 0) {
                publishTime = publishTimes.stream().filter(o -> ObjectUtils.isNotEmpty(o)).reduce((first, second) -> second).orElse(LocalDateTime.now());
            }
            oaRejectsVO.setPublishTime(publishTime);
            if (opinionUserIds.contains(userVo.getId()) && v.getSetpStaus() > 1) {
                oaRejectsVO.setBackStatus(1);
            }
            if (StringUtils.isEmpty(v.getTransactor()) || v.getTransactor().equals(userMapper.selectById(userVo.getId()).getUserName())) {
                oaRejectsVO.setOpenStatus(1);
            }
            rejectsInfos.add(oaRejectsVO);
        });
        BaseVO<OaRejectsVO> baseVO = new BaseVO<>(rejectsInfos, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
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
        List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(oaRejects.getId(), oaRejects.getSetpStaus());
        if (CollectionUtils.isNotEmpty(oaRejectsOpinions)) {
            oaRejectsVO.setOaRejectsOpinionVOS(oaRejectsOpinions);
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

    public Callback backRejects(Integer id, Integer setpStaus) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = oaRejectsMapper.selectByPrimaryKey(id);
        List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(id, setpStaus);
        List<OaRejectsOpinionVO> oaRejectsOpinionss = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(id, setpStaus - 1);

        for (OaRejectsOpinionVO oaRejectsOpinionVO : oaRejectsOpinions) {
            OaRejectsOpinion oaRejectsOpinion = new OaRejectsOpinion();
            oaRejectsOpinion.setId(oaRejectsOpinionVO.getId());
            oaRejectsOpinion.setDelFlag(1);
            oaRejectsOpinionMapper.updateByPrimaryKeySelective(oaRejectsOpinion);
        }

        for (OaRejectsOpinionVO oaRejectsOpinionVO : oaRejectsOpinionss) {
            OaRejectsOpinion oaRejectsOpinion = BeanConvertUtils.convertTo(oaRejectsOpinionVO, OaRejectsOpinion::new);
            oaRejectsOpinion.setOpinionContent(null);
            oaRejectsOpinion.setPublishTime(null);
            oaRejectsOpinionMapper.updateByPrimaryKey(oaRejectsOpinion);
        }

        if (oaRejects.getSetpStaus() > 1) {
            oaRejects.setId(id);
            oaRejects.setSetpStaus(setpStaus - 1);
            String opinionUsers = oaRejectsOpinionss.stream().filter(v -> StringUtils.isEmpty(v.getOpinionContent())).map(v -> v.getOpinionUser()).collect(Collectors.joining(","));
            oaRejects.setTransactor(opinionUsers);
            oaRejectsMapper.updateByPrimaryKeySelective(oaRejects);
            return Callback.success(true);
        }else {
            return Callback.error(2, "当前不能回退!");
        }
    }

    public Callback pressRejects(Integer id, Integer setpStaus) throws MessagingException {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaRejects oaRejects = oaRejectsMapper.selectByPrimaryKey(id);
        List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsIdAndSetpStaus(id, setpStaus);
        List<String> opinionUsers = oaRejectsOpinions.stream().filter(v -> StringUtils.isEmpty(v.getOpinionContent())).map(v -> v.getOpinionUser()).collect(Collectors.toList());

        List<Mail> mailList = new ArrayList<>();
        for (String opinionUser : opinionUsers) {
            Mail mail = mailMapper.selectByName(opinionUser);
            mailList.add(mail);
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

        return Callback.success(true);
    }

}
