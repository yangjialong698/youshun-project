package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value="特别关注异常信息")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpSpecialException {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
    * 模块异常信息
    */
    @ApiModelProperty(value="模块异常信息")
    private String exceptionMessage;

    /**
    * 是否删除：0-保留 1-删除
    */
    @ApiModelProperty(value="是否删除：0-保留 1-删除")
    private Integer delFlag;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
}