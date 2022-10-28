package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 意见箱
 */
@ApiModel(value = "意见箱")
@Data
public class OpinionBoxVO extends BaseOpinionBoxVO {
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 匿名名称
     */
    @ApiModelProperty(value = "匿名名称")
    private String name;


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 意见箱附件
     */
    @ApiModelProperty(value = "上传的附件：仅用于列表展示")
    private List<OpinionBoxFileVO> fileVOList;

    // 无参构造方法
    public OpinionBoxVO() {
        super();
    }

    public OpinionBoxVO(Integer id, String name, List<OpinionBoxFileVO> fileVOList) {
        super();
        this.id = id;
        this.name = name;
        this.fileVOList = fileVOList;
    }

}


