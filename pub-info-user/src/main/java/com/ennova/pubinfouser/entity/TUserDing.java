package com.ennova.pubinfouser.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 公共信息平台用户表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TUserDing {
    /**
    * id
    */
    private Integer id;

    /**
    * 姓名
    */
    private String username;

    /**
    * 密码
    */
    private String password;

    /**
    * 微信号
    */
    private String wechatNo;

    /**
    * 手机号
    */
    private String mobile;

    /**
    * 工号
    */
    private String jobNum;

    /**
    * 单位
    */
    private String company;

    /**
    * 部门
    */
    private String department;

    /**
    * 职位
    */
    private String position;

    /**
    * 状态(0-已启用；1-已禁用)
    */
    private String status;

    /**
    * 是否删除（0-未删除；1-已删除）
    */
    private Integer isDelete;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 首页是否展示（0-不展示）
    */
    private Integer isShow;

    /**
    * 用户ID
    */
    private String userId;

    /**
    * 登录后默认密码是否修改（0-未修改；1-已修改）
    */
    private Integer isUpdate;
}