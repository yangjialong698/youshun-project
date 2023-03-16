package com.ennova.pubinfotask.service.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import org.springframework.stereotype.Component;

@Component
public class WebSiteFallback implements WebsiteClient {

    @Override
    public Callback delete(Integer id) {
        return Callback.error("获取失败");
    }
}
