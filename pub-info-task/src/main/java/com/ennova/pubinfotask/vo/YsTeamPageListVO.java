package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/11
 * @Description: com.ennova.pubinfotask.vo
 * @Version: 1.0
 */
@ApiModel(value = "团队管理")
@Data
@Builder
public class YsTeamPageListVO {


    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称")
    private String masterTaskName;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String jobTitle;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String phone;

    /**
     * 成本：  元/每小时
     */
    @ApiModelProperty(value = "成本：  元/每小时")
    private Double cost;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @ApiModelProperty(value = "任务组：1 非任务组：0")
    private Integer taskFlag;


}
