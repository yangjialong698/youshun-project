package com.ennova.pubinfouser.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value = "用户部门统计")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptNumVO {

    /**
     * 总数量
     */
    @ApiModelProperty(value = "totalNum")
    private Integer totalNum;


    /**
     * 各部门数量
     */
    @ApiModelProperty(notes = "各部门数量")
    private List<PerDeptNumVO> deptNum;


}

