package com.ennova.pubinfofile.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 日报表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsDayRep {
    /**
    * ID
    */
    private Integer id;

    /**
    * 日报名称
    */
    private String fileName;

    /**
    * 是否对外开放 0- 不对外开放  1- 对外开放
    */
    private Integer openFile;

    /**
    * 是否删除： 0- 保留  1- 删除
    */
    private Integer delFlag;

    /**
    * 主任务ID
    */
    private Integer ysMasterTaskId;

    /**
    * 日报内容
    */
    private String fileContent;

    /**
    * 用户ID
    */
    private Integer userId;

    /**
    * 日报日期
    */
    private Date dayrepTime;

    /**
    * 创建日期
    */
    private Date createTime;

    /**
    * 更新日期
    */
    private Date updateTime;
}