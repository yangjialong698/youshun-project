package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.dao.PrdDetailMapper;
import com.ennova.pubinfoproduct.vo.PrdCgBodyVO;
import com.ennova.pubinfoproduct.vo.PrdDetailHeadVO;
import com.ennova.pubinfoproduct.vo.PrdDetailVO;
import com.ennova.pubinfoproduct.vo.PrdZzBodyVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (null != prdDetailHeadVO){
            prdDetailVO.setPrdDetailHeadVO(prdDetailHeadVO);
        }
        //2.根据货品号查询自制单表身集合
        List<PrdZzBodyVO> prdZzBodyVOList = prdDetailMapper.selectZzBodyByPrdNo(prdNo);
        if (CollectionUtil.isNotEmpty(prdZzBodyVOList)){
            prdDetailVO.setPrdZzBodyVOList(prdZzBodyVOList);
        }
        //2.根据货品号查询采购单表身集合
        List<PrdCgBodyVO> prdCgBodyVOList = prdDetailMapper.selectCgBodyByPrdNo(prdNo);
        if (CollectionUtil.isNotEmpty(prdCgBodyVOList)){
            prdDetailVO.setPrdCgBodyVOList(prdCgBodyVOList);
        }
        return Callback.success(prdDetailVO);
    }
}
