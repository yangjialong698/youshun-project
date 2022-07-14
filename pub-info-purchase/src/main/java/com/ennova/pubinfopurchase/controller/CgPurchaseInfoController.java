package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfopurchase.dto.FileDelDTO;
import com.ennova.pubinfopurchase.entity.CgContactInformation;
import com.ennova.pubinfopurchase.service.CgPurchaseInfoService;
import com.ennova.pubinfopurchase.vo.CgPurchaseInfoVO;
import com.ennova.pubinfopurchase.vo.FileVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/11
 */
@Api(tags = "公共信息平台-采购信息")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/purchase")
public class CgPurchaseInfoController {

    private final CgPurchaseInfoService cgPurchaseInfoService;

    @ApiOperation(value = "采购附件 - 文件上传")
    @PostMapping("/upload")
    public Callback<FileVO> upload(MultipartFile file){
        return cgPurchaseInfoService.uploadFile(file);
    }

    @ApiOperation(value = "采购附件 - 附件下载")
    @GetMapping("/netDownLoadFile")
    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        cgPurchaseInfoService.netDownLoadFile(netAddress, filename, response);
    }

    @ApiOperation(value = "采购附件 - 文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO){
        return cgPurchaseInfoService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "采购信息 - 发布和修改采集信息")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated CgPurchaseInfoVO cgPurchaseInfoVO){
        return cgPurchaseInfoService.insertOrUpdate(cgPurchaseInfoVO);
    }

    @ApiOperation(value = "采购信息 - 删除")
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return cgPurchaseInfoService.delete(id);
    }

    @ApiOperation(value = "采购信息 - 采购信息首页分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "name", value = "物料名称")
    })
    @GetMapping("/selectPurchaseInfo")
    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(@RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "10") Integer pageSize, String name){
        return cgPurchaseInfoService.selectPurchaseInfo(page, pageSize, name);
    }

    @ApiOperation(value = "采购信息 - 采购信息首页不分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "物料名称")
    })
    @GetMapping("/selectAllPurchaseInfo")
    public Callback<List<CgPurchaseInfoVO>> selectAllPurchaseInfo(String name){
        return cgPurchaseInfoService.selectAllPurchaseInfo(name);
    }

    @ApiOperation(value = "采购信息 - 采购信息查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "采购信息ID", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<CgPurchaseInfoVO> getDetail(Integer id){
        return cgPurchaseInfoService.getDetail(id);
    }

    @ApiOperation(value = "联系信息")
    @GetMapping("/contactInformation")
    public Callback<CgContactInformation> contactInformation(){
        return cgPurchaseInfoService.contactInformation();
    }
}
