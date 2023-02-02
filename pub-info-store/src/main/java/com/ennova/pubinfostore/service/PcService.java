package com.ennova.pubinfostore.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfostore.dao.ScProblemFeedbackMapper;
import com.ennova.pubinfostore.dao.ScProblemFileMapper;
import com.ennova.pubinfostore.entity.ScProblemFeedback;
import com.ennova.pubinfostore.entity.ScProblemFile;
import com.ennova.pubinfostore.vo.ScProblemFeedbackVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Callback<BaseVO<ScProblemFeedbackVO>> getDateBoardList(Integer page, Integer pageSize) {

        if(page==null || page<1){
            page = 1;
        }
        if(pageSize==null || pageSize<1){
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
}
