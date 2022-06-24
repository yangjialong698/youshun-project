package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/25
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */
/**
    * 子任务表
    */
@ApiModel(value="子任务表")
@Data
@Builder
public class YsSonTaskVO {
    /**
    * ID
    */
    @NotNull(message = "子任务ID不能为空", groups = {Update.class})
    @ApiModelProperty(value="ID", example = "1")
    private Integer id;

    /**
    * 任务名称
    */
    @NotEmpty(message="任务名称(name): 不能为空!", groups = {Save.class, Update.class})
    @ApiModelProperty(value="任务名称")
    private String name;

    /**
    * 主任务ID
    */
    @NotNull(message="主任务ID(ysMasterTaskId): 不能为空!", groups = {Save.class, Update.class})
    @ApiModelProperty(value="主任务ID", example = "1")
    private Integer ysMasterTaskId;

    /**
    * 紧急程度：0-  一般、1- 重要、2- 紧急
    */
    @NotNull(message="紧急程度(pressingLevel): 不能为空!", groups = {Save.class, Update.class})
    @ApiModelProperty(value="紧急程度：0-  一般、1- 重要、2- 紧急", example = "1")
    private Integer pressingLevel;

    /**
    * 开始日期
    */
    @NotNull(message = "开始日期(startTime): 不能为空!", groups = {Save.class, Update.class})
    @ApiModelProperty(value="开始日期")
    private LocalDateTime startTime;

    /**
    * 结束日期
    */
    @NotNull(message = "结束日期(endTime): 不能为空!", groups = {Save.class, Update.class})
    @ApiModelProperty(value="结束日期")
    private LocalDateTime endTime;

    /**
    * 预计工时
    */
    //@NotNull(message = "预计工时(estimateWorkTime): 不能为空", groups = {Save.class, Update.class})
    //@Positive(message = "必须为正整数",groups = {Save.class, Update.class})
    //@Digits(integer = 4,fraction = 0,message = "预计工时(estimateWorkTime): 请输入4位以内的整数", groups = {Save.class, Update.class})
    private Double estimateWorkTime;

    /**
    * 团队成员ID
    */
    @NotNull(message = "团队成员ID: 不能为空!", groups = {Save.class, Update.class})
    @ApiModelProperty(value="团队成员ID", example = "1")
    private Integer ysTeamId;

    /**
    * 单位成本：  元／每小时  需求修改：不需要填写
    */
    @ApiModelProperty(value="单位成本：元／每小时")
    //@Positive(message = "必须为正整数",groups = {Save.class, Update.class})
    //@NotNull(message = "单位成本(cost): 不能为空", groups = {Save.class, Update.class})
    //@Digits(integer = 4,fraction = 1,message = "单位成本(cost): 请输入4位以内的整数,可以有1位小数", groups = {Save.class, Update.class})
    //private BigDecimal cost;
    private String cost;



    @ApiModelProperty(value = "masterFileId文件ID", example = "1")
    private Integer masterFileId;
   /**
     * 文档名称
     */
    //@ApiModelProperty(value = "主任务名称")
    //@NotEmpty(message = "主任务名称(fileName): 不能为空!")
    //private String fileName;

    ///**
    // * 是否对外开放 0- 不对外开放  1- 对外开放
    // */
    ////@ApiModelProperty(value = "是否对外开放 0- 不对外开放  1- 对外开放", example = "1")
    ////private Integer openFile;

    /**
     * 文档类型ID
     */
    @ApiModelProperty(value = "文档类型ID", example = "1")
   // @NotNull(message = "文档类型(ysFileTypeId): 不能为空!")
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

    public interface Update{}
    public interface Save{}

}

