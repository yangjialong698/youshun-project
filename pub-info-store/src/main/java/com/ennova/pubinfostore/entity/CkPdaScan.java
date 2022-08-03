package com.ennova.pubinfostore.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * PDA扫描表
 * */
@ApiOperation(value = "PDA扫描表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CkPdaScan {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    /**
     * 条形码1
     */
    @ApiModelProperty(value = "条形码1")
    private String barCode1;

    /**
     * 条形码2
     */
    @ApiModelProperty(value = "条形码2")
    private String barCode2;

    /**
     * 校验条形码是否成功（0 - 失败；1 - 成功）
     */
    @ApiModelProperty(value = "校验条形码是否成功（0 - 失败；1 - 成功）", example = "1")
    private Integer checkStatus;

    /**
     * 条形码2
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}