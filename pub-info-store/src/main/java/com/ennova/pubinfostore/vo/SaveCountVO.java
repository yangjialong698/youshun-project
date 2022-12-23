package com.ennova.pubinfostore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "我的统计VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCountVO {
    @ApiModelProperty(value = "待解决")
    private Long toDoProblem;
    @ApiModelProperty(value = "解决中")
    private Long doingProblem;
    @ApiModelProperty(value = "已解决")
    private Long doneProblem;
    @ApiModelProperty(value = "未解决")
    private Long unDoneProblem;
}
