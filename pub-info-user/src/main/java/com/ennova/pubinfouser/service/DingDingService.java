package com.ennova.pubinfouser.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.*;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfouser.dao.*;
import com.ennova.pubinfouser.entity.*;
import com.ennova.pubinfouser.utils.DingDingUtil;
import com.ennova.pubinfouser.vo.DingDeptVO;
import com.ennova.pubinfouser.vo.DingUserVO;
import com.ennova.pubinfouser.vo.TDingClockVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DingDingService  {

    @Autowired
    private TUserDingMapper tUserDingMapper;
    @Autowired
    private TDeptDingMapper tDeptDingMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private TDingClockMapper tDingClockMapper;

    //获取最后一级部门接口
    public Callback<List<Long>> lastDeptIds() {
        String accesstoken = DingDingUtil.getAccess_Token();
        ArrayList<Long> deptListFinal = new ArrayList<>();
        List<Long> deptParentIds = null ;
        if (StringUtils.isNotEmpty(accesstoken)){
            //获取一级部门列表
            List<OapiV2DepartmentListsubResponse.DeptBaseResponse> parentDepIdString = DingDingUtil.getParentDepIdList(accesstoken);
            deptParentIds = parentDepIdString.stream().map(e -> e.getDeptId()).collect(Collectors.toList());
        }
        //获取最后一级部门列表
        if (CollectionUtil.isNotEmpty(deptParentIds)){
            List<Long> aa = getLastDepts(accesstoken, deptListFinal, deptParentIds);
            redisTemplate.opsForValue().set("lastdepts",JSONObject.toJSONString(aa), 24, TimeUnit.HOURS);
            return Callback.success(aa) ;
        }
        return null;
    }
    //递归调用获取子集部门
    private List<Long> getLastDepts(String accesstoken, ArrayList<Long> deptListFinal, List<Long> deptParentIds) {
        if (CollectionUtil.isNotEmpty(deptParentIds)){
            deptParentIds.forEach(deptId->{
                OapiV2DepartmentListsubidResponse.DeptListSubIdResponse sunDepIdS = DingDingUtil.getSunDepIdList(deptId, accesstoken);
                List<Long> deptIdList = null ;
                if (sunDepIdS.getDeptIdList().size()>0){
                    deptIdList = sunDepIdS.getDeptIdList();
                    getLastDepts(accesstoken,deptListFinal,deptIdList);
                } else {
                    deptListFinal.add(deptId);
                }
            });
        }
        return deptListFinal;
    }


    //获取公司所有的部门
    public Callback<List<Long>> listDeptAllIds() {
        String accesstoken = DingDingUtil.getAccess_Token();
        ArrayList<Long> deptListFinal = new ArrayList<>();
        List<Long> deptParentIds = null ;
        if (StringUtils.isNotEmpty(accesstoken)){
            //获取一级部门列表
            List<OapiV2DepartmentListsubResponse.DeptBaseResponse> parentDepIdString = DingDingUtil.getParentDepIdList(accesstoken);
            deptParentIds = parentDepIdString.stream().map(e -> e.getDeptId()).collect(Collectors.toList());
            deptListFinal.addAll(deptParentIds);
        }
        //获取最后一级部门列表
        if (CollectionUtil.isNotEmpty(deptParentIds)){
            List<Long> aa = getLastAllDepts(accesstoken, deptListFinal, deptParentIds);
            List<Long> collect = aa.stream().distinct().collect(Collectors.toList());
            redisTemplate.opsForValue().set("alldepts",JSONObject.toJSONString(collect), 24, TimeUnit.HOURS);
            return Callback.success(collect) ;
        }
        return null;
    }
    //递归调用获取子集部门
    private List<Long> getLastAllDepts(String accesstoken, ArrayList<Long> deptListFinal, List<Long> deptParentIds) {
        if (CollectionUtil.isNotEmpty(deptParentIds)){
            deptParentIds.forEach(deptId->{
                OapiV2DepartmentListsubidResponse.DeptListSubIdResponse sunDepIdS = DingDingUtil.getSunDepIdList(deptId, accesstoken);
                List<Long> deptIdList = null ;
                if (sunDepIdS.getDeptIdList().size()>0){
                    deptIdList = sunDepIdS.getDeptIdList();
                    deptListFinal.addAll(deptIdList);
                    getLastAllDepts(accesstoken,deptListFinal,deptIdList);
                }else {
                    deptListFinal.add(deptId);
                }
            });
        }
        return deptListFinal;
    }

    //每天上午10点下午3点跑一次获取钉钉用户列表

    @Scheduled(cron="0 0 09,14/12 * * ?") //测试
    //@Scheduled(cron="0 0 01,12/12 * * ?") //线上 中午12点出发一次
    public void userDetails() {
        String accesstoken = DingDingUtil.getAccess_Token();
        List<Long> deptIds = null ;
        List<Long> alldeptIds = null ;
        String lastdepts = redisTemplate.opsForValue().get("lastdepts");
        if (StringUtils.isNotEmpty(lastdepts)){
             deptIds = JSONObject.parseArray(lastdepts, Long.class);
        }else {
            Callback<List<Long>> listCallback = this.lastDeptIds();
            deptIds = listCallback.getData();
        }
        ArrayList<DingUserVO> dingUserVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deptIds)){
            deptIds.forEach(deptId->{
                OapiV2UserListResponse.PageResult departmentUser = DingDingUtil.getDepartmentUser(deptId, 0, 100, accesstoken);
                List<OapiV2UserListResponse.ListUserResponse> userList = departmentUser.getList();
                userList.forEach(e->{
                    DingUserVO dingUserVO = new DingUserVO();
                    BeanUtils.copyProperties(e,dingUserVO);
                    dingUserVO.setDeptIdList(e.getDeptIdList());
                    dingUserVOS.add(dingUserVO);
                });
            });
            ArrayList<String> deptManagerUseridList = new ArrayList<>();
            String alldepts = redisTemplate.opsForValue().get("alldepts");
            if (StringUtils.isNotEmpty(alldepts)){
                alldeptIds = JSONObject.parseArray(alldepts, Long.class);
            }else {
                Callback<List<Long>> listCallback = this.listDeptAllIds();
                alldeptIds = listCallback.getData();
            }
            //取差集-查助理
            List<Long> finalDeptIds = deptIds;
            List<Long> reduce1 = alldeptIds.stream().filter(item -> !finalDeptIds.contains(item)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(reduce1)){
                reduce1.forEach(e->{
                    OapiV2UserListResponse.PageResult departmentUser = DingDingUtil.getDepartmentUser(e, 0, 100, accesstoken);
                    List<OapiV2UserListResponse.ListUserResponse> userList = departmentUser.getList();
                    userList.forEach(a->{
                        deptManagerUseridList.add( a.getUserid());
                    });
                });
            }
            alldeptIds.forEach(deptId->{
                OapiV2DepartmentGetResponse.DeptGetResponse deptDetails = DingDingUtil.getDeptDetails(deptId, accesstoken);
                if (null != deptDetails){
                    //部门管理者
                    List<String> managerUseridList = deptDetails.getDeptManagerUseridList();
                    if (CollectionUtil.isNotEmpty(managerUseridList)){
                        deptManagerUseridList.addAll(managerUseridList);
                    }
                    //群主
                    String orgDeptOwner = deptDetails.getOrgDeptOwner();
                    if (StringUtils.isNotEmpty(orgDeptOwner)){
                        deptManagerUseridList.add(orgDeptOwner);
                    }

                }
            });
            List<String> collect = deptManagerUseridList.stream().distinct().collect(Collectors.toList());
            collect.forEach(e->{
                DingUserVO dingUserVO = new DingUserVO();
                OapiV2UserGetResponse.UserGetResponse userDetail = DingDingUtil.getUserDetail(e, accesstoken);
                BeanUtils.copyProperties(userDetail,dingUserVO);
                dingUserVO.setDeptIdList(userDetail.getDeptIdList());
                dingUserVOS.add(dingUserVO);
            });
        }
        ArrayList<TUserDing> TUserDings = new ArrayList<TUserDing>();
        dingUserVOS.forEach(e->{
            TUserDing tUserDing = new TUserDing();
            tUserDing.setUserId(e.getUserid());
            tUserDing.setUsername(e.getName());
            tUserDing.setMobile(e.getMobile());
            tUserDing.setJobNum(e.getJobNumber());
            tUserDing.setPosition(e.getTitle());
            tUserDing.setDepartment(e.getDeptIdList().get(0).toString());
            tUserDing.setCreateTime(new Date());
            tUserDing.setCompany("53");
            tUserDing.setPassword("e10adc3949ba59abbe56e057f20f883e");
            tUserDing.setStatus("0");
            tUserDing.setIsDelete(0);
            tUserDing.setIsShow(1);
            tUserDing.setIsUpdate(0);
            TUserDings.add(tUserDing);
        });
        List<TUserDing> collect = TUserDings.stream().filter(e -> StringUtils.isNotEmpty(e.getJobNum())).collect(Collectors.toList());
        List<TUserDing> uniqueList = collect.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TUserDing::getJobNum))), ArrayList::new)
        );
        tUserDingMapper.deleteAll();
        tUserDingMapper.batchInsert(uniqueList);
    }



    @Scheduled(cron="0 0 3 * * ? ") //测试
    //@Scheduled(cron="0 0 2 * * ? ") //线上1
    //@Scheduled(cron="0 0 02,12/12 * * ?") //线上2 中午12点出发一次
    public Callback<List<DingDeptVO>> deptDetails() {
        String accesstoken = DingDingUtil.getAccess_Token();
        Callback<List<Long>> listCallback = this.listDeptAllIds();
        List<Long> deptIds = listCallback.getData();
        ArrayList<DingDeptVO> dingDeptVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deptIds)){
            deptIds.forEach(deptId->{
                OapiV2DepartmentGetResponse.DeptGetResponse deptDetails = DingDingUtil.getDeptDetails(deptId, accesstoken);
                DingDeptVO dingDeptVO = new DingDeptVO();
                Long depId = deptDetails.getDeptId();
                String deptName = deptDetails.getName();
                Long parentId = deptDetails.getParentId();
                dingDeptVO.setDeptId(depId);
                dingDeptVO.setName(deptName);
                dingDeptVO.setParentId(parentId);
                dingDeptVO.setManageId(null != deptDetails.getDeptManagerUseridList() ? StringUtils.strip(deptDetails.getDeptManagerUseridList().toString(),"[]") : "");
                dingDeptVOS.add(dingDeptVO);
            });
        }
        ArrayList<TDeptDing> tDeptDings = new ArrayList<>();
        dingDeptVOS.forEach(e->{
            TDeptDing tDeptDing = new TDeptDing();
            tDeptDing.setCompany(53);
            tDeptDing.setDeptName(e.getName());
            tDeptDing.setDeptId(e.getDeptId());
            tDeptDing.setParentId(e.getParentId());
            tDeptDing.setCreateTime(new Date());
            tDeptDing.setIsDelete(0);
            tDeptDing.setIsOperate(0);
            tDeptDing.setUseStatus("0");
            tDeptDing.setManageId(e.getManageId());
            tDeptDings.add(tDeptDing);
        });
        tDeptDingMapper.deleteAll();
        tDeptDingMapper.batchInsert(tDeptDings);
        return Callback.success(dingDeptVOS);
    }



    @Scheduled(cron="0 0 10,15/12 * * ?") //测试
    //@Scheduled(cron="0 0 02,13/12 * * ?") //线上 下1点出发一次
    public void updatTuser() {
        //1.查询t_user无,t_user_ding有的数据(新入职)
        List<TUserDing> tUserDingList = tUserDingMapper.selectEntry();
        ArrayList<UserEntity> userEntities = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(tUserDingList)){
           tUserDingList.forEach(tUserDing -> {
               UserEntity userEntity = new UserEntity();
               BeanUtils.copyProperties(tUserDing,userEntity);
               userEntity.setUpdateTime(new Date());
               userEntities.add(userEntity);
           });
       }
        userEntities.forEach(e->{
            userDao.insert(e);
        });
        //2.查询t_user有,t_user_ding无的数据(已离职-排除isshow等于0)
        List<UserEntity> userEntityList = userDao.selectLeave();
        if (CollectionUtil.isNotEmpty(userEntityList)){
            userEntityList.forEach(e->{
                e.setUpdateTime(new Date());
                userDao.deleteUser(e.getJobNum());
            });
        }
        //更新最新用户表所有部门
        userDao.updateAllDept();
    }


