package com.ennova.pubinfonew.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/9
 */
@ApiModel(value = "新闻期刊")
@Data
@Builder
public class NewsPeriodicalVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 报刊期数
     */
    @ApiModelProperty(value = "报刊期数（第几期）")
    private Integer periodicalNum;

    /**
     * 报刊版数
     */
    @ApiModelProperty(value = "报刊版数（第几版）")
    private Integer editionNum;

    /**
     * 报刊标题
     */
    @ApiModelProperty(value = "报刊标题")
    @NotBlank(message = "报刊标题(editionTitle): 不能为空!")
    private String editionTitle;

    /**
     * 报刊标题对应html位置
     */
    @ApiModelProperty(value = "报刊标题对应html位置")
    @NotBlank(message = "报刊标题对应html位置(divPosition): 不能为空!")
    private String divPosition;

    /**
     * 上传日期
     */
    @ApiModelProperty(value = "上传日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
