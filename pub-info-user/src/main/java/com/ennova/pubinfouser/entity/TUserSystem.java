package com.ennova.pubinfouser.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TUserSystem {
    /**
    * 主键
    */
    private Integer id;

    /**
    * 系统代号
    */
    private String sysNum;

    /**
    * 用户编码
    */
    private Integer userId;

    /**
    * 系统名称
    */
    private String sysName;
}