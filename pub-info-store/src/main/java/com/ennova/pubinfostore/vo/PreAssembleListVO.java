package com.ennova.pubinfostore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 预装人员表
    */
@ApiModel(value="预装人员表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreAssembleListVO {
    /**
    * 主键ID
    */
    @ApiModelProperty(value="主键ID")
    private Integer id;

    /**
    * 装配项目
    */
    @ApiModelProperty(value="装配项目")
    private String assembleItem;

    /**
    * 预装人员ID
    */
    @ApiModelProperty(value="预装人员ID")
    private String preAssembleId;

    /**
     * 预装人员
     */
    @ApiModelProperty(value="预装人员信息")
    private List<AssembleUserVO> userList;

    /**
    * 排产装配表ID
    */
    @ApiModelProperty(value="排产装配表ID")
    private Integer ysScheduleAssembleId;

    /**
    * 是否删除：（冗余字段，实际无使用）
    */
    @ApiModelProperty(value="是否删除：（冗余字段，实际无使用）")
    @JsonIgnore
    private Integer flag;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}