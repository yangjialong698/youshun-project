package com.ennova.pubinfouser.service.feign;

import com.ennova.pubinfocommon.entity.Callback;
import org.springframework.stereotype.Component;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-05-10
 */
@Component
public class PubInforTaskFallback implements PubInfoTaskClient {
    @Override
    public Callback selectByHaveUserId(Integer userId) {
        return Callback.error("获取失败");
    }
}
