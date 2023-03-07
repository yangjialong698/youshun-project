package com.ennova.pubinfoproduct.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.daos.ErpScrapLossMapper;
import com.ennova.pubinfoproduct.daos.ErpTransferOrderMapper;
import com.ennova.pubinfoproduct.daos.ScrapPerOutnoMapper;
import com.ennova.pubinfoproduct.entity.ErpScrapLoss;
import com.ennova.pubinfoproduct.entity.ErpTransferOrder;
import com.ennova.pubinfoproduct.entity.ScrapPerOutno;
import com.ennova.pubinfoproduct.vo.ErpPerhourCostVO;
import com.ennova.pubinfoproduct.vo.ErpPrdNameVO;
import com.ennova.pubinfoproduct.vo.ScrapVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ErpTransferOrderService {

    @Resource
    private ErpTransferOrderMapper erpTransferOrderMapper;
    @Resource
    private ErpScrapLossService erpScrapLossService;
    @Resource
    private ErpScrapLossMapper erpScrapLossMapper;
    @Resource
    private ScrapPerOutnoMapper scrapPerOutnoMapper;

    public Callback<List<ScrapVO>> erpinputscrap(String moveOutNo) {
        List<String> gxList = null;
        if (moveOutNo.contains(",")) {
            gxList = Arrays.asList(moveOutNo.split(","));
        } else {
            gxList = Arrays.asList(moveOutNo);
        }
        List<ErpTransferOrder> erpTransferOrders = erpTransferOrderMapper.selectAllByMoveOutNo(gxList);
        ArrayList<ScrapVO> scrapVOArrayList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(erpTransferOrders)) {
            Map<String, List<ErpTransferOrder>> listMap = erpTransferOrders.stream().collect(Collectors.groupingBy(ErpTransferOrder::getOrderDate));
            listMap.entrySet().stream().map(key -> {
                ScrapVO scrapVO = new ScrapVO();
                List<ErpTransferOrder> value = key.getValue();
                int rcCount = value.stream().mapToInt(o -> Objects.isNull(o.getAcceptanceNum()) ? 0 : o.getAcceptanceNum()).sum();// 日产量单品汇总
                int rkCount = value.stream().mapToInt(o -> Objects.isNull(o.getTotalNum()) ? 0 : o.getTotalNum()).sum(); // 入库数量单品汇总
                int bfCount = value.stream().mapToInt(o -> Objects.isNull(o.getScrapNum()) ? 0 : o.getScrapNum()).sum(); // 报废数量单品汇总
                int badCount = value.stream().mapToInt(o -> Objects.isNull(o.getBadNum()) ? 0 : o.getBadNum()).sum(); // 不良数量单品汇总
                //Double scrapCostCount = value.stream().mapToDouble(o->Objects.isNull(o.getScrapCost()) ? 0 :o.getScrapCost()).sum(); // 报废金额单品汇总
                NumberFormat num = NumberFormat.getInstance();
                num.setMaximumFractionDigits(2);
                String percent = num.format((float) (bfCount + badCount) / (float) rkCount * 100);
                scrapVO.setScrapNum(bfCount);
                scrapVO.setBadNum(badCount);
                scrapVO.setDayPrdNum(rcCount);
                scrapVO.setOrderDate(value.get(0).getOrderDate());
                BigDecimal decimalPercent = new BigDecimal(percent);
                if (decimalPercent.compareTo(new BigDecimal(15)) >= 0) {
                    percent = String.valueOf(15);
                }
                scrapVO.setBadScrapRate(percent);
                //scrapVO.setScrapCostCount(scrapCostCount);
                scrapVOArrayList.add(scrapVO);
                return scrapVOArrayList;
            }).collect(Collectors.toList());
        }
        List<ScrapVO> collect = scrapVOArrayList.stream().sorted(Comparator.comparing(ScrapVO::getOrderDate)).collect(Collectors.toList());
        return Callback.success(collect);
    }

    //计算近一个月的轮播图报废金额数据(手动跑一次)
