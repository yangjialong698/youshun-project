package com.ennova.pubinfonew.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
     * 报刊内容
     */
    @ApiModelProperty(value = "报刊内容")
    @NotBlank(message = "报刊内容(newsContent): 不能为空!")
    private String newsContent;

    /**
     * 报刊内容起点x坐标
     */
    @ApiModelProperty(value = "报刊内容起点x坐标")
    @NotNull(message = "报刊内容起点x坐标不能为空")
    private Integer newsX;

    /**
     * 报刊内容起点y坐标
     */
    @ApiModelProperty(value = "报刊内容起点y坐标")
    @NotNull(message = "报刊内容起点y坐标不能为空")
    private Integer newsY;

    /**
     * 报刊内容对应宽度
     */
    @ApiModelProperty(value = "报刊内容对应宽度")
    @Digits(integer = 6, fraction = 2, message = "宽度只能是两位小数")
    private Double newsWidth;

    /**
     * 报刊内容对应高度
     */
    @ApiModelProperty(value = "报刊内容对应高度")
    @Digits(integer = 6, fraction = 2, message = "高度只能是两位小数")
    private Double newsHeight;

    /**
     * 上传日期
     */
    @ApiModelProperty(value = "上传日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "是否有上一期")
    private boolean upPeriodical;

    @ApiModelProperty(value = "是否有下一期")
    private boolean downPeriodical;

    @ApiModelProperty(value = "是否有上一版")
    private boolean upEdition;

    @ApiModelProperty(value = "是否有下一版")
    private boolean downEdition;
}
