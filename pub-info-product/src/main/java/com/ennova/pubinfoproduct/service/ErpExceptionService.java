package com.ennova.pubinfoproduct.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfoproduct.daos.ErpExceptionMapper;
import com.ennova.pubinfoproduct.entity.ErpException;
import com.ennova.pubinfoproduct.utils.BeanConvertUtils;
import com.ennova.pubinfoproduct.utils.DayTypeEnum;
import com.ennova.pubinfoproduct.utils.MuduleTypeEnum;
import com.ennova.pubinfoproduct.vo.ErpExceptionCountVO;
import com.ennova.pubinfoproduct.vo.ErpExceptionVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
//@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ErpExceptionService {

    private final ErpExceptionMapper erpExceptionMapper;

    public Callback insertOrUpdate(@NonNull ErpExceptionVO erpExceptionVO) {

        ErpException erpException = BeanConvertUtils.convertTo(erpExceptionVO, ErpException::new);
        if (erpException.getId() != null) {
            ErpException erpExceptions = erpExceptionMapper.selectByPrimaryKey(erpExceptionVO.getId());
            if (erpExceptions != null) {
                erpException.setUpdateTime(new Date());
            }
            int i = erpExceptionMapper.updateByPrimaryKeySelective(erpException);
            if (i > 0) {
                return Callback.success("修改异常信息成功!");
            }
        } else {
            erpException.setCreateTime(new Date());
            erpException.setDelFlag(0);
            int count = erpExceptionMapper.insertSelective(erpException);
            if (count > 0) {
                return Callback.success("新增异常信息成功!");
            }
        }
        return Callback.error(2, "数据处理失败!");
    }


    public Callback<BaseVO<ErpExceptionVO>> exceptionList(Integer page, Integer pageSize, Integer muduleType) {
        Page<ErpExceptionVO> startPage = PageHelper.startPage(page, pageSize);
        List<ErpExceptionVO> erpExceptionVOS = erpExceptionMapper.selectBymuduleTypeLikes(muduleType);
        BaseVO<ErpExceptionVO> baseVO = new BaseVO<>(erpExceptionVOS, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<ErpException> getDetail(Integer id) {
        if (id != null) {
            ErpException erpException = erpExceptionMapper.selectByPrimaryKey(id);
            return Callback.success(erpException);
        }
        return Callback.error("暂无数据");
    }

    public Callback delete(Integer id) {
        ErpException erpException = erpExceptionMapper.selectByPrimaryKey(id);
        if (erpException != null) {
            int i = erpExceptionMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

    public Callback<Map<String, List<String>>> selectExceptionCountMap(Integer dayType) {

        String dayNumStr = DayTypeEnum.getDayTypeEnum(dayType).getName();
        List<ErpExceptionVO> erpExceptionVOS = erpExceptionMapper.selectExceptionCountList(dayNumStr);
        Map<Integer, List<String>> listMap = erpExceptionVOS.parallelStream().collect(Collectors.groupingBy(erpExceptionVO -> erpExceptionVO.getMuduleType(), Collectors.mapping(ErpExceptionVO::getModuleExceptionMessage, Collectors.toList())));
        Map<String, List<String>> stringListMap = new HashMap<>();
        for (Map.Entry<Integer, List<String>> integerListEntry : listMap.entrySet()) {
            stringListMap.put(MuduleTypeEnum.getMuduleTypeEnum(integerListEntry.getKey()).getName(), integerListEntry.getValue());
        }
        return Callback.success(stringListMap);
    }

    public Callback<BaseVO<ErpExceptionCountVO>> selectExceptionCounList(Integer page, Integer pageSize, Integer muduleType) {
        Page<ErpExceptionVO> startPage = PageHelper.startPage(page, pageSize);
        List<ErpExceptionVO> erpExceptionVOS = erpExceptionMapper.selectBymuduleTypeLike(muduleType);
        Map<String, List<String>> dateListMap = erpExceptionVOS.parallelStream().collect(Collectors.groupingBy(erpExceptionVO -> DateFormatUtils.format(erpExceptionVO.getCreateTime(), "yyyy-MM-dd"), Collectors.mapping(ErpExceptionVO::getModuleExceptionMessage, Collectors.toList())));
        List<ErpExceptionCountVO> erpExceptionCountVOList = new ArrayList<>();
        for (Map.Entry<String, List<String>> dateListEntry : dateListMap.entrySet()) {
            ErpExceptionCountVO erpExceptionCountVO = new ErpExceptionCountVO();
            erpExceptionCountVO.setCreateTime(dateListEntry.getKey());
            List<Map<String,String>> list = new ArrayList<>();
            for (String s : dateListEntry.getValue()) {
                Map<String, String> map = new HashMap<>();
                map.put("title", s);
                list.add(map);
            }
            erpExceptionCountVO.setModuleExceptionMessage(list);
            erpExceptionCountVOList.add(erpExceptionCountVO);
        }
        List<ErpExceptionCountVO> erpExceptionCountVOLists = erpExceptionCountVOList.stream().sorted(Comparator.comparing(erpExceptionCountVO -> erpExceptionCountVO.getCreateTime())).collect(Collectors.toList());
        Collections.reverse(erpExceptionCountVOLists);
        BaseVO<ErpExceptionCountVO> erpExceptionCountVOBaseVO = new BaseVO<>(erpExceptionCountVOLists, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(erpExceptionCountVOBaseVO);
    }
}