//    @Scheduled(cron = " 0 0 23 * * ?")
    public void calMonthErpScrapInfo(String outNo) {
//      String outNo ===>  String ybz = "1003,1018,1019"  jjyb = "1008"   ybhcl = "1009"   zp = "1010"
        List<String> gxList = null;
        if (outNo.contains(",")) {
            gxList = Arrays.asList(outNo.split(","));
        } else {
            gxList = Arrays.asList(outNo);
        }
        AtomicReference<Double> scrapCostTotal = new AtomicReference<>(0.0); //单种工作中心+某天汇总所有品号总报废金额
        ArrayList<ScrapPerOutno> scrapVOArrayList = new ArrayList<>();
        for (String moveOutNo : gxList) {
            //1.通过单类工作中心查询近一个月的转移单数据
            List<ErpTransferOrder> erpTransferOrders = erpTransferOrderMapper.selectByMoveOutNo(moveOutNo);
            if (CollectionUtil.isNotEmpty(erpTransferOrders)) {
                Map<String, List<ErpTransferOrder>> listMap = erpTransferOrders.stream().collect(Collectors.groupingBy(ErpTransferOrder::getOrderDate));
                listMap.entrySet().stream().map(key -> {
                    ScrapPerOutno scrapPerOutno = new ScrapPerOutno();
                    //2.groupBy日期获取每天的转移单数据
                    String orderDate = key.getKey();
                    List<ErpTransferOrder> value = key.getValue();
                    int rcCount = value.stream().mapToInt(o -> Objects.isNull(o.getAcceptanceNum()) ? 0 : o.getAcceptanceNum()).sum();// 日产量单品汇总
                    int rkCount = value.stream().mapToInt(o -> Objects.isNull(o.getTotalNum()) ? 0 : o.getTotalNum()).sum(); // 入库数量单品汇总
                    int bfCount = value.stream().mapToInt(o -> Objects.isNull(o.getScrapNum()) ? 0 : o.getScrapNum()).sum(); // 报废数量单品汇总
                    int badCount = value.stream().mapToInt(o -> Objects.isNull(o.getBadNum()) ? 0 : o.getBadNum()).sum(); // 不良数量单品汇总
                    NumberFormat num = NumberFormat.getInstance();
                    num.setMaximumFractionDigits(2);
                    String percent = num.format((float) (bfCount + badCount) / (float) rkCount * 100);
                    scrapPerOutno.setScrapNum(bfCount);
                    scrapPerOutno.setBadNum(badCount);
                    scrapPerOutno.setDayPrdNum(rcCount);
                    scrapPerOutno.setOrderDate(orderDate);
                    BigDecimal decimalPercent = new BigDecimal(percent);
                    if (decimalPercent.compareTo(new BigDecimal(15)) >= 0) {
                        percent = String.valueOf(15);
                    }
                    scrapPerOutno.setBadScrapRate(percent);
                    //3.每天的转移单数据groupBy品号获取每天每个品号的转移单列表
                    if (CollectionUtil.isNotEmpty(value)) {
                        Map<String, List<ErpTransferOrder>> prdNoListMap = value.stream().collect(Collectors.groupingBy(ErpTransferOrder::getProductNo));
                        prdNoListMap.entrySet().stream().map(key1 -> {
                            String prdNo = key1.getKey(); // 获取到某个工作中心,某天,某个品号及对应的转移单
                            List<ErpTransferOrder> value1 = key1.getValue();
                            Integer scrapNumTotal = value1.stream().mapToInt(ErpTransferOrder::getScrapNum).sum();//单天单品号总报废数量
                            Integer acceptanceNumTotal = value1.stream().mapToInt(ErpTransferOrder::getAcceptanceNum).sum();//单天单品号总合格数量
                            //4.通过工作中心查询平均小时成本,通过工作中心+品号查询单件材料费，通过工作中心(mOutNo)+日期(orderDate)+品号(prdNo)查询工时
                            ErpPerhourCostVO erpPerhourCost = erpScrapLossService.getErpPerhourCost(moveOutNo); //平均小时成本实体
                            ErpPrdNameVO erpPrdNameVO = erpScrapLossService.getErpPrdByPrdno(moveOutNo, prdNo).getData(); //单件材料费实体
                            Double prdPerCost = 0.0;
                            if (null != erpPrdNameVO){
                                prdPerCost = erpPrdNameVO.getPrdPerCost();
                            }
                            ErpScrapLoss erpScrapLossOne = erpScrapLossMapper.selByOmpNo(orderDate, moveOutNo, prdNo); //工时实体
                            if (null != erpScrapLossOne.getHourCost()){
                                if (acceptanceNumTotal != 0){
                                    Double perPerson = erpPerhourCost.getHourCost() * erpScrapLossOne.getHourCost() / acceptanceNumTotal;
                                    //5.单种工作中心+某天汇总所有品号总报废金额 = 报废数量*(单件人工+单件材料费)
                                    Double scrapCostPerPrdNo = scrapNumTotal * (perPerson + prdPerCost);
                                    scrapCostTotal.updateAndGet(v -> v + scrapCostPerPrdNo);
                                }else {
                                    Double scrapCostPerPrdNo = scrapNumTotal * prdPerCost + erpPerhourCost.getHourCost() * erpScrapLossOne.getHourCost();
                                    scrapCostTotal.updateAndGet(v -> v + scrapCostPerPrdNo);
                                }
                            }
                            return null;
                        }).collect(Collectors.toList());
                    }
                    Double perMoveScrap = scrapCostTotal.get();
                    scrapPerOutno.setScrapCost(perMoveScrap);
                    scrapVOArrayList.add(scrapPerOutno);
                    //6.单个工作中心报废金额入库(orderDate,moveOutNo,perMoveScrap)
                    return scrapVOArrayList;
                }).collect(Collectors.toList());
            }
        }
        scrapPerOutnoMapper.batchInsert(scrapVOArrayList);
    }
}
