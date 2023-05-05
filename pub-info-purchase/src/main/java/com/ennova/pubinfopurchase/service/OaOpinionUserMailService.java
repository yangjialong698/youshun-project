package com.ennova.pubinfopurchase.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.OaOperationLogMapper;
import com.ennova.pubinfopurchase.dao.OaOpinionUserMailMapper;
import com.ennova.pubinfopurchase.dao.OaRejectsOpinionMapper;
import com.ennova.pubinfopurchase.dao.UserMapper;
import com.ennova.pubinfopurchase.entity.OaOperationLog;
import com.ennova.pubinfopurchase.entity.OaOpinionUserMail;
import com.ennova.pubinfopurchase.entity.OaRejectsOpinion;
import com.ennova.pubinfopurchase.utils.BeanConvertUtils;
import com.ennova.pubinfopurchase.vo.OaOpinionUserMailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/5/5
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OaOpinionUserMailService {

    private final HttpServletRequest request;
    private final OaOpinionUserMailMapper oaOpinionUserMailMapper;
    private final OaRejectsOpinionMapper oaRejectsOpinionMapper;
    private final OaOperationLogMapper oaOperationLogMapper;
    private final UserMapper userMapper;

    public Callback insertOrUpdateOpinionUser(OaOpinionUserMailVO oaOpinionUserMailVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        OaOpinionUserMail oaOpinionUserMail = new OaOpinionUserMail();
        BeanConvertUtils.copyProperties(oaOpinionUserMailVO, oaOpinionUserMail);

        if (ObjectUtils.isNotEmpty(oaOpinionUserMailVO) && oaOpinionUserMailVO.getId() != null) {
            oaOpinionUserMailVO.setUpdateTime(LocalDateTime.now());
            oaOpinionUserMailMapper.updateByPrimaryKeySelective(oaOpinionUserMail);

            OaOpinionUserMail oaOpinionUser = oaOpinionUserMailMapper.selectByPrimaryKey(oaOpinionUserMail.getUserId());
            if (StringUtils.isNotEmpty(oaOpinionUser.getMandator()) && ObjectUtils.isNotEmpty(oaOpinionUser.getMandatorId()) && !oaOpinionUser.getMandator().equals(oaOpinionUserMailVO.getMandator())) {
                List<OaRejectsOpinion> oaRejectsOpinions = oaRejectsOpinionMapper.selectByOpinionUserIdAndOpinionUser(oaOpinionUser.getUserId(), oaOpinionUser.getName());
                for (OaRejectsOpinion oaRejectsOpinion : oaRejectsOpinions) {
                    oaRejectsOpinion.setOpinionUserId(oaOpinionUser.getMandatorId());
                    oaRejectsOpinion.setOpinionUser(oaOpinionUser.getMandator());
                    oaRejectsOpinionMapper.updateByPrimaryKeySelective(oaRejectsOpinion);
                }
            }

            OaOperationLog oaOperationLog = OaOperationLog.builder().userId(userVo.getId()).userName(userMapper.selectById(userVo.getId()).getUserName()).createTime(LocalDateTime.now()).description("修改会签人").build();
            oaOperationLogMapper.insertSelective(oaOperationLog);

            return Callback.success(true);
        } else {
            String name = oaOpinionUserMailMapper.selectByName(oaOpinionUserMail.getName()).getName();
            if (name != null) {
                return Callback.error("请勿重复添加会签人");
            }
            oaOpinionUserMail.setCreateTime(LocalDateTime.now());
            oaOpinionUserMail.setDelFlag(0);
            oaOpinionUserMailMapper.insertSelective(oaOpinionUserMail);

            OaOpinionUserMail oaOpinionUser = oaOpinionUserMailMapper.selectByPrimaryKey(oaOpinionUserMail.getUserId());
            if (StringUtils.isNotEmpty(oaOpinionUser.getMandator()) && ObjectUtils.isNotEmpty(oaOpinionUser.getMandatorId())) {
                List<OaRejectsOpinion> oaRejectsOpinions = oaRejectsOpinionMapper.selectByOpinionUserIdAndOpinionUser(oaOpinionUser.getUserId(), oaOpinionUser.getName());
                for (OaRejectsOpinion oaRejectsOpinion : oaRejectsOpinions) {
                    oaRejectsOpinion.setOpinionUserId(oaOpinionUser.getMandatorId());
                    oaRejectsOpinion.setOpinionUser(oaOpinionUser.getMandator());
                    oaRejectsOpinionMapper.updateByPrimaryKeySelective(oaRejectsOpinion);
                }
            }

            OaOperationLog oaOperationLog = OaOperationLog.builder().userId(userVo.getId()).userName(userMapper.selectById(userVo.getId()).getUserName()).createTime(LocalDateTime.now()).description("新增会签人").build();
            oaOperationLogMapper.insertSelective(oaOperationLog);

            return Callback.success(true);
        }
    }

    public Callback<List<OaOpinionUserMailVO>> selectOpinionUser(Integer setpStaus) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        List<OaOpinionUserMailVO> oaOpinionUserMailVOS = oaOpinionUserMailMapper.selectBySetpStaus(setpStaus);

        return Callback.success(oaOpinionUserMailVOS);
    }

    public Callback deleteOpinionUser(Integer id) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        // 判断ID是不是数字
        if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
            return Callback.error(2, "ID不能为空");
        }

        OaOpinionUserMail oaOpinionUserMail = oaOpinionUserMailMapper.selectByPrimaryKey(id);
        if (oaOpinionUserMail != null) {
            int i = oaOpinionUserMailMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }
}
