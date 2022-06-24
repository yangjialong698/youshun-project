package com.ennova.pubinfofile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-28
 */
@ApiModel(value = "文档更新")
@Data
public class FileUpdateVO {

    /**
     * 文档今日更新数量
     */
    @ApiModelProperty(value = "文档今日更新数量")
    private Integer dayUpNum;

    /**
     * 文档往日更新数量
     */
    @ApiModelProperty(value = "文档往日更新数量")
    private Integer earUpNum;

    /**
     * 我的文档数量
     */
    @ApiModelProperty(value = "我的文档数量")
    private Integer myFileNum;

    /**
     * 我的建议文档数量
     */
    @ApiModelProperty(value = "我的建议文档数量")
    private Integer mySugNum;

    /**
     * 文档更新占比
     */
    @ApiModelProperty(value = "文档更新占比")
    private Integer updateRatio;

    /**
     * 我的文档占比
     */
    @ApiModelProperty(value = "我的文档占比")
    private Integer myFileRatio;

    /**
     * 我的建议文档占比
     */
    @ApiModelProperty(value = "我的建议文档占比")
    private Integer mySugRatio;

}
