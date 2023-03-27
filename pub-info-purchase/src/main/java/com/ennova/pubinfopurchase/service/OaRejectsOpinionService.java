package com.ennova.pubinfopurchase.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfopurchase.dao.OaRejectsMapper;
import com.ennova.pubinfopurchase.dao.OaRejectsOpinionMapper;
import com.ennova.pubinfopurchase.entity.OaRejects;
import com.ennova.pubinfopurchase.entity.OaRejectsOpinion;
import com.ennova.pubinfopurchase.utils.BeanConvertUtils;
import com.ennova.pubinfopurchase.vo.OaRejectsOpinionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OaRejectsOpinionService {

    private final OaRejectsOpinionMapper oaRejectsOpinionMapper;
    private final OaRejectsMapper oaRejectsMapper;
    private final HttpServletRequest request;

    public Callback insertRejectsOpinion(List<OaRejectsOpinionVO> oaRejectsOpinionVOS) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        List<Integer> rejectsIds = oaRejectsOpinionVOS.stream().map(oaRejectsOpinionVO -> oaRejectsOpinionVO.getRejectsId()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(oaRejectsOpinionVOS) && CollectionUtils.isNotEmpty(rejectsIds)) {
            for (OaRejectsOpinionVO oaRejectsOpinionVO : oaRejectsOpinionVOS) {
                OaRejectsOpinion oaRejectsOpinion = new OaRejectsOpinion();
                BeanConvertUtils.copyProperties(oaRejectsOpinionVO, oaRejectsOpinion);
                oaRejectsOpinion.setCreateTime(LocalDateTime.now());
                oaRejectsOpinion.setDelFlag(0);
                oaRejectsOpinionMapper.insertSelective(oaRejectsOpinion);
            }
            List<Integer> collect = oaRejectsOpinionVOS.stream().map(oaRejectsOpinionVO -> oaRejectsOpinionVO.getRejectsId()).limit(1).collect(Collectors.toList());
            List<Integer> setpStaus = oaRejectsOpinionVOS.stream().map(oaRejectsOpinionVO -> oaRejectsOpinionVO.getSetpStaus()).limit(1).collect(Collectors.toList());
            List<String> opinionUsers = oaRejectsOpinionVOS.stream().map(oaRejectsOpinionVO -> oaRejectsOpinionVO.getOpinionUser()).collect(Collectors.toList());
            String mobileStr = StringUtils.strip(opinionUsers.toString(), "[]").replace(" ", "");
            OaRejects build = OaRejects.builder().id(collect.get(0)).setpStaus(setpStaus.get(0)).transactor(mobileStr).build();
            int i = oaRejectsMapper.updateByPrimaryKeySelective(build);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "数据处理失败!");
    }


    public Callback countersignOpinion(OaRejectsOpinionVO oaRejectsOpinionVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        if (ObjectUtils.isNotEmpty(oaRejectsOpinionVO) && oaRejectsOpinionVO.getRejectsId() != null) {
            List<OaRejectsOpinionVO> oaRejectsOpinions = oaRejectsOpinionMapper.selectByRejectsId(oaRejectsOpinionVO.getRejectsId());
            List<Integer> opinionUserIds = oaRejectsOpinions.stream().map(oaRejectsOpinion -> oaRejectsOpinion.getOpinionUserId()).collect(Collectors.toList());
            if (opinionUserIds.stream().anyMatch(o -> o.equals(userVo.getId()))) {
                OaRejectsOpinion oaRejectsOpinion = new OaRejectsOpinion();
                oaRejectsOpinion.setId(oaRejectsOpinionVO.getId());
                oaRejectsOpinion.setOpinionContent(oaRejectsOpinionVO.getOpinionContent());
                oaRejectsOpinion.setPublishTime(LocalDateTime.now());
                int i = oaRejectsOpinionMapper.updateByPrimaryKeySelective(oaRejectsOpinion);
                // 查询会签表中没有进行会签的人员更新不良品处理单最新未办理人员字段
                List<OaRejectsOpinionVO> oaRejectsOpinionss = oaRejectsOpinionMapper.selectByRejectsId(oaRejectsOpinionVO.getRejectsId());
                List<Integer> collect = oaRejectsOpinionss.stream().map(v -> v.getRejectsId()).limit(1).collect(Collectors.toList());
                String opinionUsers = oaRejectsOpinionss.stream().filter(v -> StringUtils.isEmpty(v.getOpinionContent())).map(v -> v.getOpinionUser()).collect(Collectors.joining(","));
                OaRejects build = OaRejects.builder().id(collect.get(0)).transactor(opinionUsers).build();
                int j = oaRejectsMapper.updateByPrimaryKeySelective(build);
                if (i > 0 && j > 0) {
                    return Callback.success(true);
                }
            }else {
                return Callback.error(2,"您不在会签人列表中");
            }
        }
        return Callback.error(2, "数据处理失败!");
    }
}
