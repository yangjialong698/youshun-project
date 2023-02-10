package com.ennova.pubinfoproduct.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.daos.CustomerAccountInfoMapper;
import com.ennova.pubinfoproduct.daos.ErpReworkRepairMapper;
import com.ennova.pubinfoproduct.daos.SupplierEvaluationMapper;
import com.ennova.pubinfoproduct.daos.SupplierInfoMapper;
import com.ennova.pubinfoproduct.entity.SupplierEvaluation;
import com.ennova.pubinfoproduct.entity.SupplierInfo;
import com.ennova.pubinfoproduct.utils.ExcelReaderUtil;
import com.ennova.pubinfoproduct.vo.ComplaintVO;
import com.ennova.pubinfoproduct.vo.DefectRateVO;
import com.ennova.pubinfoproduct.vo.SupplierEvaluationVO;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class SupplierEvaluationService{

    private final SupplierEvaluationMapper supplierEvaluationMapper;
    private final ErpReworkRepairMapper erpReworkRepairMapper;
    private final CustomerAccountInfoMapper customerAccountInfoMapper;
    private final SupplierInfoMapper supplierInfoMapper;

    public Callback uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            return Callback.error("文件为空");
        }

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

            if(!"供应商编号".equals(header1) ||!"供应商名称".equals(header2) ||!"来料不良率".equals(header3) ||!"到货及时性".equals(header4)
             ||!"配合度".equals(header5) ||!"来料短缺量".equals(header6) ||!"评价时间".equals(header7)){
                return Callback.error("请导入正确的模板");
            }
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if (row == null || StringUtils.isBlank(ExcelReaderUtil.convertCellValueToString(row.getCell(0)))) {
                    continue;
                }

                String supplierNo = ExcelReaderUtil.convertCellValueToString(row.getCell(0)); // 供应商编码
                String supplierName = ExcelReaderUtil.convertCellValueToString(row.getCell(1)); // 供应商名称
                String incomingDefectiveRate = ExcelReaderUtil.convertCellValueToString(row.getCell(2)); // 来料不良率
                String timelyDelivery = ExcelReaderUtil.convertCellValueToString(row.getCell(3)); // 到货及时性
                String cooperation = ExcelReaderUtil.convertCellValueToString(row.getCell(4)); // 配合度
                String incomingShortage = ExcelReaderUtil.convertCellValueToString(row.getCell(5)); // 来料短缺量
                String evaluationTime = ExcelReaderUtil.convertCellValueToString(row.getCell(6)); // 评价时间
                SupplierEvaluation supplierEvaluation = SupplierEvaluation.builder().supplierNo(Integer.valueOf(supplierNo)).supplierName(supplierName).incomingDefectiveRate(Integer.valueOf(incomingDefectiveRate))
                        .timelyDelivery(Integer.valueOf(timelyDelivery)).cooperation(Integer.valueOf(cooperation)).incomingShortage(Integer.valueOf(incomingShortage)).build();

                if (StringUtils.isNotBlank(evaluationTime) && DateUtil.parse(evaluationTime, "yyyy-MM-dd") != null) {
                    DateTime paseEvaluationTime = DateUtil.parseDate(evaluationTime);
                    supplierEvaluation.setEvaluationTime(paseEvaluationTime);
                }
                list.add(supplierEvaluation);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        

        list.forEach(supplierEvaluation -> {
            Date evaluationTime = supplierEvaluation.getEvaluationTime();
            int year = DateUtil.year(evaluationTime);
            int month = DateUtil.month(evaluationTime) + 1;

            SupplierEvaluation exist = supplierEvaluationMapper.selectBySupplierNoAndEvaluationTime(supplierEvaluation.getSupplierNo(), year, month);
            if (exist != null) {
                supplierEvaluation.setId(exist.getId());
                // 返工返修、汇总需保留两位小数；汇总6条记录的平均值
                Integer incomingDefectiveRate = supplierEvaluation.getIncomingDefectiveRate() == null ? 0 : supplierEvaluation.getIncomingDefectiveRate();
                Integer timelyDelivery = supplierEvaluation.getTimelyDelivery() == null ? 0 : supplierEvaluation.getTimelyDelivery();
                Double reworkRate = exist.getReworkRate() == null ? 0 : exist.getReworkRate();
                Integer cooperation = supplierEvaluation.getCooperation() == null ? 0 : supplierEvaluation.getCooperation();
                Integer incomingShortage = supplierEvaluation.getIncomingShortage() == null ? 0 : supplierEvaluation.getIncomingShortage();
                Integer customerComplaints = exist.getCustomerComplaints() == null ? 0 : exist.getCustomerComplaints();
                Double summary = Double.valueOf((incomingDefectiveRate + timelyDelivery + reworkRate + cooperation + incomingShortage + customerComplaints) / 6);
                summary = new BigDecimal(summary).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                supplierEvaluation.setSummary(summary);
                supplierEvaluation.setUpdateTime(new Date());
                supplierEvaluationMapper.updateByPrimaryKeySelective(supplierEvaluation);
            } else {
                // 没有返工返修、客诉记录，直接新增
                Integer incomingDefectiveRate = supplierEvaluation.getIncomingDefectiveRate() == null ? 0 : supplierEvaluation.getIncomingDefectiveRate();
                Integer timelyDelivery = supplierEvaluation.getTimelyDelivery() == null ? 0 : supplierEvaluation.getTimelyDelivery();
                Integer cooperation = supplierEvaluation.getCooperation() == null ? 0 : supplierEvaluation.getCooperation();
                Integer incomingShortage = supplierEvaluation.getIncomingShortage() == null ? 0 : supplierEvaluation.getIncomingShortage();
                Double summary = Double.valueOf((incomingDefectiveRate + timelyDelivery + cooperation + incomingShortage) / 6);
                summary = new BigDecimal(summary).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                supplierEvaluation.setSummary(summary);
                supplierEvaluation.setCreateTime(new Date());
                supplierEvaluationMapper.insertSelective(supplierEvaluation);
            }
        });
        return Callback.success();
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


    // 定时任务，每天凌晨1点执行
    @Scheduled(cron = "0 0 1 * * ?")
    public void supplierEvaluationJob(){
        int year = DateUtil.thisYear();
        int month = DateUtil.thisMonth() + 1;

        List<DefectRateVO> defectList = erpReworkRepairMapper.selectByReworkTime(year, month);  // 月度不良率
        List<ComplaintVO> complaintList = customerAccountInfoMapper.selectByComplainTime(year, month); // 月度投诉

        for (DefectRateVO defectRateVO : defectList) {
            boolean flag = true;
            for (ComplaintVO complaintVO : complaintList) {
                if (defectRateVO.getSupplierNo() == complaintVO.getSupplierNo()) {
                    complaintVO.setDefectRate(defectRateVO.getDefectRate());
                    flag = false;
                }
            }
            if (flag) {
                ComplaintVO complaintVO = new ComplaintVO();
                complaintVO.setSupplierNo(defectRateVO.getSupplierNo());
                complaintVO.setDefectRate(defectRateVO.getDefectRate());
                complaintList.add(complaintVO);
            }
        };
        List<ComplaintVO> newList = complaintList;

        // 查询所有供应商，判断是否存在评价表中，不存在则新增，存在则更新
        List<SupplierInfo> supplierInfoList = supplierInfoMapper.selectAll();
        if (CollectionUtil.isNotEmpty(supplierInfoList)) {
            int finalMonth = month;
            supplierInfoList.forEach(supplierInfo -> {
                SupplierEvaluation supplierEvaluation = supplierEvaluationMapper.selectBySupplierNoAndTime(supplierInfo.getSupplierNo(), year, finalMonth);
                // 不存在，新增
                if (supplierEvaluation == null) {
                    for (int i = 0; i < newList.size(); i++) {
                        ComplaintVO vo = newList.get(i);
                        if (vo.getSupplierNo() == supplierInfo.getSupplierNo()) {
                            SupplierEvaluation temp = new SupplierEvaluation();
                            temp.setSupplierNo(vo.getSupplierNo());
                            temp.setSupplierName(supplierInfo.getSupplier());
                            temp.setReworkRate(vo.getDefectRate());
                            temp.setCustomerComplaints(vo.getCountNum() != null && vo.getCountNum() > 0 ? 0 : 100);
                            temp.setEvaluationTime(new Date());
                            temp.setCreateTime(new Date());
                            supplierEvaluationMapper.insert(temp);
                        }
                    }
                }else{
                    for (int i = 0; i < newList.size(); i++) {
                        ComplaintVO vo = newList.get(i);
                        if (vo.getSupplierNo() == supplierEvaluation.getSupplierNo()) {
                            SupplierEvaluation temp = new SupplierEvaluation();
                            temp.setId(supplierEvaluation.getId());
                            // 返工返修、汇总需保留两位小数；汇总6条记录的平均值
                            Integer incomingDefectiveRate = supplierEvaluation.getIncomingDefectiveRate() == null ? 0 : supplierEvaluation.getIncomingDefectiveRate();
                            Integer timelyDelivery = supplierEvaluation.getTimelyDelivery() == null ? 0 : supplierEvaluation.getTimelyDelivery();
                            Double reworkRate = vo.getDefectRate() == null ? 0 : vo.getDefectRate();
                            Integer cooperation = supplierEvaluation.getCooperation() == null ? 0 : supplierEvaluation.getCooperation();
                            Integer incomingShortage = supplierEvaluation.getIncomingShortage() == null ? 0 : supplierEvaluation.getIncomingShortage();
                            Integer customerComplaints = vo.getCountNum() != null && vo.getCountNum() > 0 ? 0 : 100;
                            Double summary = (incomingDefectiveRate + timelyDelivery + reworkRate + cooperation + incomingShortage + customerComplaints) / 6;
                            summary = new BigDecimal(summary).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            temp.setSummary(summary);
                            temp.setUpdateTime(new Date());
                            supplierEvaluationMapper.updateByPrimaryKeySelective(temp);
                        }
                    }
                }
            });
        }
    }



}
