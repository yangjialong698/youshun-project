package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfouser.dto.BaseDTO;
import com.ennova.pubinfouser.dto.RoleDTO;
import com.ennova.pubinfouser.entity.RoleEntity;
import com.ennova.pubinfouser.service.RoleService;
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

@Api(tags = "角色API")
@RequestMapping("/role")
@RestController
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/addRole")
    @ApiOperation(value = "角色-添加", tags = "角色API")
    public Callback addRole(@RequestBody RoleDTO roleDTO) {
        return roleService.addRole(BaseDTO.convertBean(roleDTO));
    }

    @GetMapping("/getRoleById")
    @ApiOperation(value = "角色-获取角色信息", tags = "角色API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id")
    })
    public Callback getRoleById(Integer id) {
        return roleService.getRoleById(id);
    }

    @PostMapping("/updateRole")
    @ApiOperation(value = "角色-修改", tags = "角色API")
    public Callback updateRole(@RequestBody RoleDTO roleDTO) {
        return roleService.updateRole(BaseDTO.convertBean(roleDTO));
    }

    @PostMapping("/deleteRole")
    @ApiOperation(value = "角色-删除角色", tags = "角色API")
    public Callback deleteRole(Integer id) {
        return roleService.deleteRole(id);
    }

    @GetMapping("/listRoles")
    @ApiOperation(value = "角色-分页查询角色列表", tags = "角色API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "条数"),
            @ApiImplicitParam(name = "searchKey", value = "关键字")})
    public Callback listRoles(Integer page, Integer pageSize,Integer company, String searchKey) {
        return roleService.listRoles(page, pageSize, company,searchKey);
    }

    @GetMapping("/getPermissions")
    @ApiOperation(value = "权限-获取权限（全部/选中）", notes = "权限-获取权限", tags = "角色API")
    @ApiImplicitParam(name = "roleId", value = "角色id")
    public Callback getPermissions(Integer roleId) {
        return roleService.getPermissions(roleId);
    }

    @PostMapping("/saveRolePermission")
    @ApiOperation(value = "权限-添加/修改权限", tags = "角色API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id"),
            @ApiImplicitParam(name = "pIds", value = "权限id，逗号拼接")
    })
    public Callback saveRolePermission(Integer roleId, Integer[] pIds) {
        return roleService.saveRolePermission(roleId,pIds);
    }

    @GetMapping("/listRoleList")
    @ApiOperation(value = "角色-下拉列表", tags = "角色API")
    public Callback<List<RoleEntity>> listRoleList(Integer company) {
        return roleService.listRoleList(company);
    }

}
