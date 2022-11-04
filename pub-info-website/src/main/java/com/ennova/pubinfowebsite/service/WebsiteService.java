package com.ennova.pubinfowebsite.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfowebsite.dao.GwMessageMapper;
import com.ennova.pubinfowebsite.entity.GwMessage;
import com.ennova.pubinfowebsite.vo.GwMessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class WebsiteService {

    private final GwMessageMapper gwMessageMapper;

    public Callback onlineMessage(GwMessageVO gwMessageVO) {

        if (!StringUtils.isNumeric(gwMessageVO.getPhone())) {
            return Callback.error("电话输入有误");
        }
        GwMessage gwMessage = new GwMessage();
        BeanUtils.copyProperties(gwMessageVO, gwMessage);
        int i = gwMessageMapper.insert(gwMessage);
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error("提交失败");
    }

    public Callback delete(Integer id) {

        GwMessage gwMessage = gwMessageMapper.selectByPrimaryKey(id);
        if (gwMessage != null) {
            int i = gwMessageMapper.deleteByPrimaryKey(id);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2, "删除失败");
    }



}
