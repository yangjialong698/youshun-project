package com.ennova.pubinfotask.service;

import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.*;
import com.ennova.pubinfotask.dto.MasterTeamGroupDTO;
import com.ennova.pubinfotask.dto.UserMasterDTO;
import com.ennova.pubinfotask.dto.WorkTimeResidueDTO;
import com.ennova.pubinfotask.entity.*;
import com.ennova.pubinfotask.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Author:
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/25
 * @Description: com.ennova.pubinfotask.service
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsSonTaskService {

    private final YsSonTaskMapper ysSonTaskMapper;
    private final YsMasterTaskMapper ysMasterTaskMapper;
    private final YsMasterFileMapper ysMasterFileMapper;
    private final YsSlaveFileMapper ysSlaveFileMapper;
    private final YsWorkTimeMapper ysWorkTimeMapper;
    private final YsTeamMapper ysTeamMapper;
    private final HttpServletRequest req;
    private final UserMapper userMapper;
    private final YsTaskReceiveMapper ysTaskReceiveMapper;
    private final RedisTemplate<String, String> redisTemplate;


    public Callback<List<LinkedHashMap>> selectMasterNameByReceiveId() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        Integer userId = null;
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
             userId = userVo.getId();
        }
        if ("executor".equals(currentUserVO.getRoleCode())) {
            List<LinkedHashMap> listMap = ysSonTaskMapper.selectMasterNameByExecutor(userVo.getId());
            return Callback.success(listMap);
        }
        List<LinkedHashMap> list = ysSonTaskMapper.selectMasterNameByReceiveId(userId);
        return Callback.success(list);
    }

    public Callback<List<LinkedHashMap>> selectMasterNameNotCloseByReceiveId() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        Set<Integer> userList = Sets.newHashSet();
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            Integer userId = userVo.getId();
            userList.add(userId);
        }
        if ("executor".equals(currentUserVO.getRoleCode())) {
            List<LinkedHashMap> listMap = ysSonTaskMapper.selectMasterNameNotCloseByExecutor(userVo.getId());
            return Callback.success(listMap);
        }
        List<LinkedHashMap> list = ysSonTaskMapper.selectMasterNameNotCloseByReceiveId(userList);
        return Callback.success(list);
    }

    public Callback<List<EditYsTeamVO>> selectAllByUserId(Integer masterTaskId) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        Integer userId = null;
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            userId = userVo.getId();
        }
        List<EditYsTeamVO> list = ysTeamMapper.selectAllByUserId(userId, masterTaskId);
        return Callback.success(list);
    }


    public Callback insertSonTask(YsSonTaskVO record) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        /* 具有子任务管理角色，并且主任务是认领用户的任务，才能创建子任务 **/
        // 需求提出bug,可重复
        //List<YsSonTask> existencelist = ysSonTaskMapper.selectAllByYsMasterTaskIdAndYsTeamId(record.getYsMasterTaskId(), record.getYsTeamId());
        // if (existencelist.size() >= 0 ){
        //     return Callback.error(2,"同一个主任务，子任务执行人不能重复");
        // }

        assert userVo != null;
        CurrentUserVO userVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(userVO.getRoleCode())) {
            LinkedHashMap<String, Integer> map = ysMasterTaskMapper.selectStatusAndReceiveIdById(record.getYsMasterTaskId());
            if (MapUtils.isEmpty(map)) {
                return Callback.error(2, "没有认领的任务");
            }
            Integer receiveId = map.get("receiveId");
            if (userVO.getUserId().equals(receiveId)) {
                YsSonTask sonTask = new YsSonTask();
                boolean notBlank = StringUtils.isNotBlank(record.getCost());
                if (notBlank) {
                    boolean creatable = NumberUtils.isCreatable(record.getCost());
                    if (creatable) {
                        sonTask.setCost(new BigDecimal(record.getCost()));
                    }
                }
                BeanUtils.copyProperties(record, sonTask);
                if (record.getEstimateWorkTime() != null) {
                    sonTask.setEstimateWorkTime(record.getEstimateWorkTime().intValue());
                }
                // sonTask.setEstimateWorkTime(record.getEstimateWorkTime().intValue());
                sonTask.setReceiveId(userVo.getId());
                sonTask.setCreateTime(LocalDateTime.now());
                sonTask.setStatus(0); // 未开始
                ysSonTaskMapper.insertSelective(sonTask); // 新增主任务

                List<FileVO> fileVOList = record.getFileVOList();
                if (fileVOList != null && !fileVOList.isEmpty()) {
                    String formatTime = DateUtil.format(new Date(), "yyyyMMddHHmmss");
                    YsMasterFile master = YsMasterFile.builder().serialNumber(record.getFilePrefix() + formatTime)
                            .fileName(record.getName())
                            .openFile(0) // 默认不对外开放
                            .ysFileTypeId(record.getYsFileTypeId()).createTime(LocalDateTime.now()).delFlag(0).fileContent(record.getFileContent())
                            .versionNo(record.getVersionNo()).ysSonTaskId(sonTask.getId()).ysMasterTaskId(sonTask.getYsMasterTaskId()).userId(userVo.getId()).build();
                    ysMasterFileMapper.insertSelective(master); // 新增主任务附件封皮
                    for (FileVO x : fileVOList) {
                        YsSlaveFile ysSlaveFile1 = ysSlaveFileMapper.selectByPrimaryKey(x.getId());
                        Optional.ofNullable(ysSlaveFile1).ifPresent(ysSlaveFile -> {
                            // 防止非法篡改文件
                            if (ysSlaveFile.getFileMasterId() == null) {
                                YsSlaveFile ysSlaveFile2 = YsSlaveFile.builder().id(x.getId()).updateTime(LocalDateTime.now()).fileMasterId(master.getId())
                                        .updateTime(LocalDateTime.now()).build();
                                ysSlaveFileMapper.updateByPrimaryKeySelective(ysSlaveFile2);
                            }
                        });

                        //YsSlaveFile ysSlaveFile = YsSlaveFile.builder().id(x.getId()).updateTime(LocalDateTime.now()).fileMasterId(master.getId())
                        //        .updateTime(LocalDateTime.now()).build();
                        //ysSlaveFileMapper.updateByPrimaryKeySelective(ysSlaveFile);
                    }

                    YsSonTask son = new YsSonTask();
                    son.setId(sonTask.getId());
                    son.setYsMasterFileId(master.getId());
                    ysSonTaskMapper.updateByPrimaryKeySelective(son);
                }
                // 新建子任务，主任务状态改为进行中
                YsMasterTask ysMasterTask = YsMasterTask.builder().id(record.getYsMasterTaskId()).status(3).build();
                ysMasterTaskMapper.updateByPrimaryKeySelective(ysMasterTask); // 更新附件表
                return Callback.success(true);
            }
        }
        return Callback.error(2, "新增失败!");
    }


    public Callback updateSonTask(YsSonTaskVO record) {
        Integer finalYsFileMasterId = record.getMasterFileId();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        // 需求提出bug,可重复
        //List<YsSonTask> existencelist = ysSonTaskMapper.selectAllByYsMasterTaskIdAndYsTeamId(record.getYsMasterTaskId(), record.getYsTeamId());
        //if (existencelist.size() >= 2 ){
        //    return Callback.error(2,"同一个主任务，子任务执行人不能重复");
        //}

        assert userVo != null;
        CurrentUserVO userVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(userVO.getRoleCode())) {
            /* 具有子任务管理角色，并且主任务是认领用户的任务，才能更新自己的子任务 **/
            LinkedHashMap<String, Integer> map = ysMasterTaskMapper.selectStatusAndReceiveIdById(record.getYsMasterTaskId());
            if (MapUtils.isEmpty(map)) {
                return Callback.error(2, "没有认领的任务");
            }

            Integer receiveId = map.get("receiveId");
            if (userVO.getUserId().equals(receiveId)) {
                YsSonTask task = ysSonTaskMapper.selectByPrimaryKey(record.getId()); // 查原有工时
                if (task != null) {
                    YsSonTask sonTask = new YsSonTask();
                    sonTask.setCost(null);
                    boolean notBlank = StringUtils.isNotBlank(record.getCost());
                    if (notBlank) {
                        boolean creatable = NumberUtils.isCreatable(record.getCost());
                        if (creatable) {
                            sonTask.setCost(new BigDecimal(record.getCost()));
                        }
                    }
                    BeanUtils.copyProperties(record, sonTask);
                    if (record.getEstimateWorkTime() != null) {
                        sonTask.setEstimateWorkTime(record.getEstimateWorkTime().intValue());
                    }
                    //sonTask.setEstimateWorkTime(record.getEstimateWorkTime().intValue());
                    sonTask.setReceiveId(task.getReceiveId());
                    sonTask.setStatus(0);
                    sonTask.setCreateTime(task.getCreateTime());
                    sonTask.setUpdateTime(LocalDateTime.now());

                    // 工时》 报工   工时《报工
                    List<YsWorkTime> ysWorkTimes1 = ysWorkTimeMapper.selectAllByYsSonTaskId(record.getId());
                    if (CollectionUtils.isNotEmpty(ysWorkTimes1)) {
                        Integer total = ysWorkTimes1.stream().map(YsWorkTime::getTotalConsume).reduce(0, Integer::sum);
                        sonTask.setStatus(2);
                        if (record.getEstimateWorkTime() != null){
                            if (record.getEstimateWorkTime() > total) {
                                sonTask.setStatus(1);
                            }
                        }
                    }

                    ysSonTaskMapper.updateByPrimaryKey(sonTask);
                    Integer newWorkTime = 0;
                    if (record.getEstimateWorkTime() != null) {
                         newWorkTime = record.getEstimateWorkTime().intValue();
                    }
                    Integer originalWorkTime = task.getEstimateWorkTime();

                    Integer diffWorkTime = null;
                    if (originalWorkTime != null) {
                          diffWorkTime = originalWorkTime - newWorkTime;
                    }


                    List<YsWorkTime> ysWorkTimes = ysWorkTimeMapper.selectAllByYsSonTaskId(record.getId());
                    if (CollectionUtils.isNotEmpty(ysWorkTimes)) {
                        //Integer total = ysWorkTimes.stream().map(YsWorkTime::getTotalConsume).reduce(0, Integer::sum);
                        if (originalWorkTime != null && diffWorkTime != null){
                            for (YsWorkTime ysWorkTime : ysWorkTimes) {
                                Integer workTime = ysWorkTime.getSurplus() - diffWorkTime;
                                ysWorkTime.setSurplus(workTime);
                                ysWorkTimeMapper.updateByPrimaryKey(ysWorkTime);
                            }
                        }



                        //if (newWorkTime < total) {
                        //    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        //    return Callback.error(2, "工时不能小于已报工工时");
                        //}
                        //if (newWorkTime >= total) {
                        //    for (YsWorkTime ysWorkTime : ysWorkTimes) {
                        //        Integer workTime = ysWorkTime.getSurplus() - diffWorkTime;
                        //        ysWorkTime.setSurplus(workTime);
                        //        ysWorkTimeMapper.updateByPrimaryKey(ysWorkTime);
                        //    }
                        //}
                    }

                    YsMasterFile ysMasterFile = ysMasterFileMapper.selectByPrimaryKey(finalYsFileMasterId);
                    List<FileVO> fileVOList = record.getFileVOList();
                    if (fileVOList != null && !fileVOList.isEmpty()) {
                        // 查出原始数据
                        if (ysMasterFile == null) {
                            ysMasterFile = new YsMasterFile();
                            String formatTime = DateUtil.format(new Date(), "yyyyMMddHHmmss");
                            ysMasterFile.setSerialNumber((record.getFilePrefix() + formatTime));
                            ysMasterFile.setFileName(record.getName());
                            ysMasterFile.setOpenFile(0);
                            ysMasterFile.setYsFileTypeId(record.getYsFileTypeId());
                            ysMasterFile.setCreateTime(LocalDateTime.now());
                            ysMasterFile.setDelFlag(0);
                            ysMasterFile.setYsSonTaskId(sonTask.getId());
                            ysMasterFile.setYsMasterTaskId(sonTask.getYsMasterTaskId());
                            ysMasterFile.setUserId(userVO.getUserId());
                            ysMasterFileMapper.insertSelective(ysMasterFile); // 新增主任务附件封皮
                        }
                        if (CollectionUtils.isNotEmpty(fileVOList)) {
                            for (FileVO x : fileVOList) {
                                YsSlaveFile ysSlaveFile = YsSlaveFile.builder().id(x.getId()).updateTime(LocalDateTime.now()).fileMasterId(ysMasterFile.getId())
                                        .updateTime(LocalDateTime.now()).build();
                                ysSlaveFileMapper.updateByPrimaryKeySelective(ysSlaveFile);
                            }
                        }

                        YsSonTask son = new YsSonTask();
                        son.setId(sonTask.getId());
                        son.setYsMasterFileId(ysMasterFile.getId());
                        ysSonTaskMapper.updateByPrimaryKeySelective(son);

                    }

                    log.info("子任务更新成功!");
                    return Callback.success(true);
                }
            }
        }

        return Callback.error(2, "更新失败!");
    }


    // 查询当前认领用户的子任务列表
    public Callback<BaseVO<YsSonTaskPageListVO>> selectSonTaskPagelist(Integer page, Integer pageSize, Integer status,
                                                                       Integer teamUserId, String sonTaskName, Integer masterTaskId) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        // 非子任务管理角色，可查看所有
        Set<Integer> setList = Sets.newHashSet();
        Set<Integer> masterIds = Sets.newHashSet();
        masterIds.add(masterTaskId);
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            Integer userId = userVo.getId();
            setList.add(userId);
        }
        // 执行人可以查看某个主任务下，子任务的所有团队成员记录
        if ("executor".equals(currentUserVO.getRoleCode())) {
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByYsMasterTaskIdAndExecutorId(userVo.getId(), masterTaskId);
            if (CollectionUtils.isEmpty(ysTeams)) {
                log.info("团队成员的上级人员，无认领的子任务");
                List<YsSonTaskPageListVO> newList = new ArrayList<>();
                BaseVO<YsSonTaskPageListVO> newBaseVO = new BaseVO<>(newList, new PageUtil(0, 0, 0));
                return Callback.success(newBaseVO);
            }
            // 上级领导相关的主任务有多个，存在不是自己负责的主任务
            setList.add(userVo.getId());
            masterIds = ysTeams.stream().map(YsTeam::getYsMasterTaskId).collect(Collectors.toSet());
        }
        setList.removeIf(Objects::isNull);
        masterIds.removeIf(Objects::isNull);
        UserMasterDTO dto = UserMasterDTO.builder().masterIds(masterIds).userList(setList).build();
        Page<YsSonTaskPageListVO> startPage = PageHelper.startPage(page, pageSize);
        //List<YsSonTaskPageListVO> list = ysSonTaskMapper.selectSonTaskPagelist(status, teamUserId, sonTaskName, dto, masterTaskName);
        List<YsSonTaskPageListVO> list = ysSonTaskMapper.selectSonTaskPagelist(status, teamUserId, sonTaskName, dto, masterTaskId);
        if (list != null && !list.isEmpty()) {
            list.forEach(x -> {
                List<YsMasterFile> masterFiles = ysMasterFileMapper.selectAllByYsSonTaskId(x.getId());
                for (YsMasterFile masterFile : masterFiles) {
                    x.setYsFileTypeId(masterFile.getYsFileTypeId());
                }
                x.setButtonStatus(1);
                if (masterFiles != null && !masterFiles.isEmpty()) {
                    x.setButtonStatus(0);
                }
                x.setMasterFinish(false);
                if (x.getMasterStatus() == 4 || x.getMasterStatus() == 5) {
                    x.setMasterFinish(true);
                }
            });
        }

        BaseVO<YsSonTaskPageListVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);

    }

    //  // 查询当前认领用户的子任务列表
    //public Callback<BaseVO<YsSonTaskPageListVO>> selectSonTaskPagelist(Integer page, Integer pageSize, Integer status,
    //                                                                   Integer teamUserId, String sonTaskName, Integer masterTaskId) {
    //    String token = req.getHeader("Authorization");
    //    UserVO userVo = JWTUtil.getUserVOByToken(token);
    //    assert userVo != null;
    //    Integer userId = null;
    //    CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
    //
    //    Set<Integer> masterIds = Sets.newHashSet();
    //    masterIds.add(masterTaskId);
    //    // 执行人可以查看某个主任务下，子任务的所有团队成员记录
    //    if ("executor".equals(currentUserVO.getRoleCode())) {
    //        List<YsTeam> ysTeams = ysTeamMapper.selectAllByYsMasterTaskIdAndExecutorId(userVo.getId(), masterTaskId);
    //        if (CollectionUtils.isEmpty(ysTeams)) {
    //            log.info("团队成员的上级人员，无认领的子任务");
    //            List<YsSonTaskPageListVO> newList = new ArrayList<>();
    //            BaseVO<YsSonTaskPageListVO> newBaseVO = new BaseVO<>(newList, new PageUtil(0, 0, 0));
    //            return Callback.success(newBaseVO);
    //        }
    //        userId = currentUserVO.getUserId();
    //        //masterIds是否为空
    //        if (CollectionUtils.isEmpty(masterIds)) {
    //            masterIds = ysTeams.stream().map(YsTeam::getYsMasterTaskId).collect(Collectors.toSet());
    //        }
    //
    //        ysTeamMapper.selectByPrimaryKey(teamUserId);
    //
    //
    //    }
    //    masterIds.removeIf(Objects::isNull);
    //    Page<YsSonTaskPageListVO> startPage = PageHelper.startPage(page, pageSize);
    //    List<YsSonTaskPageListVO> list = ysSonTaskMapper.selectSonTaskPagelist(status, teamUserId, sonTaskName,userId,masterIds);
    //    if (list != null && !list.isEmpty()) {
    //        list.forEach(x -> {
    //            List<YsMasterFile> masterFiles = ysMasterFileMapper.selectAllByYsSonTaskId(x.getId());
    //            for (YsMasterFile masterFile : masterFiles) {
    //                x.setYsFileTypeId(masterFile.getYsFileTypeId());
    //            }
    //            x.setButtonStatus(1);
    //            if (masterFiles != null && !masterFiles.isEmpty()) {
    //                x.setButtonStatus(0);
    //            }
    //        });
    //    }
    //
    //    BaseVO<YsSonTaskPageListVO> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
    //    return Callback.success(baseVO);
    //
    //}

    // 子任务改为已完成状态
    public Callback updateSonTaskStatus(Integer sonTaskId) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(currentUserVO.getRoleCode()) || "executor".equals(currentUserVO.getRoleCode())) {
            YsSonTask ysSonTask = null;
            // 查询子任务是不是本人认领的
            if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
                ysSonTask = ysSonTaskMapper.selectByIdAndReceiveId(sonTaskId, userVo.getId());
                if (ysSonTask == null) {
                    return Callback.error(2,"您无权限操作该子任务");
                }
            }
            // 查询执行人是不是该子任务的
            if ("executor".equals(currentUserVO.getRoleCode())){
                ysSonTask = ysSonTaskMapper.selectByIdAndExecutorId(sonTaskId, userVo.getId());
                if (ysSonTask == null) {
                    return Callback.error(2,"您无权限操作该子任务");
                }
            }

            if (sonTaskId != null) {
                if (ysSonTask.getStatus().equals(0) || ysSonTask.getStatus().equals(1)) {
                    ysSonTask.setStatus(2);
                    ysSonTask.setUpdateTime(LocalDateTime.now());
                    int count = ysSonTaskMapper.updateByPrimaryKeySelective(ysSonTask);
                    if (count > 0) {
                        return Callback.success(true);
                    }
                }
            }
        }
        return Callback.error(2, "状态修改失败!");
    }


    //public Callback batchInsertOrUpdateWorkTime(WorkTimeAddAndUpdateVO workTimeAddAndUpdateVO) {
    //    //, List<YsWorkTimeVO> list,Integer ysSonTaskId
    //    List<YsWorkTimeVO> list = workTimeAddAndUpdateVO.getList();
    //    String token = req.getHeader("Authorization");
    //    UserVO userVo = JWTUtil.getUserVOByToken(token);
    //    CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
    //    if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
    //        YsSonTask ysSonTask = ysSonTaskMapper.selectByPrimaryKey(workTimeAddAndUpdateVO.getYsSonTaskId());
    //        // 认领人的任务报工
    //        if (userVo.getId().equals(ysSonTask.getReceiveId())) {
    //            // 一组拥用同一个子任务ID
    //            if (workTimeAddAndUpdateVO.getYsSonTaskId() != null) {
    //                // 预存上次消耗剩余
    //                Integer temp = null;
    //                YsWorkTime ysWorkTimes = ysWorkTimeMapper.selectOneByYsSonTaskId(ysSonTask.getId());
    //                for (int i = 0; i < list.size(); i++) {
    //                    int totalConsume = list.get(i).getTotalConsume() == null ? 0 : list.get(i).getTotalConsume();
    //                    int surplus = list.get(i).getSurplus() == null ? 0 : list.get(i).getSurplus();
    //                    int estimateWorkTime = ysSonTask.getEstimateWorkTime() == null ? 0 : ysSonTask.getEstimateWorkTime();
    //                    boolean b1 = estimateWorkTime >= totalConsume;
    //                    boolean b2 = estimateWorkTime >= surplus;
    //                    if (!b1) {
    //                        return Callback.error(2, "总计消耗不能大于该子任务预计工时！");
    //                    }
    //                    if (!b2) {
    //                        return Callback.error(2, "总计剩余不能大于该子任务预计工时！");
    //                    }
    //
    //                    if (ysWorkTimes == null) {
    //                        boolean b;
    //                        if (i == 0) {
    //                            b = estimateWorkTime == totalConsume + surplus;
    //                        } else {
    //                            b = temp == totalConsume + surplus;
    //                        }
    //                        temp = surplus;
    //                        if (!b) {
    //                            return Callback.error(2, "消耗+剩余不能超过子任务总工时; 总消耗不能超过子任务总工时");
    //                        }
    //                    } else {
    //                        boolean b;
    //                        if (i == 0) {
    //                            //b = ysWorkTimes.getSurplus() == totalConsume + surplus;
    //                            b = ysSonTask.getEstimateWorkTime()== totalConsume + surplus;
    //                        } else {
    //                            b = temp == totalConsume + surplus;
    //                        }
    //                        temp = surplus;
    //                        if (!b) {
    //                            return Callback.error(2, "总消耗不能超过子任务总工时; 剩余不能超过子任务总工时;");
    //                        }
    //                    }
    //                }
    //                Integer sumTotalConsume = list.stream().map(YsWorkTimeVO::getTotalConsume).reduce(0, Integer::sum);if (ysSonTask.getEstimateWorkTime() >= sumTotalConsume) {
    //                    list.forEach(x -> {
    //                        YsWorkTime workTime = new YsWorkTime();
    //                        BeanUtils.copyProperties(x, workTime);
    //                        if (x.getId() == null) {
    //                            workTime.setReceiveId(ysSonTask.getReceiveId());//认领人
    //                            workTime.setExecutorId(ysSonTask.getYsTeamId());//执行人
    //                            workTime.setCreateTime(LocalDateTime.now());
    //                            workTime.setYsSonTaskId(ysSonTask.getId());
    //                            ysWorkTimeMapper.insertSelective(workTime);
    //                        } else {
    //                            workTime.setUpdateTime(LocalDateTime.now());
    //                            ysWorkTimeMapper.updateByPrimaryKeySelective(workTime);
    //                        }
    //                    });
    //                    // 总消耗 == 子任务工时 ，状态改为已完成
    //                    if (ysSonTask.getEstimateWorkTime().equals(sumTotalConsume)) {
    //                        YsSonTask completeStatus = YsSonTask.builder().id(ysSonTask.getId()).status(2).build();
    //                        ysSonTaskMapper.updateByPrimaryKeySelective(completeStatus);
    //                        return Callback.success(true);
    //                    }
    //                    if (ysSonTask.getStatus() != 1) {
    //                        // 新增时，更改子任务的状态：进行中
    //                        YsSonTask progressStatus = YsSonTask.builder().id(workTimeAddAndUpdateVO.getYsSonTaskId()).status(1).build();
    //                        ysSonTaskMapper.updateByPrimaryKeySelective(progressStatus);
    //                        return Callback.success(true);
    //                    }
    //                    return Callback.success(true);
    //                }
    //            }
    //        }
    //    }
    //    return Callback.error(2, "批量新增失败!");
    //}


    public Callback saveWorkTime(WorkTimeAddAndUpdateVO workTimeAddAndUpdateVO) {
        List<YsWorkTimeVO> list = workTimeAddAndUpdateVO.getList();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {

            if (workTimeAddAndUpdateVO.getYsSonTaskId() != null) {
                YsSonTask ysSonTask = ysSonTaskMapper.selectByPrimaryKey(workTimeAddAndUpdateVO.getYsSonTaskId());
                // 认领人的任务报工
                if (userVo.getId().equals(ysSonTask.getReceiveId())) {
                    // 预存上次消耗剩余
                    Integer temp = null;
                    // 获取最后一条的工时
                    YsWorkTime ysWorkTimes = ysWorkTimeMapper.selectOneByYsSonTaskId(ysSonTask.getId());
                    for (int i = 0; i < list.size(); i++) {
                        int totalConsume = list.get(i).getTotalConsume() == null ? 0 : list.get(i).getTotalConsume();
                        int surplus = list.get(i).getSurplus() == null ? 0 : list.get(i).getSurplus();
                        int estimateWorkTime = ysSonTask.getEstimateWorkTime() == null ? 0 : ysSonTask.getEstimateWorkTime();
                        boolean b1 = estimateWorkTime >= totalConsume;
                        boolean b2 = estimateWorkTime >= surplus;
                        if (!b1) {
                            return Callback.error(2, "总计消耗不能大于该子任务预计工时！");
                        }
                        if (!b2) {
                            return Callback.error(2, "总计剩余不能大于该子任务预计工时！");
                        }

                        // 判断数据库中没记录
                        if (ysWorkTimes == null) {
                            boolean b;
                            if (i == 0) {
                                b = estimateWorkTime == totalConsume + surplus;
                            } else {
                                b = temp == totalConsume + surplus;
                            }
                            temp = surplus;
                            if (!b) {
                                return Callback.error(2, "消耗+剩余不能超过子任务总工时; 总消耗不能超过子任务总工时");
                            }
                        } else {
                            boolean b;
                            if (i == 0) {
                                b = ysWorkTimes.getSurplus() == totalConsume + surplus;
                            } else {
                                b = temp == totalConsume + surplus;
                            }
                            temp = surplus;
                            if (!b) {
                                return Callback.error(2, "总消耗不能超过子任务总工时; 剩余不能超过子任务总工时;");
                            }
                        }
                    }
                    Integer sumTotalConsume = list.stream().map(YsWorkTimeVO::getTotalConsume).reduce(0, Integer::sum);
                    if (ysSonTask.getEstimateWorkTime() >= sumTotalConsume) {
                        list.forEach(x -> {
                            YsWorkTime workTime = new YsWorkTime();
                            BeanUtils.copyProperties(x, workTime);
                            workTime.setReceiveId(ysSonTask.getReceiveId());//认领人
                            workTime.setExecutorId(ysSonTask.getYsTeamId());//执行人
                            workTime.setCreateTime(LocalDateTime.now());
                            workTime.setYsSonTaskId(ysSonTask.getId());
                            ysWorkTimeMapper.insertSelective(workTime);
                        });
                        if (ysSonTask.getStatus() != 1) {
                            // 新增时，更改子任务的状态：进行中
                            YsSonTask progressStatus = new YsSonTask();
                            progressStatus.setId(ysSonTask.getId());
                            progressStatus.setStatus(1);
                            //sumToTalConsume如果等于总工时，则更改为已完成
                            if (ysSonTask.getEstimateWorkTime().equals(sumTotalConsume)) {
                                progressStatus.setStatus(2);
                            }
                            ysSonTaskMapper.updateByPrimaryKeySelective(progressStatus);
                            return Callback.success(true);
                        }

                        ysWorkTimeMapper.selectAllByYsSonTaskId(ysSonTask.getId()).forEach(x -> {
                            if (x.getSurplus() == 0) {
                                // 更改子任务状态：完成
                                YsSonTask completeStatus = YsSonTask.builder().id(ysSonTask.getId()).status(2).build();
                                ysSonTaskMapper.updateByPrimaryKeySelective(completeStatus);
                            }
                        });
                    } else {
                        return Callback.error(2, "总计消耗，不能超过子任务计划工时");
                    }
                    return Callback.success(true);
                }
            }
        }
        return Callback.error(2, "批量新增失败!");
    }


    public Callback updateWorkTime(WorkTimeAddAndUpdateVO workTimeAddAndUpdateVO) {
        List<YsWorkTimeVO> list = workTimeAddAndUpdateVO.getList();
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            if (workTimeAddAndUpdateVO.getYsSonTaskId() != null) {
                YsSonTask ysSonTask = ysSonTaskMapper.selectByPrimaryKey(workTimeAddAndUpdateVO.getYsSonTaskId());
                if (userVo.getId().equals(ysSonTask.getReceiveId())) {
                    YsWorkTimeVO ysWorkTimeVO = new YsWorkTimeVO();
                    if (CollectionUtils.isNotEmpty(list)) {
                        int size = list.size();
                        if (size > 1) {
                            return Callback.error(2, "不支持批量修改！");
                        }
                        for (int i = 0; i < list.size(); i++) {
                            int totalConsume = list.get(i).getTotalConsume() == null ? 0 : list.get(i).getTotalConsume();
                            int surplus = list.get(i).getSurplus() == null ? 0 : list.get(i).getSurplus();
                            int estimateWorkTime = ysSonTask.getEstimateWorkTime() == null ? 0 : ysSonTask.getEstimateWorkTime();
                            boolean b1 = estimateWorkTime >= totalConsume;
                            boolean b2 = estimateWorkTime >= surplus;
                            if (!b1) {
                                return Callback.error(2, "总计消耗不能大于该子任务预计工时！");
                            }
                            if (!b2) {
                                return Callback.error(2, "总计剩余不能大于该子任务预计工时！");
                            }
                            ysWorkTimeVO = list.get(i);
                        }
                    }
                    HashMap<String, Integer> map = Maps.newHashMap();
                    List<YsWorkTime> ysWorkTimes = ysWorkTimeMapper.selectAllByYsSonTaskId(ysSonTask.getId());
                    Integer finalId = ysWorkTimeVO.getId();
                    Optional<YsWorkTime> pgupOp = null;
                    List<YsWorkTime> pgdnList = Lists.newArrayList();
                    Integer upSurplus = null;
                    if (CollectionUtils.isNotEmpty(ysWorkTimes)) {
                        Stream.iterate(0, i -> i + 1).limit(ysWorkTimes.size()).forEach(i -> {
                            if (finalId.equals(ysWorkTimes.get(i).getId())) {
                                map.put("subscript", i);
                                if (i != 0) {
                                    map.put("pgup", i - 1);
                                }
                                map.put("pgdn", i + 1);
                            }
                        });
                        if (MapUtils.isNotEmpty(map)) {
                            if (null != map.get("pgup")) {
                                Integer pgup = map.get("pgup");
                                pgupOp = Stream.iterate(0, i -> i + 1).limit(ysWorkTimes.size()).filter(i -> i.equals(pgup)).map(i -> ysWorkTimes.get(i)).findFirst();
                                if (pgupOp.isPresent()) {
                                    upSurplus = pgupOp.get().getSurplus();
                                }
                            }
                        }
                        if (MapUtils.isNotEmpty(map)) {
                            if (null != map.get("pgdn") && null != map.get("subscript")) {
                                Integer subscript = map.get("subscript");
                                pgdnList = Stream.iterate(0, i -> i + 1).limit(ysWorkTimes.size()).filter(i -> i > subscript).map(i -> ysWorkTimes.get(i)).collect(Collectors.toList());

                            }
                        }
                        upSurplus = upSurplus == null ? ysSonTask.getEstimateWorkTime() : upSurplus;
                        Integer sum1 = ysWorkTimeVO.getTotalConsume() + ysWorkTimeVO.getSurplus();
                        if (!upSurplus.equals(sum1)) {
                            return Callback.error(2, "消耗+剩余，不能超过上一条的剩余；如果是第一条，不能超过子任务计划工时");
                        }
                        YsWorkTime oldWorkTime = ysWorkTimeMapper.selectByPrimaryKey(ysWorkTimeVO.getId());
                        int difference = ysWorkTimeVO.getTotalConsume() - oldWorkTime.getTotalConsume();
                        if (MapUtils.isNotEmpty(map)) {
                            if (null != map.get("pgdn")) {
                                if (CollectionUtils.isNotEmpty(pgdnList)) {
                                    pgdnList.forEach(x -> {
                                        x.setUpdateTime(LocalDateTime.now());
                                        x.setSurplus(x.getSurplus() - difference);
                                    });
                                    if (pgdnList.get(pgdnList.size() - 1).getSurplus() < 0) {
                                        return Callback.error(2, "最后一条记录，剩余工时不能为负数！");
                                    }
                                    pgdnList.forEach(x -> {
                                        ysWorkTimeMapper.updateByPrimaryKey(x);
                                    });
                                }
                            }
                        }
                        YsWorkTime ysWorkTime = new YsWorkTime();
                        BeanUtils.copyProperties(ysWorkTimeVO, ysWorkTime);
                        ysWorkTime.setYsSonTaskId(ysSonTask.getId());
                        ysWorkTime.setReceiveId(oldWorkTime.getReceiveId());
                        ysWorkTime.setExecutorId(oldWorkTime.getExecutorId());
                        ysWorkTime.setCreateTime(oldWorkTime.getCreateTime());
                        ysWorkTime.setRemark(oldWorkTime.getRemark());
                        ysWorkTime.setUpdateTime(LocalDateTime.now());
                        ysWorkTimeMapper.updateByPrimaryKey(ysWorkTime);
                    }
                    List<YsWorkTime> newWorkTimes = ysWorkTimeMapper.selectAllByYsSonTaskId(ysSonTask.getId());
                    Integer sumTotalConsume = newWorkTimes.stream().map(YsWorkTime::getTotalConsume).reduce(0, Integer::sum);
                    if (ysSonTask.getEstimateWorkTime().equals(sumTotalConsume)) {
                        YsSonTask completeStatus = YsSonTask.builder().id(ysSonTask.getId()).status(2).build();
                        ysSonTaskMapper.updateByPrimaryKeySelective(completeStatus);
                        return Callback.success(true);
                    } else {
                        YsSonTask progressStatus = YsSonTask.builder().id(workTimeAddAndUpdateVO.getYsSonTaskId()).status(1).build();
                        ysSonTaskMapper.updateByPrimaryKeySelective(progressStatus);
                        return Callback.success(true);
                    }
                }
            }
        }
        return Callback.error(2, "批量新增失败!");
    }


    public Callback<List<YsWorkTime>> selectAllByYsSonTaskId(Integer ysSonTaskId) {
        if (ysSonTaskId != null) {
            List<YsWorkTime> list = ysWorkTimeMapper.selectAllByYsSonTaskId(ysSonTaskId);
            return Callback.success(list);
        }
        return Callback.error(2, "查询失败!");
    }


