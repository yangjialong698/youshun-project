package com.ennova.pubinfostore.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfostore.dao.TDeptDingMapper;
import com.ennova.pubinfostore.dao.TUserDingMapper;
import com.ennova.pubinfostore.entity.TDeptDing;
import com.ennova.pubinfostore.entity.TUserDing;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrdUserManageService {
    @Autowired
    private TDeptDingMapper tDeptDingMapper;
    @Autowired
    private TUserDingMapper tUserDingMapper;

    private static final Long PRDNUM =  533915348l; // 生产部deptId

    public Callback<List<TDeptDing>> queryPrdDeptIds() {
        ArrayList<TDeptDing> tDeptDingArrayList = new ArrayList<>();
        List<TDeptDing> tDeptDings = tDeptDingMapper.selectAllByParentId(PRDNUM);
        if (CollectionUtil.isNotEmpty(tDeptDings)){
            for (TDeptDing tDeptDing : tDeptDings) {
                queryLastDeptDingList(tDeptDing,tDeptDingArrayList);
            }
        }
        return Callback.success(tDeptDingArrayList);
    }

    public List<TDeptDing> queryLastDeptDingList(TDeptDing tDeptDing,ArrayList<TDeptDing> tDeptDingArrayList){
        List<TDeptDing> tDeptDings = tDeptDingMapper.selectAllByParentId(tDeptDing.getDeptId());
        if (CollectionUtil.isNotEmpty(tDeptDings)){
            for (TDeptDing deptDing : tDeptDings) {
                List<TDeptDing> tDeptDing1 = tDeptDingMapper.selectAllByParentId(deptDing.getDeptId());
                if (CollectionUtil.isEmpty(tDeptDing1)){
                    tDeptDingArrayList.add(deptDing);
                    continue;
                }else {
                    queryLastDeptDingList(deptDing,tDeptDingArrayList);
                }
            }
        }else {
            tDeptDingArrayList.add(tDeptDing);
        }
        return tDeptDingArrayList;
    }

    public Callback<String> queryNameByManageId(String manageId) {
        String username = tUserDingMapper.selectByUserId(manageId);
        if (StringUtils.isNotEmpty(username)){
            return Callback.success(username);
        }
        return Callback.error("无该用户名称!");
    }
}
