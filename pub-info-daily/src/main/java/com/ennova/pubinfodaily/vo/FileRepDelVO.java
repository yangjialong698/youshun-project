package com.ennova.pubinfodaily.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-06-21
 */
@ApiModel(value = "附件删除")
@Data
public class FileRepDelVO {

    private List<FileVO> fileVOList;

    private Integer dailyRepId;
}
