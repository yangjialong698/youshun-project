package com.ennova.pubinfouser.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "钉钉部门VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DingDeptVO {
    private Boolean autoAddUser;
    private Boolean createDeptGroup;
    private Long deptId;
    private String name;
    private Long parentId;
    private String manageId;
}
