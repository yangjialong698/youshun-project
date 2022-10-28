package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 意见箱
 */
@ApiModel(value = "意见箱")
@Data
public class BaseOpinionBoxVO {

    /**
     * 内容：2000汉字，每个汉字3个字节
     */
    @ApiModelProperty(value = "内容：2000汉字，每个汉字3个字节")
    @Length(max = 6000, message = "内容不能超过2000汉字")
    private String content;

    /**
     * 意见箱附件ID
     */
    @ApiModelProperty(value = "意见箱附件ID")
    private String ysOpinionFileId;

    public BaseOpinionBoxVO() {
        super();
    }

    public BaseOpinionBoxVO(String content, String ysOpinionFileId) {
        super();
        this.content = content;
        this.ysOpinionFileId = ysOpinionFileId;
    }

}


