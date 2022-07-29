package com.ennova.pubinfouser.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-20
 */

@ApiModel(value = "用户DTO")
@Data
public class UserDTO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String conPassWord;

    @ApiModelProperty(value = "部门")
    private Integer department;

    @Value("role_id")
    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "微信号")
    private String wechatNo;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "工号")
    private String jobNum;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "状态(0-启用；1-禁用)")
    private String status;

    @ApiModelProperty(value = "单位id")
    private Integer company;

    @ApiModelProperty(value = "登录后默认密码是否修改")
    private Integer isUpdate;

}
