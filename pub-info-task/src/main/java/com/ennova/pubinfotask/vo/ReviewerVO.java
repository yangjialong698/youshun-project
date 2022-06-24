package com.ennova.pubinfotask.vo;

import lombok.Data;

/**
 * @className: ReviewerDTO
 * @Description: task_manage、check_person 任务管理员、审核人角色返回的用户信息
 * @author: shibingyang1990@gmail.com
 * @date: 2022/6/15 15:04:03
 */
@Data
public class ReviewerVO {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

}