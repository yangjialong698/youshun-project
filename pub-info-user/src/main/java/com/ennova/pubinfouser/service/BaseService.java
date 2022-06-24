package com.ennova.pubinfouser.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfouser.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService<T> {

    @Autowired
    private BaseDao<T> baseDao;

    // 添加
    protected Callback insert(T t) {
        int result = baseDao.insert(t);
        if (result < 1) {
            return Callback.error("操作失败");
        }
        return Callback.success();
    }

    // 修改
    protected Callback update(T t) {
        int result = baseDao.update(t);
        if (result < 1) {
            return Callback.error("操作失败");
        }
        return Callback.success();
    }


    public Callback delete(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        int result = baseDao.delete(id);
        if (result < 1) {
            return Callback.error("操作失败");
        }
        return Callback.success();
    }

    public Callback status(Integer id, Integer status) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        if (status == null || status < 0 || status > 1) {
            return Callback.error("参数错误");
        }
        int result = baseDao.status(id, status);
        if (result < 1) {
            return Callback.error("操作失败");
        }
        return Callback.success();
    }

}
