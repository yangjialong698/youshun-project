package com.ennova.pubinfotask.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.dao.YsMasterTaskCostMapper;
import com.ennova.pubinfotask.entity.YsMasterTaskCost;
import com.ennova.pubinfotask.utils.BeanConvertUtils;
import com.ennova.pubinfotask.vo.BaseVO;
import com.ennova.pubinfotask.vo.CostBaseVO;
import com.ennova.pubinfotask.vo.CurrentUserVO;
import com.ennova.pubinfotask.vo.YsMasterTaskCostVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsMasterTaskCostService {

    private final YsMasterTaskCostMapper ysMasterTaskCostMapper;
    private final UserMapper userMapper;
    private final HttpServletRequest req;

    private String getRoleCode() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        return currentUserVO.getRoleCode();
    }


    public Callback<CostBaseVO<YsMasterTaskCost>> selectByYsMasterTaskId(Integer page, Integer pageSize, Integer ysMasterTaskId, String costDate) {
        String roleCode = getRoleCode();
        if ("cost_accountant".equals(roleCode)) {
            //求和,reduce默认值0,保留两位小数
            AtomicReference<Double> cost = new AtomicReference<>(0D);
            List<YsMasterTaskCost> costList = ysMasterTaskCostMapper.selectByYsMasterTaskIdAndCostDate(ysMasterTaskId, costDate);
            if (CollectionUtils.isNotEmpty(costList)) {
                // 求和，Double类型的数据相加
                Double sumCost = costList.stream().filter(v -> v.getCost() != null).map(YsMasterTaskCost::getCost).reduce((v1, v2) -> v1 + v2).orElse(0D);
                double newCost = new BigDecimal(sumCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                cost.set(newCost);
            }
            Page<YsMasterTaskCost> startPage = PageHelper.startPage(page, pageSize);
            List<YsMasterTaskCost> list = ysMasterTaskCostMapper.selectByYsMasterTaskIdAndCostDate(ysMasterTaskId, costDate);
            CostBaseVO<YsMasterTaskCost> baseVO = new CostBaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page), cost.get());
            return Callback.success(baseVO);
        } else {
                return Callback.error(2, "没有权限");
        }

    }


    public Callback batchCostInsert(BaseVO<YsMasterTaskCostVO> baseVO) {
        List<YsMasterTaskCostVO> list = baseVO.getList();
        String roleCode = getRoleCode();
        if ("cost_accountant".equals(roleCode)) {
            if (CollectionUtils.isNotEmpty(list)) {
                List<YsMasterTaskCost> costList = list.stream().map(ysMasterTaskCostVO -> {
                    YsMasterTaskCost ysMasterTaskCost = BeanConvertUtils.convertTo(ysMasterTaskCostVO, YsMasterTaskCost::new);
                    ysMasterTaskCost.setCreateTime(LocalDateTime.now());
                    return ysMasterTaskCost;
                }).collect(Collectors.toList());
                int count = ysMasterTaskCostMapper.batchInsert(costList);
                if (count > 0) {
                    return Callback.success();
                }
            }
        } else {
            return Callback.error(2, "您没有权限操作");
        }
        return Callback.error(2, "新增失败");
    }


    public Callback deleteByPrimaryKey (Integer id){
        String roleCode = getRoleCode();
        if ("cost_accountant".equals(roleCode)) {
            ysMasterTaskCostMapper.deleteByPrimaryKey(id);
            return Callback.success();
        } else {
            return Callback.error(2, "您没有权限操作");
        }
    }

    public Callback updateCostBatch(BaseVO<YsMasterTaskCostVO> baseVO) {
        List<YsMasterTaskCostVO> list = baseVO.getList();
        String roleCode = getRoleCode();
        if ("cost_accountant".equals(roleCode)) {
            if (CollectionUtils.isNotEmpty(list)) {
                list.stream().forEach(ysMasterTaskCostVO -> {
                    YsMasterTaskCost ysMasterTaskCost = BeanConvertUtils.convertTo(ysMasterTaskCostVO, YsMasterTaskCost::new);
                    ysMasterTaskCost.setCost(null);
                    ysMasterTaskCost.setUpdateTime(LocalDateTime.now());
                    ysMasterTaskCostMapper.updateByPrimaryKeySelective(ysMasterTaskCost);
                });
            }
            return Callback.success();
        } else {
            return Callback.error(2, "您没有权限操作");
        }
    }




}




