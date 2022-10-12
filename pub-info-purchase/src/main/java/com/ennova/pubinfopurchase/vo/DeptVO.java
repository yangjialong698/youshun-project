package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 公共信息平台部门表
    */
@ApiModel(value="公共信息平台部门表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeptVO {

    /**
    * 部门名称
    */
    @ApiModelProperty(value="部门名称")
    private String deptName;


    /**
    * 部门ID
    */
    @ApiModelProperty(value="部门ID")
    private Long deptId;


}