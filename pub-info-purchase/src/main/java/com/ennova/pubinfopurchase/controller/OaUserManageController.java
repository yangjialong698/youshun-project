package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.entity.TDeptDing;
import com.ennova.pubinfopurchase.entity.TUserDing;
import com.ennova.pubinfopurchase.service.OaUserService;
import com.ennova.pubinfopurchase.vo.TDeptDingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/20
 */
@Api(tags = "查询OA部门管理者")
@RestController
@RequestMapping("/oaUser")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OaUserManageController {

    private final OaUserService oaUserService;

    @ApiOperation(value = "查询树状子部门管理者")
    @GetMapping("/queryOaDeptChildList")
    public Callback<TDeptDingVO> queryPrdDeptChildList() {
        return oaUserService.queryPrdDeptChildList();
    }

    @ApiOperation(value = "查询子部门管理者")
    @GetMapping("/queryPrdDeptIds")
    public Callback<List<TDeptDing>> queryPrdDeptIds() {
        return oaUserService.queryPrdDeptIds();
    }

    @ApiOperation(value = "根据manageId或者部门ID查询用户姓名")
    @GetMapping("/queryNameByManageId")
    public Callback<List<TUserDing>> queryNameByManageIdOrDeptId(@RequestParam("manageId") String manageId,
                                                                 @RequestParam("deptName") String deptName,
                                                                 @RequestParam("deptId") String deptId) {
        return oaUserService.queryNameByManageIdOrDeptId(manageId, deptName, deptId);
    }
}
