package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.dao.ProduceMapper;
import com.ennova.pubinfoproduct.vo.PrdZzBodyFinVO;
import com.ennova.pubinfoproduct.vo.PrdZzBodyVO;
import com.ennova.pubinfoproduct.vo.ProduceDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProduceService {
    @Autowired
    private ProduceMapper produceMapper;

    public Callback<List<ProduceDetailVO>> selectProduceList(String startTime, String endTime, String processNo, String workOrderNo) {
        List<ProduceDetailVO> produceDetailVOList = produceMapper.selectProduceList(startTime,endTime,processNo,workOrderNo);
        ArrayList<ProduceDetailVO> produceDetailVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(produceDetailVOList)){
            Map<String, List<ProduceDetailVO>> listMap = produceDetailVOList.stream().collect(Collectors.groupingBy(ProduceDetailVO::getWorkOrderNo));
            listMap.entrySet().stream().map(key->{
                ProduceDetailVO produceDetailVOTemp = new ProduceDetailVO();
                List<Integer> newOrderNumList = produceMapper.selOrderNumByOrderNo(key.getKey());
                produceDetailVOTemp.setNewOrderNum(newOrderNumList.get(0));
                List<ProduceDetailVO> value = key.getValue();
                int normalNum = value.stream().mapToInt(ProduceDetailVO::getNormalNum).sum();
                int waitNum = value.stream().mapToInt(ProduceDetailVO::getCount).sum();
                produceDetailVOTemp.setNormalNum(normalNum);
                int scrappedNum = value.stream().mapToInt(ProduceDetailVO::getScrappedNum).sum();
                produceDetailVOTemp.setScrappedNum(scrappedNum);
                produceDetailVOTemp.setProcessNo(value.get(0).getProcessNo());
                produceDetailVOTemp.setProcessName(value.get(0).getProcessName());
                produceDetailVOTemp.setWorkOrderNo(value.get(0).getWorkOrderNo());
                produceDetailVOTemp.setWaitNum(waitNum);
                produceDetailVOS.add(produceDetailVOTemp);
                return produceDetailVOS;
            }).collect(Collectors.toList());
        }

        return Callback.success(produceDetailVOS);
    }
}
