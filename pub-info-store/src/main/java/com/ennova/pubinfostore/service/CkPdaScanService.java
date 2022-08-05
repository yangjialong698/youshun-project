package com.ennova.pubinfostore.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfostore.dao.CkPadScanMapper;
import com.ennova.pubinfostore.entity.CkPdaScan;
import com.ennova.pubinfostore.vo.CkPdaScanVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/1
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class CkPdaScanService {

    private final HttpServletRequest request;
    private final CkPadScanMapper ckPadScanMapper;

    public Callback insert(CkPdaScanVO ckPdaScanVO){

        CkPdaScan ckPdaScan = new CkPdaScan();
        BeanUtils.copyProperties(ckPdaScanVO, ckPdaScan);
        //雪花算法(时间戳、工作机器id、)
        Snowflake snowflake= IdUtil.getSnowflake(4,4);
        //新增条形码比对入库
        ckPdaScan.setId(snowflake.nextId());
        ckPdaScan.setCreateTime(new Date());
        int i = ckPadScanMapper.insert(ckPdaScan);
        if (i > 0){
            return Callback.success("新增条形码比对入库成功");
        }
        return Callback.error("入库失败");
    }

    public Callback<BaseVO<CkPdaScanVO>> selectPdaInfo(Integer page, Integer pageSize, String barCode){
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        Page<LinkedHashMap> startPage = PageMethod.startPage(page, pageSize);
        List<CkPdaScanVO> ckPdaScanVOS = ckPadScanMapper.selectPdaInfo(barCode);
        BaseVO<CkPdaScanVO> baseVO = new BaseVO<>(ckPdaScanVOS, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

}
