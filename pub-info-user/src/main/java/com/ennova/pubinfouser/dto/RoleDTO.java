package com.ennova.pubinfouser.dto;

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

@ApiModel(value = "角色DTO")
@Data
public class RoleDTO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 角色名
     */
    @ApiModelProperty(value = "角色名")
    private String roleName;

    /**
     * 角色代号
     */
    @ApiModelProperty(value = "角色代号")
    private String roleCode;

    /**
     * 使用状态
     */
    @ApiModelProperty(value = "使用状态")
    private String useStatus;

    /**
     * 是否可删除/修改（0-可以；1-不可以）
     */
    @ApiModelProperty(value = "是否可删除/修改（0-可以；1-不可以）")
    private Integer isOperate;

    /**
     * 删除（0-未删除；1-已删除）
     */
    @ApiModelProperty(value = "删除（0-未删除；1-已删除）")
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
