package com.ennova.pubinfouser.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-20
 */
@ApiModel(value = "部门表")
@Data
public class DeptEntity {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 使用状态
     */
    @ApiModelProperty(value = "使用状态")
    private String useStatus;

    /**
     * 是否可修改
     */
    @ApiModelProperty(value = "是否可修改")
    private Integer isOperate;

    /**
     * 删除状态
     */
    @ApiModelProperty(value = "删除状态")
    private Integer isDelete;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Integer company;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
