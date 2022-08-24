package com.ennova.pubinfodaily.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 公共信息平台部门表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TDept {
    /**
    * id
    */
    private Integer id;

    /**
    * 部门名称
    */
    private String deptName;

    /**
    * 使用状态(0-已启用；1-已禁用)
    */
    private String useStatus;

    /**
    * 是否可修改（0-可以；1-不可以）
    */
    private Integer isOperate;

    /**
    * 删除（0-未删除；1-已删除）
    */
    private Integer isDelete;

    /**
    * 企业id
    */
    private Integer company;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 上级部门ID
    */
    private Long parentId;

    /**
    * 部门ID
    */
    private Long deptId;

    /**
    * 主管用户ID
    */
    private String manageId;

    /**
    * 审核人ID
    */
    private String checkId;
}