package com.ennova.pubinfostore.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.ExcelWriteUtil;
import com.ennova.pubinfocommon.utils.FileNameUtils;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfostore.dao.PreAssembleMapper;
import com.ennova.pubinfostore.dao.ScheduleAssembleMapper;
import com.ennova.pubinfostore.entity.PreAssemble;
import com.ennova.pubinfostore.entity.ScheduleAssemble;
import com.ennova.pubinfostore.vo.AssembleUserVO;
import com.ennova.pubinfostore.vo.ScheduleAssembleListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ScheduleAssembleService{

    private final ScheduleAssembleMapper scheduleAssembleMapper;
    private final PreAssembleMapper preAssembleMapper;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public Callback selectAssembleUserList(String userName) {
        return Callback.success(scheduleAssembleMapper.selectAssembleUserList(userName));
    }


    public Callback insert(ScheduleAssembleListVO scheduleAssembleVO) {
        if (null != scheduleAssembleVO){
            ScheduleAssemble scheduleAssemble = new ScheduleAssemble();
            BeanUtils.copyProperties(scheduleAssembleVO, scheduleAssemble);
            scheduleAssemble.setCreateTime(new Date());
            scheduleAssembleMapper.insert(scheduleAssemble);
            if (CollectionUtils.isNotEmpty(scheduleAssembleVO.getPreAssembleList())){
                scheduleAssembleVO.getPreAssembleList().forEach(preAssembleVO -> {
                    PreAssemble preAssemble = new PreAssemble();
                    BeanUtils.copyProperties(preAssembleVO, preAssemble);
                    preAssemble.setCreateTime(new Date());
                    preAssemble.setYsScheduleAssembleId(scheduleAssemble.getId());
                    preAssembleMapper.insert(preAssemble);
                });
                return Callback.success("新增成功");
            }
        }
        return Callback.error("新增失败");
    }

    // 删除排产装配表数据和预排人员表数据
    public Callback delete(Integer id) {
        if (id != null) {
            scheduleAssembleMapper.deleteByPrimaryKey(id);
            preAssembleMapper.deleteByYsScheduleAssembleId(id);
            return Callback.success("删除成功");
        }
        return Callback.error("删除失败");
    }

    // 根据id查询排产装配表数据
    public Callback<ScheduleAssembleListVO> selectById(Integer id) {
        List<ScheduleAssembleListVO> list = scheduleAssembleMapper.selectPreAssembleList(null, null, id);
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(scheduleAssembleListVO -> {
                // 获取用户id
                List<Integer> userIds = Arrays.stream(scheduleAssembleListVO.getAssembleId().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                // 根据用户id查询用户姓名
                List<AssembleUserVO> assembleUserVOList = scheduleAssembleMapper.selectUserByIds(userIds);
                scheduleAssembleListVO.setUserList(assembleUserVOList);

                scheduleAssembleListVO.getPreAssembleList().stream().forEach(preAssembleListVO -> {
                    // 获取用户id
                    List<Integer> userIds1 = Arrays.stream(preAssembleListVO.getPreAssembleId().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                    // 根据用户id查询用户姓名
                    List<AssembleUserVO> assembleUserVOList1 = scheduleAssembleMapper.selectUserByIds(userIds1);
                    preAssembleListVO.setUserList(assembleUserVOList1);
                });
            });
            return Callback.success(list.get(0));
        }
        return Callback.error("查询失败");
    }

    // 修改排产装配表数据和预排人员表数据
    public Callback update(ScheduleAssembleListVO scheduleAssembleVO) {
        if (null != scheduleAssembleVO && scheduleAssembleVO.getId() != null){
            ScheduleAssemble scheduleAssemble = new ScheduleAssemble();
            BeanUtils.copyProperties(scheduleAssembleVO, scheduleAssemble);
            scheduleAssemble.setCreateTime(scheduleAssembleVO.getCreateTime());
            scheduleAssemble.setUpdateTime(new Date());
            scheduleAssembleMapper.updateByPrimaryKey(scheduleAssemble);
            if (CollectionUtils.isNotEmpty(scheduleAssembleVO.getPreAssembleList())){
                scheduleAssembleVO.getPreAssembleList().forEach(preAssembleVO -> {
                    if (preAssembleVO.getId() != null) {
                        PreAssemble preAssemble = new PreAssemble();
                        BeanUtils.copyProperties(preAssembleVO, preAssemble);
                        preAssemble.setYsScheduleAssembleId(scheduleAssemble.getId());
                        preAssemble.setCreateTime(preAssembleVO.getCreateTime());
                        preAssemble.setUpdateTime(new Date());
                        preAssembleMapper.updateByPrimaryKey(preAssemble);
                    }
                });
                return Callback.success("修改成功");
            }
        }
        return Callback.error("修改失败");
    }

    // 查询预排产列表
    public Callback<BaseVO<ScheduleAssembleListVO>> selectPreAssembleList(Integer page, Integer pageSize, String deliveryDate, String searchKey) {
        Page<ScheduleAssembleListVO> startPage = PageMethod.startPage(page, pageSize);
        List<ScheduleAssembleListVO> list = scheduleAssembleMapper.selectPreAssembleList(deliveryDate, searchKey, null);
        list.forEach(scheduleAssembleListVO -> {
            List<Integer> assembleIdList = Arrays.asList(scheduleAssembleListVO.getAssembleId().split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            List<AssembleUserVO> assembleUserVOS = scheduleAssembleMapper.selectUserByIds(assembleIdList);
            // 装配人员
            scheduleAssembleListVO.setUserList(assembleUserVOS);
            scheduleAssembleListVO.getPreAssembleList().forEach(preAssembleVO -> {
                List<Integer> preAssembleIdList = Arrays.asList(preAssembleVO.getPreAssembleId().split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
                List<AssembleUserVO> preUserList = scheduleAssembleMapper.selectUserByIds(preAssembleIdList);
                // 预装人员
                preAssembleVO.setUserList(preUserList);
            });
        });
        BaseVO<ScheduleAssembleListVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public void exportData(HttpServletResponse response, String deliveryDate, String searchKey) {
        List<ScheduleAssembleListVO> list = scheduleAssembleMapper.selectPreAssembleList(deliveryDate, searchKey, null);
        list.forEach(scheduleAssembleListVO -> {
            List<Integer> assembleIdList = Arrays.asList(scheduleAssembleListVO.getAssembleId().split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            List<AssembleUserVO> assembleUserVOS = scheduleAssembleMapper.selectUserByIds(assembleIdList);
            // 装配人员
            scheduleAssembleListVO.setUserList(assembleUserVOS);
            scheduleAssembleListVO.getPreAssembleList().forEach(preAssembleVO -> {
                List<Integer> preAssembleIdList = Arrays.asList(preAssembleVO.getPreAssembleId().split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
                List<AssembleUserVO> preUserList = scheduleAssembleMapper.selectUserByIds(preAssembleIdList);
                // 预装人员
                preAssembleVO.setUserList(preUserList);
            });
        });

        // 导出数据
        StringBuffer sb = new StringBuffer();
        log.info(sb + "总记录条数 size:{}",list.size());
        List<String> titleList = new ArrayList<>();
        titleList.add("排产日期");
        titleList.add("产品品号");
        titleList.add("产品名称");
        titleList.add("工单号");
        titleList.add("开单总数(套)");
        titleList.add("生产总数(套)");
        titleList.add("备注");
        titleList.add("需生产日期");
        titleList.add("完成情况");
        titleList.add("装配人员");
        titleList.add("预装人员");

        String dateToString = FileNameUtils.dateToString(new Date(), "yyyyMMddHHmmss");
        List<List<String>> exportlist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)){
            for (ScheduleAssembleListVO scheduleAssembleListVO : list) {
                List<String> rowList = new ArrayList<>();
                rowList.add(sdf.format(scheduleAssembleListVO.getDeliveryDate())); // 排产日期
                rowList.add(scheduleAssembleListVO.getProductId()); // 产品品号
                rowList.add(scheduleAssembleListVO.getProductName()); // 产品名称
                rowList.add(scheduleAssembleListVO.getWorkOrder()); // 工单号
                rowList.add(String.valueOf(scheduleAssembleListVO.getTotalOrder())); // 开单总数(套)
                rowList.add(String.valueOf(scheduleAssembleListVO.getTotalProduction())); // 生产总数(套)
                rowList.add(scheduleAssembleListVO.getRemark()); // 备注
                rowList.add(sdf.format(scheduleAssembleListVO.getNeedDate())); // 需生产日期
                rowList.add(scheduleAssembleListVO.getStatus() == 0 ? "未完成" : "已完成"); // 完成情况
                rowList.add(scheduleAssembleListVO.getUserList().stream().map(AssembleUserVO::getUserName).collect(Collectors.joining("、"))); // 装配人员

                List<String> preAssembleList = new ArrayList<>();
                scheduleAssembleListVO.getPreAssembleList().forEach(preAssembleVO -> {
                    String preAssemble = preAssembleVO.getAssembleItem() + " (" + preAssembleVO.getUserList().stream().map(AssembleUserVO::getUserName).collect(Collectors.joining("、")) + ") ";
                    preAssembleList.add(preAssemble);
                });
                rowList.add(StringUtils.join(preAssembleList, "、")); // 预装人员
                exportlist.add(rowList);
            }
        }
        ExcelWriteUtil.createHeader(response, "排产信息" + dateToString, titleList, exportlist);
    }


}



