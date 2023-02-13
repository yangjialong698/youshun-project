package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfopurchase.dto.FileDelDTO;
import com.ennova.pubinfopurchase.entity.CgContactInformation;
import com.ennova.pubinfopurchase.service.CgPurchaseInfoService;
import com.ennova.pubinfopurchase.vo.CgPurchaseInfoVO;
import com.ennova.pubinfopurchase.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/11
 */
@Slf4j
@Api(tags = "公共信息平台-采购信息")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/purchase")
public class CgPurchaseInfoController {

    private final CgPurchaseInfoService cgPurchaseInfoService;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${spring.mail.mailFromNick}")
    private String mailFromNick;

    @ApiOperation(value = "采购附件 - 文件上传")
    @PostMapping("/upload")
    public Callback<FileVO> upload(MultipartFile file) {
        return cgPurchaseInfoService.uploadFile(file);
    }

    @ApiOperation(value = "采购附件 - 附件下载")
    @GetMapping("/netDownLoadFile")
    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        cgPurchaseInfoService.netDownLoadFile(netAddress, filename, response);
    }

    @ApiOperation(value = "采购附件 - 文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO) {
        return cgPurchaseInfoService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "采购信息 - 发布和修改采集信息")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated CgPurchaseInfoVO cgPurchaseInfoVO) {
        return cgPurchaseInfoService.insertOrUpdate(cgPurchaseInfoVO);
    }

    @ApiOperation(value = "采购信息 - 获取主任务编号和名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "主任务名称")
    })
    @GetMapping("/selectTaskNumber")
    public Callback selectTaskNumber(String name) {
        return cgPurchaseInfoService.selectTaskNumber(name);
    }

    @ApiOperation(value = "采购信息 - 删除")
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return cgPurchaseInfoService.delete(id);
    }

    @ApiOperation(value = "采购信息 - 采购信息首页分页列表(后台列表查询，官网Fegin调用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "name", value = "物料名称"),
            @ApiImplicitParam(name = "type", value = "类型： 0：采购 1：合作"),
    })
    @GetMapping("/selectPurchaseInfo")
    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(@RequestParam(defaultValue = "1") Integer page,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                                 @RequestParam("name") String name,
                                                                 @RequestParam("type") Integer type) {
        return cgPurchaseInfoService.selectPurchaseInfo(page, pageSize, name, type);
    }

    @ApiOperation(value = "采购信息 - 采购信息首页不分页列表")
    @GetMapping("/selectAllPurchaseInfo")
    public Callback<List<CgPurchaseInfoVO>> selectAllPurchaseInfo() {
        return cgPurchaseInfoService.selectAllPurchaseInfo();
    }

    @ApiOperation(value = "采购信息 - 采购信息查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "采购信息ID", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<CgPurchaseInfoVO> getDetail(Integer id) {
        return cgPurchaseInfoService.getDetail(id);
    }

    @ApiOperation(value = "联系信息")
    @GetMapping("/contactInformation")
    public Callback<CgContactInformation> contactInformation() {
        return cgPurchaseInfoService.contactInformation();
    }

    @ApiOperation(value = "采购供应商回复")
    @PostMapping("/contact")
    public Callback contact() {
        //发件人
//        String mailFrom = "youshunkongqi@ennova.com.cn";
        //发件人昵称
//        String mailFromNick = "浦江在线服务";

        //抄送人
        String cc = null;
        //主题
        String subject = "供应商回复";

        //收件人
        String mailTo = "yangjialong@ennova.com.cn";
//        Context context = new Context();
//        context.setVariable("回复信息内容为：", "2023.2.9.10");
//        String content = templateEngine.process("mail.html", context);
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
            mimeMessageHelper.setText("回复信息内容为：2023.2.9.10");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.info("发送邮件失败", e);
        }
        return Callback.success();
    }

}
