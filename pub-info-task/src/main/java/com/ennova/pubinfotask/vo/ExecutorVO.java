package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/20
 * @Description: com.ennova.pubinfotask.vo
 * @Version: 1.0
 */
@ApiModel(value="执行人")
@Data
@Builder
public class ExecutorVO {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "职务")
    private String jobTitle;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;


}
