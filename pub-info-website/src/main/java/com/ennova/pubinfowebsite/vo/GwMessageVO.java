package com.ennova.pubinfowebsite.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/23
 */
@ApiModel(value = "官网在线留言-提交")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GwMessageVO {

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名: 不能为空!")
    private String name;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    @NotBlank(message = "电话: 不能为空!")
    private String phone;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @NotBlank(message = "留言: 不能为空!")
    private String remark;
}
