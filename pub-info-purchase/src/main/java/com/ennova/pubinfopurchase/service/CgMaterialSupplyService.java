package com.ennova.pubinfopurchase.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.CgMaterialSupplyMapper;
import com.ennova.pubinfopurchase.entity.CgMaterialSupply;
import com.ennova.pubinfopurchase.utils.BeanConvertUtils;
import com.ennova.pubinfopurchase.vo.CgMaterialSupplyVO;
import com.ennova.pubinfopurchase.vo.PrdInfoVO;
import com.ennova.pubinfopurchase.vo.SupplierInfoVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/1
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class CgMaterialSupplyService {

    private final HttpServletRequest request;
    private final CgMaterialSupplyMapper cgMaterialSupplyMapper;

    public Callback<List<PrdInfoVO>> getPrdName(String prdNo) {
        /*if (StringUtils.isEmpty(productNo)) {
            return Callback.error("请输入品号");
        }*/
        List<PrdInfoVO> erpPrdInfos = cgMaterialSupplyMapper.selectByPrdNo(prdNo);
        return Callback.success(erpPrdInfos);
    }

    public Callback<List<SupplierInfoVO>> getSupplierInfo(String supplierNo) {
        List<SupplierInfoVO> supplierInfoVOS = cgMaterialSupplyMapper.selectBySupplierNo(supplierNo);
        return Callback.success(supplierInfoVOS);
    }

    public Callback insertOrUpdate(CgMaterialSupplyVO cgMaterialSupplyVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        CgMaterialSupply cgMaterialSupply = new CgMaterialSupply();
        BeanConvertUtils.copyProperties(cgMaterialSupplyVO, cgMaterialSupply);
        if (cgMaterialSupplyVO.getId() != null) {
            //修改采集信息
            CgMaterialSupply cgMaterialSupplyOriginal = cgMaterialSupplyMapper.selectByPrimaryKey(cgMaterialSupplyVO.getId());
            if (cgMaterialSupplyOriginal != null) {
                cgMaterialSupply.setUpdateTime(LocalDateTime.now());
                cgMaterialSupplyMapper.updateByPrimaryKey(cgMaterialSupply);
                return Callback.success(true);
            }
        } else {
            //添加物料采购供需信息
            cgMaterialSupply.setCreateTime(LocalDateTime.now());
            cgMaterialSupply.setDelFlag(0);
            cgMaterialSupplyMapper.insertSelective(cgMaterialSupply);
            return Callback.success(true);
        }
        return Callback.error(2, "数据处理失败!");
    }

    public Callback<BaseVO<CgMaterialSupplyVO>> selectMaterialSupply(Integer page, Integer pageSize) {
        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<CgMaterialSupplyVO> materialSupplyVOList = cgMaterialSupplyMapper.selectMaterialSupplyInfo();
        BaseVO<CgMaterialSupplyVO> baseVO = new BaseVO<>(materialSupplyVOList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<CgMaterialSupplyVO> getDetail(Integer id) {
        CgMaterialSupply cgMaterialSupply = cgMaterialSupplyMapper.selectByPrimaryKey(id);
        CgMaterialSupplyVO cgMaterialSupplyVO = BeanConvertUtils.convertTo(cgMaterialSupply, CgMaterialSupplyVO::new);
        if (cgMaterialSupply != null) {
            return Callback.success(cgMaterialSupplyVO);
        } else {
            return Callback.error("未找到采购供需信息");
        }
    }

    public Callback delete(Integer id) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CgMaterialSupply cgMaterialSupply = cgMaterialSupplyMapper.selectByPrimaryKey(id);
        if (cgMaterialSupply != null) {
            cgMaterialSupplyMapper.deleteByPrimaryKey(cgMaterialSupply.getId());
            return Callback.success(true);
        }
        return Callback.error(2, "删除失败");
    }

}
