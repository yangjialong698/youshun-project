package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/20
 * @Description: com.ennova.pubinfotask.vo
 * @Version: 1.0
 */
@ApiModel(value="团队管理")
@Data
@Builder
public class EditYsTeamVO {

    @ApiModelProperty(value="ID", example = "1")
    private Integer id;

    @ApiModelProperty(value="执行人ID", example = "1")
    private Integer executorId;

    @ApiModelProperty(value="用户名")
    private String name;

    @ApiModelProperty(value="部门ID")
    private Integer deptId;

    @ApiModelProperty(value="部门")
    private String deptName;

    @ApiModelProperty(value="职位")
    private String jobTitle;

    @ApiModelProperty(value="电话")
    private String phone;

    @ApiModelProperty(value="成本：元/每小时", example = "1")
    private Double cost;

    @ApiModelProperty(value="主任务ID", example = "1")
    private Integer ysMasterTaskId;

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;

}
