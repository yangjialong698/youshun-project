package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProduceDetailVO {
    @ApiModelProperty(value = "工序代号")
    private String  processNo;
    @ApiModelProperty(value = "工序名称")
    private String  processName;
//    @ApiModelProperty(value = "工单时间")
//    private Date workOrderTime;
    @ApiModelProperty(value = "工单单号")
    private String workOrderNo;
    @ApiModelProperty(value = " 新工单投入数量")
    private Integer  newOrderNum;
    @ApiModelProperty(value = " 正常完工数量")
    private Integer normalNum;
    @ApiModelProperty(value = " 待处理仓数量")
    private Integer waitNum;
    @ApiModelProperty(value = " 报废处理数量")
    private Integer scrappedNum;
    @ApiModelProperty(value = "数量")
    private Integer count;
//    @ApiModelProperty(value = " 期初在产数量")
//    private Integer begingNum;
//    @ApiModelProperty(value = " 返工工单新增数量")
//    private Integer backOrderNum;
//    @ApiModelProperty(value = " 返修完工数量")
//    private Integer backRepairNum;
//    @ApiModelProperty(value = " 期末在产数量")
//    private Integer endNum;
//    @ApiModelProperty(value = "备注")
//    private String remark;
}
