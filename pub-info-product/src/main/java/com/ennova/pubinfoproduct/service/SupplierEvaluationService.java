package com.ennova.pubinfoproduct.service;

import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfoproduct.daos.SupplierEvaluationMapper;
import com.ennova.pubinfoproduct.entity.SupplierEvaluation;
import com.ennova.pubinfoproduct.utils.ExcelReaderUtil;
import com.ennova.pubinfoproduct.vo.SupplierEvaluationVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class SupplierEvaluationService{

    private final SupplierEvaluationMapper supplierEvaluationMapper;

    public Callback uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            return Callback.error("文件为空");
        }
        Date paseEvaluationTime = null;
        List<SupplierEvaluation> list = new ArrayList<>();
        try{
            Sheet sheet = ExcelReaderUtil.getSheetByExcel(file);
            if (sheet == null) {
                return Callback.error("文件错误！");
            }
            Row headRow = sheet.getRow(0);
            if(headRow == null) {
                return Callback.error("请导入正确的模板");
            }
            String header1 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(0));
            String header2 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(1));
            String header3 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(2));
            String header4 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(3));
            String header5 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(4));
            String header6 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(5));
            String header7 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(6));
            String header8 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(7));
            String header9 = ExcelReaderUtil.convertCellValueToString(headRow.getCell(8));
            if(!"供应商名称".equals(header1) ||!"来料不良率".equals(header2) ||!"到货及时性".equals(header3) ||!"返工返修".equals(header4)
             ||!"配合度".equals(header5) ||!"来料短缺量".equals(header6) ||!"客诉".equals(header7) ||!"汇总".equals(header8) ||!"评价时间".equals(header9)){
                return Callback.error("请导入正确的模板");
            }
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if (row == null || StringUtils.isBlank(ExcelReaderUtil.convertCellValueToString(row.getCell(0)))) {
                    continue;
                }

                String supplierName = ExcelReaderUtil.convertCellValueToString(row.getCell(0)); // 供应商名称
                String incomingDefectiveRate = ExcelReaderUtil.convertCellValueToString(row.getCell(1)); // 来料不良率
                String timelyDelivery = ExcelReaderUtil.convertCellValueToString(row.getCell(2)); // 到货及时性
                String reworkRate = ExcelReaderUtil.convertCellValueToString(row.getCell(3)); // 返工返修
                String cooperation = ExcelReaderUtil.convertCellValueToString(row.getCell(4)); // 配合度
                String incomingShortage = ExcelReaderUtil.convertCellValueToString(row.getCell(5)); // 来料短缺量
                String customerComplaints = ExcelReaderUtil.convertCellValueToString(row.getCell(6)); // 客诉
                String summary = ExcelReaderUtil.convertCellValueToString(row.getCell(7)); // 汇总
                String evaluationTime = ExcelReaderUtil.convertCellValueToString(row.getCell(8)); // 评价时间

                SupplierEvaluation supplierEvaluation = SupplierEvaluation.builder().supplierName(supplierName).incomingDefectiveRate(Integer.valueOf(incomingDefectiveRate)).timelyDelivery(Integer.valueOf(timelyDelivery))
                        .reworkRate(Integer.valueOf(reworkRate)).cooperation(Integer.valueOf(cooperation)).incomingShortage(Integer.valueOf(incomingShortage))
                        .customerComplaints(Integer.valueOf(customerComplaints)).summary(Integer.valueOf(summary)).createTime(new Date()).build();

                if (StringUtils.isNotBlank(evaluationTime) && DateUtil.parse(evaluationTime, "yyyy-MM-dd") != null) {
                    paseEvaluationTime = DateUtil.parseDate(evaluationTime);
                    supplierEvaluation.setEvaluationTime(paseEvaluationTime);
                }
                list.add(supplierEvaluation);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(paseEvaluationTime != null){
            int exist = supplierEvaluationMapper.selectByEvaluationTime(paseEvaluationTime);
            if(exist > 0){
                supplierEvaluationMapper.deleteByEvaluationTime(paseEvaluationTime);
            }
        }

        int i = supplierEvaluationMapper.insertList(list);
        if (i > 0) {
            return Callback.success("上传成功");
        }
        return Callback.error("上传失败");
    }

    public void downloadTemplate(HttpServletResponse response) {
        try {
            String fileName = "供应商体系模板.xlsx";
            String filePath = "/mnt/ennova/template/supplierEvaluationTemplate.xlsx";
//            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
            InputStream inputStream = new FileInputStream(filePath);
            if (inputStream == null) {
                log.error("模板文件不存在");
                return;
            }
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            inputStream.close();
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.addHeader("Content-Length", "" + bytes.length);
            response.setContentType("application/octet-stream;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            log.info("文件大小：{}", bytes.length);
        } catch (Exception e) {
            log.info("下载模板失败", e);
            e.printStackTrace();
        }
    }

    public Callback<List<SupplierEvaluation>> supplierEvaluationList(SupplierEvaluationVO supplierEvaluationVO) {

        String[] sortArr = supplierEvaluationVO.getSortArr();
        String[] orderArr = supplierEvaluationVO.getOrderArr();

        String sort = "";
        if (sortArr == null && orderArr == null || sortArr.length == 0 && orderArr.length == 0) {
            // 默认按照综合排序
            sort = "summary desc";
        } else {
            for (int i = 0; i < sortArr.length; i++) {
                String sortStr = sortArr[i];
                if("supplierName".equals(sortStr)){
                    sortStr = "supplier_name";
                }else if("incomingDefectiveRate".equals(sortStr)){
                    sortStr = "incoming_defective_rate";
                }else if("timelyDelivery".equals(sortStr)){
                    sortStr = "timely_delivery";
                }else if("reworkRate".equals(sortStr)){
                    sortStr = "rework_rate";
                }else if("cooperation".equals(sortStr)){
                    sortStr = "cooperation";
                }else if("incomingShortage".equals(sortStr)){
                    sortStr = "incoming_shortage";
                }else if("customerComplaints".equals(sortStr)){
                    sortStr = "customer_complaints";
                }
                sort += sortStr + " " + orderArr[i] + ",";
            }
            sort = sort.substring(0, sort.length() - 1);
        }
        PageHelper.orderBy(sort);
        String year = "";
        String month = "";
        String searchDate = supplierEvaluationVO.getSearchDate();
        if (StringUtils.isNotBlank(searchDate)) {
            String[] split = searchDate.split("-");
            year = split[0];
            month = split[1];
        }
        List<SupplierEvaluation> list = supplierEvaluationMapper.selectBySort(year, month);
        return Callback.success(list);
    }

}
