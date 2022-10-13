package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.dao.PrdDetailMapper;
import com.ennova.pubinfoproduct.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrdDetailService {

    @Autowired
    private PrdDetailMapper prdDetailMapper;

    public Callback<PrdDetailVO> selectPrdDetail(String prdNo) {
        if (StringUtils.isEmpty(prdNo)){
            return Callback.error("请输入货品号!");
        }
        PrdDetailVO prdDetailVO = new PrdDetailVO();
        //1.根据货品号查询表头
        PrdDetailHeadVO prdDetailHeadVO = prdDetailMapper.selectHeadByPrdNo(prdNo);
        //2.根据货品号查询采购单表身集合
        List<PrdCgBodyVO> prdCgBodyVOList = prdDetailMapper.selectCgBodyByPrdNo(prdNo);
        if (CollectionUtil.isNotEmpty(prdCgBodyVOList)){
            prdDetailVO.setPrdCgBodyVOList(prdCgBodyVOList);
            int sumUndeliveredAmount = prdCgBodyVOList.stream().mapToInt(PrdCgBodyVO::getUndeliveredAmount).sum();//未交数量统计
            prdDetailHeadVO.setCgUnHandOrder(sumUndeliveredAmount);
        }
        //3.根据货品号查询自制单表身集合
        List<PrdZzBodyVO> prdZzBodyVOList = prdDetailMapper.selectZzBodyByPrdNo(prdNo);
        ArrayList<PrdZzBodyFinVO> prdZzBodyFinVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(prdZzBodyVOList)){
            Map<String, List<PrdZzBodyVO>> listMap = prdZzBodyVOList.stream().collect(Collectors.groupingBy(PrdZzBodyVO::getWorkOrderNo));
            listMap.entrySet().stream().map(key->{
                PrdZzBodyFinVO prdZzBodyFinVO = new PrdZzBodyFinVO();
                List<PrdZzBodyVO> value = key.getValue();
                PrdZzBodyVO prdZzBodyVO = value.get(0);
                prdZzBodyFinVO.setWorkOrderKind(prdZzBodyVO.getWorkOrderKind());
                prdZzBodyFinVO.setWorkOrderNo(prdZzBodyVO.getWorkOrderNo());
                prdZzBodyFinVO.setProperties(prdZzBodyVO.getProperties());
                prdZzBodyFinVO.setInputAmount(prdZzBodyVO.getInputAmount());
                int sum1 = 0 ;
                int sum2 = 0 ;
                int sum3 = 0 ;
                if (CollectionUtil.isNotEmpty(value)){
                    List<PrdZzBodyVO> collect1 = value.stream().filter(e -> e.getSupplierNo().trim().equals("1008")).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(collect1)){sum1 = collect1.get(0).getLeaveAmount();}
                    List<PrdZzBodyVO> collect2 = value.stream().filter(e -> e.getSupplierNo().trim().equals("1009")).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(collect2)){sum2 = collect2.get(0).getLeaveAmount();}
                    List<PrdZzBodyVO> collect3 = value.stream().filter(e -> e.getSupplierNo().trim().equals("1010")).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(collect3)){sum3 = collect3.get(0).getLeaveAmount();}
                }
                prdZzBodyFinVO.setJjybAmount(sum1);
                prdZzBodyFinVO.setJjybHclAmount(sum2);
                prdZzBodyFinVO.setAssemblyAmount(sum3);
                int bfCount = value.stream().mapToInt(PrdZzBodyVO::getScrapAmount).sum();//报废数量统计
                prdZzBodyFinVO.setScrappedAmount(bfCount);
                prdZzBodyFinVO.setOnProduceAmount(sum1+sum2+sum3);
                prdZzBodyFinVOS.add(prdZzBodyFinVO);
                return prdZzBodyFinVOS;
            }).collect(Collectors.toList());

        }
        prdDetailVO.setPrdZzBodyFinVOList(prdZzBodyFinVOS);
        prdDetailVO.setPrdDetailHeadVO(prdDetailHeadVO);
        return Callback.success(prdDetailVO);
    }
}
