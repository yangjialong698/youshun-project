package com.ennova.pubinfostore.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfostore.dao.ScProblemFeedbackMapper;
import com.ennova.pubinfostore.entity.ScProblemFeedback;
import com.ennova.pubinfostore.vo.ScProblemFeedbackVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest req;

    public Callback<BaseVO<ScProblemFeedbackVO>> getDateBoardList(Integer page, Integer pageSize) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

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

    public Callback<List<ScProblemFeedbackVO>> getHistoryDateBoardList() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        List<ScProblemFeedback> scProblemFeedbacks = scProblemFeedbackMapper.selectHistoryDateBoardList();
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

    public Callback<ScProblemFeedbackVO> getProblemsStatus() {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        ScProblemFeedbackVO myProblemsStatus = scProblemFeedbackMapper.getProblemsStatus();
        return Callback.success(myProblemsStatus);
    }
}
