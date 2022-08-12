package com.ennova.pubinfodaily.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsDailyRepVO {
    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "日报名称")
    private String fileName;

    @ApiModelProperty(value = "主要内容", example = "1")
    private String fileContent;

    @ApiModelProperty(value = "日报日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dailyRepTime;

    @ApiModelProperty(value = "反馈内容", example = "1")
    private String feedContent;

    private List<FileVO> fileVOList;
}
