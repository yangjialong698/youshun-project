package com.ennova.pubinfofile.service.feign;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.UserVO;
import org.springframework.stereotype.Component;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-29
 */
@Component
public class PubInfoUserFallback implements PubInfoUserClient{

    @Override
    public Callback<UserVO> getUserById(Integer id) {
        return Callback.error("获取失败");
    }
}
