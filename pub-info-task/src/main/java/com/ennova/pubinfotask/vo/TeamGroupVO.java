package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @className: TeamGroupVO
 * @author: shibingyang1990@gmail.com
 * @date: 2022/6/9 15:19:06
 */
@ApiModel(value = "子任务占比")
@Data
@Builder
public class TeamGroupVO {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "子任务数量")
    private Integer countNum;

    @ApiModelProperty(value = "占比")
    private int proportion;


}