package com.ennova.pubinfostore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="装配人员")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssembleUserVO {

    @ApiModelProperty(value="装配人员ID")
    private Integer id;

    @ApiModelProperty(value="装配人员姓名")
    private String userName;

}
