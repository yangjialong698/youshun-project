package com.ennova.pubinfotask.dto;

import com.ennova.pubinfotask.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/16
 * @Description: com.ennova.pubinfotask.dto
 * @Version: 1.0
 */
@Data
public class FileDelDTO {

    private List<FileVO> fileVos;
}
