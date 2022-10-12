package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value="官网采购信息 - 立即合作卡片")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupPurchaseCardVO{

    @ApiModelProperty(value="采购信息详情ID")
    private Integer id;

    @ApiModelProperty(value="物料名称")
    private String name;

    @ApiModelProperty(value="采购联系人组")
    private List<PurchaseCardVO> list;

}

@ApiModel(value="官网采购信息 - 立即合作卡片")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class PurchaseCardVO {

    @ApiModelProperty(value="采购人")
    private String username;

    @ApiModelProperty(value="采购人手机号")
    private String mobile;

}
