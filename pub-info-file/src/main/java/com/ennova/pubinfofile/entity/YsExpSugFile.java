package com.ennova.pubinfofile.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 经验建议文件表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsExpSugFile {
    /**
    * ID
    */
    private Integer id;

    /**
    * 经验建议ID
    */
    private Integer expSugId;

    /**
    * 原文件名
    */
    private String name;

    /**
    * MD5
    */
    private String fileMd5;

    /**
    * 文件大小
    */
    private String fileSize;

    /**
    * 文档访问路径
    */
    private String fileUrl;

    /**
    * 是否删除： 0- 保留  1- 删除
    */
    private Integer delFlag;

    /**
    * 是否对外开放 0- 不对外开放  1- 对外开放
    */
    private Integer openFile;

    /**
    * 创建日期
    */
    private Date createTime;

    /**
    * 更新日期
    */
    private Date updateTime;

    /**
    * 上传人ID
    */
    private Integer userId;
}