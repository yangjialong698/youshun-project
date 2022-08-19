package com.ennova.pubinfonew.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfonew.dao.NewsPeriodicalMapper;
import com.ennova.pubinfonew.entity.NewsPeriodical;
import com.ennova.pubinfonew.vo.NewsPeriodicalVO;
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
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class NewsPeriodicalService {

    private final HttpServletRequest request;
    private final NewsPeriodicalMapper newsPeriodicalMapper;

    public Callback insertOrUpdate(NewsPeriodicalVO newsPeriodicalVO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        NewsPeriodical newsPeriodical = new NewsPeriodical();
        BeanUtils.copyProperties(newsPeriodicalVO, newsPeriodical);
        if (newsPeriodicalVO.getId() != null) {
            //修改新闻期刊
            NewsPeriodical newsPeriodicals = newsPeriodicalMapper.selectByPrimaryKey(newsPeriodicalVO.getId());
            if (newsPeriodicals != null) {
                newsPeriodical.setCreateTime(new Date());
                int i = newsPeriodicalMapper.updateByPrimaryKey(newsPeriodical);
                if (i > 0) {
                    return Callback.success(true);
                }
                return Callback.error(2, "数据处理失败!");
            }
            return Callback.error("数据已经被删除");
        } else {
            //发布新闻期刊
            newsPeriodical.setCreateTime(new Date());
            if (newsPeriodical != null) {
                int i = newsPeriodicalMapper.insertSelective(newsPeriodical);
                if (i > 0) {
                    return Callback.success(true);
                }
            }
            return Callback.error(2, "数据处理失败!");
        }
    }

    public Callback<BaseVO<NewsPeriodicalVO>> selectAllNewPeriodical(Integer page, Integer pageSize, Integer periodicalNum, Integer editionNum, String editionTitle) {
        Page<NewsPeriodicalVO> startPage = PageMethod.startPage(page, pageSize);
        List<NewsPeriodicalVO> newsPeriodicalVOS = newsPeriodicalMapper.selectAllNewPeriodical(periodicalNum, editionNum, editionTitle);
        BaseVO<NewsPeriodicalVO> newsPeriodicalVOBaseVO = new BaseVO<>(newsPeriodicalVOS, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(newsPeriodicalVOBaseVO);
    }

    public Callback delete(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        NewsPeriodical newsPeriodical = newsPeriodicalMapper.selectByPrimaryKey(id);
        if (newsPeriodical != null) {
            int i = newsPeriodicalMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }


}
