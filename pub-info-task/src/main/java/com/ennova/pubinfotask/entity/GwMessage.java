package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 官网在线留言表
    */
@ApiModel(value="官网在线留言表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GwMessage {
    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 姓名
    */
    @ApiModelProperty(value="姓名")
    private String name;

    /**
    * 邮箱
    */
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
    * 电话
    */
    @ApiModelProperty(value="电话")
    private String phone;

    /**
    * 公司名称
    */
    @ApiModelProperty(value="公司名称")
    private String companyName;

    /**
    * 备注
    */
    @ApiModelProperty(value="备注")
    private String remark;
}