package com.ennova.pubinfostore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="责任部门")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyDepartmentVO {

    @ApiModelProperty(value="责任部门id")
    private Long id;

    @ApiModelProperty(value="责任部门名称")
    private String department;
}
