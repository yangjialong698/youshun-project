package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/29
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */

/**
 * 任务计划
 */
@ApiModel(value = "任务计划")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsTaskPlanVO {
    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = {Update.class})
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 计划名称
     */
    @NotNull(message = "计划名称不能为空", groups = {Save.class, Update.class})
    @Length(min = 2, max = 40,message = "请输入2-40位的中文字符", groups = {Save.class, Update.class})
    @ApiModelProperty(value = "计划名称")
    private String name;

    /**
     * 主任务ID
     */
    @NotNull(message = "主任务ID不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 紧急程度：0-  一般、1- 重要、2- 紧急
     */
    @NotNull(message = "紧急程度不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty(value = "紧急程度：0-  一般、1- 重要、2- 紧急")
    private Integer pressingLevel;

    /**
     * 计划状态：0- 未开始、1- 进行中、2- 已完成、3- 已关闭
     */
    @NotNull(message = "计划状态不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty(value = "计划状态：0- 未开始、1- 已完成")
    private Integer planStatus;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startTime;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endTime;

    ///**
    // * 实际完成日期
    // */
    //private LocalDateTime actualTime;

    public interface Save {}

    public interface Update {}

}


