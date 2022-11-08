package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.vo.ScrapVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ennova.pubinfoproduct.daos.ErpTransferOrderMapper;
import com.ennova.pubinfoproduct.entity.ErpTransferOrder;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
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
            Map<String, List<ErpTransferOrder>> listMap = erpTransferOrders.stream().collect(Collectors.groupingBy(ErpTransferOrder::getOrderDate));
            listMap.entrySet().stream().map(key->{
                ScrapVO scrapVO = new ScrapVO();
                List<ErpTransferOrder> value = key.getValue();
                int rcCount = value.stream().mapToInt(o->Objects.isNull(o.getAcceptanceNum()) ? 0 :o.getAcceptanceNum()).sum();// 日产量单品汇总
                int rkCount = value.stream().mapToInt(o->Objects.isNull(o.getTotalNum()) ? 0 :o.getTotalNum()).sum(); // 入库数量单品汇总
                int bfCount = value.stream().mapToInt(o->Objects.isNull(o.getScrapNum()) ? 0 :o.getScrapNum()).sum(); // 报废数量单品汇总
                int badCount = value.stream().mapToInt(o->Objects.isNull(o.getBadNum()) ? 0 :o.getBadNum()).sum(); // 不良数量单品汇总
                NumberFormat num = NumberFormat.getInstance();
                num.setMaximumFractionDigits(2);
                String percent = num.format((float) (bfCount+badCount) / (float) rkCount * 100);
                scrapVO.setScrapNum(bfCount);
                scrapVO.setBadNum(badCount);
                scrapVO.setDayPrdNum(rcCount);
                scrapVO.setOrderDate(value.get(0).getOrderDate());
                if (percent.compareTo("15") >= 0){
                    percent = String.valueOf(15);
                }
                scrapVO.setBadScrapRate(percent);
                scrapVOArrayList.add(scrapVO);
                return scrapVOArrayList;
            }).collect(Collectors.toList());
        }
        List<ScrapVO> collect = scrapVOArrayList.stream().sorted(Comparator.comparing(ScrapVO::getOrderDate)).collect(Collectors.toList());
        return Callback.success(collect);
    }
}
