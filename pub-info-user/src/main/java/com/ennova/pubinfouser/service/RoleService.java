package com.ennova.pubinfouser.service;


import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfouser.dao.PermissionDao;
import com.ennova.pubinfouser.dao.RoleDao;
import com.ennova.pubinfouser.dao.RolePermissionMapper;
import com.ennova.pubinfouser.dao.UserDao;
import com.ennova.pubinfouser.dao.UserRoleMapper;
import com.ennova.pubinfouser.entity.RoleEntity;
import com.ennova.pubinfouser.entity.RolePermissionEntity;
import com.ennova.pubinfouser.entity.UserRole;
import com.ennova.pubinfouser.vo.MenuVO;
import com.ennova.pubinfouser.vo.PermissionVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RoleService extends BaseService<RoleEntity> {

    @Resource
    private RoleDao roleDao;

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private UserDao userDao;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    // 获取菜单
    public List<MenuVO> getMenu(Integer roleId) {

        List<PermissionVO> permissionResponse = new ArrayList<>();
        permissionResponse = permissionDao.getPermissions();
        List<RolePermissionEntity> rolePermissions = permissionDao.getRolePermissions(roleId);
        List<MenuVO> menuVOList = new ArrayList<>();
        // 获取一级菜单
        for (PermissionVO permissionVO : permissionResponse) {
            if (permissionVO.getLevel() == 1) {
                for (RolePermissionEntity rolePermission : rolePermissions) {
                    if (rolePermission.getPermissionId().equals(permissionVO.getId())) {
                        MenuVO menuVO = new MenuVO();
                        menuVO.setId(permissionVO.getId());
                        menuVO.setPath(permissionVO.getUrl());
                        menuVO.setIcon(permissionVO.getIcon());
                        menuVO.setName(permissionVO.getPermissionName());
                        menuVO.setId(permissionVO.getId());
                        menuVO.setRedirect(permissionVO.getRedirect());
                        menuVO.setComponent(permissionVO.getComponent());
                        menuVO.setHidden(permissionVO.getHidden());
                        menuVO.setVal(permissionVO.getVal());
                        menuVO.setActive(permissionVO.getActive());
                        menuVOList.add(menuVO);
                    }
                }
            }
        }
        // 获取二级菜单
        for (PermissionVO permissionVO : permissionResponse) {
            if (permissionVO.getLevel() == 2) {
                for (MenuVO menuVO : menuVOList) {
                    if (menuVO.getId() == permissionVO.getParentId()) {
                        for (RolePermissionEntity rolePermission : rolePermissions) {
                            if (rolePermission.getPermissionId().equals(permissionVO.getId())) {
                                MenuVO.MenuBan menuBan = new MenuVO.MenuBan();
                                menuBan.setId(permissionVO.getId());
                                menuBan.setPath(permissionVO.getUrl());
                                menuBan.setName(permissionVO.getPermissionName());
                                menuBan.setIcon(permissionVO.getIcon());
                                menuBan.setRedirect(permissionVO.getRedirect());
                                menuBan.setComponent(permissionVO.getComponent());
                                menuBan.setHidden(permissionVO.getHidden());
                                menuBan.setVal(permissionVO.getVal());
                                menuBan.setActive(permissionVO.getActive());
                                menuVO.getChildren().add(menuBan);
                            }
                        }
                    }
                }
            }

        }
        // 获取三级权限
        for (PermissionVO p1 : permissionResponse) {
            if (p1.getLevel() == 3) {
                for (MenuVO menuVO : menuVOList) {
                    List<MenuVO.MenuBan> secondMenuVOs = menuVO.getChildren();
                    if (secondMenuVOs == null || secondMenuVOs.size() == 0) {
                        continue;
                    }
                    for (MenuVO.MenuBan secondMenu : secondMenuVOs) {
                        if (secondMenu.getId() == p1.getParentId()) {
                            for (RolePermissionEntity rolePermission : rolePermissions) {
                                if (rolePermission.getPermissionId().equals(p1.getId())) {
                                    secondMenu.getBtnName().add(p1.getVal());
                                }
                            }
                        }
                    }
                }
            }
        }
        return menuVOList;
    }


    // 获取菜单
    public List<MenuVO> getPermissionsModule(Integer roleId) {

        List<PermissionVO> permissionResponse = new ArrayList<>();
        permissionResponse = permissionDao.getPermissions();
        List<RolePermissionEntity> rolePermissions = permissionDao.getRolePermissions(roleId);
        List<MenuVO> menuVOList = new ArrayList<>();
        // 获取一级菜单
        for (PermissionVO permissionVO : permissionResponse) {
            if (permissionVO.getLevel() == 1) {
                for (RolePermissionEntity rolePermission : rolePermissions) {
                    if (rolePermission.getPermissionId().equals(permissionVO.getId())) {
                        MenuVO menuVO = new MenuVO();
                        menuVO.setId(permissionVO.getId());
                        menuVO.setPath(permissionVO.getUrl());
                        menuVO.setIcon(permissionVO.getIcon());
                        menuVO.setName(permissionVO.getPermissionName());
                        menuVO.setId(permissionVO.getId());
                        menuVOList.add(menuVO);
                    }
                }
            }
        }
        // 获取二级菜单
        for (PermissionVO permissionVO : permissionResponse) {
            if (permissionVO.getLevel() == 2) {
                for (MenuVO menuVO : menuVOList) {
                    if (menuVO.getId() == permissionVO.getParentId()) {
                        for (RolePermissionEntity rolePermission : rolePermissions) {
                            if (rolePermission.getPermissionId().equals(permissionVO.getId())) {
                                MenuVO.MenuBan menuBan = new MenuVO.MenuBan();
                                menuBan.setId(permissionVO.getId());
                                menuBan.setPath(permissionVO.getUrl());
                                menuBan.setName(permissionVO.getPermissionName());
                                menuBan.setIcon(permissionVO.getIcon());
                                menuVO.getChildren().add(menuBan);
                            }
                        }
                    }
                }
            }

        }
        // 获取三级权限
        for (PermissionVO p1 : permissionResponse) {
            if (p1.getLevel() == 3) {
                for (MenuVO menuVO : menuVOList) {
                    List<MenuVO.MenuBan> secondMenuVOs = menuVO.getChildren();
                    if (secondMenuVOs == null || secondMenuVOs.size() == 0) {
                        continue;
                    }
                    for (MenuVO.MenuBan secondMenu : secondMenuVOs) {
                        if (secondMenu.getId() == p1.getParentId()) {
                            for (RolePermissionEntity rolePermission : rolePermissions) {
                                if (rolePermission.getPermissionId().equals(p1.getId())) {
                                    secondMenu.getBtnName().add(p1.getVal());
                                }
                            }
                        }
                    }
                }
            }
        }
        return menuVOList;
    }


    public Callback addRole(RoleEntity roleEntity) {
        List<RoleEntity> list = roleDao.getRolesByName(roleEntity.getRoleName());
        if (list.size() > 0) {
            return Callback.error("角色名称已经存在!");
        }
        String roleCode = roleEntity.getRoleCode();
        if (StringUtils.isNotEmpty(roleCode)){
            RoleEntity entity = roleDao.selectByRoleCode(roleCode);
            if (entity != null) {
                return Callback.error("角色代号已经存在!");
            }
        }
        int i = roleDao.insertSelective(roleEntity);
        return Callback.success(i > 0);
    }

    public Callback getRoleById(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        return Callback.success(roleDao.getRoleById(id));
    }

    public Callback updateRole(RoleEntity roleEntity) {
//        RoleEntity entity = roleDao.getRoleById(roleEntity.getId());
//        if (entity.getRoleName().equals(roleEntity.getRoleName())) {
//            return Callback.error("角色名称已经存在!");
//        }
//        if (entity.getRoleCode().equals(roleEntity.getRoleCode())) {
//            return Callback.error("角色代号已经存在!");
//        }
        int i = roleDao.updateByPrimaryKeySelective(roleEntity);
        return Callback.success(i > 0);
    }

    public Callback deleteRole(Integer roleId) {
        if (roleId == null || roleId <= 0) {
            return Callback.error("请选择要删除的角色");
        }
        List<UserRole> roleList = userRoleMapper.selectByRoleId(roleId);
        if (roleList.size() > 0) {
            return Callback.error("该角色在使用中，无法删除，请确认");
        }
        rolePermissionMapper.deleteByRoleId(roleId);
        return delete(roleId);
    }

    public Callback<BaseVO<RoleEntity>> listRoles(Integer page, Integer pageSize, Integer company,String searchKey) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        Page<RoleEntity> startPage = PageHelper.startPage(page, pageSize);
        List<RoleEntity> roleVoList = roleDao.listRoles(company,searchKey);
        BaseVO<RoleEntity> baseVO = new BaseVO<>(roleVoList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);

    }

    public Callback getPermissions(Integer roleId) {
        boolean isSelected = true;
        List<RolePermissionEntity> rolePermissions = new ArrayList<>();
        if (roleId == null || roleId < 1) {
            isSelected = false;
        }
        // 根据角色获取到权限列表
        List<PermissionVO> permissionResponse  = permissionDao.getPermissions();
        if (isSelected) {
            rolePermissions = permissionDao.getRolePermissions(roleId);
        }
        // 获取一级权限
        List<PermissionVO> firstPermissionRespons = new ArrayList<>();
        for (PermissionVO permissionVO : permissionResponse) {
            if (permissionVO.getLevel() == 1) {
                for (RolePermissionEntity rp : rolePermissions) {
                    if (rp.getPermissionId().equals(permissionVO.getId())) {
                        permissionVO.setSelected(true);
                    }
                }
                firstPermissionRespons.add(permissionVO);
            }
        }
        // 获取二级权限
        for (PermissionVO p1 : permissionResponse) {
            if (p1.getLevel() == 2) {
                for (PermissionVO p2 : firstPermissionRespons) {
                    if (p2.getId().equals(p1.getParentId())) {
                        for (RolePermissionEntity rp : rolePermissions) {
                            if (rp.getPermissionId().equals(p1.getId())) {
                                p1.setSelected(true);
                            }
                        }
                        p2.getChildPermissions().add(p1);
                    }
                }
            }
        }
        // 获取三级权限
        for (PermissionVO p1 : permissionResponse) {
            if (p1.getLevel() == 3) {
                for (PermissionVO p2 : firstPermissionRespons) {
                    for (PermissionVO p3 : p2.getChildPermissions()) {
                        if (p3.getId().equals(p1.getParentId())) {
                            for (RolePermissionEntity rp : rolePermissions) {
                                if (rp.getPermissionId().equals(p1.getId())) {
                                    p1.setSelected(true);
                                }
                            }
                            p3.getChildPermissions().add(p1);
                        }
                    }
                }
            }
        }
        // 排序
        for (PermissionVO p1 : firstPermissionRespons) {
            for (PermissionVO p2 : p1.getChildPermissions()) {
                Collections.sort(p2.getChildPermissions());
            }
            Collections.sort(p1.getChildPermissions());
        }
        Collections.sort(firstPermissionRespons);
        return Callback.success(firstPermissionRespons);
    }

    public Callback saveRolePermission(Integer roleId, Integer[] pIds) {
        if (roleId == null) {
            return Callback.error("角色id不能为空");
        }
        if (pIds.length == 0) {
            permissionDao.delRolePermission(roleId);
            return Callback.success();
        }
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, pIds);
        permissionDao.delRolePermission(roleId);
        int result = permissionDao.addRolePermission(roleId, list);
        if (result < 1) {
            return Callback.error("操作失败");
        }
        return Callback.success();
    }

    public Callback<List<RoleEntity>> listRoleList(Integer company) {
        List<RoleEntity>  roleEntityList = roleDao.listRoleList(company);
        return Callback.success(roleEntityList);
    }
}
