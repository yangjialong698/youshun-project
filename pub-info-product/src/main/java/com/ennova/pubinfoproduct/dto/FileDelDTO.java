package com.ennova.pubinfoproduct.dto;


import com.ennova.pubinfoproduct.vo.FileVO;
import lombok.Data;

import java.util.List;


/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-12-27
 */
@Data
public class FileDelDTO {
    private List<FileVO> fileVos;
}
