package com.ennova.pubinfopurchase.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.OaOperationLogMapper;
import com.ennova.pubinfopurchase.vo.OaOperationLogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/5/5
 * @version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OaOperationLogService{

    private final OaOperationLogMapper oaOperationLogMapper;
    private final HttpServletRequest request;

    public Callback<List<OaOperationLogVO>> selectOperationLogDetail(Integer id) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        List<OaOperationLogVO> oaOperationLogVOS = oaOperationLogMapper.selectByRejectsId(id);
        return Callback.success(oaOperationLogVOS);
    }

}
