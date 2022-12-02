package com.ennova.pubinfostore.service.feign;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfostore.vo.AppUserVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PubInfoUserFallback implements PubInfoUserClient{

    @Override
    public Callback<List<AppUserVO>> listAppUsers() {
        return Callback.error("获取失败");
    }
}
