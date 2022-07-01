package com.ennova.pubinfofile.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-06-20
 */

@ApiModel(value = "工作日报反馈VO")
@Data
public class SugCommentVO {

    @ApiModelProperty(value = "经验建议ID")
    private Integer id;

    @ApiModelProperty(value = "评论内容", example = "1")
    private String sugContent;

    @ApiModelProperty(value = "评论状态")
    private Integer sugStatus;

    @ApiModelProperty(value = "评论用户ID")
    private String userId;


}
