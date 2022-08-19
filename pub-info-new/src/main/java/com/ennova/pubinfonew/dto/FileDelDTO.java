package com.ennova.pubinfonew.dto;

import com.ennova.pubinfonew.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
 */
@Data
public class FileDelDTO {
    private List<FileVO> fileVos;
}
