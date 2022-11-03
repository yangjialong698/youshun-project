package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.vo.PrdZzBodyVO;
import com.ennova.pubinfoproduct.vo.ScrapVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ennova.pubinfoproduct.daos.ErpTransferOrderMapper;
import com.ennova.pubinfoproduct.entity.ErpTransferOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ErpTransferOrderService{

    @Resource
    private ErpTransferOrderMapper erpTransferOrderMapper;



    public Callback<List<ScrapVO>> erpinputscrap(String moveOutNo) {
        List<String> gxList = null ;
        if (moveOutNo.contains(",")){
            gxList = Arrays.asList(moveOutNo.split(","));
        }else {
            gxList = Arrays.asList(moveOutNo);
        }
        List<ErpTransferOrder> erpTransferOrders = erpTransferOrderMapper.selectAllByMoveOutNo(gxList);
        ArrayList<ScrapVO> scrapVOArrayList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(erpTransferOrders)){
            Map<String, List<ErpTransferOrder>> listMap = erpTransferOrders.stream().collect(Collectors.groupingBy(ErpTransferOrder::getMoveOutNo));
            listMap.entrySet().stream().map(key->{
                ScrapVO scrapVO = new ScrapVO();
                List<ErpTransferOrder> value = key.getValue();
                int rkCount = value.stream().mapToInt(ErpTransferOrder::getTotalNum).sum(); // 入库数量单品汇总
                int bfCount = value.stream().mapToInt(ErpTransferOrder::getScrapNum).sum(); // 报废数量单品汇总
                BigDecimal decimal = new BigDecimal(rkCount).divide(new BigDecimal(bfCount),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                Integer scrapRate = decimal.intValue();
                scrapVO.setTotalNum(rkCount);
                scrapVO.setOrderDate(value.get(0).getOrderDate());
                scrapVO.setScrapRate(scrapRate);
                scrapVOArrayList.add(scrapVO);
                return scrapVOArrayList;
            }).collect(Collectors.toList());

        }
        return Callback.success(scrapVOArrayList);

    }
}
