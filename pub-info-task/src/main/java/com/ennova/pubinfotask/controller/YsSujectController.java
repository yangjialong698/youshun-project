package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.entity.YsSuject;
import com.ennova.pubinfotask.service.YsSujectService;
import com.ennova.pubinfotask.vo.YsSujectVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 成本核算角色： cost_accountant
 */
@Api(tags = "公共信息平台-科目")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/ysSuject")
public class YsSujectController {

    private final YsSujectService ysSujectService;

    @ApiOperation(value = "科目 - 根据ID获取科目")
    @ApiOperationSort(value = 1)
    @ApiImplicitParam(name = "id", value = "科目ID")
    @PostMapping("/selectById")
    public Callback selectOne(Integer id) {
        return ysSujectService.selectByIdAndDelFlag(id);
    }

    @ApiOperation(value = "科目 - 新增科目")
    @ApiOperationSort(value = 2)
    @PostMapping("/insertSelective")
    public Callback insertSelective(YsSujectVO vo) {
        return ysSujectService.insertSelective(vo);
    }

    @ApiOperation(value = "科目 - 修改科目")
    @ApiOperationSort(value = 3)
    @PostMapping("/updateByPrimaryKeySelective")
    public Callback updateByPrimaryKeySelective(YsSujectVO vo) {
        return ysSujectService.updateByPrimaryKeySelective(vo);
    }

    @ApiOperation(value = "科目 - 逻辑删除科目")
    @ApiOperationSort(value = 4)
    @GetMapping("/deleteById")
    public Callback deleteById(Integer id){
        return ysSujectService.deleteById(id);
    }

    @ApiOperation(value = "科目 - 列表")
    @ApiOperationSort(value = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "suject", value = "科目名称")
    })
    @GetMapping("/selectDetailList")
    public Callback<BaseVO<YsSuject>> selectDetailList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                       String suject) {
        return ysSujectService.selectDetailList(page, pageSize, suject);
    }





}
