package com.ennova.pubinfostore.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.AssembleExcelWriteUtil;
import com.ennova.pubinfocommon.utils.FileNameUtils;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfostore.dao.ScAssembleQualityIssueMapper;
import com.ennova.pubinfostore.entity.ScAssembleQualityIssue;
import com.ennova.pubinfostore.utils.BeanConvertUtils;
import com.ennova.pubinfostore.vo.ScAssembleQualityIssueVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ScAssembleQualityIssueService {

    private final ScAssembleQualityIssueMapper scAssembleQualityIssueMapper;

    private final HttpServletResponse response;

    public Callback insertOrUpdate(@NonNull ScAssembleQualityIssueVO scAssembleQualityIssueVO) {

        ScAssembleQualityIssue scAssembleQualityIssue = BeanConvertUtils.convertTo(scAssembleQualityIssueVO, ScAssembleQualityIssue::new);
        if (scAssembleQualityIssue.getId() != null) {
            ScAssembleQualityIssue sc = scAssembleQualityIssueMapper.selectByPrimaryKey(scAssembleQualityIssueVO.getId());
            if (sc != null) {
                scAssembleQualityIssue.setUpdateTime(new Date());
            }
            int i = scAssembleQualityIssueMapper.updateByPrimaryKeySelective(scAssembleQualityIssue);
            if (i > 0) {
                return Callback.success("修改装配每日现场质量异常信息成功!");
            }
        } else {
            scAssembleQualityIssue.setDelFlag(0);
            int count = scAssembleQualityIssueMapper.insertSelective(scAssembleQualityIssue);
            if (count > 0) {
                return Callback.success("新增装配每日现场质量异常信息成功!");
            }
        }
        return Callback.error(2, "数据处理失败!");
    }

    public Callback<BaseVO<ScAssembleQualityIssueVO>> selectAssembleInfoList(Integer page, Integer pageSize, String startTime, String endTime, String productName) {
        Page<ScAssembleQualityIssueVO> startPage = PageHelper.startPage(page, pageSize);
        List<ScAssembleQualityIssueVO> scAssembleQualityIssueVOS = scAssembleQualityIssueMapper.selectByProductNumberLike(startTime, endTime, productName);
        for (ScAssembleQualityIssueVO scAssembleQualityIssueVO : scAssembleQualityIssueVOS) {
            scAssembleQualityIssueVO.setAssembleInspector(scAssembleQualityIssueMapper.selectAssembleUserById(Integer.valueOf(scAssembleQualityIssueVO.getAssembleInspector())).getUserName());
        }
        BaseVO<ScAssembleQualityIssueVO> baseVO = new BaseVO<>(scAssembleQualityIssueVOS, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public void assembleInfoExportData(String startTime, String endTime, String productNumber) {
        List<ScAssembleQualityIssueVO> scAssembleQualityIssueVOS = scAssembleQualityIssueMapper.assembleInfoListData(startTime, endTime, productNumber);
        log.info("装配每日现场质量异常登记数据导出size:{}", scAssembleQualityIssueVOS.size());
        FileNameUtils fileNameUtils = new FileNameUtils();
        String titleStr = "装配每日现场质量异常登记" + fileNameUtils.getfileName();
        List<String> titleList = new ArrayList<>();
        titleList.add("日期");
        titleList.add("产品名称");
        titleList.add("品号");
        titleList.add("不良描述");
        titleList.add("不良数量");
        titleList.add("责任单位");
        titleList.add("责任人");
        titleList.add("质量判定结果");
        titleList.add("装配发现人员");
        titleList.add("检查来源");

        List<List<String>> list = new ArrayList<>();
        for (ScAssembleQualityIssueVO scAssembleQualityIssueVO : scAssembleQualityIssueVOS) {
            List<String> contentStr = new ArrayList<>();
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            contentStr.add(formatter.format(scAssembleQualityIssueVO.getCreateTime()));
            contentStr.add(scAssembleQualityIssueVO.getProductName());
            contentStr.add(scAssembleQualityIssueVO.getProductNumber());
            contentStr.add(scAssembleQualityIssueVO.getBadDescription());
            contentStr.add(scAssembleQualityIssueVO.getBadNumber().toString());
            contentStr.add(scAssembleQualityIssueVO.getDutyUnit());
            contentStr.add(scAssembleQualityIssueVO.getDutyPerson());
            contentStr.add(scAssembleQualityIssueVO.getQualityStatus());
            contentStr.add(scAssembleQualityIssueVO.getAssembleInspector());
            contentStr.add(scAssembleQualityIssueVO.getDetectionSource());
            list.add(contentStr);
        }
        AssembleExcelWriteUtil.createHeader(response, titleStr, titleList, list);
    }

    public Callback<ScAssembleQualityIssue> getDetail(Integer id) {
        if (id != null) {
            ScAssembleQualityIssue scAssembleQualityIssue = scAssembleQualityIssueMapper.selectByPrimaryKey(id);
            return Callback.success(scAssembleQualityIssue);
        }
        return Callback.error("暂无数据");
    }

    public Callback delete(Integer id) {
        ScAssembleQualityIssue scAssembleQualityIssue = scAssembleQualityIssueMapper.selectByPrimaryKey(id);
        if (scAssembleQualityIssue != null) {
            int i = scAssembleQualityIssueMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }

}
