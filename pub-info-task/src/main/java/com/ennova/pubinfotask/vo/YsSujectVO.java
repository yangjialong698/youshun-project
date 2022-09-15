package com.ennova.pubinfotask.vo;

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
public class YsSujectVO {
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

}