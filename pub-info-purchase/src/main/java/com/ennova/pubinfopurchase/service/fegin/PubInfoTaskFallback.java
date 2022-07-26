package com.ennova.pubinfopurchase.service.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.vo.MessageVO;
import org.springframework.stereotype.Component;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/26
 */
@Component
public class PubInfoTaskFallback implements PubInfoTaskClient{

    @Override
    public Callback addSupplierPursh(MessageVO messageVO) {
        return Callback.error("新增供应商消息推送-失败");
    }
}
