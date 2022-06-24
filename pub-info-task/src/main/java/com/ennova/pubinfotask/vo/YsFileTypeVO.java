package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/22
 * @Description: 文件类型
 * @Version: 1.0
 */
/**
    * 文件类型表
    */
@ApiModel(value="文件类型表")
@Data
@Builder
public class YsFileTypeVO {
    /**
    * ID
    */
    @ApiModelProperty(value="ID", example = "1")
    private Integer id;

    /**
    * 类型名称
    */
    @ApiModelProperty(value="类型名称")
    @NotBlank(message = "类型名称(name): 不能为空")
    @Length(min = 2, message = "类型名称(name): 请输入2个以上中文汉字")
    @Length(max = 20, message = "类型名称(name): 您输入的文字超过长度")
    private String name;

    /**
     * 前缀
     */
    @NotBlank(message = "前缀(filePrefix):前缀不能为空!")
    @ApiModelProperty(value = "前缀")
    private String filePrefix;

    /**
    * 使用状态： 0- 已启用  1- 已禁用
    */
    @NotNull(message = "使用状态(status): 不能为空!")
    @ApiModelProperty(value="使用状态： 0- 已启用  1- 已禁用", example = "1")
    private Integer status;

}