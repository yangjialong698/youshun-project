package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author  yangjialong
 * @date  2023/5/5
 * @version 1.0
 */

@ApiModel(description="会签人和邮箱")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaOpinionUserMailVO {
    /**
    * ID
    */
    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 用户id
    */
    @ApiModelProperty(value="用户id")
    private Integer userId;

    /**
    * 姓名
    */
    @ApiModelProperty(value="姓名")
    private String name;

    /**
    * 委托人id
    */
    @ApiModelProperty(value="委托人id")
    private Integer mandatorId;

    /**
    * 委托人
    */
    @ApiModelProperty(value="委托人")
    private String mandator;

    /**
    * '最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )'
    */
    @ApiModelProperty(value="'最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )'")
    private Integer setpStaus;

    /**
    * 邮箱
    */
    @ApiModelProperty(value="邮箱")
    private String mail;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
}