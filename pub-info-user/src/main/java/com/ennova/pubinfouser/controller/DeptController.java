package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfouser.dto.BaseDTO;
import com.ennova.pubinfouser.dto.DeptDTO;
import com.ennova.pubinfouser.service.DeptService;
import com.ennova.pubinfouser.vo.DeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-20
 */

@Api(tags = "部门API")
@RequestMapping("/dept")
@RestController
@Slf4j
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping("/addDept")
    @ApiOperation(value = "部门-添加", tags = "部门API")
    public Callback addDept(@RequestBody DeptDTO deptDTO) {
        return deptService.addDept(BaseDTO.convertBean(deptDTO));
    }

    @GetMapping("/getDeptById")
    @ApiOperation(value = "部门-获取部门信息", tags = "部门API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门id")
    })
    public Callback getDeptById(Integer id) {
        return deptService.getDeptById(id);
    }

    @PostMapping("/updateDept")
    @ApiOperation(value = "部门-修改", tags = "部门API")
    public Callback updateDept(@RequestBody DeptDTO deptDTO) {
        return deptService.updateDept(BaseDTO.convertBean(deptDTO));
    }

    @PostMapping("/deleteDept")
    @ApiOperation(value = "部门-删除", tags = "部门API")
    public Callback deleteDept(Integer id) {
        return deptService.deleteDept(id);
    }

    @ApiOperation(value = "部门-获取部门列表", tags = "部门API")
    @GetMapping("/listDepts")
    public Callback<BaseVO<DeptVO>> listDepts(Integer page, Integer pageSize, Integer company, String searchKey) {
        return deptService.listDepts(page, pageSize, company, searchKey);
    }

    @ApiOperation(value = "部门-下拉列表", tags = "部门API")
    @GetMapping("/listDeptList")
    public Callback<List<DeptVO>> listDeptList(Integer company) {
        return deptService.listDeptList(company);
    }

    @ApiOperation(value = "首页-获取所有用户对应的部门-下拉列表", tags = "用户API")
    @GetMapping("/listUserDeptList")
    public Callback<List<DeptVO>> listUserDeptList(Integer company) {
        return deptService.listUserDeptList(company);
    }

}
