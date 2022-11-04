package com.ennova.pubinfowebsite.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@ApiModel(value = "异常信息管理 - 新增")
@Data
@Builder
public class ErpExceptionVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 模块类型：1-摇臂机加 2-摇臂轴 3-后处理 4-装配
     */
    @ApiModelProperty(value = "模块类型：1-机加摇臂 2-摇臂轴 3-摇臂后处理 4-装配")
    private Integer muduleType;

    /**
     * 模块异常信息
     */
    @ApiModelProperty(value = "模块异常信息")
    @NotBlank(message = "模块异常信息: 不能为空!")
    private String moduleExceptionMessage;

    /**
     * 是否删除：0-保留 1-删除
     */
    @ApiModelProperty(value = "是否删除：0-保留 1-删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
