package com.ennova.pubinfotask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="ys_login_month")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsLoginMonth {
    /**
    * ID
    */
    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 用户ID
    */
    @ApiModelProperty(value="用户ID")
    private Integer userId;

    /**
    * 姓名
    */
    @ApiModelProperty(value="姓名")
    private String userName;

    /**
    * 访问次数
    */
    @ApiModelProperty(value="访问次数")
    private Integer count;

    /**
    * 按月访问日期
    */
    @ApiModelProperty(value="按月访问日期")
    // 日期格式化为yyyy-MM
    private String loginDate;

    /**
    * 创建日期
    */
    @ApiModelProperty(value="创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
    * 更新日期
    */
    @ApiModelProperty(value="更新日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}