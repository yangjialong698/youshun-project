package com.ennova.pubinfoproduct.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客述台账表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountInfo {
    private Integer id;

    /**
     * 月份
     */
    private Integer monthNum;

    /**
     * 投诉日期
     */
    private Date complainTime;

    /**
     * 客户
     */
    private String businessPerson;

    /**
     * 产品项目
     */
    private String prdItem;

    /**
     * 产品名称
     */
    private String prdName;

    /**
     * 反馈人id
     */
    private String backPersonId;

    /**
     * 反馈人
     */
    private String backPersonName;

    /**
     * 不良类型
     */
    private String defectType;

    /**
     * 不合格名称
     */
    private String nonName;

    /**
     * 投诉性质
     */
    private String natureComplaint;

    /**
     * 严重性（0为一般1为严重）
     */
    private String ponderance;

    /**
     * 问题具体描述
     */
    private String problemDis;

    /**
     * 责任方
     */
    private String responsParty;

    /**
     * 供应商编号
     */
    private String supplierNo;

    /**
     * 责任单位
     */
    private String responsUnit;

    /**
     * 责任人id
     */
    private String responsPersonId;

    /**
     * 责任人
     */
    private String responsPerson;

    /**
     * 不良数
     */
    private Integer badNum;

    /**
     * 内部原因分析
     */
    private String problemIn;

    /**
     * 临时永久措施
     */
    private String measures;

    /**
     * 负责人id
     */
    private String chargeNameId;

    /**
     * 负责人
     */
    private String chargeName;

    /**
     * 计划完成日期
     */
    private Date planFinTime;

    /**
     * 实际完成日期
     */
    private Date actFinTime;

    /**
     * 效果确认 （0-未确认 ，1-已确认）
     */
    private String resultConfirm;

    /**
     * 关闭情况（0-未关闭，1-已关闭）
     */
    private String closeInfo;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}