package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  yangjialong
 * @date  2023/4/7
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaSystemManage {

    @ApiModelProperty(value="")
    private Integer id;

    /**
    * OA系统管理员用户id
    */
    @ApiModelProperty(value="OA系统管理员用户id")
    private Integer userId;

    /**
    * 是否删除（0-未删除 1-已删除）
    */
    @ApiModelProperty(value="是否删除（0-未删除 1-已删除）")
    private Integer delFlag;
}