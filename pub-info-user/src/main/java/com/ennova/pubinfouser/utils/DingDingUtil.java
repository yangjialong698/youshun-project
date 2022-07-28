package com.ennova.pubinfouser.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.ennova.pubinfouser.vo.DingDeptVO;
import com.ennova.pubinfouser.vo.DingUserVO;
import com.taobao.api.ApiException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class DingDingUtil {
    //应用的唯一标识key。
    private static String appkey = "dinggiib6p8wlhrtn8ss";
    //应用的密钥。
    private static String appsecret = "jPKNoMXYsIFLW42ahj4Y5-wLgw8QzGGLp0BXgfeu2Mpta-Jm01JEw8JoiW65or7K";

    //获取accessToken
    public static String getAccess_Token(){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appkey);
        request.setAppsecret(appsecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = null;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return response.getAccessToken();
    }

    //获取一级部门列表
    public static List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getParentDepIdList(String accesstoken){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            req.setDeptId(1L);
            OapiV2DepartmentListsubResponse rsp = client.execute(req, accesstoken);
            System.out.println(rsp.getResult());
            return rsp.getResult();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取部门详情
    public static OapiV2DepartmentGetResponse.DeptGetResponse getDeptDetails(Long deptId,String accesstoken){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/get");
            OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
            req.setDeptId(deptId);
            req.setLanguage("zh_CN");
            OapiV2DepartmentGetResponse rsp = client.execute(req, accesstoken);
            return rsp.getResult();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取子部门ID列表
    public static OapiV2DepartmentListsubidResponse.DeptListSubIdResponse getSunDepIdList(Long deptId,String accesstoken){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsubid");
            OapiV2DepartmentListsubidRequest req = new OapiV2DepartmentListsubidRequest();
            req.setDeptId(deptId);
            OapiV2DepartmentListsubidResponse rsp = client.execute(req, accesstoken);
            System.out.println(rsp.getResult());
            return rsp.getResult();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取部门用户userid列表
    public static List<String> getUserIdsByDeptId(Long deptId,String accesstoken){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
            OapiUserListidRequest req = new OapiUserListidRequest();
            req.setDeptId(deptId);
            OapiUserListidResponse rsp = client.execute(req, accesstoken);
            OapiUserListidResponse.ListUserByDeptResponse result = rsp.getResult();
            return result.getUseridList();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取部门下的所有用户列表
    public static OapiV2UserListResponse.PageResult getDepartmentUser(Long departmentId, long cursor, long size,String accesstoken){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
        OapiV2UserListRequest request = new OapiV2UserListRequest();
        request.setDeptId(departmentId);
        request.setCursor(cursor);
        request.setSize(size);
        request.setOrderField("modify_desc");
        request.setContainAccessLimit(false);
        request.setLanguage("zh_CN");
        try {
            OapiV2UserListResponse  response = client.execute(request, accesstoken);
            return response.isSuccess()?response.getResult():null;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
    //获取用户详情
    public static OapiV2UserGetResponse.UserGetResponse getUserDetail(String userId,String accesstoken){
        try {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId.toString());
        OapiV2UserGetResponse rsp = client.execute(req, accesstoken);
            OapiV2UserGetResponse.UserGetResponse result = rsp.getResult();
        return result;
    } catch (ApiException e) {
        e.printStackTrace();
    }
        return null;
    }




    public static void main(String[] args) {
        String access_token = DingDingUtil.getAccess_Token();
        OapiV2UserGetResponse.UserGetResponse userDetail = DingDingUtil.getUserDetail("5150", access_token);
        DingUserVO dingUserVO = new DingUserVO();
        BeanUtils.copyProperties(userDetail,dingUserVO);
//        List<String> a = DingDingUtil.getUserIdsByDeptId(578030066L, access_token);

        System.out.println(dingUserVO);
//        ArrayList<DingUserVO> dingUserVOS = new ArrayList<>();
//        OapiV2UserListResponse.PageResult departmentUser = DingDingUtil.getDepartmentUser(578030066L, 0L, 100L, access_token);
//        List<OapiV2UserListResponse.ListUserResponse> list = departmentUser.getList();
//        list.forEach(e->{
//            DingUserVO dingUserVO = new DingUserVO();
//            BeanUtils.copyProperties(e,dingUserVO);
//            dingUserVOS.add(dingUserVO);
//        });
//        System.out.println(dingUserVOS);
//        OapiV2UserGetResponse.UserGetResponse userDetail = DingDingUtil.getUserDetail(502443691526110958L, access_token);
//        System.out.println(userDetail);
    }


}
