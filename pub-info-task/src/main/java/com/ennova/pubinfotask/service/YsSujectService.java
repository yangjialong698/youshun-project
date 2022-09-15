package com.ennova.pubinfotask.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.dao.YsSujectMapper;
import com.ennova.pubinfotask.entity.YsSuject;
import com.ennova.pubinfotask.utils.BeanConvertUtils;
import com.ennova.pubinfotask.vo.CurrentUserVO;
import com.ennova.pubinfotask.vo.YsSujectVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsSujectService{

    private final YsSujectMapper ysSujectMapper;
    private final UserMapper userMapper;
    private final HttpServletRequest req;

    public Callback deleteById(Integer id) {
        CurrentUserVO currentUser = getCurrentUser(req);
        if (currentUser.getRoleCode().equalsIgnoreCase("cost_accountant")) {
            YsSuject build = YsSuject.builder().delFlag(1).id(id).build();
            int i = ysSujectMapper.updateByPrimaryKeySelective(build);
            return i > 0 ? Callback.success() : Callback.error("删除失败");
        }
        return Callback.error("无权限");
    }

    
    public Callback insertSelective(YsSujectVO vo) {
        CurrentUserVO currentUser = getCurrentUser(req);
        if (currentUser.getRoleCode().equalsIgnoreCase("cost_accountant")) {
            YsSuject ysSuject = BeanConvertUtils.convertTo(vo, YsSuject::new);
            ysSuject.setCreateTime(LocalDateTime.now());
            ysSuject.setDelFlag(0);
            List<YsSuject> ysSujects = ysSujectMapper.selectBySuject(vo.getSuject());
            if (ysSujects.size() > 0) {
                return Callback.error("科目已存在");
            }
            int i = ysSujectMapper.insertSelective(ysSuject);
            return i > 0 ? Callback.success() : Callback.error("添加失败");
        }
        return Callback.error("无权限");
    }


    public Callback selectByIdAndDelFlag(Integer id){
        CurrentUserVO currentUser = getCurrentUser(req);
        if (currentUser.getRoleCode().equalsIgnoreCase("cost_accountant")) {
            YsSuject ysSuject = ysSujectMapper.selectById(id);
            return Callback.success(ysSuject);
        }
        return Callback.error("无权限");
    }

    public Callback updateByPrimaryKeySelective(YsSujectVO vo) {
        CurrentUserVO currentUser = getCurrentUser(req);
        if (currentUser.getRoleCode().equalsIgnoreCase("cost_accountant")) {
            YsSuject ysSuject = BeanConvertUtils.convertTo(vo, YsSuject::new);
            ysSuject.setUpdateTime(LocalDateTime.now());
            List<YsSuject> ysSujects = ysSujectMapper.selectExist(ysSuject.getSuject(), ysSuject.getId());
            if (ysSujects.size() > 0) {
                return Callback.error("科目已存在");
            }
            int i = ysSujectMapper.updateByPrimaryKeySelective(ysSuject);
            return i > 0 ? Callback.success() : Callback.error("修改失败");
        }
        return Callback.error("无权限");
    }

    // 访问日志明细
    public Callback<BaseVO<YsSuject>> selectDetailList(Integer page, Integer pageSize, String suject) {
        CurrentUserVO currentUser = getCurrentUser(req);
        Page<YsSuject> startPage = PageMethod.startPage(page, pageSize);
        List<YsSuject> sujectList = Lists.newArrayList();
        if (currentUser.getRoleCode().equalsIgnoreCase("cost_accountant")) {
            sujectList = ysSujectMapper.selectBySujectList(suject);
        }
        BaseVO<YsSuject> baseVO = new BaseVO<>(sujectList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public CurrentUserVO getCurrentUser(HttpServletRequest req){
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        return currentUserVO;
    }




}
