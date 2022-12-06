package com.ennova.pubinfostore.vo;

import com.ennova.pubinfostore.entity.ScProblemFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 问题反馈表
 */
@ApiModel(value = "问题反馈表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScProblemFeedbackDetailVO {



    private List<ScProblemFeedbackVO> scProblemFeedbackVOS;

}