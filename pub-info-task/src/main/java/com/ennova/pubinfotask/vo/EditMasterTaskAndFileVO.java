package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/25
 * @Description: 主任务修改时，查询一条数据
 * @Version: 1.0
 */
@Data
public class EditMasterTaskAndFileVO {

 /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号", example = "1")
    private Integer serialNumber;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称")
    private String name;

    /**
     * 任务类型： 0- 实验类  1- 研发类
     */
    @ApiModelProperty(value = "任务类型： 0- 实验类  1- 研发类", example = "1")
    private Integer type;

    /**
     * 任务状态： 0- 未发布 1- 已发布，待认领  2- 已认领，未开始  3- 进行中 4- 已完成 5- 已关闭
     */
    @ApiModelProperty(value = "任务状态： 0- 未发布 1- 已发布，待认领  2- 已认领，未开始  3- 进行中 4- 已完成 5- 已关闭")
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
    private Integer estimateHour;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate taskEndDate;


      /**
     * 预计工时
     */
    @ApiModelProperty(value = "文件主表 - 类型ID", example = "1")
    private Integer ysFileTypeId;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "文件主表ID")
    private Integer fileMasterId;




    @ApiModelProperty(value = "附件信息")
    private List<FileVO> fileVOList;

}



