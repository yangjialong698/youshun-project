package com.ennova.pubinfostore.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.ExcelWriteUtil;
import com.ennova.pubinfocommon.utils.FileNameUtils;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfostore.dao.ScProblemFeedbackMapper;
import com.ennova.pubinfostore.dao.ScProblemFileMapper;
import com.ennova.pubinfostore.entity.ScProblemFeedback;
import com.ennova.pubinfostore.entity.ScProblemFile;
import com.ennova.pubinfostore.utils.ProblemStatusEnum;
import com.ennova.pubinfostore.vo.ScProblemFeedbackVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/12/26
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class PcService {

    private final ScProblemFeedbackMapper scProblemFeedbackMapper;
    private final ScProblemFileMapper scProblemFileMapper;
    private final HttpServletResponse response;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Callback<BaseVO<ScProblemFeedbackVO>> getDateBoardList(Integer page, Integer pageSize) {

        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        int index = (page - 1) * pageSize;
        BaseVO<ScProblemFeedbackVO> baseVO;
        ArrayList<ScProblemFeedbackVO> scProblemFeedbackVOS = new ArrayList<>();
        List<ScProblemFeedback> scProblemFeedbacks = scProblemFeedbackMapper.selectDateBoardList();
        if (CollectionUtil.isNotEmpty(scProblemFeedbacks)){
            scProblemFeedbacks.forEach(e->{
                ScProblemFeedbackVO scProblemFeedbackVO = new ScProblemFeedbackVO();
                BeanUtils.copyProperties(e,scProblemFeedbackVO);
                if(!e.getBackStatus().equals("1")){
                    long betweenHour = DateUtil.between(e.getCreateTime(), new Date(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }else {
                    long betweenHour = DateUtil.between(e.getCreateTime(), e.getSolveTime(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }
                scProblemFeedbackVOS.add(scProblemFeedbackVO);
            });
            baseVO = new BaseVO<>(pageing(index,pageSize,scProblemFeedbackVOS), new PageUtil(pageSize, scProblemFeedbackVOS.size(), page));
        } else {
            baseVO = new BaseVO<>(scProblemFeedbackVOS, new PageUtil(pageSize, 0, page));
        }
        return Callback.success(baseVO);
    }

    public Callback<BaseVO<ScProblemFeedbackVO>> getDateBoardLists(Integer page, Integer pageSize) {

        if(page==null || page<1){
            page = 1;
        }
        if(pageSize==null || pageSize<1){
            pageSize = 10;
        }
        int index = (page - 1) * pageSize;
        BaseVO<ScProblemFeedbackVO> baseVO;
        ArrayList<ScProblemFeedbackVO> scProblemFeedbackVOS = new ArrayList<>();
        List<ScProblemFeedback> scProblemFeedbacks = scProblemFeedbackMapper.selectDateBoardLists();
        if (CollectionUtil.isNotEmpty(scProblemFeedbacks)){
            scProblemFeedbacks.forEach(e->{
                ScProblemFeedbackVO scProblemFeedbackVO = new ScProblemFeedbackVO();
                BeanUtils.copyProperties(e,scProblemFeedbackVO);
                if(!e.getBackStatus().equals("1")){
                    long betweenHour = DateUtil.between(e.getCreateTime(), new Date(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }else {
                    long betweenHour = DateUtil.between(e.getCreateTime(), e.getSolveTime(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }
                scProblemFeedbackVOS.add(scProblemFeedbackVO);
            });
            baseVO = new BaseVO<>(pageing(index,pageSize,scProblemFeedbackVOS), new PageUtil(pageSize, scProblemFeedbackVOS.size(), page));
        } else {
            baseVO = new BaseVO<>(scProblemFeedbackVOS, new PageUtil(pageSize, 0, page));
        }
        return Callback.success(baseVO);
    }

    public List<ScProblemFeedbackVO> pageing(int index, int pageSize, List<ScProblemFeedbackVO> list){
        if (index < 0 || index >= list.size() || pageSize <= 0) {
            return null;
        }
        int lastIndex = index + pageSize;
        if (lastIndex > list.size()) {
            lastIndex = list.size();
        }
        list = list.subList(index, lastIndex);
        return list;
    }

    public Callback<List<ScProblemFeedbackVO>> getHistoryDateBoardList(Integer status) {

        List<ScProblemFeedback> scProblemFeedbacks = scProblemFeedbackMapper.selectHistoryDateBoardList(status);
        List<ScProblemFeedbackVO> scProblemFeedbackVOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(scProblemFeedbacks)){
            for (ScProblemFeedback scProblemFeedback : scProblemFeedbacks) {
                ScProblemFeedbackVO scProblemFeedbackVO = new ScProblemFeedbackVO();
                BeanUtils.copyProperties(scProblemFeedback,scProblemFeedbackVO);
                if(!scProblemFeedback.getBackStatus().equals("1")){
                    long betweenHour = DateUtil.between(scProblemFeedback.getCreateTime(), new Date(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }else {
                    long betweenHour = DateUtil.between(scProblemFeedback.getCreateTime(), scProblemFeedback.getSolveTime(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }
                scProblemFeedbackVOS.add(scProblemFeedbackVO);
            }
        }
        return Callback.success(scProblemFeedbackVOS);
    }

    public Callback<ScProblemFeedbackVO> getHistoryDateBoardDetail(Integer id) {
        if (id != null) {
            ScProblemFeedback scProblemFeedback = scProblemFeedbackMapper.selectByPrimaryKey(id);
            ScProblemFeedbackVO scProblemFeedbackVO = new ScProblemFeedbackVO();
            BeanUtils.copyProperties(scProblemFeedback, scProblemFeedbackVO);
            if (ObjectUtil.isNotEmpty(scProblemFeedbackVO)) {
                List<ScProblemFile> scProblemFiles = scProblemFileMapper.selectFilesByProblemIds(scProblemFeedbackVO.getId());
                scProblemFeedbackVO.setFileVOList(scProblemFiles);
            }
            if(!scProblemFeedback.getBackStatus().equals("1")){
                long betweenHour = DateUtil.between(scProblemFeedback.getCreateTime(), new Date(), DateUnit.HOUR);
                scProblemFeedbackVO.setGqTime(betweenHour);
            }else {
                long betweenHour = DateUtil.between(scProblemFeedback.getCreateTime(), scProblemFeedback.getSolveTime(), DateUnit.HOUR);
                scProblemFeedbackVO.setGqTime(betweenHour);
            }
            return Callback.success(scProblemFeedbackVO);
        }
        return Callback.error("暂无数据");
    }

    public Callback<ScProblemFeedbackVO> getProblemsStatus() {

        ScProblemFeedbackVO myProblemsStatus = scProblemFeedbackMapper.getProblemsStatus();
        return Callback.success(myProblemsStatus);
    }

    public void exportHistoryDateBoardData(Integer status) {
        List<ScProblemFeedback> scProblemFeedbacks = scProblemFeedbackMapper.selectHistoryDateBoardList(status);
        List<ScProblemFeedbackVO> scProblemFeedbackVOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(scProblemFeedbacks)) {
            for (ScProblemFeedback scProblemFeedback : scProblemFeedbacks) {
                ScProblemFeedbackVO scProblemFeedbackVO = new ScProblemFeedbackVO();
                BeanUtils.copyProperties(scProblemFeedback, scProblemFeedbackVO);
                if (!scProblemFeedback.getBackStatus().equals("1")) {
                    long betweenHour = DateUtil.between(scProblemFeedback.getCreateTime(), new Date(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                } else {
                    long betweenHour = DateUtil.between(scProblemFeedback.getCreateTime(), scProblemFeedback.getSolveTime(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }
                scProblemFeedbackVOS.add(scProblemFeedbackVO);
            }
        }
        log.info("呼叫系统数据报表导出size:{}", scProblemFeedbacks.size());
        FileNameUtils fileNameUtils = new FileNameUtils();
        String titleStr = "呼叫系统数据报表" + fileNameUtils.getfileName();
        List<String> titleList = new ArrayList<>();
        titleList.add("序号");
        titleList.add("反馈时间");
        titleList.add("问题描述");
        titleList.add("反馈人");
        titleList.add("责任人");
        titleList.add("解决时间");
        titleList.add("确认时间");
        titleList.add("挂起时间");
        titleList.add("问题状态");

        List<List<String>> list = new ArrayList<>();
        AtomicReference<Integer> serialNumber = new AtomicReference<>(0);
        for (ScProblemFeedbackVO scProblemFeedbackVO : scProblemFeedbackVOS) {
            List<String> rowList = new ArrayList<>();
            rowList.add(serialNumber.updateAndGet(v -> v + 1).toString());
            rowList.add(sdf.format(scProblemFeedbackVO.getCreateTime()));
            rowList.add(scProblemFeedbackVO.getProblemDescription());
            rowList.add(scProblemFeedbackVO.getBackPerson());
            rowList.add(scProblemFeedbackVO.getDutyPerson());
            rowList.add(scProblemFeedbackVO.getUpdateTime() == null ? null : sdf.format(scProblemFeedbackVO.getUpdateTime()));
            rowList.add(scProblemFeedbackVO.getSolveTime() == null ? null : sdf.format(scProblemFeedbackVO.getSolveTime()));
            rowList.add(scProblemFeedbackVO.getGqTime().toString());
            rowList.add(ProblemStatusEnum.getProblemStatusEnum(Integer.valueOf(scProblemFeedbackVO.getBackStatus())).getName());
            list.add(rowList);
        }
        ExcelWriteUtil.createHeader(response, titleStr, titleList, list);
    }
}
