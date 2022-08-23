package com.ennova.pubinfotask.vo;

import com.ennova.pubinfocommon.vo.PageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostBaseVO<T> {
    public List<T> list;
    public PageUtil pageUtil;
    public Double cost;
}
