package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfoproduct.dto.FileDelDTO;
import com.ennova.pubinfoproduct.service.CustomerAccountInfoService;
import com.ennova.pubinfoproduct.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-12-27
 */
@Api(tags = "客述台账信息")
@RestController
@RequestMapping("/customeraccount")
public class CustomerAccountInfoController {

    @Autowired
    private CustomerAccountInfoService customerAccountInfoService;

    @ApiOperation(value = "根据根据供应商名称模糊查询供应商详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "responsParty", value = "供应商名称", required = true)
    })
    @GetMapping("/getCusAccSupplierInfo")
    public Callback<List<CusAccSupplierVO>> getCusAccSupplierInfo(String responsParty) {
        return customerAccountInfoService.getCusAccSupplierInfo(responsParty);
    }

    @ApiOperation(value = "客述台账附件 - 文件上传")
    @PostMapping("/upload")
    public Callback<FileVO> upload(MultipartFile file){
        return customerAccountInfoService.uploadFile(file);
    }

    @ApiOperation(value = "客述台账附件 - 文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO){
        return customerAccountInfoService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "客述台账 - 插入和修改客述台账信息")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated CustomerAccountInfoVO customerAccountInfoVO){
        return customerAccountInfoService.insertOrUpdate(customerAccountInfoVO);
    }

    @ApiOperation(value = "客述台账 - 客述台账查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "客述台账id", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<CustomerAccountInfoDetailVO> getDetail(Integer id){
        return customerAccountInfoService.getDetail(id);
    }

    @ApiOperation(value = "客述台账 - 根据id删除")
    @DeleteMapping("/delete")
    public Callback delete(Integer id) {
        return customerAccountInfoService.delete(id);
    }

    @ApiOperation(value = "客述台账 - 客述台账信息分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "monthNum", value = "投诉月份"),
            @ApiImplicitParam(name = "key"),
    })
    @GetMapping("/selectCustomerAccountInfoList")
    public Callback<BaseVO<CustomerAccountInfoVO>> selectCustomerAccountInfoList(
                                                                      @RequestParam(defaultValue = "1") Integer page,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      @RequestParam("monthNum") Integer monthNum,
                                                                      @RequestParam("key") String key) {
        return customerAccountInfoService.selectCustomerAccountInfoList(page, pageSize, monthNum, key);
    }
}
