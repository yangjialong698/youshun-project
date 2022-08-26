package com.ennova.pubinfonew.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/11
 */
@Data
@Builder
public class NewsVO {

    @ApiModelProperty(value = "附件ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "期数", example = "1")
    private Integer periodicalNum;

    @ApiModelProperty(value = "版数", example = "1")
    private Integer editionNum;

    @ApiModelProperty(value = "创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "原文件名")
    private String fileName;

    @ApiModelProperty(value = "MD5")
    private String newfileName;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "文件url")
    private String fileUrl;
}
