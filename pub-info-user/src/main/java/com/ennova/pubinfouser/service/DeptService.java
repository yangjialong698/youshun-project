package com.ennova.pubinfouser.service;


import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfouser.dao.DeptDao;
import com.ennova.pubinfouser.dao.UserDeptMapper;
import com.ennova.pubinfouser.entity.DeptEntity;
import com.ennova.pubinfouser.entity.UserDept;
import com.ennova.pubinfouser.vo.DeptVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-20
 */

@Service
@Slf4j
public class DeptService {

    @Autowired
    private DeptDao deptDao;
    @Autowired
    private UserDeptMapper userDeptMapper;

    public Callback addDept(DeptEntity deptEntity) {
        Date date = new Date();
        List<DeptEntity> list = deptDao.getDeptByName(deptEntity.getDeptName());
        if (list.size() > 0) {
            return Callback.error("部门名称已经存在!");
        }
        deptEntity.setCreateTime(date);
//        deptEntity.setCompany(53);
        int i = deptDao.insertSelective(deptEntity);
        return Callback.success(i > 0);
    }

    public Callback getDeptById(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        return Callback.success(deptDao.getDeptById(id));
    }

    public Callback updateDept(DeptEntity deptEntity) {
        Date date = new Date();
//        DeptEntity entity = deptDao.getDeptById(deptEntity.getId());
//        if (deptEntity != null && entity.getDeptName().equals(deptEntity.getDeptName())) {
//            return Callback.error("部门名称已经存在!");
//        }
        deptEntity.setUpdateTime(date);
        int i = deptDao.updateByPrimaryKeySelective(deptEntity);
        return Callback.success(i > 0);
    }

    public Callback deleteDept(Integer deptId) {
        if (deptId == null || deptId <= 0) {
            return Callback.error("参数错误");
        }
        List<UserDept> userDeptList = userDeptMapper.selectByDeptId(deptId);
        if (userDeptList.size()>0){
            return Callback.error("该部门在使用中，无法删除，请确认");
        }
        int result = deptDao.delete(deptId);
        if (result < 1) {
            return Callback.error("删除失败");
        }
        return Callback.success();
    }

    public Callback<BaseVO<DeptVO>> listDepts(Integer page, Integer pageSize, Integer company, String searchKey) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        Page<DeptVO> startPage = PageHelper.startPage(page, pageSize);
        List<DeptVO> deptVoList = deptDao.listDepts(company, searchKey);
        BaseVO<DeptVO> baseVO = new BaseVO<>(deptVoList,new PageUtil(pageSize,(int) startPage.getTotal(),page));
        return Callback.success(baseVO);
    }

    public Callback<List<DeptVO>> listDeptList(Integer company) {
        List<DeptVO> deptVoList = deptDao.listDeptList(company);
        return Callback.success(deptVoList);
    }

    public Callback<List<DeptVO>> listUserDeptList(Integer company) {
        List<DeptVO> deptVoList = deptDao.listUserDeptList(company);
        return Callback.success(deptVoList);
    }
}
