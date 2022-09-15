package com.ennova.pubinfonew.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "新闻评论表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsCommentDto {

    /**
     * 期刊ID
     */
    @ApiModelProperty(value = "期刊ID")
    @NotNull(message = "期刊ID不能为空!")
    private Integer newsId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容(commentContent): 不能为空!")
    private String commentContent;

}