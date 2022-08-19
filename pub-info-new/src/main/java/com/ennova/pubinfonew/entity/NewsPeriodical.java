package com.ennova.pubinfonew.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/9
 */
@ApiModel(value = "新闻期刊表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsPeriodical {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 报刊期数
     */
    @ApiModelProperty(value = "报刊期数")
    private Integer periodicalNum;

    /**
     * 报刊版数
     */
    @ApiModelProperty(value = "报刊版数")
    private Integer editionNum;

    /**
     * 报刊标题
     */
    @ApiModelProperty(value = "报刊标题")
    private String editionTitle;

    /**
     * 报刊标题对应html位置
     */
    @ApiModelProperty(value = "报刊标题对应html位置")
    private String divPosition;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
