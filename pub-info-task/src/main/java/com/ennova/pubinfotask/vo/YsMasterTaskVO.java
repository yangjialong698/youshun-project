package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/20
 * @Description: 主任务
 * @Version: 1.0
 */

/**
 * 主任务表
 */
@ApiModel(value = "主任务表 - 上传")
@Data
@Builder
public class YsMasterTaskVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称")
    @NotBlank(message = "主任务名称(name): 不能为空!")
    @Length(max = 50,message = "主任务名称(name): 请输入50个以内的中文字符")
    private String name;

    /**
     * 任务类型： 0- 实验类  1- 研发类
     */
    @ApiModelProperty(value = "任务类型： 0- 实验类  1- 研发类", example = "1")
    @NotNull(message = "任务类型(type): 不能为空!")
    private Integer type;

    @ApiModelProperty(value = "紧急程度：0-  一般、1- 重要、2- 紧急")
    @NotNull(message = "紧急程度(pressingLevel): 不能为空!")
    private Integer pressingLevel;

    /**
     * 任务状态： 0- 未发布 1- 待认领  2- 未开始  3- 进行中 4- 已完成 5- 已关闭
     */
    @ApiModelProperty(value = "任务状态： 0- 未发布 1- 待认领  2- 未开始  3- 进行中 4- 已完成 5- 已关闭", example = "1")
    private Integer status;

    /**
     * 任务简介
     */
    @ApiModelProperty(value = "任务简介")
    @NotBlank(message = "任务简介(summary): 不能为空!")
    @Length(max = 250,message = "任务简介(summary): 请输入250个以内的中文字符")
    private String summary;

    /**
     * 任务成本  需求更改：非必填
     */
    @ApiModelProperty(value = "任务成本")
    //@NotNull(message = "任务成本(cost): 不能为空")
    //@Digits(integer = 9,fraction = 2,message = "任务成本(cost): 请输入9位以内的整数,可以有2位小数")
    private String cost;

    /**
     * 考核标准
     */
    @ApiModelProperty(value = "考核标准")
    @NotBlank(message = "考核标准(checkStandard): 不能为空")
    @Length(max = 250,message = "考核标准(checkStandard):请输入250个以内的中文字符")
    private String checkStandard;

    /**
     * 预计工时
     */
    @ApiModelProperty(value = "预计工时", example = "1")
    //@NotNull(message = "预计工时(estimateHour): 不能为空!")
    private Integer estimateHour;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @NotNull(message = "结束日期(taskEndDate): 不能为空!")
   // @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate taskEndDate;

    @ApiModelProperty(value = "fileMasterId文件ID", example = "1")
    private Integer fileMasterId;

    ///**
    // * 是否对外开放 0- 不对外开放  1- 对外开放
    // */
    ////@ApiModelProperty(value = "是否对外开放 0- 不对外开放  1- 对外开放", example = "1")
    ////private Integer openFile;

    /**
     * 文档类型ID
     */
    @ApiModelProperty(value = "文档类型ID", example = "1")
    //@NotNull(message = "文档类型(ysFileTypeId): 不能为空!")
    private Integer ysFileTypeId;

    /**
     * 文档类型ID
     */
    @ApiModelProperty(value = "前缀", example = "1")
    //@NotEmpty(message = "前缀(filePrefix): 不能为空!")
    private String filePrefix;


    /**
     * 主要内容
     */
    @ApiModelProperty(value = "主要内容", example = "1")
    private String fileContent;

    @ApiModelProperty(value = "版本号")
    public String versionNo;


    /**
     * 上传的附件
     */
    @ApiModelProperty(value = "上传的附件")
    private List<FileVO> fileVOList;

}
