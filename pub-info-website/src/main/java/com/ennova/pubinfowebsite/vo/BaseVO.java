package com.ennova.pubinfowebsite.vo;

import com.ennova.pubinfocommon.vo.PageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseVO<T> {
    public List<T> list;
    public PageUtil pageUtil;
}