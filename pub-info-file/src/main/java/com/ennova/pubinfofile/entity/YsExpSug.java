package com.ennova.pubinfofile.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 经验建议表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsExpSug {
    /**
    * ID
    */
    private Integer id;

    /**
    * 经验建议名称
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
    * 经验建议内容
    */
    private String fileContent;

    /**
    * 用户ID
    */
    private Integer userId;

    /**
    * 经验建议日期
    */
    private Date sugTime;

    /**
    * 创建日期
    */
    private Date createTime;

    /**
    * 更新日期
    */
    private Date updateTime;
}