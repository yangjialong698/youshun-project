package com.ennova.pubinfodaily.vo;


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
public class FeedBackVO {

    @ApiModelProperty(value = "日报ID")
    private Integer id;

    @ApiModelProperty(value = "反馈内容", example = "1")
    private String feedContent;

    @ApiModelProperty(value = "反馈状态")
    private Integer feedStatus;

    @ApiModelProperty(value = "反馈用户ID")
    private String userId;


}
