package com.ennova.pubinfostore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/1
 */
@Data
@Builder
public class CkPdaScanVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 条形码1
     */
    @ApiModelProperty(value = "条形码1")
    @NotBlank(message = "条形码1: 不能为空!")
    private String barCode1;

    /**
     * 条形码2
     */
    @ApiModelProperty(value = "条形码2")
    @NotBlank(message = "条形码2: 不能为空!")
    private String barCode2;

    /**
     * 校验条形码是否成功（0 - 失败；1 - 成功）
     */
    @ApiModelProperty(value = "校验条形码是否成功（0 - 失败；1 - 成功）")
    private Integer checkStatus;

    /**
     * 条形码2
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
