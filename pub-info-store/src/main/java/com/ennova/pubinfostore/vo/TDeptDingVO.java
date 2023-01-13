package com.ennova.pubinfostore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
    * 公共信息平台
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TDeptDingVO {
    /**
    * id
    */
    @ApiModelProperty(value="id")
    private Integer id;

    /**
    * 部门名称
    */
    @ApiModelProperty(value="部门名称")
    private String deptName;

    /**
    * 上级部门ID
    */
    @ApiModelProperty(value="上级部门ID")
    private Long parentId;

    /**
    * 部门ID
    */
    @ApiModelProperty(value="部门ID")
    private Long deptId;

    /**
    * 主管用户ID
    */
    @ApiModelProperty(value="主管用户ID")
    private String manageId;

    /**
     * 子部门
     */
    private List<TDeptDingVO> children;
}