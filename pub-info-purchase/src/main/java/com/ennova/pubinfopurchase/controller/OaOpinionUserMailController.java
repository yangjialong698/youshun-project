package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.service.OaOpinionUserMailService;
import com.ennova.pubinfopurchase.vo.OaOpinionUserMailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "公共信息平台-oa不合格品处理单-会签人")
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/opinionUser")
public class OaOpinionUserMailController {

    private final OaOpinionUserMailService oaOpinionUserMailService;

    @ApiOperation(value = "oa不合格品处理单 - 新增会签人")
    @PostMapping("/insertOrUpdateOpinionUser")
    public Callback insertOrUpdateOpinionUser(@RequestBody @Validated OaOpinionUserMailVO oaOpinionUserMailVO) {
        return oaOpinionUserMailService.insertOrUpdateOpinionUser(oaOpinionUserMailVO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 查询会签人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "setpStaus", value = "(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见)")
    })
    @GetMapping("/selectOpinionUser")
    public Callback<List<OaOpinionUserMailVO>> selectOpinionUser(@RequestParam("setpStaus") Integer setpStaus) {
        return oaOpinionUserMailService.selectOpinionUser(setpStaus);
    }

    @ApiOperation(value = "oa不合格品处理单 - 禁用会签人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true)
    })
    @GetMapping("/deleteOpinionUser")
    public Callback deleteOpinionUser(Integer id) {
        return oaOpinionUserMailService.deleteOpinionUser(id);
    }
}
