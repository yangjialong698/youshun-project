package com.ennova.pubinfotask.vo;

import com.ennova.pubinfotask.entity.YsBulletinFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @className: YsBulletinVO
 * @author: shibingyang1990@gmail.com
 * @date: 2022/6/17 15:01:36
 */
@ApiModel(value = "公告")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsBulletinVO {
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
     * 发布人
     */
    @ApiModelProperty(value = "发布人")
    private String createName;

    /**
     * 审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private Integer checkUserId;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String checkName;

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

    @ApiModelProperty(value = "是否可修改 true:可修改 false:不可修改")
    private Boolean isEdit;

    @ApiModelProperty(value = "是否可删除 true:可删除 false:不可删除")
    private Boolean isDeleteable;

    @ApiModelProperty(value = "是否可审核 true:可审核 false:不可审核")
    private Boolean isCheckable;

    @ApiModelProperty(value = "是否可驳回 true:可驳回 false:不可驳回")
    private Boolean isReject;

    // 文件
    @ApiModelProperty(value = "文件")
    private List<YsBulletinFile> files;
}