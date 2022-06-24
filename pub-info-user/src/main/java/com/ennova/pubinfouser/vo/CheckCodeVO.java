package com.ennova.pubinfouser.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-23
 */
@Data
public class CheckCodeVO {
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String verificationCode;
}
