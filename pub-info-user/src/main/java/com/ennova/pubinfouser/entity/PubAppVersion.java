package com.ennova.pubinfouser.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description="pub_app_version")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PubAppVersion {
    @ApiModelProperty(value="")
    private Integer id;

    /**
    * 版本号
    */
    @ApiModelProperty(value="版本号")
    private String appVersion;

    /**
    * 1-安卓，2-ios
    */
    @ApiModelProperty(value="1-安卓，2-ios")
    private Integer versionType;

    @ApiModelProperty(value="")
    private Date createTime;
}