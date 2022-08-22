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
 * @date 2022/8/10
 */
@ApiModel(value = "新闻评论")
@Data
@Builder
public class NewsCommentVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 评论期刊标题id
     */
    @ApiModelProperty(value = "评论期刊标题id")
    private Integer newId;

    /**
     * 评论期刊标题
     */
    @ApiModelProperty(value = "评论期刊标题")
    private String editionTitle;

    /**
     * 评论人id
     */
    @ApiModelProperty(value = "评论人id")
    private Integer commentUserId;

    /**
     * 评论人
     */
    @ApiModelProperty(value = "评论人")
    private String commentUser;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容(commentContent): 不能为空!")
    private String commentContent;

    /**
     * 报刊标题对应html位置
     */
    @ApiModelProperty(value = "报刊标题对应html位置")
    private String divPosition;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 置顶id
     */
    @ApiModelProperty(value = "置顶id")
    private Integer sortId;
}
