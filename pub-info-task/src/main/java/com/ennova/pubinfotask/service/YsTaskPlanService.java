package com.ennova.pubinfotask.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.dao.YsSonTaskMapper;
import com.ennova.pubinfotask.dao.YsTaskPlanMapper;
import com.ennova.pubinfotask.dao.YsTeamMapper;
import com.ennova.pubinfotask.dto.UserMasterDTO;
import com.ennova.pubinfotask.entity.YsTaskPlan;
import com.ennova.pubinfotask.entity.YsTeam;
import com.ennova.pubinfotask.vo.CurrentUserVO;
import com.ennova.pubinfotask.vo.YsTaskPlanPageListVO;
import com.ennova.pubinfotask.vo.YsTaskPlanTreeVO;
import com.ennova.pubinfotask.vo.YsTaskPlanVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/25
 * @Description: com.ennova.pubinfotask.service
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsTaskPlanService {

    private final YsTaskPlanMapper ysTaskPlanMapper;
    private final UserMapper userMapper;
    private final YsTeamMapper ysTeamMapper;
    private final YsSonTaskMapper ysSonTaskMapper;
    private final HttpServletRequest req;

    // 查询当前用户，已认领的所有主任务
    public Callback<List<LinkedHashMap>> selectMasterTaskAll(){
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        Set<Integer> userList = Sets.newHashSet();
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())){
            Integer userId = userVo.getId();
            userList.add(userId);
        }
        if ("executor".equals(currentUserVO.getRoleCode())) {
           // List<YsTeam> ysTeams = ysTeamMapper.selectAllByExecutorId(userVo.getId());
           // if (CollectionUtils.isEmpty(ysTeams)){
           //     log.info("没有子任务，无法查询当前用户，已认领的所有主任务");
           //     List<LinkedHashMap> newList = new ArrayList<>();
           //     return Callback.success(newList);
           // }
           //userList = ysTeams.stream().map(YsTeam::getUserId).collect(Collectors.toSet());
            List<LinkedHashMap> listMap = ysSonTaskMapper.selectMasterNameByExecutor(userVo.getId());
            return Callback.success(listMap);
        }
        List<LinkedHashMap> masterTaskAll = ysTaskPlanMapper.selectMasterTaskAll(userList);
        return Callback.success(masterTaskAll);
    }


    public Callback<YsTaskPlan> selectOneGroupChecked(Integer id){
        if(id != null){
            YsTaskPlan vo = ysTaskPlanMapper.selectByPrimaryKey(id);
            return Callback.success(vo);
        }
        return Callback.error(2,"id不能为空");
    }

    public Callback insert(YsTaskPlanVO record) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        //当前主任务计划名称不允许重复
        List<YsTaskPlan> list = ysTaskPlanMapper.selectAllByReceiveIdAndYsMasterTaskIdAndName(userVo.getId(), record.getYsMasterTaskId(), record.getName(),null);
        if (!list.isEmpty()){
            Callback.error(2,"新增的主任务，计划名称已经存在");
        }
        YsTaskPlan ysTaskPlan = new YsTaskPlan();
        BeanUtils.copyProperties(record,ysTaskPlan);
        ysTaskPlan.setCreateTime(LocalDateTime.now());
        ysTaskPlan.setReceiveId(userVo.getId()); // 当前登陆人ID
        if (record.getPlanStatus().equals(1)){
            ysTaskPlan.setActualTime(LocalDateTime.now());
        }
        int count = ysTaskPlanMapper.insert(ysTaskPlan);
        if (count > 0){
            return Callback.success(true);
        }
        return Callback.error(2,"新增失败!");
    }

    public Callback updateByPrimaryKeySelective(YsTaskPlanVO record) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        assert userVo != null;
        YsTaskPlan ysTaskPlan = new YsTaskPlan();
        BeanUtils.copyProperties(record,ysTaskPlan);
        YsTaskPlan ysTaskPlanOld = ysTaskPlanMapper.selectByPrimaryKey(record.getId());
        ysTaskPlan.setCreateTime(ysTaskPlanOld.getCreateTime());
        ysTaskPlan.setUpdateTime(LocalDateTime.now());
        ysTaskPlan.setReceiveId(ysTaskPlanOld.getReceiveId());
        /* 已完成 **/
        if(record.getPlanStatus().equals(1)){
            ysTaskPlan.setActualTime(LocalDateTime.now());
        }
        //当前主任务计划名称不允许重复，排除当前ID
        List<YsTaskPlan> list = ysTaskPlanMapper.selectAllByReceiveIdAndYsMasterTaskIdAndName(userVo.getId(), record.getYsMasterTaskId(), record.getName(), record.getId());
        if (!list.isEmpty()){
            Callback.error(2,"修改的主任务，计划名称已经存在");
        }

        int count = ysTaskPlanMapper.updateByPrimaryKey(ysTaskPlan);
        if (count > 0){
            return Callback.success(true);
        }
        return Callback.error(2,"更新失败!");
    }


    public Callback<BaseVO<YsTaskPlanPageListVO>> selectTaskPlanList(Integer page, Integer pageSize, Integer ysMasterTaskId, Integer planStatus, String likeName){

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        Integer userId = null;
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())){
            userId = userVo.getId();
        }
        Page<YsTaskPlanPageListVO> startPage = PageMethod.startPage(page, pageSize);
        List<YsTaskPlanPageListVO> list = ysTaskPlanMapper.selectTaskPlanList(ysMasterTaskId, planStatus, likeName, userId);
        BaseVO<YsTaskPlanPageListVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int)startPage.getTotal(), page));
        return Callback.success(baseVO);

    }


    public Callback<List<YsTaskPlanTreeVO>> selectTaskPlanTree(Integer ysMasterTaskId){
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        Set<Integer> userList = Sets.newHashSet();
        Set<Integer> masterIds = Sets.newHashSet();
        masterIds.add(ysMasterTaskId);
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())){
            Integer userId = userVo.getId();
            userList.add(userId);
        }
        // 团队成员可根据某个主任务，查所有的计划
        if ("executor".equals(currentUserVO.getRoleCode())) {
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByYsMasterTaskIdAndExecutorId(userVo.getId(), ysMasterTaskId);
            if (CollectionUtils.isEmpty(ysTeams)){
                log.info("团队成员的上领人员，无子任务");
                List<YsTaskPlanTreeVO> list = new ArrayList<>();
                return Callback.success(list);
            }
            masterIds = ysTeams.stream().map(YsTeam::getYsMasterTaskId).collect(Collectors.toSet());
        }
        userList.removeIf(Objects::isNull);
        masterIds.removeIf(Objects::isNull);
        UserMasterDTO dto = UserMasterDTO.builder().userList(userList).masterIds(masterIds).build();
        List<YsTaskPlanTreeVO> list = ysTaskPlanMapper.selectTaskPlanTree(dto);
        return Callback.success(list);

    }

    public Callback selectByHaveUserId(Integer userId){
        Integer count = userMapper.selectByHaveUserId(userId);
        return Callback.success(count);
    }


    

}

