package com.ennova.pubinfopurchase.vo;

import com.ennova.pubinfocommon.vo.PageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardVO<T, K> {
        public List<T> list;
        public List<K> cardList;
        public PageUtil pageUtil;
}
