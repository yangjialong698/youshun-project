package com.ennova.pubinfotask.dto;

import com.ennova.pubinfotask.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @className: PublishDTO
 * @Description: 发布公告
 * @author: shibingyang1990@gmail.com
 * @date: 2022/6/15 15:16:06
 */
@ApiModel(value = "发布公告")
@Data
public class PublishDTO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    @ApiModelProperty(value = "公告标题")
    private String title;
    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    @NotBlank(message = "公告内容不能为空")
    @Length(max = 500, message = "公告内容不能超过500个字")
    private String content;
    /**
     * 审核人ID
     */
    @NotNull(message = "审核人ID不能为空")
    @ApiModelProperty(value = "审核人ID")
    private Integer checkUserId;

    /**
     * 上传的附件
     */
    @ApiModelProperty(value = "上传的附件")
    private List<FileVO> fileVOList;

}