package com.ennova.pubinfoproduct.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 供应商字典表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierInfo {
    private Integer id;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 供应商登入密码
    */
    private String password;

    /**
    * 供应商编号
    */
    private Integer supplierNo;

    /**
    * 供应商名称
    */
    private String supplier;

    /**
    * 是否删除（0-未删除；1-已删除）
    */
    private Integer isDelete;
}