package com.ennova.pubinfonew.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfonew.dao.NewsCommentMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalFileMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalMapper;
import com.ennova.pubinfonew.dao.NewsPeriodicalPictureMapper;
import com.ennova.pubinfonew.entity.NewsComment;
import com.ennova.pubinfonew.entity.NewsPeriodical;
import com.ennova.pubinfonew.vo.NewsPeriodicalVO;
import com.ennova.pubinfonew.vo.NewsVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class NewsPeriodicalService {

    private final HttpServletRequest request;
    private final NewsPeriodicalMapper newsPeriodicalMapper;
    private final NewsCommentMapper newsCommentMapper;
    private final NewsPeriodicalFileMapper newsPeriodicalFileMapper;
    private final NewsPeriodicalPictureMapper newsPeriodicalPictureMapper;

    public Callback insertOrUpdate(NewsPeriodicalVO newsPeriodicalVO) throws IOException {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        NewsPeriodical newsPeriodical = new NewsPeriodical();
        BeanUtils.copyProperties(newsPeriodicalVO, newsPeriodical);
        if (newsPeriodicalVO.getId() != null) {
            //修改新闻期刊
            NewsPeriodical newsPeriodicals = newsPeriodicalMapper.selectByPrimaryKey(newsPeriodicalVO.getId());
            newsPeriodical.setUserId(userVo.getId());
            if (newsPeriodicals != null) {
                newsPeriodical.setCreateTime(new Date());
                int i = newsPeriodicalMapper.updateByPrimaryKeyWithBLOBs(newsPeriodical);
                if (i > 0) {
                    return Callback.success(true);
                }
                return Callback.error(2, "数据处理失败!");
            }
            return Callback.error("数据已经被删除");
        } else {
            //发布新闻期刊
            newsPeriodical.setCreateTime(new Date());
            newsPeriodical.setUserId(userVo.getId());
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

    //需要验证登录
    public Callback<BaseVO<NewsPeriodicalVO>> selectAllNewPeriodicals(Integer page, Integer pageSize, Integer periodicalNum, Integer editionNum, String editionTitle) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
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
            List<NewsComment> newsComments = newsCommentMapper.selectCommentByNewsId(newsPeriodical.getId());
            if (CollectionUtils.isNotEmpty(newsComments)){
                newsComments.forEach(newsComment -> {
                    newsCommentMapper.deleteComment(newsComment.getId());
                });
            }
           /* List<NewsVO> newsFiles= newsPeriodicalFileMapper.selectPeriodicalFile(newsPeriodical.getPeriodicalNum(), newsPeriodical.getEditionNum());
            if (CollectionUtils.isNotEmpty(newsFiles)){
                newsFiles.forEach(newsVO -> {
                    newsPeriodicalFileMapper.deletePeriodicalFile(newsVO.getId());
                });
            }*/
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

    public Callback<NewsPeriodical> getNewsDetail(Integer id) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        NewsPeriodical newsPeriodical = newsPeriodicalMapper.selectByPrimaryKey(id);
        return Callback.success(newsPeriodical);
    }

    public Callback<List<Integer>> getNoSealPeriodicalNum() {
        List<NewsVO> newsVOS = newsPeriodicalPictureMapper.selectPeriodicalPicture(null, null);
        List<Integer> periodicalNums = newsVOS.stream().filter(newsVO -> newsVO.getPeriodicalNum() != null).map(newsVO -> newsVO.getPeriodicalNum()).collect(Collectors.toList());
        List<Integer> collect = periodicalNums.stream().distinct().collect(Collectors.toList());
        //已封版版数
        List<Integer> sealNums = Lists.newArrayList(1,2,3,4);
        //已封板期数
        List<Integer> sealPeriodicalNums = Lists.newArrayList();
        for (Integer periodicalNum : collect) {
            List<NewsVO> newsVOS1 = newsPeriodicalPictureMapper.selectPeriodicalPicture(periodicalNum, null);
            List<Integer> editionNums = newsVOS1.stream().filter(newsVO -> newsVO.getPeriodicalNum() != null).map(newsVO -> newsVO.getEditionNum()).collect(Collectors.toList());
            if (editionNums.containsAll(sealNums)){
                sealPeriodicalNums.add(periodicalNum);
            }
        }
        //移除封板的期数
        collect.removeAll(sealPeriodicalNums);
        return Callback.success(collect);
    }

    public Callback<NewsPeriodicalVO> getPeriodicalAndEditionNums(Integer periodicalNum, Integer editionNum) {
        NewsPeriodicalVO newsPeriodicalVO = NewsPeriodicalVO.builder().build();
        if (periodicalNum!= null && editionNum!=null) {
            List<NewsPeriodicalVO> UpPeriodicals = newsPeriodicalMapper.selectAllNewPeriodical(periodicalNum - 1, null, null);
            if (UpPeriodicals.size() > 0) {
                newsPeriodicalVO.setUpPeriodical(true);
            } else {
                newsPeriodicalVO.setUpPeriodical(false);
            }
            List<NewsPeriodicalVO> downPeriodicals = newsPeriodicalMapper.selectAllNewPeriodical(periodicalNum + 1, null, null);
            if (downPeriodicals.size() > 0) {
                newsPeriodicalVO.setDownPeriodical(true);
            } else {
                newsPeriodicalVO.setDownPeriodical(false);
            }
            List<NewsPeriodicalVO> upEditions = newsPeriodicalMapper.selectAllNewPeriodical(periodicalNum, editionNum - 1, null);
            if (upEditions.size() > 0) {
                newsPeriodicalVO.setUpEdition(true);
            } else {
                newsPeriodicalVO.setUpEdition(false);
            }
            List<NewsPeriodicalVO> downEditions = newsPeriodicalMapper.selectAllNewPeriodical(periodicalNum, editionNum + 1, null);
            if (downEditions.size() > 0) {
                newsPeriodicalVO.setDownEdition(true);
            } else {
                newsPeriodicalVO.setDownEdition(false);
            }
        }else {
            newsPeriodicalVO = NewsPeriodicalVO.builder().upPeriodical(false).downPeriodical(false).upEdition(false).downEdition(false).build();
        }
        return Callback.success(newsPeriodicalVO);
    }

    public Callback<List<Integer>> getPeriodicalNum() {
        List<NewsPeriodicalVO> newsPeriodicalVOS = newsPeriodicalMapper.selectAllNewPeriodical(null, null, null);
        List<NewsPeriodicalVO> collect = newsPeriodicalVOS.stream().filter(newsPeriodicalVO -> newsPeriodicalVO.getPeriodicalNum() != null).sorted(Comparator.comparing(NewsPeriodicalVO::getPeriodicalNum).reversed()).collect(Collectors.toList());
        List<Integer> periodicalNums = collect.stream().map(newsVO -> newsVO.getPeriodicalNum()).collect(Collectors.toList());
        List<Integer> collect1 = periodicalNums.stream().distinct().collect(Collectors.toList());
        return Callback.success(collect1);
    }

    public Callback<List<Integer>> getEditionNum(Integer periodicalNum) {
        List<NewsPeriodicalVO> newsPeriodicalVOS = newsPeriodicalMapper.selectAllNewPeriodical(periodicalNum, null, null);
        List<NewsPeriodicalVO> collect = newsPeriodicalVOS.stream().filter(newsPeriodicalVO -> newsPeriodicalVO.getPeriodicalNum() != null).sorted(Comparator.comparing(NewsPeriodicalVO::getEditionNum)).collect(Collectors.toList());
        List<Integer> editionNums = collect.stream().map(newsVO -> newsVO.getEditionNum()).collect(Collectors.toList());
        List<Integer> collect1 = editionNums.stream().distinct().collect(Collectors.toList());
        return Callback.success(collect1);
    }
}
