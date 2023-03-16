package com.ennova.pubinfotask.service.fegin;


import com.ennova.pubinfocommon.entity.Callback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Component
@FeignClient(name ="pub-info-website" ,url = "http://139.196.150.88:8011", fallback = WebSiteFallback.class)
public interface WebsiteClient {

    @GetMapping("/api/website/delete") //调用
    Callback delete(@RequestParam("id") Integer id);

}