//    public Callback batchDeleteByPrimaryKey(List<Integer> ids) {
//        String token = req.getHeader("Authorization");
//        UserVO userVo = JWTUtil.getUserVOByToken(token);
//        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
//        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
//            if (ids != null && !ids.isEmpty()) {
//                int count = ysWorkTimeMapper.batchDeleteByPrimaryKey(ids);
//                if (count > 0) {
//                    return Callback.success(true);
//                }
//            }
//        }
//        return Callback.error(2, "删除失败!");
//    }

    public Callback batchDeleteByPrimaryKey(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            if (id != null) {
                // 当前删除的报工工时数据
                YsWorkTime ysWorkTime = ysWorkTimeMapper.selectByPrimaryKey(id);
                List<YsWorkTime> ysWorkTimes = ysWorkTimeMapper.selectAllByYsSonTaskId(ysWorkTime.getYsSonTaskId());
                // 删除工时id的下标
                AtomicInteger idi = new AtomicInteger();
                Stream.iterate(0, i -> i + 1).limit(ysWorkTimes.size()).forEach(i -> {
                    if (ysWorkTime.equals(ysWorkTimes.get(i))) {
                        ;
                        idi.set(i);
                    }
                });
                // 删除工时的下标后所有报工剩余工时
                List<YsWorkTime> ysWorkTimeSurplus = Stream.iterate(0, i -> i + 1).limit(ysWorkTimes.size()).filter(i -> i > idi.get()).map(i -> ysWorkTimes.get(i)).collect(Collectors.toList());
                // 删除当前报工工时，下边还有报工工时
                if (ysWorkTimeSurplus.size() > 0) {
                    // 需要修改的删除后的报工工时
                    List<YsWorkTime> surplusTime = new ArrayList<>();
                    for (YsWorkTime surpluss : ysWorkTimeSurplus) {
                        YsWorkTime surplusTimes = new YsWorkTime();
                        Integer surplus = surpluss.getSurplus();
                        surplus += ysWorkTime.getTotalConsume();
                        surplusTimes.setId(surpluss.getId());
                        surplusTimes.setYsSonTaskId(surpluss.getYsSonTaskId());
                        surplusTimes.setTakeTime(surpluss.getTakeTime());
                        surplusTimes.setTotalConsume(surpluss.getTotalConsume());
                        surplusTimes.setSurplus(surplus);
                        surplusTimes.setRemark(surpluss.getRemark());
                        surplusTimes.setReceiveId(surpluss.getReceiveId());
                        surplusTimes.setExecutorId(surpluss.getExecutorId());
                        surplusTimes.setCreateTime(surpluss.getCreateTime());
                        surplusTimes.setUpdateTime(LocalDateTime.now());
                        surplusTime.add(surplusTimes);
                    }
                    int i = ysWorkTimeMapper.updateBatch(surplusTime);
                    if (i > 0) {
                        int count = ysWorkTimeMapper.batchDeleteByPrimaryKey(Collections.singletonList(id));
                        if (count > 0) {
                            return Callback.success(true);
                        }
                    }
                } else {
                    int count = ysWorkTimeMapper.batchDeleteByPrimaryKey(Collections.singletonList(id));
                    if (count > 0) {
                        return Callback.success(true);
                    }
                }
            }
        }
        return Callback.error(2, "删除失败!");
    }

    public Callback<List<LinkedHashMap>> selectJobTitleGroup(Integer masterTaskId) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        // 非子任务管理角色，可查看所有
        List<LinkedHashMap> list = new ArrayList<>();
        Set<Integer> userList = Sets.newHashSet();
        Set<Integer> masterIds = Sets.newHashSet();
        masterIds.add(masterTaskId);
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            Integer userId = userVo.getId();
            userList.add(userId);
        }
        // 子任务团队成员，可以查看主任务下，所有团队成员构成
        if ("executor".equals(currentUserVO.getRoleCode())) {
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByYsMasterTaskIdAndExecutorId(userVo.getId(), masterTaskId);
            if (CollectionUtils.isEmpty(ysTeams)) {
                log.info("团队成员的上级人员，无认领的子任务");
                return Callback.success(list);
            }
            masterIds = ysTeams.stream().map(YsTeam::getYsMasterTaskId).collect(Collectors.toSet());
        }
        userList.removeIf(Objects::isNull);
        masterIds.removeIf(Objects::isNull);
        UserMasterDTO dto = UserMasterDTO.builder().userList(userList).masterIds(masterIds).build();
        // 更改后，只根据主任务ID查询
        if (masterTaskId != null) {
            list = ysTeamMapper.selectTeamGroup(dto);
        }
        return Callback.success(list);
    }


    //public Callback<List<WorkTimeChartVO>> selectTeamWokeTimeGroup(Integer masterTaskId) {
    //    String token = req.getHeader("Authorization");
    //    UserVO userVo = JWTUtil.getUserVOByToken(token);
    //    assert userVo != null;
    //    CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
    //    // 非子任务管理角色，可查看所有
    //    Set<Integer> userList = Sets.newHashSet();
    //    Set<Integer> masterIds = Sets.newHashSet();
    //    masterIds.add(masterTaskId);
    //    if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
    //        Integer userId = userVo.getId();
    //        userList.add(userId);
    //    }
    //    // 执行人可根据某个主任务，查询所有的执行人工时占比
    //    if ("executor".equals(currentUserVO.getRoleCode())) {
    //        List<YsTeam> ysTeams = ysTeamMapper.selectAllByYsMasterTaskIdAndExecutorId(userVo.getId(), masterTaskId);
    //        if (CollectionUtils.isEmpty(ysTeams)) {
    //            log.info("没有子任务，无法查询工时占比");
    //            List<WorkTimeChartVO> newList = new ArrayList<>();
    //            return Callback.success(newList);
    //        }
    //        // userList = ysTeams.stream().map(YsTeam::getUserId).collect(Collectors.toSet());
    //        masterIds = ysTeams.stream().map(YsTeam::getYsMasterTaskId).collect(Collectors.toSet());
    //    }
    //    userList.removeIf(Objects::isNull);
    //    masterIds.removeIf(Objects::isNull);
    //    UserMasterDTO dto = UserMasterDTO.builder().userList(userList).masterIds(masterIds).build();
    //    List<WorkTimeChartVO> resultlist = new ArrayList<>();
    //    List<YsSonTaskPageListVO> list = ysSonTaskMapper.selectSonTaskPagelist(null, null, null, dto, null);
    //    Map<Integer, List<YsSonTaskPageListVO>> listMap = list.stream().collect(Collectors.groupingBy(YsSonTaskPageListVO::getTeamUserId));
    //
    //    Integer finalReduce = list.stream().map(x -> x.getTotalConsume() == null ? 0 : x.getTotalConsume()).reduce(Integer::sum).orElse(0);
    //    resultlist = listMap.entrySet().stream().map(key -> {
    //        Integer teamId = key.getKey();
    //        WorkTimeChartVO chartVO = null;
    //        List<YsSonTaskPageListVO> value = key.getValue();
    //        if (value != null && !value.isEmpty()) {
    //            int totalConsume = key.getValue().stream().mapToInt(x -> x.getTotalConsume() == null ? 0 : x.getTotalConsume()).sum();
    //            String executorName = key.getValue().stream().findFirst().map(YsSonTaskPageListVO::getUsername).orElse(null);
    //            int ratio = 0;
    //            if (finalReduce != 0) {
    //                BigDecimal multiply = new BigDecimal(totalConsume).divide(new BigDecimal(finalReduce), 2, RoundingMode.HALF_UP)
    //                        .multiply(new BigDecimal(100));
    //                ratio = multiply.intValue();
    //            }
    //            chartVO = WorkTimeChartVO.builder().executorName(executorName).ratio(ratio).sumConsume(totalConsume)
    //                    .total(finalReduce).executorId(teamId).build();
    //        }
    //        return chartVO;
    //    }).collect(Collectors.toList());
    //    return Callback.success(resultlist);
    //
    //
    //    //String token = req.getHeader("Authorization");
    //    //UserVO userVo = JWTUtil.getUserVOByToken(token);
    //    //assert userVo != null;
    //    //CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
    //    //Integer userId = null;
    //    //if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
    //    //    userId = currentUserVO.getUserId();
    //    //}
    //    //
    //    //List<WorkTimeChartVO> resultlist = new ArrayList<>();
    //    //List<TeamDTO> dtoList = ysTeamMapper.selectTeamDtoByMasterId(userId, masterTaskId);
    //    //if (!dtoList.isEmpty()) {
    //    //    List<Integer> sonlist = dtoList.stream().map(TeamDTO::getSonId).collect(Collectors.toList());
    //    //    List<Integer> teamIdlist = dtoList.stream().map(TeamDTO::getYsTeamId).collect(Collectors.toList());
    //    //    //查出所有工时
    //    //    List<YsWorkTimeDTO> ysWorkTimeDTOS = ysWorkTimeMapper.selectAllByYsSonTaskIdInAndExecutorIdIn(sonlist, teamIdlist);
    //    //    if (!ysWorkTimeDTOS.isEmpty()) {
    //    //        //总工时
    //    //        Integer reduce = ysWorkTimeDTOS.stream().map(YsWorkTimeDTO::getTotalConsume).reduce(0, (a, b) -> a + b);
    //    //        Map<Integer, List<YsWorkTimeDTO>> listMap = ysWorkTimeDTOS.stream().collect(Collectors.groupingBy(vos -> {
    //    //            return vos.getExecutorId();
    //    //        }));
    //    //        resultlist = listMap.entrySet().stream().map(key -> {
    //    //            Integer teamId = key.getKey();
    //    //            WorkTimeChartVO chartVO = null;
    //    //            List<YsWorkTimeDTO> value = key.getValue();
    //    //            if (value.size() > 0) {
    //    //                Integer totalConsume = key.getValue().stream().mapToInt(YsWorkTimeDTO::getTotalConsume).sum();
    //    //                String executorName = key.getValue().stream().findFirst().map(YsWorkTimeDTO::getExecutorName).get();
    //    //
    //    //                BigDecimal multiply = new BigDecimal(totalConsume).divide(new BigDecimal(reduce), 2, BigDecimal.ROUND_HALF_UP)
    //    //                        .multiply(new BigDecimal(100));
    //    //                Integer ratio = multiply.intValue();
    //    //                chartVO = WorkTimeChartVO.builder().executorName(executorName).ratio(ratio).sumConsume(totalConsume)
    //    //                        .total(reduce).executorId(teamId).build();
    //    //            }
    //    //            return chartVO;
    //    //        }).collect(Collectors.toList());
    //    //    }
    //    //}
    //    //return Callback.success(resultlist);
    //}

    //
    //public Callback selectTeamRatio(Integer masterTaskId) {
    //
    //    String token = req.getHeader("Authorization");
    //    UserVO userVo = JWTUtil.getUserVOByToken(token);
    //    assert userVo != null;
    //    CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
    //    // 非子任务管理角色，可查看所有
    //    Integer userId = null;
    //    if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
    //        userId = userVo.getId();
    //    }
    //    List<WorkTimeChartVO> resultlist = new ArrayList<>();
    //    List<YsSonTaskPageListVO> list = ysSonTaskMapper.selectSonTaskPagelist(null, null, null, userId, masterTaskId, null);
    //    Map<Integer, List<YsSonTaskPageListVO>> listMap = list.stream().collect(Collectors.groupingBy(vos -> {
    //        return vos.getTeamUserId();
    //    }));
    //
    //    Integer reduce = list.stream().map(YsSonTaskPageListVO::getTotalConsume).reduce(0, (a, b) -> a + b);
    //
    //    resultlist = listMap.entrySet().stream().map(key -> {
    //        Integer teamId = key.getKey();
    //        WorkTimeChartVO chartVO = null;
    //        List<YsSonTaskPageListVO> value = key.getValue();
    //        if (value.size() > 0) {
    //            Integer totalConsume = key.getValue().stream().mapToInt(YsSonTaskPageListVO::getTotalConsume).sum();
    //            String executorName = key.getValue().stream().findFirst().map(YsSonTaskPageListVO::getUsername).get();
    //
    //            BigDecimal multiply = new BigDecimal(totalConsume).divide(new BigDecimal(reduce), 2, BigDecimal.ROUND_HALF_UP)
    //                    .multiply(new BigDecimal(100));
    //            Integer ratio = multiply.intValue();
    //            chartVO = WorkTimeChartVO.builder().executorName(executorName).ratio(ratio).sumConsume(totalConsume)
    //                    .total(reduce).executorId(teamId).build();
    //        }
    //        return chartVO;
    //    }).collect(Collectors.toList());
    //    return Callback.success(resultlist);
    //}


    public Callback<EditSontTaskAndFileVO> selectSonTaskOne(Integer sonTaskId) {
        if (sonTaskId != null) {
            EditSontTaskAndFileVO sonTaskOne = ysSonTaskMapper.selectSonTaskOne(sonTaskId);
            if (sonTaskOne.getMasterFileId() != null) {
                List<FileVO> fileVos = ysSlaveFileMapper.selectByFileMasterIdlist(sonTaskOne.getMasterFileId());
                sonTaskOne.setFileVOList(fileVos);
            }
            return Callback.success(sonTaskOne);
        }
        return Callback.error(2, "查询失败!");
    }

    public Callback<WorkTimeResidueVO> selectMasterWorkTimeResidueByMasterTaskId(Integer masterTaskId) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        log.info("当前用户角色：{}", currentUserVO.getRoleCode());
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            WorkTimeResidueVO workTimeResidueVO = ysSonTaskMapper.selectEstimateHourByMasterTaskId(masterTaskId);
            log.info("Total：{}", workTimeResidueVO.getTotal() != null ? workTimeResidueVO.getTotal() : 0);
            if (workTimeResidueVO.getTotal() != null) {
                val workTimeResidueDTOS = ysSonTaskMapper.selectWorkTimeResidueDTOByMasterId(userVo.getId(), masterTaskId);
                log.info("workTimeResidueDTOS：{}", workTimeResidueDTOS != null ? workTimeResidueDTOS.size() : 0);
                log.info("workTimeResidueDTOS：{}", workTimeResidueDTOS.isEmpty() ? "空" : "不为空");
                if (workTimeResidueDTOS != null && !workTimeResidueDTOS.isEmpty()) {
                    int residueTimeSum = workTimeResidueDTOS.stream().filter(workTimeResidueDTO -> workTimeResidueDTO.getEwtime() != null).mapToInt(WorkTimeResidueDTO::getEwtime).sum();
                    log.info("residueTimeSum：{}", residueTimeSum);
                    Integer residueTime = workTimeResidueVO.getTotal() - Integer.valueOf(residueTimeSum);
                    if (residueTime <= 0) {
                        workTimeResidueVO.setResidueTime(0);
                        return Callback.success(workTimeResidueVO);
                    }
                    workTimeResidueVO.setResidueTime(residueTime);
                } else {
                    //剩余工时 = 总工时
                    workTimeResidueVO.setResidueTime(workTimeResidueVO.getTotal());
                    return Callback.success(workTimeResidueVO);
                }
            }
            return Callback.success(workTimeResidueVO);
        }
        return Callback.error(2, "查询失败!");
    }


    public Callback selectGroupTeamIdByMasterTaskId(Integer ysMasterTaskId){
         String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());

        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            List<YsTaskReceive> ysTaskReceives = ysTaskReceiveMapper.selectByYsMasterTaskId(ysMasterTaskId);
            if (CollectionUtils.isEmpty(ysTaskReceives)){
                return Callback.error(2,"无权限查看此任务");
            }
        }

        if ("executor".equals(currentUserVO.getRoleCode())) {
            List<YsTeam> ysTeams = ysTeamMapper.selectAllByYsMasterTaskIdAndExecutorId(currentUserVO.getUserId(), ysMasterTaskId);
            if (CollectionUtils.isEmpty(ysTeams)){
                return Callback.error(2,"无权限查看此任务");
            }
        }


        if(ysMasterTaskId != null){
            List<MasterTeamGroupDTO> list = ysSonTaskMapper.selectGroupTeamIdByMasterTaskId(ysMasterTaskId);
            if(CollectionUtils.isNotEmpty(list)){
                int total = list.stream().mapToInt(MasterTeamGroupDTO::getCountNum).sum();
                List<TeamGroupVO> teamGroupVOS = Lists.newArrayList();
                list.forEach(x->{
                    int proportion = 0;
                    if (total != 0){
                         // 占比
                        BigDecimal decimal = new BigDecimal(x.getCountNum()).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        proportion = decimal.intValue();
                    }
                    String username = ysTeamMapper.selectUserNameByTeamId(x.getYsTeamId());
                    TeamGroupVO teamGroupVO = TeamGroupVO.builder().username(username).countNum(x.getCountNum()).proportion(proportion).build();
                    teamGroupVOS.add(teamGroupVO);
                });

                return Callback.success(teamGroupVOS);
            }
        }

        return Callback.error(2,"查询失败!");
    }


    /**
     * 子任务状态为未开始，点击开始按钮，子任务状态改为进行中
     */
    public Callback updateStatusBySonTaskId(Integer sonTaskId) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        log.info("当前用户角色：{}", currentUserVO.getRoleCode());
        if(!"executor".equals(currentUserVO.getRoleCode())){
            YsSonTask ysSonTask = ysSonTaskMapper.selectByPrimaryKey(sonTaskId);
            if(ysSonTask != null){
                YsTeam ysTeam = ysTeamMapper.selectByPrimaryKey(ysSonTask.getYsTeamId());
                if(ysTeam != null){
                    if(!currentUserVO.getUserId().equals(ysTeam.getExecutorId())){
                        return Callback.error(2,"无权限操作此任务");
                    }
                    if (ysSonTask.getStatus() == 0){
                        ysSonTask.setStatus(1);
                        ysSonTaskMapper.updateByPrimaryKeySelective(ysSonTask);
                        return Callback.success("操作成功");
                    }
                }
            }
        }
        if ("executor".equals(currentUserVO.getRoleCode())) {
            YsSonTask ysSonTask = ysSonTaskMapper.selectByIdAndExecutorId(sonTaskId, currentUserVO.getUserId());
            if (ysSonTask == null) {
                return Callback.error(2, "无权限查看此任务");
            }
            if (ysSonTask.getStatus() == 0) {
                ysSonTask.setStatus(1);
                ysSonTaskMapper.updateByPrimaryKeySelective(ysSonTask);
                return Callback.success();
            }else{
                return Callback.error(2, "只有未开始的任务才能开始");
            }
        }
        return Callback.error(2, "更新状态失败！");
    }


}
