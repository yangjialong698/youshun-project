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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ErpTransferOrderMapper erpTransferOrderMapper;

    @Autowired
    private ErpPrdInfoMapper erpPrdInfoMapper;

    public Callback insertOrUpdate(ErpScrapLossVO erpScrapLossVO) {
        String orderDate = erpScrapLossVO.getOrderDate();
        String workCenterNo = erpScrapLossVO.getWorkCenterNo();
        String prdNo = erpScrapLossVO.getPrdNo();
        ErpScrapLoss erpScrapLoss = new ErpScrapLoss();
        BeanUtils.copyProperties(erpScrapLossVO,erpScrapLoss);
        if (erpScrapLossVO.getId() != null){
            //更新
            erpScrapLossMapper.updateByPrimaryKey(erpScrapLoss);
        }else {
            //新增
            ErpScrapLoss erpScrapLossOne = erpScrapLossMapper.selByOmpNo(orderDate, workCenterNo, prdNo);
            if (null != erpScrapLossOne){
                return Callback.success("当天工作中心关联品号已存在!");
            }
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

    public List<String> getPrdNos(String key) {
        List<String> prdNoList = erpPrdInfoMapper.selectPrdNoList(key);
        return prdNoList;
    }

    public ErpPerhourCostVO getErpPerhourCost(String workCenterNo) {
        ErpPerhourCostVO erpPerhourCostVO = erpPrdCostMapper.getErpPerhourCost(workCenterNo);
        return erpPerhourCostVO;
    }

    public Callback<ErpPrdNameVO> getErpPrdByPrdno(String workCenterNo,String prdNo) {
        String prdName = "";
        List<ErpPrdInfo> erpPrdInfos = erpPrdInfoMapper.selectByPrdNo(prdNo);
        if (CollectionUtil.isNotEmpty(erpPrdInfos)){
            prdName = erpPrdInfos.get(0).getPrdName();
        }
        ErpPrdNameVO erpPrdNameVO = erpPrdCostMapper.selectErpPrdNameVoByPrdno(workCenterNo,prdNo);
        if (null != erpPrdNameVO){
            erpPrdNameVO.setPrdName(prdName);
        }
        return Callback.success(erpPrdNameVO);
    }

    public Callback<ErpScrapLoss> getDetailById(Integer id) {
        ErpScrapLoss erpScrapLoss = erpScrapLossMapper.selectByPrimaryKey(id);
        return Callback.success(erpScrapLoss);
    }

    //计算报废数量+报废金额
    @Scheduled(cron = " 0 0 23 * * ?")
    public void calculateScrapInfo() {
        List<ErpScrapLoss> erpScrapLossList = erpScrapLossMapper.selectNullInfo();
        if (CollectionUtil.isNotEmpty(erpScrapLossList)) {
            erpScrapLossList.forEach(e -> {
                String orderDate = e.getOrderDate();//单据日期
                String workCenterNo = e.getWorkCenterNo();//工作中心
                String prdNo = e.getPrdNo();//产品品号
                Double hourCost = e.getHourCost();//平均小时成本含社保
                Double prdPerCost = 0.0;
                if (null != e.getPrdPerCost()){
                    prdPerCost = e.getPrdPerCost();//单件材料费
                    Double workHours = e.getWorkHours();//工时
                    List<ErpTransferOrder> erpTransferOrderList = erpTransferOrderMapper.selByOmpNo(orderDate, workCenterNo, prdNo);
                    if (CollectionUtil.isNotEmpty(erpTransferOrderList)) {
                        Integer scrapNumTotal = erpTransferOrderList.stream().mapToInt(ErpTransferOrder::getScrapNum).sum();//总报废数量
                        Integer acceptanceNumTotal = erpTransferOrderList.stream().mapToInt(ErpTransferOrder::getAcceptanceNum).sum();//总合格数量
                        Double perPerson = 0.0; //单件人工
                        Double scrapCostTotal = 0.0; //总报废金额
                        if (acceptanceNumTotal != 0){
                            perPerson = hourCost * workHours / acceptanceNumTotal;
                            //报废金额 = 报废数量*(单件人工+单件材料费)
                            scrapCostTotal = scrapNumTotal * (perPerson + prdPerCost );
                        }else {
                            scrapCostTotal = scrapNumTotal * prdPerCost + hourCost * workHours;
                        }
                        e.setScrapNum(scrapNumTotal);
                        e.setScrapCost(scrapCostTotal);
                        //更新报废数量+报废金额
                        erpScrapLossMapper.updateByPrimaryKeySelective(e);
                    }
                }
            });
        }
    }


}
