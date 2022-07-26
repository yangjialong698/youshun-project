package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfopurchase.dto.CgSupplierCertificationDTO;
import com.ennova.pubinfopurchase.dto.FileDelDTO;
import com.ennova.pubinfopurchase.service.CgSupplierCertificationService;
import com.ennova.pubinfopurchase.vo.CgSupplierCertificationVO;
import com.ennova.pubinfopurchase.vo.CurrentUserVO;
import com.ennova.pubinfopurchase.vo.FileVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/21
 */
@Api(tags = "采购信息-供应商认证")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/supplier")
public class CgSupplierCertificationController {

    private final CgSupplierCertificationService cgSupplierCertificationService;

    @ApiOperation(value = "供应商资质认证 - 文件上传")
    @PostMapping("/upload")
    public Callback<FileVO> upload(MultipartFile file) {
        return cgSupplierCertificationService.uploadFile(file);
    }

    @ApiOperation(value = "供应商资质认证 - 文件下载")
    @GetMapping("/netDownLoadFile")
    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        cgSupplierCertificationService.netDownLoadFile(netAddress, filename, response);
    }

    @ApiOperation(value = "供应商资质认证 - 文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO){
        return cgSupplierCertificationService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "供应商 - 新增")
    @PostMapping("/insert")
    public Callback insert(@RequestBody @Validated CgSupplierCertificationDTO cgSupplierCertificationDTO){
        return cgSupplierCertificationService.insert(cgSupplierCertificationDTO);
    }

    @ApiOperation(value = "供应商 - 新增 - 选择审核人")
    @GetMapping("/selectCheckPerson")
    public Callback<CurrentUserVO> selectCheckPerson(){
        return cgSupplierCertificationService.selectCheckPerson();
    }

    @ApiOperation(value = "供应商 - 修改")
    @PostMapping("/update")
    public Callback update(@RequestBody @Validated CgSupplierCertificationDTO cgSupplierCertificationDTO) {
        return cgSupplierCertificationService.update(cgSupplierCertificationDTO);
    }

    @ApiOperation(value = "供应商 - 列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "status", value = "状态   0:待审核 1:审核通过 2:审核不通过 - 驳回"),
            @ApiImplicitParam(name = "supplierName", value = "供应商名称 或者 供应商id")
    })
    @GetMapping("/getSupplierList")
    public Callback<BaseVO<CgSupplierCertificationVO>> getSupplierList(@RequestParam(defaultValue = "1") Integer page,
                                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                                       Integer status, String supplierName) {
        return cgSupplierCertificationService.getSupplierList(page, pageSize, status, supplierName);
    }

    @ApiOperation(value = "供应商 - 审核")
    @ApiOperationSort(value = 7)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "供应商id"),
            @ApiImplicitParam(name = "status", value = "状态   0:待审核 1:审核通过 2:审核不通过 - 驳回"),
    })
    @GetMapping("/checkSupplier")
    public Callback checkSupplier(Integer id, Integer status) {
        return cgSupplierCertificationService.checkSupplier(id, status);
    }

    @ApiOperation(value = "供应商 - 详情(根据id)")
    @GetMapping("/getSupplierDetail")
    public Callback<CgSupplierCertificationVO> getSupplierDetail(Integer id) {
        return cgSupplierCertificationService.getSupplierDetail(id);
    }

    @ApiOperation(value = "供应商 - 删除")
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return cgSupplierCertificationService.delete(id);
    }

}
