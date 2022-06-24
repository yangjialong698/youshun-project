package com.ennova.pubinfotask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/26
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */

/**
 * 文件类型表
 */
@ApiModel(value = "文件类型表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YsFileType {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String name;

    /**
     * 前缀
     */
    @ApiModelProperty(value = "前缀")
    private String filePrefix;

    /**
     * 是否删除： 0- 保留  1- 删除
     */
    @JsonIgnore
    @ApiModelProperty(value = "是否删除： 0- 保留  1- 删除", example = "1")
    private Integer delFlag;

    /**
     * 使用状态： 0- 已启用  1- 已禁用
     */
    @ApiModelProperty(value = "使用状态： 0- 已启用  1- 已禁用", example = "1")
    private Integer status;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @ApiModelProperty(value = "修改日期")
    private LocalDateTime updateTime;
}