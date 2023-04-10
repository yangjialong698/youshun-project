package com.ennova.pubinfopurchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/3/20
 * @version 1.0
 */

/**
    * 不良品处理单
    */
@ApiModel(description="不良品处理单")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaRejectsVO {

    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 标题
    */
    @ApiModelProperty(value="标题")
    private String headline;

    /**
    * 流水号
    */
    @ApiModelProperty(value="流水号")
    private String serialNumber;

    /**
    * 最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )
    */
    @ApiModelProperty(value="最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )")
    private Integer setpStaus;

    /**
    * 最新未办理人员
    */
    @ApiModelProperty(value="最新未办理人员")
    private String transactor;

    /**
    * 制单日期
    */
    @ApiModelProperty(value="制单日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime documentationDate;

    /**
    * 检验日期
    */
    @ApiModelProperty(value="检验日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime surveyDate;

    /**
    * 工作中心
    */
    @ApiModelProperty(value="工作中心")
    private String workCenter;

    /**
    * 创建人id
    */
    @ApiModelProperty(value="创建人id")
    private Integer userId;

    /**
    * 创建人
    */
    @ApiModelProperty(value="创建人")
    private String userName;

    /**
    * 处理明细
    */
    @ApiModelProperty(value="处理明细")
    private String processingDetails;

    /**
    * 质量部确认
    */
    @ApiModelProperty(value="质量部确认")
    private String qualityConfirm;

    /**
    * 紧急状态
    */
    @ApiModelProperty(value="紧急状态")
    private String exigencyStatus;

    /**
    * 进度
    */
    @ApiModelProperty(value="进度")
    private String schedule;

    /**
     * 不良品明细
     * */
    @ApiModelProperty(value="不良品明细")
    List<OaRejectsDetailVO> oaRejectsDetails;

    /**
     * 会签人明细
     * */
    @ApiModelProperty(value="会签人明细")
     List<OaRejectsOpinionVO> oaRejectsOpinionVOS;

    /**
     * 是否弹框选择会签人状态
     * */
    @ApiModelProperty(value="是否弹框选择会签人状态")
    private Integer openStatus;

    /**
     * 是否可以点击回退
     * */
    @ApiModelProperty(value="是否可以点击回退")
    private Integer backStatus;

    /**
     * 是否可以进行会签
     * */
    @ApiModelProperty(value="是否可以进行会签")
    private Integer opinionStatus;

    /**
     * 是否可以批量删除
     * */
    @ApiModelProperty(value="是否可以批量删除")
    private Integer batchDeletes;

    /**
     * 办理时间
     */
    @ApiModelProperty(value="办理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 是否删除（0-未删除 1-已删除）
     */
    @ApiModelProperty(value = "是否删除（0-未删除 1-已删除）")
    private Integer delFlag;
}