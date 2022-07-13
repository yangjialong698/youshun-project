package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
 */

@ApiModel(value = "采购信息表 - 发布")
@Data
@Builder
public class CgPurchaseInfoVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @NotBlank(message = "物料名称(name): 不能为空!")
    private String name;

    /**
     * 采购人
     */
    @ApiModelProperty(value = "请购人")
    @NotBlank(message = "请购人(applyName): 不能为空!")
    private String applyName;

    /**
     * 任务编号
     */
    @ApiModelProperty(value = "任务编号")
    @NotBlank(message = "任务编号(estimateHour): 不能为空!")
    private String taskNumber;

    /**
     * 采购要求
     */
    @ApiModelProperty(value = "采购要求")
    @Length(max = 2000,message = "采购要求(checkStandard):请输入2000个以内的中文字符")
    private String purchaseRequirements;

    /**
     * 上传的附件
     */
    @ApiModelProperty(value = "上传的附件")
    private List<FileVO> fileVOList;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime createTime;
}
