package com.ennova.pubinfouser.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@ApiModel(value = "钉钉用户VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DingUserVO {

    private String userid;

    private String unionid;

    private String name;

    private String avatar;

    private String stateCode;

    private String mobile;

    private String hideMobile;

    private String telephone;

    private String jobNumber;

    private String title;

    private String email;

    private String orgEmail;

    private String workPlace;

    private String remark;

    private List<Long> deptIdList;

    private Integer deptOrder;

    private String extension;

    private Integer hiredDate;

    private Boolean active;

    private Boolean admin;

    private Boolean boss;

    private Boolean leader;

    private Boolean exclusiveAccount;

}
