package com.ennova.pubinfonew.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfonew.dao.NewsCommentMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalMapper;
import com.ennova.pubinfonew.dao.UserMapper;
import com.ennova.pubinfonew.dto.NewsCommentDto;
import com.ennova.pubinfonew.entity.NewsComment;
import com.ennova.pubinfonew.entity.NewsPeriodical;
import com.ennova.pubinfonew.vo.NewsCommentVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class NewsCommentService {

    private final HttpServletRequest request;
    private final NewsCommentMapper newsCommentMapper;
    private final UserMapper userMapper;
    private final NewsPeriodicalMapper newsPeriodicalMapper;


    public Callback publishComment(NewsCommentDto newsCommentDto) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        NewsComment newsComment = new NewsComment();
        BeanUtils.copyProperties(newsCommentDto, newsComment);
        NewsPeriodical newsPeriodical = newsPeriodicalMapper.selectNewIdByDivPosition(newsComment.getDivPosition());
        if (ObjectUtils.isEmpty(newsPeriodical)){
            return Callback.error(2, "报刊标题对应网页名称未找到");
        }
        newsComment.setNewId(newsPeriodical.getId());
        newsComment.setCommentUserId(userVo.getId());
        newsComment.setCreateTime(new Date());
        int i = newsCommentMapper.insertSelective(newsComment);
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error("发表评论失败");
    }

    //评论置顶
    public Callback stickComment(Integer id) {

        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        // 判断ID是不是数字
        if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
            return Callback.error(2, "ID不能为空");
        }
        int i = newsCommentMapper.updateSortIdById(id);
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error("评论置顶失败");
    }

    //取消评论置顶
    public Callback noStickComment(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        // 判断ID是不是数字
        if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
            return Callback.error(2, "ID不能为空");
        }
        int i = newsCommentMapper.updateSortIdsById(id);
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error("评论取消置顶失败");
    }

    public Callback<BaseVO<NewsCommentVO>> getCommentList(Integer page, Integer pageSize, String editionTitle){

        Page<NewsCommentVO> startPage = PageMethod.startPage(page, pageSize);
        List<NewsCommentVO> newsCommentVOS = newsCommentMapper.selectCommentByEditionTitle();
        for (NewsCommentVO newsCommentVO : newsCommentVOS) {
            newsCommentVO.setCommentUser(userMapper.selectCurrentUser(newsCommentVO.getCommentUserId()).getUsername());
            newsCommentVO.setEditionTitle(newsPeriodicalMapper.selectByPrimaryKey(newsCommentVO.getNewId()).getEditionTitle());
        }
        List<NewsCommentVO> collect = newsCommentVOS.stream().filter(newsCommentVO -> newsCommentVO.getEditionTitle().contains(editionTitle)).collect(Collectors.toList());
        BaseVO<NewsCommentVO> baseVO = new BaseVO<>(collect, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback deleteComment(Integer id){
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        // 判断ID是不是数字
        if (id == null || !StringUtils.isNumeric(String.valueOf(id))) {
            return Callback.error(2, "ID不能为空");
        }
        int i = newsCommentMapper.deleteComment(id);
        if (i > 0){
            return Callback.success(true);
        }
        return Callback.error("删除失败");
    }
}
