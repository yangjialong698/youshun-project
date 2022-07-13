package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 采购信息文件表
 */
@ApiModel(value = "联系信息表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CgContactInformation {

    private Integer id;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    /**
     * 公司地址
     */
    @ApiModelProperty(value = "公司地址")
    private String companyAddress;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String companyPhone;

    /**
     * E-mail
     */
    @ApiModelProperty(value = "E-mail")
    private String companyEmail;

    /**
     * QQ号码
     */
    @ApiModelProperty(value = "QQ号码")
    private Integer qqNumber;

}