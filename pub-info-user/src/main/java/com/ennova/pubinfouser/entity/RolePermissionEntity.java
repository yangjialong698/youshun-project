package com.ennova.pubinfouser.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "角色权限", description = "角色权限")
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionEntity {
    @ApiModelProperty(example = "1", notes = "角色权限id")
    private Integer id;
    @ApiModelProperty(example = "1", notes = "角色id")
    private Integer roleId;
    @ApiModelProperty(example = "1", notes = "权限id")
    private Integer permissionId;
}
