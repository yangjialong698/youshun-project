package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "VisitVO", description = "访问量")
@Data
public class VisitVO {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "访问量")
    private Integer count;

    @ApiModelProperty(value="按月访问日期")
    // 日期格式化为yyyy-MM
    private String loginDate;
}
