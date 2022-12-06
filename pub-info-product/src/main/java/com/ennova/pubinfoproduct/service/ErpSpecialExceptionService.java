package com.ennova.pubinfoproduct.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfoproduct.daos.ErpSpecialExceptionMapper;
import com.ennova.pubinfoproduct.entity.ErpSpecialException;
import com.ennova.pubinfoproduct.utils.BeanConvertUtils;
import com.ennova.pubinfoproduct.vo.ErpExceptionCountVO;
import com.ennova.pubinfoproduct.vo.ErpSpecialExceptionVO;
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
public class ErpSpecialExceptionService {

    private final ErpSpecialExceptionMapper erpSpecialExceptionMapper;

    public Callback insertOrUpdate(@NonNull ErpSpecialExceptionVO erpSpecialExceptionVO) {

        ErpSpecialException erpException = BeanConvertUtils.convertTo(erpSpecialExceptionVO, ErpSpecialException::new);
        if (erpException.getId() != null) {
            ErpSpecialException erpExceptions = erpSpecialExceptionMapper.selectByPrimaryKey(erpSpecialExceptionVO.getId());
            if (erpExceptions != null) {
                erpException.setUpdateTime(new Date());
            }
            int i = erpSpecialExceptionMapper.updateByPrimaryKeySelective(erpException);
            if (i > 0) {
                return Callback.success("修改异常信息成功!");
            }
        } else {
            erpException.setCreateTime(new Date());
            erpException.setDelFlag(0);
            int count = erpSpecialExceptionMapper.insertSelective(erpException);
            if (count > 0) {
                return Callback.success("新增异常信息成功!");
            }
        }
        return Callback.error(2, "数据处理失败!");
    }


    public Callback<BaseVO<ErpSpecialExceptionVO>> exceptionList(Integer page, Integer pageSize) {
        Page<ErpSpecialExceptionVO> startPage = PageHelper.startPage(page, pageSize);
        List<ErpSpecialExceptionVO> erpExceptionVOS = erpSpecialExceptionMapper.selectSpecialExceptionLists();
        BaseVO<ErpSpecialExceptionVO> baseVO = new BaseVO<>(erpExceptionVOS, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<ErpSpecialException> getDetail(Integer id) {
        if (id != null) {
            ErpSpecialException erpSpecialException = erpSpecialExceptionMapper.selectByPrimaryKey(id);
            return Callback.success(erpSpecialException);
        }
        return Callback.error("暂无数据");
    }

    public Callback delete(Integer id) {
        ErpSpecialException erpSpecialException = erpSpecialExceptionMapper.selectByPrimaryKey(id);
        if (erpSpecialException != null) {
            int i = erpSpecialExceptionMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

    public Callback<List<ErpExceptionCountVO>> selectExceptionCountList() {
        List<ErpSpecialExceptionVO> erpExceptionVOS = erpSpecialExceptionMapper.selectSpecialExceptionLists();

        Map<String, List<String>> dateListMap = erpExceptionVOS.parallelStream().collect(Collectors.groupingBy(erpSpecialExceptionVO -> DateFormatUtils.format(erpSpecialExceptionVO.getCreateTime(), "yyyy-MM-dd"),
                        Collectors.mapping(ErpSpecialExceptionVO::getExceptionMessage, Collectors.toList())));

        List<ErpExceptionCountVO> erpExceptionCountVOList = new ArrayList<>();
        for (Map.Entry<String, List<String>> dateListEntry : dateListMap.entrySet()) {
            ErpExceptionCountVO erpExceptionCountVO = new ErpExceptionCountVO();
            erpExceptionCountVO.setCreateTime(dateListEntry.getKey());
            List<Map<String, String>> list = new ArrayList<>();
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
        return Callback.success(erpExceptionCountVOLists);
    }
}
