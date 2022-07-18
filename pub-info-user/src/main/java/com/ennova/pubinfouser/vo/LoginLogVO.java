package com.ennova.pubinfouser.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("登录日志列表")
@Data
public class LoginLogVO {

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("记录数")
    private Integer count;

    @ApiModelProperty("登录时间")
    private String loginDate;

}
