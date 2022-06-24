package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告表
 */
@ApiModel(value = "公告表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsBulletin {
    /**
     * 公告ID
     */
    @ApiModelProperty(value = "公告ID")
    private Integer id;

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    private String title;

    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    private String content;

    /**
     * 公告类型
     */
    @ApiModelProperty(value = "公告类型")
    private Integer type;

    /**
     * 公告状态
     * 0:待审核 1:审核通过 2:审核不通过 - 驳回
     */
    @ApiModelProperty(value = "公告状态,     * 0:待审核 1:审核通过 2:审核不通过 - 驳回")
    private Integer status;

    /**
     * 发布人ID
     */
    @ApiModelProperty(value = "发布人ID")
    private Integer createId;

    /**
     * 审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private Integer checkUserId;

    /**
     * 审核通过日期
     */
    @ApiModelProperty(value = "审核通过日期")
    private LocalDateTime checkTime;

    /**
     * 冗余删除（0-未删除；1-已删除）
     */
    @ApiModelProperty(value = "冗余删除（0-未删除；1-已删除）")
    private Integer isDelete;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;
}