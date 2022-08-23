package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/10
 * @Description: com.ennova.pubinfotask.vo
 * @Version: 1.0
 */
@ApiModel(value = "子任务表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditSontTaskAndFileVO {

     /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号", example = "1")
    private String serialNumber;


    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String name;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 紧急程度：0-  一般、1- 重要、2- 紧急
     */
    @ApiModelProperty(value = "紧急程度：0-  一般、1- 重要、2- 紧急")
    private Integer pressingLevel;

    /**
     * 任务状态： 0- 未开始  1- 进行中 2- 已完成 3- 已关闭
     */
    @ApiModelProperty(value = "任务状态： 0- 未开始  1- 进行中 2- 已完成 3- 已关闭")
    private Integer status;

    /**
     * 开始日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startTime;

    /**
     * 结束日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endTime;

    /**
     * 预计工时
     */
    @ApiModelProperty(value = "预计工时")
    private Integer estimateWorkTime;

    /**
     * 团队成员ID
     */
    @ApiModelProperty(value = "团队成员ID")
    private Integer ysTeamId;

    /**
     * 单位成本：  元／每小时
     */
    @ApiModelProperty(value = "单位成本：元／每小时")
    private BigDecimal cost;


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
     * 团队成员ID
     */
    @ApiModelProperty(value = "文件主表ID")
    private Integer masterFileId;

    /**
     * 团队成员ID
     */
    @ApiModelProperty(value = "文件主表 - 文件类型ID")
    private Integer ysFileTypeId;


    @ApiModelProperty(value = "附件信息")
    private List<FileVO> fileVOList;
}

