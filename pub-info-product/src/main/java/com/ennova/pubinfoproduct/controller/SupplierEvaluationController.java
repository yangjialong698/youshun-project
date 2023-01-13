package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfoproduct.entity.SupplierEvaluation;
import com.ennova.pubinfoproduct.service.SupplierEvaluationService;
import com.ennova.pubinfoproduct.vo.SupplierEvaluationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "供应商评价体系")
@Slf4j
@RestController
@RequestMapping("/purveyor")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class SupplierEvaluationController {

    private final SupplierEvaluationService supplierEvaluationService;

    @ApiOperation(value = "上传excel文件", tags = "供应商评价体系")
    @ApiOperationSort(value = 1)
    @PostMapping("/uploadFile")
    public Callback uploadFile(MultipartFile file){
        return supplierEvaluationService.uploadFile(file);
    }

    @ApiOperation(value = "下载模板", tags = "供应商评价体系")
    @ApiOperationSort(value = 2)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        supplierEvaluationService.downloadTemplate(response);
    }

    @ApiOperation(value = "列表查询", tags = "供应商评价体系")
    @ApiOperationSort(value = 3)
    @PostMapping("/supplierEvaluationList")
    public Callback<List<SupplierEvaluation>> supplierEvaluationList(@RequestBody @Validated SupplierEvaluationVO supplierEvaluationVO) {
        return supplierEvaluationService.supplierEvaluationList(supplierEvaluationVO);
    }

}

