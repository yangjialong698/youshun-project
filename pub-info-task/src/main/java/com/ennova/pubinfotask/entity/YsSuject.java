package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 科目表
 */
@ApiModel(value = "科目表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsSuject {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID : 新增时无需传入，修改时必须传入")
    private Integer id;

    /**
     * 科目
     */
    @ApiModelProperty(value = "科目")
    private String suject;

    /**
     * 是否删除 ：0 - 保留  1 - 删除
     */
    @ApiModelProperty(value = "是否删除 ：0 - 保留  1 - 删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间，前端无需传入")
    private LocalDateTime createTime;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间，前端无需传入")
    private LocalDateTime updateTime;
}