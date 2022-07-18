package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/27
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */
/**
    * 团队管理
    */
@ApiModel(value="团队管理")
@Data
@Builder
public class YsTeamVO {
    /**
    * ID
    */
    @ApiModelProperty(value="ID", example = "1")
    private Integer id;

    /**
    * 执行人ID
    */
    @NotNull(message = "执行人不能为空")
    @ApiModelProperty(value="执行人ID", example = "1")
    private Integer executorId;

    /**
    * 成本：  元/每小时  需求更改：非必填
    */
    //@NotNull(message = "成本(cost): 不能为空")
    //@Digits(integer = 9,fraction = 1,message = "成本(cost): 请输入9位以内的整数,可以有1位小数")
    @ApiModelProperty(value="成本：元/每小时", example = "1")
    //private Double cost;
    private String cost;

    /**
    * ysMasterTaskId
    */
   // @NotNull(message = "主任务ID(ysMasterTaskId): 不能为空")
    @ApiModelProperty(value="主任务ID", example = "1")
    private Integer ysMasterTaskId;


    @ApiModelProperty(value = "任务组：1 非任务组：0")
    private Integer taskFlag;


}