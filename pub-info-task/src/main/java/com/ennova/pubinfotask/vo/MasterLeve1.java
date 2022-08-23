package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/26
 * @Description:  主任务 - 首页
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MasterLeve1 {

     /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号", example = "1")
    private String serialNumber;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称")
    private String name;

    @ApiModelProperty(value="任务紧急程度：0-  一般、1- 重要、2- 紧急")
    private Integer pressingLevel;

    /**
     * 任务类型： 0- 实验类  1- 研发类
     */
    @ApiModelProperty(value = "任务类型： 0- 实验类  1- 研发类", example = "1")
    private Integer type;

    /**
     * 任务状态： 0- 未发布 1- 待认领  2- 未开始  3- 进行中 4- 已完成 5- 已关闭
     */
    @ApiModelProperty(value = "任务状态： 0- 未发布 1- 待认领  2- 未开始  3- 进行中 4- 已完成 5- 已关闭", example = "1")
    private Integer status;

    /**
     * 任务简介
     */
    @ApiModelProperty(value = "任务简介")
    private String summary;

    /**
     * 任务成本
     */
    @ApiModelProperty(value = "任务成本")
    private Long cost;

    /**
     * 考核标准
     */
    @ApiModelProperty(value = "考核标准")
    private String checkStandard;

    /**
     * 预计工时
     */
    @ApiModelProperty(value = "预计工时", example = "1")
    private String estimateHour;

    /**
     * 消耗
     */
    @ApiModelProperty(value = "消耗", example = "1")
    private String totalConsume;

    /**
     * 剩余
     */
    @ApiModelProperty(value = "剩余", example = "1")
    private String surplus;

    /**
     * 进度
     */
    @ApiModelProperty(value = "进度", example = "1")
    private Integer percentage;

    /**
     * 认领人ID
     */
    @ApiModelProperty(value = "认领人ID", example = "1")
    private Integer receiveId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "认领用户名")
    private String username;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate taskEndDate;

    /**
     * 任务发布待认领日期
     */
    @ApiModelProperty(value = "任务发布待认领日期")
    private LocalDateTime publishDate;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    /**
     * 按钮状态
     */
    @ApiModelProperty(value = "按钮状态（是否存在封皮文件）： 0- 包含  1-不含", example = "1")
    private Integer buttonStatus;


    /**
     * 是否存在日报
     */
    @ApiModelProperty(value = "是否存在日报： 0- 包含  1-不含", example = "1")
    private Integer rbStatus;

    /**
     * 是否存在经验建议
     */
    @ApiModelProperty(value = "是否存在经验建议： 0- 包含  1-不含", example = "1")
    private Integer experienceStatus;
}
