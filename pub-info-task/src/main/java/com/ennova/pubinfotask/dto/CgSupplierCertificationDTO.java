package com.ennova.pubinfotask.dto;

import com.ennova.pubinfotask.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/21
 */
@ApiModel(value = "新增供应商")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CgSupplierCertificationDTO {

    /**
     *供应商认证id
     */
    @ApiModelProperty(value = "供应商认证id")
    private Integer id;

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
     *审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private Integer checkUserId;

    /**
     * 上传的附件
     */
    @ApiModelProperty(value = "上传的附件")
    private List<FileVO> fileVOList;
}
