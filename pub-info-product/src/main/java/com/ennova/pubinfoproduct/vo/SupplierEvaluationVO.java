package com.ennova.pubinfoproduct.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 供应商评价表
 */
@ApiModel(value = "供应商评价排序")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierEvaluationVO {

    @ApiModelProperty(value = "排序字段数组")
    private String[] sortArr;

    @ApiModelProperty(value = "排序方式数组")
    private String[] orderArr;

    // Validate校验日期字符串不能为空
    @ApiModelProperty(value = "日期: yyyy-MM")
    @NotBlank(message = "日期不能为空")
    private String searchDate;

}