//    @Scheduled(cron="0 0 4 * * ? ") //部门测试第一版
//    //@Scheduled(cron="0 0 3 * * ? ") //部门线上第一版
//    public void updatTdept() {
//        deptDao.deleteAll();
//        List<TDeptDing> tDeptDingList = tDeptDingMapper.selectAll();
//        ArrayList<DeptEntity> deptEntityList = new ArrayList<>();
//        if (CollectionUtil.isNotEmpty(tDeptDingList)){
//            tDeptDingList.forEach(tDeptDing -> {
//                DeptEntity deptEntity = new DeptEntity();
//                BeanUtils.copyProperties(tDeptDing,deptEntity);
//                deptEntity.setUpdateTime(new Date());
//                deptEntityList.add(deptEntity);
//            });
//        }
//        deptDao.insertBatch(deptEntityList);
//    }


    @Scheduled(cron="0 0 10 * * ? ") //部门测试第二版
    //@Scheduled(cron="0 0 3 * * ? ") //部门线上第二版
    //@Scheduled(cron="0 0 03,13/12 * * ?") //部门线上第三版 下1点出发一次
    public void updatTempTdept() {
        //1.查询t_dept无,t_dept_ding有的数据(新建部门)
        List<TDeptDing> tDeptDingList = tDeptDingMapper.selectEntry();
        ArrayList<DeptEntity> deptEntityList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(tDeptDingList)){
            tDeptDingList.forEach(tDeptDing -> {
                DeptEntity deptEntity = new DeptEntity();
                BeanUtils.copyProperties(tDeptDing,deptEntity);
                deptEntity.setUpdateTime(new Date());
                deptEntityList.add(deptEntity);
            });
            deptDao.insertBatch(deptEntityList);
        }
        //2.t_dept,t_dept_ding有的数据(钉钉已删除的部门)
        List<DeptEntity> deptEntitys = deptDao.selectDelete();
        if (CollectionUtil.isNotEmpty(deptEntitys)){
            deptEntitys.forEach(e->{
                e.setUpdateTime(new Date());
                deptDao.deleteDept(e.getDeptId());
            });
        }
    }


    public Callback<BaseVO<TDingClock>> queryClockList(Integer page, Integer pageSize,String userIds, String checkDateFrom, String checkDateTo) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        List<String> userIdList = Arrays.asList(userIds.split(","));
        Page<LinkedHashMap> startPage = PageHelper.startPage(page, pageSize);
        List<TDingClock> list = tDingClockMapper.selectByUserIdsAndDayTime(userIdList,checkDateFrom,checkDateTo);
        BaseVO<TDingClock>  baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }


    //    @Scheduled(cron="0 0 12 * * ? ")
    public Callback<List<TDingClock>> listClock(String userIds, String checkDateFrom, String checkDateTo) {
//        List<String> userIdList = Arrays.asList(userIds.split(","));
        List<String> userIdList = tUserDingMapper.selectAllUserId();
//        String checkDateFrom =getYseTime(-1,05);
//        String checkDateTo =getYseTime(-1,20);
//        String checkDateFrom =getYseTime(-1,5);
//        String checkDateTo =getYseTime(0,8);
        ArrayList<TDingClockVO> tDingClocksVoList = new ArrayList<>();
        ArrayList<TDingClock> tDingClocks = new ArrayList<>();
        int size = userIdList.size();
        int maxSize = 50;
        int loopNum =(int) Math.ceil(size * NumberUtils.DOUBLE_ONE / maxSize);
        for (int i = 0; i < loopNum; i++) {
            int endSize = (i + 1) * maxSize;
            endSize = endSize > size ? size : endSize;
            List<String> subUserIds = userIdList.subList(i * maxSize, endSize);
            String accesstoken = DingDingUtil.getAccess_Token();
            //调用钉钉API获取用户每天打卡详情
            List<OapiAttendanceListRecordResponse.Recordresult> dingClockList = DingDingUtil.getOnClassTime(subUserIds, checkDateFrom, checkDateTo, accesstoken);
            for (OapiAttendanceListRecordResponse.Recordresult recordresult : dingClockList) {
                TDingClockVO tDingClockVO = new TDingClockVO();
                BeanUtils.copyProperties(recordresult,tDingClockVO);
                tDingClocksVoList.add(tDingClockVO);
            }
        }
        Map<Date, Map<String, List<TDingClockVO>>> listMap = tDingClocksVoList.stream().collect(Collectors.groupingBy(TDingClockVO::getWorkDate, Collectors.groupingBy(TDingClockVO::getUserId)));
        listMap.entrySet().stream().map(key->{
            Map<String, List<TDingClockVO>> value1 = key.getValue();
            value1.entrySet().stream().map(key1->{
                // key这一天key1这个用户的打卡详情
                List<TDingClockVO> tDingClockVOS = key1.getValue();
                TDingClockVO tDingClockVoFirst = null;
                TDingClockVO tDingClockVoLast = null;
                if (CollectionUtil.isNotEmpty(tDingClockVOS)){
                    List<TDingClockVO> tDingClockVOList = tDingClockVOS.stream().sorted(Comparator.comparing(TDingClockVO::getUserCheckTime)).collect(Collectors.toList());
                    boolean onDuty = tDingClockVOList.stream().filter(e->e.getCheckType()!=null).filter(m -> m.getCheckType().equalsIgnoreCase("OnDuty")).findAny().isPresent();
                    boolean offDuty = tDingClockVOList.stream().filter(e->e.getCheckType()!=null).filter(m -> m.getCheckType().equalsIgnoreCase("OffDuty")).findAny().isPresent();
                    TDingClock tDingClock = new TDingClock();
                    if (onDuty && offDuty){ //正常打开无漏打卡
                        tDingClockVoFirst = tDingClockVOList.get(0);
                        tDingClockVoLast = tDingClockVOList.get(tDingClockVOList.size() - 1);
                        tDingClock.setUserId(tDingClockVoFirst.getUserId());
                        tDingClock.setUserCheckOn(tDingClockVoFirst.getUserCheckTime());
                        tDingClock.setUserCheckOff(tDingClockVoLast.getUserCheckTime());
                        tDingClock.setBaseCheckOn(tDingClockVoFirst.getBaseCheckTime());
                        tDingClock.setBaseCheckOff(tDingClockVoLast.getBaseCheckTime());
                        tDingClock.setWorkDate(tDingClockVoFirst.getWorkDate());
                        tDingClock.setWorkTime(DateUtil.between(tDingClockVoFirst.getUserCheckTime(), tDingClockVoLast.getUserCheckTime(), DateUnit.MINUTE)+" 分钟");
                        tDingClocks.add(tDingClock);
                    }
                }
                return tDingClocks;
            }).collect(Collectors.toList());
            return tDingClocks;
        }).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(tDingClocks)){
            tDingClockMapper.batchInsert(tDingClocks);
        }
        return Callback.success(tDingClocks);
    }

    public static String getYseTime(Integer day,Integer hour) {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.DATE, day);
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate1 = sdf1.format(time.getTime());
        return strDate1;
    }


}


