package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "个人日报提交统计")
@Data
public class TdailySubmitCountVO {

    @ApiModelProperty(value = "提交人ID")
    private Integer userId;
    @ApiModelProperty(value = "提交人姓名")
    private String userName;
    @ApiModelProperty(value = "主任务ID")
    private Integer masterTaskId;
    @ApiModelProperty(value = "主任务名称")
    private String name;
    @ApiModelProperty(value = "及时提交次数")
    private Integer timely;
    @ApiModelProperty(value = "非提交次数")
    private Integer notTimely;
}
