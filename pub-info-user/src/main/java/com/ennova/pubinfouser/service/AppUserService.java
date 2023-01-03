package com.ennova.pubinfouser.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfouser.dao.AppUserDao;
import com.ennova.pubinfouser.dao.UserRoleMapper;
import com.ennova.pubinfouser.entity.UserRole;
import com.ennova.pubinfouser.vo.AppUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AppUserService {

    private final AppUserDao appUserDao;
    private final UserRoleMapper userRoleMapper;

    public Callback<AppUserVO> appLoginAll(String account, String password, String cid) {
        System.out.println("*****************路由测试***************" + account + " -- " + password + " --- " + cid);
        if (account == null || "".equals(account)) {
            return Callback.error("请输入账号");
        }
        if (password == null || "".equals(password)) {
            return Callback.error("请输入密码");
        }
        AppUserVO userVO = null;
        if (Validator.isMobile(account)) {
            userVO = appUserDao.getUserInfoByMobile(account);
            if (userVO == null) {
                return Callback.error("手机号码未注册");
            }
        } else if (account.startsWith("U") && account.length() == 8) {
            userVO = appUserDao.getUserInfoByJobNum(account);
            if (userVO == null) {
                return Callback.error("工号未注册");
            }
        }
        List<UserRole> userRoleList = userRoleMapper.selectByUserId(userVO.getId());
        UserRole userRole = null;
        if (CollectionUtil.isNotEmpty(userRoleList)) {
            userRole = userRoleList.get(0);
        }
        // 验证密码是否正确
        try {
            password = md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!password.equals(userVO.getPassword())) {
            return Callback.error("手机号或密码错误，请重试");
        }
        if (userVO.getStatus().equals("1")) {
            return Callback.error("账号已被禁用，请确认");
        }
        //生成token
        String token = JWTUtil.generateTokenForLog(account, userVO.getId(), userVO.getCompany().toString());
        //生成刷新token
        String refreshToken = JWTUtil.generateRefToken(account, userVO.getId(), userVO.getCompany().toString());
        userVO.setToken(token);
        userVO.setRefreshToken(refreshToken);
        userVO.setPassword("");
        return Callback.success(userVO);
    }

    public Callback<List<AppUserVO>> listAppUsers() {
        List<AppUserVO> userVOList = appUserDao.listAppUsers().stream().filter(userVO -> StringUtils.isNotEmpty(userVO.getUserId())).collect(Collectors.toList());
        return Callback.success(userVOList);
    }

}
