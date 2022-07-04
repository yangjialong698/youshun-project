package com.ennova.pubinfotask.service;

import cn.hutool.core.date.DateUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.*;
import com.ennova.pubinfotask.dto.FileDelDTO;
import com.ennova.pubinfotask.entity.*;
import com.ennova.pubinfotask.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/20
 * @Description: com.ennova.pubinfotask.service
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsMasterTaskService {

    private final YsMasterTaskMapper ysMasterTaskMapper;
    private final YsMasterFileMapper ysMasterFileMapper;
    private final YsDivertTaskMapper ysDivertTaskMapper;
    private final YsTaskReceiveMapper ysTaskReceiveMapper;
    private final YsSonTaskMapper ysSonTaskMapper;
    private final YsSlaveFileMapper ysSlaveFileMapper;
    private final UserMapper userMapper;
    private final HttpServletRequest req;
    private final YsTeamMapper ysTeamMapper;
    private final YsTaskPlanMapper ysTaskPlanMapper;

    /**
     * 本地路径
     */
    @Value("${spring.upload.local.path}")
    private String localPath;

    /**
     * 访问url
     */
    @Value("${spring.upload.local.url}")
    private String localUrl;

    /**
     * 支持文件
     */
    @Value("${file.suffix}")
    private String[] fileSuffix;


    public Callback insertOrUpdate(YsMasterTaskVO record) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if (!"task_manage".equals(currentUserVO.getRoleCode())) {
            return Callback.error(2, "不是任务管理员，不能创建或修改主任务");
        }

        YsMasterTask ysMasterTask = new YsMasterTask();
        ysMasterTask.setCost(null);
        boolean notBlank = StringUtils.isNotBlank(record.getCost());
        if (notBlank) {
            boolean creatable = NumberUtils.isCreatable(record.getCost());
            if (creatable) {
                ysMasterTask.setCost(new Double(record.getCost()));
            }
        }
        BeanUtils.copyProperties(record, ysMasterTask);
        ysMasterTask.setUserId(userVo.getId());//标记发布ID
        ysMasterTask.setStatus(0); // 未发布状态
        String formatTime = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        // 更新
        if (record.getId() != null) {
            YsMasterTask task = ysMasterTaskMapper.selectByPrimaryKey(record.getId());
            if (task != null) {
                ysMasterTask.setUpdateTime(LocalDateTime.now());
                List<YsMasterTask> ysMasterTasks = ysMasterTaskMapper.selectByIdAndName(ysMasterTask.getId(), ysMasterTask.getName());
                if (CollectionUtils.isNotEmpty(ysMasterTasks)) {
                    return Callback.error(2, "主任务名称不能重复");
                }
                // 更新主任务
                ysMasterTask.setId(task.getId());
                ysMasterTask.setSerialNumber(task.getSerialNumber());
                ysMasterTask.setName(task.getName());
                ysMasterTask.setCreateTime(task.getCreateTime());
                ysMasterTaskMapper.updateByPrimaryKey(ysMasterTask);
                //ysMasterTaskMapper.updateByPrimaryKeySelective(ysMasterTask);
                List<FileVO> fileVOList = record.getFileVOList();
                if (fileVOList != null && !fileVOList.isEmpty()) {
                    YsMasterFile master = new YsMasterFile();
                    YsMasterFile ysMasterFile = ysMasterFileMapper.selectByPrimaryKey(task.getYsMasterFileId());
                    if (ysMasterFile != null) {
                        master = YsMasterFile.builder().id(record.getFileMasterId()).fileName(record.getName()).openFile(0) // 默认不对外开放
                                .ysFileTypeId(record.getYsFileTypeId()).delFlag(0).fileContent(record.getFileContent()).versionNo(record.getVersionNo())
                                .ysMasterTaskId(ysMasterTask.getId()).userId(userVo.getId()).updateTime(LocalDateTime.now()).build();
                        ysMasterFileMapper.updateByPrimaryKeySelective(master);
                    } else {
                        // 可能没有创建过
                        master = YsMasterFile.builder().serialNumber(record.getFilePrefix() + formatTime).fileName(record.getName()).openFile(0) // 默认不对外开放
                                .ysFileTypeId(record.getYsFileTypeId()).createTime(LocalDateTime.now()).delFlag(0).fileContent(record.getFileContent()).versionNo(record.getVersionNo())
                                .ysMasterTaskId(ysMasterTask.getId()).userId(userVo.getId()).build();
                        ysMasterFileMapper.insertSelective(master);

                    }

                    if (ysMasterFile == null) {
                        YsMasterTask masterTask = new YsMasterTask();
                        masterTask.setId(ysMasterTask.getId());
                        masterTask.setYsMasterFileId(master.getId());
                        ysMasterTaskMapper.updateByPrimaryKeySelective(masterTask);
                    }

                    for (FileVO x : fileVOList) {
                        YsSlaveFile ysSlaveFile = YsSlaveFile.builder().id(x.getId()).updateTime(LocalDateTime.now()).fileMasterId(master.getId())
                                .updateTime(LocalDateTime.now()).build();
                        ysSlaveFileMapper.updateByPrimaryKeySelective(ysSlaveFile);
                    }
                }
                log.info("主任务更新成功!");
                return Callback.success(true);
            }
        } else {
            // 新增
            Integer number = ysMasterTaskMapper.selectLastSerialNumber();
            Integer serialNumber = getSerialNumber(number);
            ysMasterTask.setSerialNumber(serialNumber);
            ysMasterTask.setCreateTime(LocalDateTime.now());
            List<YsMasterTask> ysMasterTasks = ysMasterTaskMapper.selectByName(ysMasterTask.getName());
            if (CollectionUtils.isNotEmpty(ysMasterTasks)) {
                return Callback.error(2, "主任务名称不能重复");
            }
            ysMasterTaskMapper.insertSelective(ysMasterTask);
            // 附件不为空的时候，再新建封皮和附件
            List<FileVO> fileVOList = record.getFileVOList();
            if (fileVOList != null && !fileVOList.isEmpty()) {
                YsMasterFile master = YsMasterFile.builder().serialNumber(record.getFilePrefix() + formatTime).fileName(record.getName()).openFile(0) // 默认不对外开放
                        .ysFileTypeId(record.getYsFileTypeId()).createTime(LocalDateTime.now()).delFlag(0).fileContent(record.getFileContent()).versionNo(record.getVersionNo())
                        .ysMasterTaskId(ysMasterTask.getId()).userId(userVo.getId()).build();
                ysMasterFileMapper.insertSelective(master);

                YsMasterTask task = new YsMasterTask();
                task.setId(ysMasterTask.getId());
                task.setYsMasterFileId(master.getId());
                ysMasterTaskMapper.updateByPrimaryKeySelective(task);

                for (FileVO x : fileVOList) {
                    YsSlaveFile ysSlaveFile1 = ysSlaveFileMapper.selectByPrimaryKey(x.getId());
                    Optional.ofNullable(ysSlaveFile1).ifPresent(ysSlaveFile -> {
                        YsSlaveFile ysSlaveFile2 = YsSlaveFile.builder().id(x.getId()).updateTime(LocalDateTime.now()).fileMasterId(master.getId())
                                .updateTime(LocalDateTime.now()).build();
                        ysSlaveFileMapper.updateByPrimaryKeySelective(ysSlaveFile2);
                    });
                }

            }
            log.info("主任务新增成功!");
            return Callback.success(true);
        }
        return Callback.error(2, "数据处理失败!");
    }


    private Integer getSerialNumber(Integer serialNumber) {

        String dayStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        String[] dayArr = dayStr.split("-");

        String year = dayArr[0].substring(2, 4);
        String month = dayArr[1];
        if (serialNumber == null || serialNumber.equals(0)) {
            String newNumber = year + month + "001";
            return Integer.parseInt(newNumber);
        }

        String stringNum = String.valueOf(serialNumber);
        String year2 = stringNum.substring(0, 2);
        String month2 = stringNum.substring(2, 4);

        // 如果日期一致，相加
        if (year.equals(year2) && month.equals(month2)) {
            return serialNumber.intValue() + 1;
        }

        // 日期不一致，获取一个新的日期
        String newNumber = year + month + "001";
        return Integer.parseInt(newNumber);
    }


    public Callback<FileVO> uploadFile(MultipartFile file) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            log.info("上传文件大小为空!");
            return Callback.error(2, "上传文件不能为空!");
        }
        //String fileMd5 = FileUtils.getFileMd5(file, localPath);
        //List<YsSlaveFile> files = ysSlaveFileMapper.selectAllByFileMd5AndUserId(fileMd5, userVo.getId());
        //if (files != null && !files.isEmpty()) {
        //    return Callback.error(2, "当前用户已上传该文件，请确认是否需要重复上传");
        //}
        HashMap<String, String> map = FileUtils.uploadFile(file, localPath, fileSuffix);
        if (StringUtils.isNotBlank(map.get("error"))) {
            return Callback.error(2, map.get("error"));
        }
        String subname = map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");

        YsSlaveFile slave = YsSlaveFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(LocalDateTime.now()).build();
        int count = ysSlaveFileMapper.insertSelective(slave);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(slave.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "上传失败!");
    }

    public Callback deleteFile(FileDelDTO fileDelDTO) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        fileDelDTO.getFileVos().forEach(fileVo -> {
            String path = localPath + "/" + fileVo.getNewfileName();
            // 如果是本人上传的，才能执行删除操作
            assert userVo != null;
            List<YsSlaveFile> files = ysSlaveFileMapper.selectAllByFileMd5AndUserId(fileVo.getNewfileName(), userVo.getId());
            if (files != null && !files.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    //查看是否唯一
                    int count = ysSlaveFileMapper.selectByFileMd5(fileVo.getNewfileName());
                    if (count == 1) {
                        file.delete();
                    }
                    YsSlaveFile ysSlaveFile = ysSlaveFileMapper.selectByPrimaryKey(fileVo.getId());
                    ysSlaveFileMapper.deleteByPrimaryKey(fileVo.getId());
                    Optional.ofNullable(ysSlaveFile).ifPresent(x -> {
                                List<YsSlaveFile> list = ysSlaveFileMapper.selectAllByFileMasterId(x.getFileMasterId());
                                // 如果对应的附件表没有主任务记录，删除主任务封皮数据
                                if (list.isEmpty()) {
                                    ysMasterFileMapper.deleteByPrimaryKey(x.getFileMasterId());
                                }
                            }
                    );
                }
            }
        });
        return Callback.success(true);
    }


    public Callback<BaseVO<LinkedHashMap>> selectAll(Integer page, Integer pageSize) {

        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
        List<LinkedHashMap> list = ysMasterTaskMapper.selectAll();
        BaseVO<LinkedHashMap> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);

    }


    public Callback updateById(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("task_manage".equals(currentUserVO.getRoleCode())) {
            if (id != null) {
                YsMasterTask ysMasterTask = new YsMasterTask();
                ysMasterTask.setStatus(1); // 发布任务
                ysMasterTask.setId(id);
                ysMasterTask.setPublishDate(LocalDateTime.now()); // 发布日期
                ysMasterTask.setUpdateTime(LocalDateTime.now());
                int i = ysMasterTaskMapper.updateByPrimaryKeySelective(ysMasterTask);
                if (i > 0) {
                    return Callback.success(true);
                }
            }
        }
        return Callback.error(2, "发布失败");

    }

    // 认领
    public Callback receiveTask(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        //只有子任务管理员才可以领
        if ("sub_task_manage".equals(currentUserVO.getRoleCode())) {
            if (id != null) {
                YsMasterTask masterTask = ysMasterTaskMapper.selectByPrimaryKey(id);
                if (null != masterTask) {
                    if (masterTask.getStatus().equals(1)) {
                        //认领后状态为2
                        YsMasterTask task = YsMasterTask.builder().id(id).status(2).updateTime(LocalDateTime.now()).publishDate(LocalDateTime.now()).build();
                        ysMasterTaskMapper.updateByPrimaryKeySelective(task);
                        YsTaskReceive re = YsTaskReceive.builder().ysMasterTaskId(id).receiveId(2).receiveId(userVo.getId()).createTime(LocalDateTime.now()).build();
                        int count = ysTaskReceiveMapper.insert(re);
                        if (count > 0) {
                            return Callback.success(true);
                        }
                    }
                }
            }
        }

        return Callback.error(2, "认领失败");

    }


    // 完成
    public Callback completeTask(Integer id) {
        if (id != null) {
            String token = req.getHeader("Authorization");
            UserVO userVo = JWTUtil.getUserVOByToken(token);
            assert userVo != null;
            CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
            if (!"sub_task_manage".equals(currentUserVO.getRoleCode())) {
                return Callback.error(2, "不是子任务管理员，不能操作");
            }
            LinkedHashMap<String, Integer> map = ysMasterTaskMapper.selectStatusAndReceiveIdById(id);
            if (MapUtils.isNotEmpty(map)) {
                Integer receiveId = map.get("receiveId");
                if (receiveId != null) {
                    if (receiveId.equals(userVo.getId())) {
                        Integer status = map.get("status");
                        if (status != null) {
                            YsMasterTask masterTask = ysMasterTaskMapper.selectByPrimaryKey(id);
                            /* 已认领、进行中的可进行完成 **/
                            boolean flag = masterTask.getStatus().equals(2) || masterTask.getStatus().equals(3);
                            if (flag) {
                                YsMasterTask task = YsMasterTask.builder().id(id).status(4).updateTime(LocalDateTime.now()).build();
                                int count = ysMasterTaskMapper.updateByPrimaryKeySelective(task);
                                if (count > 0) {
                                    return Callback.success(true);
                                }
                            }
                            return Callback.error(2, "只有已认领、进行中的状态可进行完成");
                        }
                    }
                }
            }
        }

        return Callback.error(2, "更新失败!");
    }


    // 关闭
    public Callback closeTask(Integer id) {
        if (id == null) {
            return Callback.error(2, "id不能为空");
        }
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO userVO = userMapper.selectCurrentUser(userVo.getId());
        if ("task_manage".equals(userVO.getRoleCode())) {
            YsMasterTask masterTask = ysMasterTaskMapper.selectByPrimaryKey(id);
            /* 任务状态： 0- 未发布 1- 待认领  2- 未开始  3- 进行中 4- 已完成 5- 已关闭 **/
            List<Integer> stateliest = Arrays.asList(1, 2, 3, 4);
            if (stateliest.contains(masterTask.getStatus())) {
                YsMasterTask task = YsMasterTask.builder().id(id).status(5).updateTime(LocalDateTime.now()).build();
                int count = ysMasterTaskMapper.updateByPrimaryKeySelective(task);
                YsSonTask sonTask = YsSonTask.builder().status(3).ysMasterTaskId(id).updateTime(LocalDateTime.now()).build();
                // 主任务关闭，同时关闭子任务
                ysSonTaskMapper.updateByYsMasterTaskId(sonTask);
                if (count > 0) {
                    return Callback.success(true);
                }
            }

        }


        return Callback.error(2, "更新失败 ( 请使用task_manage角色关闭任务 ) ");
    }


    public Callback<LinkedHashMap> selectTaskDetailsOne(Integer id) {
        if (id == null) {
            return Callback.error(2, "id不为空");
        }
        LinkedHashMap map = ysMasterTaskMapper.selectTaskDetailsOne(id);
        if (map != null) {
            return Callback.success(map);
        }
        return Callback.error(2, "查询一条主任务详情失败!");
    }

    public Callback<EditMasterTaskAndFileVO> selectTaskAndFileOne(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if ("task_manage".equals(currentUserVO.getRoleCode())) {
            if (id != null) {
                EditMasterTaskAndFileVO vo = ysMasterTaskMapper.selectTaskAndFileOne(id);
                assert vo != null;
                YsMasterFile ysMasterFile = ysMasterFileMapper.selectByPrimaryKey(vo.getFileMasterId());
                // MasterFileDTO masterFileDTO = ysMasterFileMapper.selectByYsMasterTaskId(vo.getId());
                if (ysMasterFile != null) {
                    vo.setFileMasterId(ysMasterFile.getId());
                    vo.setYsFileTypeId(ysMasterFile.getYsFileTypeId());
                    if (vo.getFileMasterId() != null) {
                        List<FileVO> fileVOS = ysSlaveFileMapper.selectByFileMasterIdlist(vo.getFileMasterId());
                        vo.setFileVOList(fileVOS);
                    }
                }
                return Callback.success(vo);
            }
        }
        return Callback.error(2, "查询失败!");
    }

    public Callback taskHandOver(Integer id, Integer diverUserId, String handOver) {
        if (id == null || diverUserId == null || !StringUtils.isNotEmpty(handOver)) {
            return Callback.error(2, "移交失败!");
        }
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);

        LinkedHashMap<String, Integer> map = ysMasterTaskMapper.selectStatusAndReceiveIdById(id);
        if (MapUtils.isNotEmpty(map)) {
            Integer receiveId = map.get("receiveId");
            if (receiveId != null) {
                /** 判断是不是登陆人的任务  **/
                assert userVo != null;
                if (receiveId.equals(userVo.getId())) {
                    Integer status = map.get("status");
                    if (status != null) {
                        YsMasterTask masterTask = ysMasterTaskMapper.selectByPrimaryKey(id);
                        // 任务状态： 0- 未发布 1- 待认领  2- 未开始  3- 进行中 4- 已完成 5- 已关闭
                        boolean flag = masterTask.getStatus().equals(2) || masterTask.getStatus().equals(3) || masterTask.getStatus().equals(4) || masterTask.getStatus().equals(5);
                        if (flag) {
                            LocalDate handOverDate = LocalDate.parse(handOver, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            LocalDateTime handOverDateTime = handOverDate.atStartOfDay();
                            YsDivertTask divertTask = YsDivertTask.builder().divertUserId(diverUserId) /* 移交后ID*/
                                    .receiveId(userVo.getId())    /*移交前用户ID，从token中获取*/
                                    .ysMasterTaskId(id).divertTime(handOverDateTime).createTime(LocalDateTime.now()).build();
                            int divertCount = ysDivertTaskMapper.insert(divertTask);// 移交表
                            int receiveCout = ysTaskReceiveMapper.updateTaskReceiveByMastId(diverUserId, id);// 认领表
                            ysSonTaskMapper.updateSonTaskByMasterId(diverUserId, id);// 子任务
                            // 查询移交的任务计划
                            List<YsTaskPlan> ysTaskPlans = ysTaskPlanMapper.selectByReceiveIdAndYsMasterTaskId(userVo.getId(), id);
                            ysTaskPlans.forEach(x -> {
                                // 更新任务计划
                                x.setReceiveId(diverUserId);
                                ysTaskPlanMapper.updateByPrimaryKeySelective(x);
                            });

                            List<YsTeam> teams = ysTeamMapper.selectByUserIdAndYsMasterTaskId(userVo.getId(), id);
                            teams.forEach(x -> {
                                //更新团队
                                x.setUserId(diverUserId);
                                ysTeamMapper.updateByPrimaryKeySelective(x);
                            });
                            if (divertCount != 0 && receiveCout != 0) {
                                return Callback.success(true);
                            }
                        }
                        return Callback.error(2, "未发布,发布后待认领状态,不允许移交操作");
                    }

                }
            }
        }
        return Callback.error(2, "移交失败!");
    }

    public Callback<LinkedHashMap> selectTaskCount() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO userVO = userMapper.selectCurrentUser(userVo.getId());
        Integer receiveId = null;
        if ("sub_task_manage".equals(userVO.getRoleCode())) {
            receiveId = userVO.getUserId();
        }
        LinkedHashMap map = ysMasterTaskMapper.selectTaskCount(receiveId);
        return Callback.success(map);
    }

    public Callback<List<LinkedHashMap>> selectBySubTaskManageAllUser() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO userVO = userMapper.selectCurrentUser(userVo.getId());
        Integer receiveId = null;
        if ("sub_task_manage".equals(userVO.getRoleCode())) {
            receiveId = userVO.getUserId();
        }
        List<LinkedHashMap> listmap = ysMasterTaskMapper.selectBySubTaskManageAllUser(receiveId);
        return Callback.success(listmap);
    }

    public Callback<List<LinkedHashMap>> selectTaskMoveAllUser() {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        List<LinkedHashMap> listmap = ysMasterTaskMapper.selectTaskMoveAllUser(userVo.getId());
        return Callback.success(listmap);
    }


    public Callback<BaseVO<MasterLeve1>> selectMasterLeve1(Integer page, Integer pageSize, Integer receiveId, String name, Integer status) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO userVO = userMapper.selectCurrentUser(userVo.getId());
        if ("sub_task_manage".equals(userVO.getRoleCode())) {
            receiveId = userVO.getUserId();
        }

        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<MasterLeve1> list = ysMasterTaskMapper.selectMasterLeve1(receiveId, name, status);

        list.forEach(x -> {
            // 预计
            Integer sumEstimateWork = ysMasterTaskMapper.selectSumEstimateWork(x.getId());
            // 消耗
            Integer sumTotalConsume = ysMasterTaskMapper.selectSumTotalConsume(x.getId());
            // 剩余
            Integer surplus = null;
            // 进度 1.0
//            Integer percentage = null;

//            if (sumEstimateWork != null && sumTotalConsume != null) {
//                surplus = sumEstimateWork - sumTotalConsume;
//                // 进度=（消耗/预计）*100%   。
//                if (sumEstimateWork != 0) {
//                    BigDecimal multiply = new BigDecimal(sumTotalConsume).divide(new BigDecimal(sumEstimateWork), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
//                    percentage = multiply.intValue();
//                }
//            }
            // 是否存在日报记录
            Integer hasRbId = ysMasterFileMapper.selectHasRbById(x.getId());
            x.setRbStatus(1);
            if (hasRbId != null && hasRbId != 0) {
                x.setRbStatus(0);
            }

            // 是否存在经验建议记录
            Integer hasJyjyId = ysMasterFileMapper.selectHasJyjyById(x.getId());
            x.setExperienceStatus(1);
            if (hasJyjyId != null && hasJyjyId != 0) {
                x.setExperienceStatus(0);
            }

            List<YsMasterFile> masterFiles = ysMasterFileMapper.selectAllByYsMasterTaskId(x.getId());
            x.setButtonStatus(1);
            if (masterFiles != null && !masterFiles.isEmpty()) {
                x.setButtonStatus(0);
            }
            x.setEstimateHour(String.valueOf(sumEstimateWork));
            x.setTotalConsume(String.valueOf(sumTotalConsume));
            x.setSurplus(String.valueOf(surplus));
            // 进度1.0
//            x.setPercentage(percentage);
        });

        list = list.stream().sorted(Comparator.comparing(MasterLeve1::getPublishDate, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());

        BaseVO<MasterLeve1> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }


    public Callback updateRateById(Integer id, Integer rate) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        if (!"task_manage".equals(currentUserVO.getRoleCode())) {
            return Callback.error(2, "您没有权限操作");
        }
        if(null == rate || !(rate <= 100 && rate >= 0)){
            return Callback.error(2, "进度不能为空且必须在0-100之间");
        }

//        YsTaskReceive ysTaskReceive = ysTaskReceiveMapper.selectByYsMasterTaskIdAndReceiveId(id, currentUserVO.getUserId());
//        if (ysTaskReceive == null) {
//            return Callback.error(2, "您没有权限操作");
//        }

        YsMasterTask ysMasterTask = ysMasterTaskMapper.selectByPrimaryKey(id);
        if(null != ysMasterTask){
            if (ysMasterTask.getStatus() == 4 || ysMasterTask.getStatus() == 5) {
                return Callback.error(2, "已完成或已关闭的任务，不能修改进度");
            }
            if(100 == rate) {
                ysMasterTask.setStatus(4);
            }
            ysMasterTask.setRate(rate);
            ysMasterTask.setUpdateTime(LocalDateTime.now());
            int update = ysMasterTaskMapper.updateByPrimaryKey(ysMasterTask);
            if (update == 1) {
                return Callback.success();
            }
        }
        return Callback.error(2, "更新失败");
    }


}


