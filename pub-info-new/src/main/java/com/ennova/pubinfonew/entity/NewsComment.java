package com.ennova.pubinfonew.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "新闻评论表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsComment {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 评论期刊标题id
     */
    @ApiModelProperty(value = "评论期刊标题id")
    private Integer newsId;

    /**
     * 评论人id
     */
    @ApiModelProperty(value = "评论人id")
    private Integer commentUserId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 置顶id
     */
    @ApiModelProperty(value = "置顶id")
    private Integer sortId;

}