package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfostore.entity.TDeptDing;
import com.ennova.pubinfostore.entity.TUserDing;
import com.ennova.pubinfostore.service.PrdUserManageService;
import com.ennova.pubinfostore.vo.TDeptDingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @authorwangwei
 * @date 2022/12/14
 */
@Api(tags = "查询部门管理者")
@RestController
@RequestMapping("/manageuser")
public class PrdUserManageController {
    @Autowired
    private PrdUserManageService prdUserManageService;

    @ApiOperation(value = "APP移动端接口 - 查询子部门管理者")
    @GetMapping("/queryPrdDeptIds")
    public Callback<List<TDeptDing>> queryPrdDeptIds() {
        return prdUserManageService.queryPrdDeptIds();
    }

//    @ApiOperation(value = "APP移动端接口 - 根据manageId查询用户姓名")
//    @GetMapping("/queryNameByManageId")
//    public Callback<TUserDing> queryNameByManageId(String manageId) {
//        return prdUserManageService.queryNameByManageId(manageId);
//    }

    @ApiOperation(value = "APP移动端接口 - 根据manageId或者部门ID查询用户姓名")
    @GetMapping("/queryNameByManageId")
    public Callback<List<TUserDing>> queryNameByManageIdOrDeptId(@RequestParam("manageId")String manageId ,
                                                                 @RequestParam("deptName")String deptName,
                                                                 @RequestParam("deptId")String deptId) {
        return prdUserManageService.queryNameByManageIdOrDeptId(manageId,deptName,deptId);
    }


    @ApiOperation(value = "APP移动端接口 - 查询树状子部门管理者")
    @GetMapping("/queryPrdDeptChildList")
    public Callback<TDeptDingVO> queryPrdDeptChildList() {
        return prdUserManageService.queryPrdDeptChildList();
    }
}
