package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yangjialong
 * @date 2023/3/17
 * @version 1.0
 */

/**
 * 不良品明细表
 */
@ApiModel(description = "不良品明细表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaRejectsDetail {

    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 不良品处理单id
     */
    @ApiModelProperty(value = "不良品处理单id")
    private Integer rejectsId;

    /**
     * 班别
     */
    @ApiModelProperty(value = "班别")
    private String workClass;

    /**
     * 工单号
     */
    @ApiModelProperty(value = "工单号")
    private String workNumber;

    /**
     * 零件号
     */
    @ApiModelProperty(value = "零件号")
    private String prdNo;

    /**
     * 零件名称
     */
    @ApiModelProperty(value = "零件名称")
    private String prdName;

    /**
     * 工序
     */
    @ApiModelProperty(value = "工序")
    private String process;

    /**
     * 机台号
     */
    @ApiModelProperty(value = "机台号")
    private String machineNumber;

    /**
     * 不良品来源
     */
    @ApiModelProperty(value = "不良品来源")
    private String badSource;

    /**
     * 不良品项目
     */
    @ApiModelProperty(value = "不良品项目")
    private String badItem;

    /**
     * 不良品数目
     */
    @ApiModelProperty(value = "不良品数目")
    private Integer badNum;

    /**
     * 不良描述
     */
    @ApiModelProperty(value = "不良描述")
    private String badDescription;

    /**
     * 责任部门
     */
    @ApiModelProperty(value = "责任部门")
    private String dutyDepartment;

    /**
     * 责任人
     */
    @ApiModelProperty(value = "责任人")
    private String dutyPerson;

    /**
     * 检验员
     */
    @ApiModelProperty(value = "检验员")
    private String inspector;

    /**
     * 不良处置
     */
    @ApiModelProperty(value = "不良处置")
    private String badDisposal;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 是否删除（0-未删除 1-已删除）
     */
    @ApiModelProperty(value = "是否删除（0-未删除 1-已删除）")
    private Integer delFlag;
}