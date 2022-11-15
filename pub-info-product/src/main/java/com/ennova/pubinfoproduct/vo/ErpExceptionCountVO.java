package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@ApiModel(value = "异常信息管理 - 近三天统计")
@Data
public class ErpExceptionCountVO {

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 模块异常信息
     */
    @ApiModelProperty(value = "模块异常信息")
    @NotBlank(message = "模块异常信息: 不能为空!")
    private List<Map<String,String>> moduleExceptionMessage;
}
