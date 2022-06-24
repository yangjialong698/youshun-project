package com.ennova.pubinfouser.vo;

import com.ennova.pubinfouser.entity.PermissionEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "权限VO", description = "权限VO")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO extends PermissionEntity implements Comparable<PermissionVO> {
    @ApiModelProperty(example = "true", notes = "是否拥有权限（true-有；false-没有）")
    private boolean isSelected;
    @ApiModelProperty(example = "[]", notes = "子权限列表")
    private List<PermissionVO> childPermissions = new ArrayList<>();

    @Override
    public int compareTo(PermissionVO p) {
        return this.sort-p.sort;
    }
}
