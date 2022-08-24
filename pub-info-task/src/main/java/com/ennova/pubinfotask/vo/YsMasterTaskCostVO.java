package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;

@ApiModel(value = "成本")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsMasterTaskCostVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 成本录入日期
     */
    @ApiModelProperty(value = "成本录入日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date costDate;

    /**
     * 成本
     */
    @ApiModelProperty(value = "成本")
    @Digits(integer = 12, fraction = 2, message = "成本只能是两位小数")
    private Double cost;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


}