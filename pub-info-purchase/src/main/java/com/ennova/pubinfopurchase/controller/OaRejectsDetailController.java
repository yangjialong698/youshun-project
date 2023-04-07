package com.ennova.pubinfopurchase.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.config.ExcelDataListener;
import com.ennova.pubinfopurchase.dao.OaRejectsDetailMapper;
import com.ennova.pubinfopurchase.dto.BadDisposalDTO;
import com.ennova.pubinfopurchase.dto.BadItemDTO;
import com.ennova.pubinfopurchase.dto.OaRejectsExportDTO;
import com.ennova.pubinfopurchase.dto.PrdInfoDTO;
import com.ennova.pubinfopurchase.entity.OaRejectsDetail;
import com.ennova.pubinfopurchase.service.OaRejectsDetailService;
import com.ennova.pubinfopurchase.utils.BeanConvertUtils;
import com.ennova.pubinfopurchase.vo.FileVO;
import com.ennova.pubinfopurchase.vo.OaRejectsDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/17
 */
@Slf4j
@Api(tags = "公共信息平台-oa不合格品处理单明细")
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/rejectsDetail")
public class OaRejectsDetailController {

    private final OaRejectsDetailService oaRejectsDetailService;
    private final OaRejectsDetailMapper oaRejectsDetailMapper;

    @ApiOperation(value = "根据工单号查询零件号和零件名称")
    @GetMapping("/getPrdInfo")
    public Callback<List<PrdInfoDTO>> getPrdInfo(String workOrderNo){
        return oaRejectsDetailService.getPrdInfo(workOrderNo);
    }

    @ApiOperation(value = "获取不良项目")
    @GetMapping("/getBadItem")
    public Callback<List<BadItemDTO>> getBadItem(){
        return oaRejectsDetailService.getBadItem();
    }

    @ApiOperation(value = "获取不良处置")
    @GetMapping("/getBadDisposal")
    public Callback<List<BadDisposalDTO>> getBadDisposal(){
        return oaRejectsDetailService.getBadDisposal();
    }

    @ApiOperation(value = "oa不合格品处理单 - 新增不良品明细信息")
    @PostMapping("/insertRejectsDetail")
    public Callback insertRejectsDetail(@RequestBody @Validated OaRejectsDetailVO oaRejectsDetailVO) {
        return oaRejectsDetailService.insertRejectsDetail(oaRejectsDetailVO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良品明细信息列表")
    @GetMapping("/selectRejectsDetail")
    public Callback<List<OaRejectsDetailVO>> selectRejectsDetail() {
        return oaRejectsDetailService.selectRejectsDetail();
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良品删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "不良品明细ID", required = true)
    })
    @GetMapping("/deleteRejectsDetail")
    public Callback deleteRejectsDetail(Integer id) {
        return oaRejectsDetailService.deleteRejectsDetail(id);
    }

    @ApiOperation(value = "oa不合格品处理单 -  不良品明细文件上传")
    @PostMapping("/upload")
    public Callback<FileVO> upload(MultipartFile file) {
        return oaRejectsDetailService.uploadFile(file);
    }

    @ApiOperation(value = "oa不合格品处理单 -  不良品明细导出")
    @GetMapping("/export")
    public void exportUserExcel(HttpServletResponse response) {
        try {
            List<OaRejectsDetailVO> oaRejectsDetailVOS = oaRejectsDetailMapper.selectRejectsDetail();
            List<OaRejectsExportDTO> oaRejectsExportDTOS = BeanConvertUtils.convertListTo(oaRejectsDetailVOS, OaRejectsExportDTO::new);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("不良品明细列表.xlsx", "UTF-8"));
            EasyExcel.write(outputStream, OaRejectsExportDTO.class).sheet("不良品明细列表").doWrite(oaRejectsExportDTOS);
        } catch (Exception e) {
            throw new RuntimeException("导出 Excel 文件失败", e);
        }
    }

    @ApiOperation(value = "oa不合格品处理单 -  不良品明细导入")
    @PostMapping("/import")
    public void importUserExcel(@RequestPart(value = "file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            ExcelDataListener listener = new ExcelDataListener();
            ExcelReader excelReader = EasyExcel.read(inputStream, OaRejectsExportDTO.class, listener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
            List<OaRejectsExportDTO> dataList = listener.getDataList();
            List<OaRejectsDetail> oaRejectsDetails = BeanConvertUtils.convertListTo(dataList, OaRejectsDetail::new);
            CompletableFuture.runAsync(() -> {
                //提交任务到默认的ForkJoinPool实例，实现异步执行任务的效果，避免任务执行阻塞主线程
                try {
                    oaRejectsDetailMapper.batchInsert(oaRejectsDetails);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("导入 Excel 文件失败", e);
        }
    }

}
