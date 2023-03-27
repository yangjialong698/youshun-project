package com.ennova.pubinfopurchase.service;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.dao.TDeptDingMapper;
import com.ennova.pubinfopurchase.dao.TUserDingMapper;
import com.ennova.pubinfopurchase.entity.TDeptDing;
import com.ennova.pubinfopurchase.entity.TUserDing;
import com.ennova.pubinfopurchase.vo.TDeptDingVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OaUserService {
    @Autowired
    private TDeptDingMapper tDeptDingMapper;
    @Autowired
    private TUserDingMapper tUserDingMapper;

    private static final Long PRDNUM =  533915348l; // 生产部deptId
    private static final Long PARENTID =  1L;
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

    public Callback<TDeptDingVO> queryPrdDeptChildList() {
        TDeptDingVO tDeptDingVOFinal = new TDeptDingVO();
        List<TDeptDingVO> tDeptDingVOs = tDeptDingMapper.findListByParentId(PARENTID);
        tDeptDingVOFinal.setChildren(tDeptDingVOs);
          if (CollectionUtil.isNotEmpty(tDeptDingVOs)){
              for (TDeptDingVO tDeptDingVO : tDeptDingVOs) {
                  findLastDeptDingVoList(tDeptDingVO);
              }
          }
        return Callback.success(tDeptDingVOFinal);
    }

    private TDeptDingVO findLastDeptDingVoList(TDeptDingVO tDeptDingVO) {
        List<TDeptDingVO> tDeptDingList = tDeptDingMapper.findListByParentId(tDeptDingVO.getDeptId());
        if (CollectionUtil.isNotEmpty(tDeptDingList)){
            tDeptDingVO.setChildren(tDeptDingList);
            for (TDeptDingVO deptVO : tDeptDingList) {
                List<TDeptDingVO> tDeptDingChilds = tDeptDingMapper.findListByParentId(deptVO.getDeptId());
                if (CollectionUtil.isEmpty(tDeptDingChilds)){
                    deptVO.setChildren(tDeptDingChilds);
                }else {
                    findLastDeptDingVoList(deptVO);
                }
            }
        }
        return tDeptDingVO;
    }

    public Callback<List<TUserDing>> queryNameByManageIdOrDeptId(String manageId, String deptName, String deptId) {
        ArrayList<TUserDing> tUserDingList = new ArrayList<TUserDing>() ;
        if (StringUtils.isNotEmpty(deptName) && StringUtils.isNotEmpty(deptId) && deptName.contains("设备") || deptName.contains("工艺部") || deptName.contains("机加")){
            List<TUserDing> tUserDingList1 = tUserDingMapper.selectByDepartment(deptId);
            if (CollectionUtil.isNotEmpty(tUserDingList1)){
                return Callback.success(tUserDingList1);
            }
        }
        else {
            TUserDing tUserDing = tUserDingMapper.selectByUserId(manageId);
            if (null != tUserDing){
                tUserDingList.add(tUserDing);
                return Callback.success(tUserDingList);
            }
        }
        return Callback.success(tUserDingList);
    }
}
