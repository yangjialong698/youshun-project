package com.ennova.pubinfotask.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.dao.YsSonTaskMapper;
import com.ennova.pubinfotask.dao.YsTeamMapper;
import com.ennova.pubinfotask.dto.UserMasterDTO;
import com.ennova.pubinfotask.entity.YsSonTask;
import com.ennova.pubinfotask.entity.YsTeam;
import com.ennova.pubinfotask.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/27
 * @Description: com.ennova.pubinfotask.service
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsTeamService{

    private final YsTeamMapper ysTeamMapper;
    private final HttpServletRequest req;
    private final UserMapper userMapper;
    private final YsSonTaskMapper ysSonTaskMapper;

    /*自动加载用户部门，故注释掉*/
    //public Callback<List<LinkedHashMap>> selectDeptAll(){
    //    List<LinkedHashMap> list = ysTeamMapper.selectDeptAll();
    //    return Callback.success(list);
    //}

    /*变更后*/
    public Callback insertOrUpdate(YsTeamVO record) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())){
            YsTeam ysTeam = new YsTeam();
            ysTeam.setCost(null);
            boolean notBlank = StringUtils.isNotBlank(record.getCost());
            if (notBlank){
                boolean creatable = NumberUtils.isCreatable(record.getCost());
                if (creatable){
                    ysTeam.setCost(Double.valueOf(record.getCost()));
                }
            }
            BeanUtils.copyProperties(record,ysTeam);
            if (ysTeam.getId() != null){
                YsTeam old = ysTeamMapper.selectByPrimaryKey(ysTeam.getId());
                ysTeam.setUserId(old.getUserId());
                ysTeam.setExecutorId(old.getExecutorId());
                ysTeam.setUpdateTime(LocalDateTime.now());
                ysTeam.setCreateTime(old.getCreateTime());
                ysTeam.setId(old.getId());
                ysTeam.setIsDelete(old.getIsDelete());
                ysTeam.setYsMasterTaskId(old.getYsMasterTaskId());

                //int count = ysTeamMapper.updateByPrimaryKeySelective(ysTeam);
                int count = ysTeamMapper.updateByPrimaryKey(ysTeam);
                if (count > 0){
                    return Callback.success(true);
                }
            }else{
                //新增，查重
                List<EditYsTeamVO> list = ysTeamMapper.selectAllByUserIdAndExecutorId(userVo.getId(), record.getYsMasterTaskId(), record.getExecutorId());
                if (!list.isEmpty()){
                    return Callback.error(2,"此主任务，已存在当前执行人，请勿重复添加");
                }
                ysTeam.setUserId(userVo.getId());
                ysTeam.setCreateTime(LocalDateTime.now());
                int count = ysTeamMapper.insertSelective(ysTeam);
                if (count > 0){
                    return Callback.success(true);
                }
            }
        }
        return Callback.error(2,"新增或修改失败!");
    }

    public Callback<EditYsTeamVO> selectTeamOne(Integer id){
        if (id != null){
            return Callback.success(ysTeamMapper.selectAllById(id));
        }
        return Callback.error(2,"查询失败!");
    }

    public Callback deleteByPrimaryKey(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())){
            if (id != null){
                List<YsSonTask> tasks = ysSonTaskMapper.selectAllByYsTeamId(id);
                if (!tasks.isEmpty()){
                    return Callback.error(2,"子任务中存在该执行人，不允许删除！");
                }
                YsTeam ysTeam = YsTeam.builder().id(id).isDelete(1).build();
                int count = ysTeamMapper.updateByPrimaryKeySelective(ysTeam);
                if (count > 0){
                    return Callback.success(true);
                }
            }
        }
        return Callback.error(2,"删除失败");
    }


    public Callback<BaseVO<YsTeamPageListVO>> selectAllPageList(Integer page,Integer pageSize, Integer ysMasterTaskId, String name){
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
        if ("executor".equals(currentUserVO.getRoleCode())) {
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByYsMasterTaskIdAndExecutorId(userVo.getId(), ysMasterTaskId);
            if (CollectionUtils.isEmpty(ysTeams)){
                log.info("团队管理列表页面，没有子任务，无法查询");
                List<YsTeamPageListVO> newList = new ArrayList<>();
                BaseVO<YsTeamPageListVO> pageListVO = new BaseVO<>(newList, new PageUtil(0, 0, 0));
                return Callback.success(pageListVO);
            }
            masterIds = ysTeams.stream().map(YsTeam::getYsMasterTaskId).collect(Collectors.toSet());
        }
        userList.removeIf(Objects::isNull);
        masterIds.removeIf(Objects::isNull);
        UserMasterDTO dto = UserMasterDTO.builder().userList(userList).masterIds(masterIds).build();
        Page<YsTeamPageListVO> startPage = PageHelper.startPage(page, pageSize);
        List<YsTeamPageListVO> list = ysTeamMapper.selectAllPageList(name,dto);
        BaseVO<YsTeamPageListVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int)startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<List<ExecutorVO>> selectAllByRoleExecutor(){
        List<ExecutorVO> executorVOS = ysTeamMapper.selectAllByRoleExecutor();
        return Callback.success(executorVOS);
    }



}