package com.ennova.pubinfonew.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "新闻评论表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsCommentDto {

    /**
     * 报刊标题对应html位置
     */
    @ApiModelProperty(value = "报刊标题对应html位置")
    @NotBlank(message = "报刊标题对应html位置不能为空!")
    private String divPosition;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容(commentContent): 不能为空!")
    private String commentContent;

}