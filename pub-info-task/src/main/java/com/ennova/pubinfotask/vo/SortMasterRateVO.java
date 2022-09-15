package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SortMasterRateVO {

    private Integer id;

    private String name;

    private String userName;

    private Integer rate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date taskEndDate;

    private Integer sortNum;

}
