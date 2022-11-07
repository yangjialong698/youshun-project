package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 异常信息表
 */
@ApiModel(value = "异常信息表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpException {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 模块类型：1-机加摇臂 2-摇臂轴 3-摇臂后处理 4-装配
     */
    @ApiModelProperty(value = "模块类型：1-机加摇臂 2-摇臂轴 3-摇臂后处理 4-装配")
    private Integer muduleType;

    /**
     * 模块异常信息
     */
    @ApiModelProperty(value = "模块异常信息")
    private String moduleExceptionMessage;

    /**
     * 是否删除：0-保留 1-删除
     */
    @ApiModelProperty(value = "是否删除：0-保留 1-删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}