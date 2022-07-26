package com.ennova.pubinfofile.service.feign;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfofile.vo.MessageVO;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@Component
public class PubInfoTaskFallback implements PubInfoTaskClient {

    @Override
    public Callback<String> sendMessByMessageVO(MessageVO messageVO) {
        return Callback.error("发送失败");
    }
}
