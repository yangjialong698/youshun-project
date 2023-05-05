package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author  yangjialong
 * @date  2023/5/5
 * @version 1.0
 */

@ApiModel(description = "操作日志")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaOperationLogVO {

    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 不良品处理单id
     */
    @ApiModelProperty(value = "不良品处理单id")
    private Integer rejectsId;

    /**
     * 登录人id
     */
    @ApiModelProperty(value = "登录人id")
    private Integer userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * '最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )'
     */
    @ApiModelProperty(value = "'最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )'")
    private Integer setpStaus;

    /**
     * 会签内容
     */
    @ApiModelProperty(value = "会签内容")
    private String opinionContent;

    /**
     * 回退理由
     */
    @ApiModelProperty(value = "回退理由")
    private String backReason;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述")
    private String description;
}