package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.dto.OaRejectsOpinionDTO;
import com.ennova.pubinfopurchase.service.OaRejectsOpinionService;
import com.ennova.pubinfopurchase.vo.OaRejectsOpinionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (project.oa_rejects_opinion)表控制层
 *
 * @author yangjialong
 */
@Slf4j
@Api(tags = "公共信息平台-oa不合格品处理单会签")
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/rejectsOpinion")
public class OaRejectsOpinionController {

    private final OaRejectsOpinionService oaRejectsOpinionService;

    @ApiOperation(value = "oa不合格品处理单 - 新增会签人明细信息")
    @PostMapping("/insertRejectsOpinion")
    public Callback insertRejectsOpinion(@RequestBody @Validated OaRejectsOpinionDTO oaRejectsOpinionVOS) {
        return oaRejectsOpinionService.insertRejectsOpinion(oaRejectsOpinionVOS);
    }

    @ApiOperation(value = "oa不合格品处理单 - 会签人进行会签")
    @PostMapping("/countersignOpinion")
    public Callback countersignOpinion(@RequestBody @Validated OaRejectsOpinionVO oaRejectsOpinionVO) {
        return oaRejectsOpinionService.countersignOpinion(oaRejectsOpinionVO);
    }


}
