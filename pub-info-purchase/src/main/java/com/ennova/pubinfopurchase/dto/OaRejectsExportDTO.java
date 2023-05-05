package com.ennova.pubinfopurchase.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/4/4
 */
@Data
public class OaRejectsExportDTO {

    @ExcelProperty("id") //核心注解，value属性可用来设置表头名称
    private Integer id;

    /**
     * 不良品处理单id
     */
    @ExcelProperty(value = "不良品处理单id")
    private Integer rejectsId;

    /**
     * 班别
     */
    @ExcelProperty(value = "班别")
    private String workClass;

    /**
     * 工单号
     */
    @ExcelProperty(value = "工单号")
    private String workNumber;

    /**
     * 零件号
     */
    @ExcelProperty(value = "零件号")
    private String prdNo;

    /**
     * 零件名称
     */
    @ExcelProperty(value = "零件名称")
    private String prdName;

    /**
     * 工序
     */
    @ExcelProperty(value = "工序")
    private String process;

    /**
     * 机台号
     */
    @ExcelProperty(value = "机台号")
    private String machineNumber;

    /**
     * 不良品来源
     */
    @ExcelProperty(value = "不良品来源")
    private String badSource;

    /**
     * 不良品项目
     */
    @ExcelProperty(value = "不良品项目")
    private String badItem;

    /**
     * 不良品数目
     */
    @ExcelProperty(value = "不良品数目")
    private Integer badNum;

    /**
     * 不良描述
     */
    @ExcelProperty(value = "不良描述")
    private String badDescription;

    /**
     * 责任部门
     */
    @ExcelProperty(value = "责任部门")
    private String dutyDepartment;

    /**
     * 责任人
     */
    @ExcelProperty(value = "责任人")
    private String dutyPerson;

    /**
     * 检验员
     */
    @ExcelProperty(value = "检验员")
    private String inspector;

    /**
     * 不良处置
     */
    @ExcelProperty(value = "不良处置")
    private String badDisposal;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDateTime createTime;

    /**
     * 是否删除（0-未删除 1-已删除）
     */
    @ExcelProperty(value = "是否删除（0-未删除 1-已删除）")
    private Integer delFlag;


}
