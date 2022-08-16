package com.ennova.pubinfodaily.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel(value = "非任务日报详情")
@Data
public class DailyRepDetailVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 日报名称
     */
    @ApiModelProperty(value = "日报名称")
    private String fileName;

    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人")
    private String username;

    /**
     * 日报日期
     */
    @ApiModelProperty(value = "日报日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dailyRepTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 附件ID
     */
    @ApiModelProperty(value = "附件ID")
    private Integer fileId;

    /**
     * 反馈状态
     */
    @ApiModelProperty(value = "反馈状态")
    private Integer feedStatus;
}
