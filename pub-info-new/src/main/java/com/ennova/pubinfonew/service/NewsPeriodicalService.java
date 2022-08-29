package com.ennova.pubinfonew.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfonew.dao.NewsCommentMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalFileMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalMapper;
import com.ennova.pubinfonew.entity.NewsComment;
import com.ennova.pubinfonew.entity.NewsPeriodical;
import com.ennova.pubinfonew.vo.NewsPeriodicalVO;
import com.ennova.pubinfonew.vo.NewsVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
    private final NewsCommentMapper newsCommentMapper;
    private final NewsPeriodicalFileMapper newsPeriodicalFileMapper;

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
                newsPeriodical.setDivPosition(newsPeriodicals.getDivPosition());
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
            List<NewsComment> newsComments = newsCommentMapper.selectCommentByNewId(newsPeriodical.getId());
            if (CollectionUtils.isNotEmpty(newsComments)){
                newsComments.forEach(newsComment -> {
                    newsCommentMapper.deleteComment(newsComment.getId());
                });
            }
            List<NewsVO> newsFiles= newsPeriodicalFileMapper.selectPeriodicalFile(newsPeriodical.getPeriodicalNum(), newsPeriodical.getEditionNum());
            if (CollectionUtils.isNotEmpty(newsFiles)){
                newsFiles.forEach(newsVO -> {
                    newsPeriodicalFileMapper.deletePeriodicalFile(newsVO.getId());
                });
            }
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

}
