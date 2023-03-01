package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfoproduct.daos.ErpPrdCostMapper;
import com.ennova.pubinfoproduct.daos.ErpPrdInfoMapper;
import com.ennova.pubinfoproduct.daos.ErpScrapLossMapper;
import com.ennova.pubinfoproduct.daos.ErpTransferOrderMapper;
import com.ennova.pubinfoproduct.entity.CustomerAccountInfo;
import com.ennova.pubinfoproduct.entity.ErpPrdInfo;
import com.ennova.pubinfoproduct.entity.ErpScrapLoss;
import com.ennova.pubinfoproduct.entity.ErpTransferOrder;
import com.ennova.pubinfoproduct.vo.ErpPerhourCostVO;
import com.ennova.pubinfoproduct.vo.ErpPrdNameVO;
import com.ennova.pubinfoproduct.vo.ErpScrapLossVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class ErpScrapLossService {
    @Autowired
    private ErpScrapLossMapper erpScrapLossMapper;

    @Autowired
    private ErpPrdCostMapper erpPrdCostMapper;

    @Autowired
    private ErpPrdInfoMapper erpPrdInfoMapper;

    @Autowired
    private ErpTransferOrderMapper erpTransferOrderMapper;

    public Callback insertOrUpdate(ErpScrapLossVO erpScrapLossVO) {
        String orderDate = erpScrapLossVO.getOrderDate();
        String workCenterNo = erpScrapLossVO.getWorkCenterNo();
        String prdNo = erpScrapLossVO.getPrdNo();
        ErpScrapLoss erpScrapLoss = new ErpScrapLoss();
        BeanUtils.copyProperties(erpScrapLossVO,erpScrapLoss);
        if (erpScrapLossVO.getId() != null){
            ErpScrapLoss erpScrapLossOne = erpScrapLossMapper.selByOmpNo(orderDate, workCenterNo, prdNo);
            if (null != erpScrapLossOne){
                return Callback.success("当天工作中心关联品号已存在!");
            }
            //更新
            erpScrapLossMapper.updateByPrimaryKey(erpScrapLoss);
        }else {
            //新增
            erpScrapLoss.setDelFlag(0);
            erpScrapLossMapper.insertSelective(erpScrapLoss);
        }
        return Callback.success(true);
    }

    public Callback delete(Integer id) {
        int i = erpScrapLossMapper.updateDelFlag(id);
        if (i>0){
            return Callback.success("删除成功!");
        }
        return Callback.success("删除失败!");
    }

    public Callback<BaseVO<ErpScrapLoss>> selectErpScrapLossList(Integer page, Integer pageSize, String keyTime, String workCenterNo, String prdNo) {
        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
        List<ErpScrapLoss> list = erpScrapLossMapper.selectErpScrapLossList(keyTime,workCenterNo,prdNo);
        BaseVO<ErpScrapLoss> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public List<String> getWorkCenterNoList() {
        List<String> workCenterNos = erpPrdCostMapper.getWorkCenterNoList();
        return workCenterNos;
    }

    public ErpPerhourCostVO getErpPerhourCost(String workCenterNo) {
        ErpPerhourCostVO erpPerhourCostVO = erpPrdCostMapper.getErpPerhourCost(workCenterNo);
        return erpPerhourCostVO;
    }

    public Callback<ErpPrdNameVO> getErpPrdByPrdno(String workCenterNo,String prdNo) {
        String prdName = "";
        ErpTransferOrder erpTransferOrder = erpTransferOrderMapper.selectByMoveOutNoAndProductNo(workCenterNo, prdNo);
        if (null != erpTransferOrder){
            prdName = erpTransferOrder.getProductName();
        }
        ErpPrdNameVO erpPrdNameVO = erpPrdCostMapper.selectErpPrdNameVoByPrdno(prdNo);
        if (null != erpPrdNameVO){
            erpPrdNameVO.setPrdName(prdName);
        }
        return Callback.success(erpPrdNameVO);
    }

    public List<ErpScrapLoss> selectHisInfoList() {
        List<ErpScrapLoss> erpScrapLossList = erpScrapLossMapper.selectNullInfo();
        return erpScrapLossList;
    }

    public void updateByPrimaryKeySelective(ErpScrapLoss e) {
        erpScrapLossMapper.updateByPrimaryKeySelective(e);
    }

    public Callback<ErpScrapLoss> getDetailById(Integer id) {
        ErpScrapLoss erpScrapLoss = erpScrapLossMapper.selectByPrimaryKey(id);
        return Callback.success(erpScrapLoss);
    }
}
