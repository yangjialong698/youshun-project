package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/20
 * @Description:  主任务新建 - 文件上传VO
 * @Version: 1.0
 */
@Data
@Builder
public class FileVO {

    @ApiModelProperty(value = "ID", example = "1")
   // @NotNull(message = "filevo: id不能为空!", groups = {Save.class, Update.class})
    private Integer id;

    @ApiModelProperty(value = "展示文件名")
    private String fileName;

    @ApiModelProperty(value = "真实文件名")
   // @NotBlank(message = "真实文件名不能为空!", groups = {Save.class, Update.class})
    private String newfileName;

    //public interface Save {}
    //
    //public interface Update {}

}
