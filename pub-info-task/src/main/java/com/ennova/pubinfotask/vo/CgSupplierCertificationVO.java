package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/21
 */
@ApiModel(value = "供应商认证")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CgSupplierCertificationVO {
    /**
     *供应商认证id
     */
    @ApiModelProperty(value = "供应商认证id", example = "1")
    private Integer id;

    /**
     *供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private Integer supplierNumber;

    /**
     *供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    @NotBlank(message = "供应商(supplierName): 不能为空!")
    private String supplierName;

    /**
     *地址
     */
    @ApiModelProperty(value = "地址")
    @NotBlank(message = "地址(address): 不能为空!")
    private String address;

    /**
     *联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @NotBlank(message = "联系方式(phone): 不能为空!")
    private String phone;

    /**
     *填报人ID
     */
    @ApiModelProperty(value = "填报人ID")
    private Integer createUserId;

    /**
     *填报人名字
     */
    @ApiModelProperty(value = "填报人名字")
    private String createName;

    /**
     *认证状态  * 0:待审核 1:审核通过(已发布) 2:审核不通过 - 驳回（待修改）
     */
    @ApiModelProperty(value = "认证状态  * 0:待审核 1:审核通过(已发布) 2:审核不通过 - 驳回（待修改）")
    private Integer status;

    /**
     *审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private Integer checkUserId;

    /**
     *审核人名字
     */
    @ApiModelProperty(value = "审核人名字")
    private String checkName;

    /**
     *审核日期
     */
    @ApiModelProperty(value = "审核日期")
    private LocalDateTime checkTime;

    /**
     *填报日期
     */
    @ApiModelProperty(value = "填报日期")
    private LocalDateTime createTime;

    /**
     *更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;

    /**
     * 上传的附件
     */
    @ApiModelProperty(value = "上传的附件")
    private List<FileVO> fileVOList;
}
