package com.ennova.pubinfostore.dto;

import com.ennova.pubinfostore.vo.FileVO;
import lombok.Data;

import java.util.List;


@Data
public class FileDelDTO {
    private List<FileVO> fileVos;
}
