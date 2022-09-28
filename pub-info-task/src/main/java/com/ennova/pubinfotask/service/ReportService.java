package com.ennova.pubinfotask.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfotask.dao.MailMapper;
import com.ennova.pubinfotask.dao.ReportMapper;
import com.ennova.pubinfotask.dao.YsLoginMonthMapper;
import com.ennova.pubinfotask.entity.Mail;
import com.ennova.pubinfotask.entity.YsLoginMonth;
import com.ennova.pubinfotask.utils.BeanConvertUtils;
import com.ennova.pubinfotask.utils.DayTypeEnum;
import com.ennova.pubinfotask.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ReportService {

    private final ReportMapper reportMapper;
    private final YsLoginMonthMapper ysLoginMonthMapper;
    private final MailMapper mailMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${spring.mail.mailFromNick}")
    private String mailFromNick;



    //定时任务，每个月1号0点1分执行
    @Scheduled(cron = "0 1 0 1 * ?")
//    @Scheduled(cron = "0 10 14 * * ?")
    public void updateYsLoginMonthJob() {
        List<VisitVO> rateList = reportMapper.selectVisitList();
        if (CollectionUtils.isNotEmpty(rateList)) {
            for (VisitVO rateVO : rateList) {
                YsLoginMonth loginMonth = BeanConvertUtils.convertTo(rateVO, YsLoginMonth::new);
                List<YsLoginMonth> list = ysLoginMonthMapper.selectByCountNumAndLoginDateAndUserIdAndUserName(loginMonth);
                if (CollectionUtils.isEmpty(list)) {
                    loginMonth.setCreateTime(new Date());
                    ysLoginMonthMapper.insertSelective(loginMonth);
                }
            }
        }
    }

    // 任务进度排名
    public Callback<BaseVO<SortMasterRateVO>> selectTaskSortList(Integer page, Integer pageSize, String name) {
        Page<SortMasterRateVO> startPage = PageMethod.startPage(page, pageSize);
        List<SortMasterRateVO> masterRateList = reportMapper.selectSortList(name);
        masterRateList.forEach(masterRateVO -> {
            if (page != 1) {
                masterRateVO.setSortNum((page - 1) * pageSize + masterRateVO.getSortNum());
            }
        });

        BaseVO<SortMasterRateVO> baseVO = new BaseVO<>(masterRateList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback selectLastMothList() {
        List<VisitVO> lastMothList = Lists.newArrayList();
        String listStr = redisTemplate.opsForValue().get("lastMothList");
        if (StringUtils.isNotBlank(listStr)) {
            List<VisitVO> visitVOS = JSON.parseArray(listStr, VisitVO.class);
            lastMothList.addAll(visitVOS);
        } else {
            lastMothList = reportMapper.selectLastMothList();
            if (CollectionUtils.isNotEmpty(lastMothList)) {
                long betweenSecond = DateUtil.between(new Date(), DateUtil.beginOfMonth(DateUtil.offsetMonth(new Date(), 1)), DateUnit.SECOND);
                redisTemplate.opsForValue().set("lastMothList", JSON.toJSONString(lastMothList), betweenSecond, TimeUnit.SECONDS);
            }
        }
        return Callback.success(lastMothList);
    }

    // 访问日志明细
    public Callback<BaseVO<YsLoginMonth>> selectSortDetailList(Integer page, Integer pageSize, String userName, String loginDate) {
        Page<YsLoginMonth> startPage = PageMethod.startPage(page, pageSize);
        List<YsLoginMonth> mothList = ysLoginMonthMapper.selectAllByUserNameAndLoginDate(userName, loginDate);
        BaseVO<YsLoginMonth> baseVO = new BaseVO<>(mothList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    // 成本
    public Callback selectCostList() {
        List<CostVO> costVOS = reportMapper.selectCostList();
        return Callback.success(costVOS);
    }

    // 成本明细
    public Callback<BaseVO<CostDetailVO>> selectCostDetailList(Integer page, Integer pageSize, String name) {
        Page<CostDetailVO> startPage = PageMethod.startPage(page, pageSize);
        List<CostDetailVO> costDetailList = reportMapper.selectCostDetailList(name);
        BaseVO<CostDetailVO> baseVO = new BaseVO<>(costDetailList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    // 日报提交统计
    public Callback<BaseVO<TdailySubmitCountVO>> selectTdailySubmitCountList(Integer page, Integer pageSize, Integer dayType, String userName) {
        String dayNumStr = DayTypeEnum.getDayTypeEnum(dayType).getName();
        Page<TdailySubmitCountVO> startPage = PageMethod.startPage(page, pageSize);
        List<TdailySubmitCountVO> tdailySubmitCountList = reportMapper.selecTdailySubmitCount(dayNumStr, userName);
        BaseVO<TdailySubmitCountVO> baseVO = new BaseVO<>(tdailySubmitCountList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    //每天早上9点执行定时任务
    @Scheduled(cron = "0 0 9 * * ?")
//    @Scheduled(cron = "00 34 16 * * ?")
    public void sendHtmlMailThymeLeaf() {
        // 发件人，配置文件中加载
        //String mailFrom = this.mailFrom;
        // 发件人昵称，配置文件中加载，此处省略
        //String mailFromNick = "浦江在线服务";
        // 抄送人
        String cc = null;
        // 主题
        String subject = "任务进度排名";
        // 收件人
        String mailTo = "";
        List<Mail> mailList = mailMapper.selectAll();
        List<SortMasterRateVO> masterRateList = reportMapper.selectSortList(null);
        List<SortMasterRateVO> taskList = masterRateList.stream().limit(10).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(mailList) && CollectionUtils.isNotEmpty(taskList)) {
            mailTo = mailList.stream().map(Mail::getMail).collect(Collectors.joining(","));
            Context context = new Context();
            context.setVariable("taskList", taskList);
            String content = templateEngine.process("mail.html", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setFrom(new InternetAddress(mailFromNick + " <" + this.mailFrom + ">"));
                // 设置多个收件人
                String[] toAddress = mailTo.split(",");
                mimeMessageHelper.setTo(toAddress);
                if (!StringUtils.isEmpty(cc)) {
                    mimeMessageHelper.setCc(cc);
                }
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(content, true);
                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                log.info("发送邮件失败", e);
            }
        }


    }


}
