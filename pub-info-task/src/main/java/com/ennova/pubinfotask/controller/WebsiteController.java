package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfotask.service.fegin.WebsiteClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebsiteController {

    @Autowired
    private WebsiteClient websiteClient;

    @ApiOperation(value = "删除留言")
    @GetMapping("/website/delete")
    public Callback delete(Integer id){
        return websiteClient.delete(id);
    }

}
