package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskNumber {

     /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号", example = "1")
    private Integer serialNumber;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称")
    private String name;

}
