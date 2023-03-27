package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author  yangjialong
 * @date  2023/3/20
 * @version 1.0
 */
/**
    * 不合格单会签意见表
    */
@ApiModel(description="不合格单会签意见表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaRejectsOpinion {

    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 不良品处理单id
    */
    @ApiModelProperty(value="不良品处理单id")
    private Integer rejectsId;

    /**
    * 会签意见人id
    */
    @ApiModelProperty(value="会签意见人id")
    private Integer opinionUserId;

    /**
    * 会签意见人
    */
    @ApiModelProperty(value="会签意见人")
    private String opinionUser;

    /**
    * 会签步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )
    */
    @ApiModelProperty(value="会签步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )")
    private Integer setpStaus;

    /**
    * 会签意见内容
    */
    @ApiModelProperty(value="会签意见内容")
    private String opinionContent;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
    * 会签意见发布时间
    */
    @ApiModelProperty(value="会签意见发布时间")
    private LocalDateTime publishTime;

    /**
    * 会签意见超时时间
    */
    @ApiModelProperty(value="会签意见超时时间")
    private LocalDateTime overtime;

    /**
    * 是否删除（0-未删除 1-已删除）
    */
    @ApiModelProperty(value="是否删除（0-未删除 1-已删除）")
    private Integer delFlag;
}