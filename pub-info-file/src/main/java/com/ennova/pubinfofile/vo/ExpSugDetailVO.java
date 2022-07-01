package com.ennova.pubinfofile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-06-17
 */
@ApiModel(value = "日报详情")
@Data
public class ExpSugDetailVO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;


    /**
     * 日报名称
     */
    @ApiModelProperty(value = "文档名称")
    private String fileName;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称", example = "1")
    private String masterTaskName;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;


    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人")
    private String username;


    /**
     * 日报日期
     */
    @ApiModelProperty(value = "经验建议日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sugTime;

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
    @ApiModelProperty(value = "评论状态")
    private Integer sugStatus;

}
