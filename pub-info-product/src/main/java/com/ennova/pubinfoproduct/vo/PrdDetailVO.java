package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "品号详情")
@Data
public class PrdDetailVO {

    @ApiModelProperty(value = "货品表头")
    private  PrdDetailHeadVO prdDetailHeadVO;

    @ApiModelProperty(value = "货品自制表身集合")
    private List<PrdZzBodyFinVO> prdZzBodyFinVOList;

    @ApiModelProperty(value = "货品采购表身集合")
    private List<PrdCgBodyVO> prdCgBodyVOList;
}
