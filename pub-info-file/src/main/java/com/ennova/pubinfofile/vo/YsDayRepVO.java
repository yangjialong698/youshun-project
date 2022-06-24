package com.ennova.pubinfofile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
    * 日报表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsDayRepVO {
    @ApiModelProperty(value = "封皮ID")
    private Integer id;

    @ApiModelProperty(value = "文档名称/名称")
    private String fileName;

    @ApiModelProperty(value = "主任务名称")
    private String masterTaskName;

    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    @ApiModelProperty(value = "主要内容", example = "1")
    private String fileContent;

//    @ApiModelProperty(value = "删除类型")
//    private Integer type;

    @ApiModelProperty(value = "日报日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayrepTime;

    @ApiModelProperty(value = "反馈内容", example = "1")
    private String feedContent;

    private List<FileVO> fileVOList;
}