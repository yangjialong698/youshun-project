package com.ennova.pubinfocommon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseVO<T> {
    public List<T> list;
    public PageUtil pageUtil;
}
