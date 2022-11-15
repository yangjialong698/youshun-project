package com.ennova.pubinfouser.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共信息平台钉钉打卡时间表
 */
@ApiModel(value = "公共信息平台钉钉打卡时间表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TDingClock {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 用户上班打卡时间
     */
    @ApiModelProperty(value = "用户上班打卡时间")
    private Date userCheckOn;

    /**
     * 用户下班打卡时间
     */
    @ApiModelProperty(value = "用户下班打卡时间")
    private Date userCheckOff;

    /**
     * 公司排班上班打卡时间
     */
    @ApiModelProperty(value = "公司排班上班打卡时间")
    private Date baseCheckOn;

    /**
     * 公司排班下班打卡时间
     */
    @ApiModelProperty(value = "公司排班下班打卡时间")
    private Date baseCheckOff;

    /**
     * 工作日期年月日
     */
    @ApiModelProperty(value = "工作日期年月日")
    private Date workDate;

    /**
     * 上班时长
     */
    @ApiModelProperty(value = "上班时长")
    private String workTime;
}