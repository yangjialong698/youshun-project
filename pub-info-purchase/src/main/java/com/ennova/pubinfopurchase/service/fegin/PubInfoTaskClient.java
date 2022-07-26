package com.ennova.pubinfopurchase.service.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.vo.MessageVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/26
 */
@Component
@FeignClient(value = "pub-info-task",fallback = PubInfoTaskFallback.class)
public interface PubInfoTaskClient {

    @ApiOperation(value = "新增供应商消息推送")
    @PostMapping("/bulletin/addSupplierPursh")
    public Callback addSupplierPursh(@RequestBody MessageVO messageVO);
}
