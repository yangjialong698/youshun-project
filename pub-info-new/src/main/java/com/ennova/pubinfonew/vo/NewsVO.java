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

    @ApiModelProperty(value = "是否有上一期")
    private boolean upPeriodical;

    @ApiModelProperty(value = "是否有下一期")
    private boolean downPeriodical;

    @ApiModelProperty(value = "是否有上一版")
    private boolean upEdition;

    @ApiModelProperty(value = "是否有下一版")
    private boolean downEdition;

    @ApiModelProperty(value = "是否可以修改删除")
    private boolean changeStatus;
}
