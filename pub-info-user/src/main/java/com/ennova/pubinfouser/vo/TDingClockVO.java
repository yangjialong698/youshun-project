package com.ennova.pubinfouser.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class TDingClockVO {
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
     * 用户打卡时间
     */
    @ApiModelProperty(value = "用户打卡时间")
    private Date userCheckTime;

    /**
     * 公司排班打卡时间
     */
    @ApiModelProperty(value = "公司排班打卡时间")
    private Date baseCheckTime;

    /**
     * 工作日期
     */
    @ApiModelProperty(value = "工作日期")
    private Date workDate;

    /**
     * 上下班状态
     */
    @ApiModelProperty(value = "上下班状态")
    private String checkType;

    /**
     * 加班状态（Normal：正常 Early：早退 Late：迟到 SeriousLate：严重迟到 Absenteeism：旷工迟到 NotSigned：未打卡）
     */
    @ApiModelProperty(value = "加班状态（Normal：正常 Early：早退 Late：迟到 SeriousLate：严重迟到 Absenteeism：旷工迟到 NotSigned：未打卡）")
    private String timeResult;

    /**
     * 打卡范围Normal:范围内 Outside:范围外
     */
    @ApiModelProperty(value = "打卡范围Normal:范围内 Outside:范围外")
    private String locationResult;

    /**
     * 上班时长
     */
    @ApiModelProperty(value = "上班时长")
    private String workTime;
}
