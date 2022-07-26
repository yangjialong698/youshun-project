package com.ennova.pubinfofile.service.feign;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfofile.vo.MessageVO;
import io.netty.channel.Channel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "pub-info-task",fallback = PubInfoTaskFallback.class)
public interface PubInfoTaskClient {
    @ApiOperation(value = "根据MessageVO推送消息")
    @PostMapping("/bulletin/sendMessByMessageVO")
    public Callback<String> sendMessByMessageVO(@RequestBody MessageVO messageVO) ;
}
