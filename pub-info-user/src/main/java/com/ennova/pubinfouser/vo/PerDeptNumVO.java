package com.ennova.pubinfouser.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-21
 */

@ApiModel(value = "部门统计")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerDeptNumVO {

    private String deptName;

    private Integer deptCou;
}
