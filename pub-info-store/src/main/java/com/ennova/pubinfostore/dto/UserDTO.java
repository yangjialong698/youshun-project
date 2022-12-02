package com.ennova.pubinfostore.dto;

import lombok.Data;

@Data
public class UserDTO {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 部门
     */
    private String department;

    /**
     * cid
     */
    private String cid;
}