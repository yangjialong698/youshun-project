package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfoproduct.daos.ErpPrdInfoMapper;
import com.ennova.pubinfoproduct.daos.ErpReworkRepairMapper;
import com.ennova.pubinfoproduct.entity.ErpPrdInfo;
import com.ennova.pubinfoproduct.entity.ErpReworkRepair;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ErpReworkRepairService {
    @Autowired
    private ErpReworkRepairMapper erpReworkRepairMapper;
    @Autowired
    private ErpPrdInfoMapper erpPrdInfoMapper;

    public Callback insertOrUpdate(ErpReworkRepair erpReworkRepair) {
        if (null != erpReworkRepair.getId()){
            //更新
            erpReworkRepairMapper.updateByPrimaryKeySelective(erpReworkRepair);
            return Callback.success("返工返修信息修改成功");
        }else {
            //新增
            erpReworkRepairMapper.insertSelective(erpReworkRepair);
            return Callback.success("返工返修信息新增成功");
        }
    }

    public Callback deleteOne(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        int result = erpReworkRepairMapper.deleteByPrimaryKey(id);
        if (result < 1) {
            return Callback.error("删除失败");
        }
        return Callback.success();
    }

    public Callback<ErpReworkRepair> getDeatil(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        return Callback.success(erpReworkRepairMapper.selectByPrimaryKey(id)) ;
    }

    public Callback<BaseVO<ErpReworkRepair>> getReworkRepairList(Integer page, Integer pageSize,String key) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        BaseVO<ErpReworkRepair> baseVO = null ;
        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
        List<ErpReworkRepair> list = erpReworkRepairMapper.selectAll(key);
        baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<String> getPrdName(String productNo) {
        if (StringUtils.isEmpty(productNo)) {
            return Callback.error("请输入品号");
        }
        String prdName = "";
        List<ErpPrdInfo> erpPrdInfos = erpPrdInfoMapper.selectByPrdNo(productNo);
        if (CollectionUtil.isNotEmpty(erpPrdInfos)){
            prdName = erpPrdInfos.get(0).getPrdName();
        }
        return Callback.success(prdName);
    }
}
