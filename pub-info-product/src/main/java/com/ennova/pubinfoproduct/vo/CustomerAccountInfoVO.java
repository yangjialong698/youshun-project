package com.ennova.pubinfoproduct.vo;

import com.ennova.pubinfoproduct.entity.CustomerAccountFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
    * 客述台账VO
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountInfoVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    /**
    * 月份
    */
    @ApiModelProperty(value = "月份")
    private Integer monthNum;

    /**
    * 投诉日期
    */
    @ApiModelProperty(value = "投诉日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date complainTime;

    /**
    * 客户
    */
    @ApiModelProperty(value = "客户")
    private String businessPerson;

    /**
    * 产品项目
    */
    @ApiModelProperty(value = "产品项目")
    private String prdItem;

    /**
    * 产品名称
    */
    @ApiModelProperty(value = "产品名称")
    private String prdName;

    /**
    * 反馈人id
    */
    @ApiModelProperty(value = "反馈人id")
    private String backPersonId;

    /**
    * 反馈人
    */
    @ApiModelProperty(value = "反馈人")
    private String backPersonName;

    /**
    * 不良类型
    */
    @ApiModelProperty(value = "不良类型")
    private String defectType;

    /**
    * 不合格名称
    */
    @ApiModelProperty(value = "不合格名称")
    private String nonName;

    /**
    * 投诉性质
    */
    @ApiModelProperty(value = "投诉性质")
    private String natureComplaint;

    /**
    * 严重性
    */
    @ApiModelProperty(value = "严重性")
    private String ponderance;

    /**
    * 问题具体描述
    */
    @ApiModelProperty(value = "问题具体描述")
    private String problemDis;

    /**
    * 责任方
    */
    @ApiModelProperty(value = "责任方")
    private String responsParty;

    /**
     * 供应商代号
     */
    @ApiModelProperty(value = "供应商代号")
    private String supplierNo;

    /**
    * 责任单位
    */
    @ApiModelProperty(value = "责任单位")
    private String responsUnit;

    /**
    * 责任人id
    */
    @ApiModelProperty(value = "责任人id")
    private String responsPersonId;

    /**
    * 责任人
    */
    @ApiModelProperty(value = "责任人")
    private String responsPerson;

    /**
    * 不良数
    */
    @ApiModelProperty(value = "不良数")
    private Integer badNum;

    /**
    * 内部原因分析
    */
    @ApiModelProperty(value = "内部原因分析")
    private String problemIn;

    /**
    * 临时永久措施
    */
    @ApiModelProperty(value = "临时永久措施")
    private String measures;

    /**
    * 负责人id
    */
    @ApiModelProperty(value = "负责人id")
    private String chargeNameId;

    /**
    * 负责人
    */
    @ApiModelProperty(value = "负责人")
    private String chargeName;

    /**
    * 计划完成日期
    */
    @ApiModelProperty(value = "计划完成日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date planFinTime;

    /**
    * 实际完成日期
    */
    @ApiModelProperty(value = "实际完成日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date actFinTime;

    /**
    * 效果确认
    */
    @ApiModelProperty(value = "效果确认")
    private String resultConfirm;

    /**
    * 关闭情况
    */
    @ApiModelProperty(value = "关闭情况")
    private String closeInfo;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 上传的附件
     */
    @ApiModelProperty(value = "上传的附件")
    private List<FileVO> fileVOList;

    /**
        * 上传的附件
     */
    @ApiModelProperty(value = "上传的附件：仅用于列表展示")
    private List<CustomerAccountFile> fileList;

}