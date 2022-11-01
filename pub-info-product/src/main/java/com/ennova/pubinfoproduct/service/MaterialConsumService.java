package com.ennova.pubinfoproduct.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.dao.MaterialConsumMapper;
import com.ennova.pubinfoproduct.vo.MaterialConsumVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialConsumService {
    @Autowired
    private MaterialConsumMapper materialConsumMapper;

    public Callback<List<MaterialConsumVO>> listMaterialConsum(String startTime, String endTime, String module,
                                                               String workOrderNo, String consumKind, String consumNo,
                                                               String prdName, String prdNo) {
        List<MaterialConsumVO> materialConsumVOList = null ;
        if (consumKind.equals("0")){
            materialConsumVOList = materialConsumMapper.listMaterialConsumByGd(startTime, endTime, module, workOrderNo, consumKind, consumNo, prdName, prdNo);
        }else {
            materialConsumVOList = materialConsumMapper.listMaterialConsumByFy(startTime, endTime, module, workOrderNo, consumKind, consumNo, prdName, prdNo);
        }

        return Callback.success(materialConsumVOList);
    }
}
