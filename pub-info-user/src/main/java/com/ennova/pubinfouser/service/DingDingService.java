package com.ennova.pubinfouser.service;


import cn.hutool.core.collection.CollectionUtil;
import com.dingtalk.api.response.*;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfouser.utils.DingDingUtil;
import com.ennova.pubinfouser.vo.DingUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DingDingService  {

    public Callback<List<Long>> listDeptIds() {
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

    public Callback<List<DingUserVO>> userDetails() {
        String accesstoken = DingDingUtil.getAccess_Token();
        Callback<List<Long>> listCallback = this.listDeptIds();
        List<Long> deptIds = listCallback.getData();
        ArrayList<DingUserVO> dingUserVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deptIds)){
            deptIds.forEach(deptId->{
                OapiV2UserListResponse.PageResult departmentUser = DingDingUtil.getDepartmentUser(deptId, 0, 100, accesstoken);
                List<OapiV2UserListResponse.ListUserResponse> userList = departmentUser.getList();
                userList.forEach(e->{
                    DingUserVO dingUserVO = new DingUserVO();
                    BeanUtils.copyProperties(e,dingUserVO);
                    dingUserVOS.add(dingUserVO);
                });
            });
            ArrayList<String> deptManagerUseridList = new ArrayList<>();
            deptIds.forEach(deptId->{
                OapiV2DepartmentGetResponse.DeptGetResponse deptDetails = DingDingUtil.getDeptDetails(deptId, accesstoken);
                if (null != deptDetails){
                    List<String> managerUseridList = deptDetails.getDeptManagerUseridList();
                    if (CollectionUtil.isNotEmpty(managerUseridList)){
                        deptManagerUseridList.addAll(managerUseridList);
                    }
                }
            });
            List<String> collect = deptManagerUseridList.stream().distinct().collect(Collectors.toList());
            collect.forEach(e->{
                DingUserVO dingUserVO = new DingUserVO();
                OapiV2UserGetResponse.UserGetResponse userDetail = DingDingUtil.getUserDetail(e, accesstoken);
                BeanUtils.copyProperties(userDetail,dingUserVO);
                dingUserVOS.add(dingUserVO);
            });
        }
        System.out.println(dingUserVOS.size());
        return Callback.success(dingUserVOS);
    }
}


