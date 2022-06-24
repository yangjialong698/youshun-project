package com.ennova.pubinfofile.controller;


import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfofile.service.FileDetailService;
import com.ennova.pubinfofile.vo.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-25
 */

@Api(tags = "文档管理API")
@RequestMapping("/filemanage")
@RestController
@Slf4j
public class FileDetailController {

    @Autowired
    private FileDetailService fileDetailService;

    @ApiOperation(value = "文档管理 - 所有文档详情",tags = "文档管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "ysFileTypeId", value = "文档类型ID"),
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID"),
            @ApiImplicitParam(name = "ysSonTaskId", value = "子任务ID")
    })
    @GetMapping("/getFileDetails")
    public Callback<BaseVO<FileDetailVO>> getFileDetails(Integer page, Integer pageSize, Integer ysFileTypeId,
                                                         Integer ysMasterTaskId,Integer ysSonTaskId,
                                                         String fileName,String startTime,String endTime){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return fileDetailService.getFileDetails(page,pageSize,ysFileTypeId,ysMasterTaskId,ysSonTaskId,fileName,startTime,endTime,req);
    }


    @ApiOperation(value = "文档管理 - 工作日报列表",tags = "文档管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID"),
            @ApiImplicitParam(name = "fileName", value = "文件夹名称")
    })
    @GetMapping("/getDayRepDetails")
    public Callback<BaseVO<FileDetailVO>> getDayRepDetails(Integer page, Integer pageSize,
                                                         Integer ysMasterTaskId,String fileName,String startTime,String endTime){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return fileDetailService.getDayRepDetails(page,pageSize,ysMasterTaskId,fileName,startTime,endTime,req);
    }


    @ApiOperation(value = "文档管理 - 我的文档详情",tags = "文档管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "ysFileTypeId", value = "文档类型ID"),
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID"),
            @ApiImplicitParam(name = "ysSonTaskId", value = "子任务ID")
    })
    @GetMapping("/getMyFileDetails")
    public Callback<BaseVO<FileDetailVO>> getMyFileDetails(Integer page, Integer pageSize, Integer ysFileTypeId,
                                                         Integer ysMasterTaskId,Integer ysSonTaskId,
                                                         String fileName){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return fileDetailService.getMyFileDetails(page,pageSize,ysFileTypeId,ysMasterTaskId,ysSonTaskId,fileName,req);
    }

    @PostMapping("/deleteFileDetail")
    @ApiOperation(value = "文档管理-删除",tags = "文档管理API")
    public Callback deleteFileDetail(Integer id) {
        return fileDetailService.deleteFileDetail(id);
    }

    @ApiOperation(value = "文档管理 - 文档类型统计",tags = "文档管理API")
    @GetMapping("/getFileTypeCou")
    public Callback<List<FileTypeCou>> getFileTypeCou(){
        return fileDetailService.getFileTypeCou();
    }

    @ApiOperation(value = "文档管理 - 文档更新统计",tags = "文档管理API")
    @GetMapping("/getFileUpRatio")
    public Callback<FileUpdateVO> getFileUpRatio(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return fileDetailService.getFileUpRatio(req);
    }

    @ApiOperation(value = "文档管理 - 下载查看详情",tags = "文档管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文档ID", required = true)
    })
    @GetMapping("/downDetail")
    public Callback<List<FileDownVO>> downDetail(Integer id){
        return fileDetailService.downDetail(id);
    }

    @ApiOperation(value = "新建文档管理/新建建议/工作日报 - 获取主任务下拉",tags = "文档管理API")
    @GetMapping("/selectMasterTask")
    public Callback<List<LinkedHashMap>> selectMasterTask(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return fileDetailService.selectMasterTask(req);
    }

    @ApiOperation(value = "文档详情搜索 - 获取主任务下拉",tags = "文档管理API")
    @GetMapping("/queryMasterTask")
    public Callback<List<LinkedHashMap>> queryMasterTask(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return fileDetailService.queryMasterTask(req);
    }

    @ApiOperation(value = "新建文档管理/新建建议 - 获取主任务下的子任务下拉",tags = "文档管理API")
    @GetMapping("/selectSonTask")
    public Callback<List<LinkedHashMap>> selectSonTask(Integer ysMasterTaskId){
        return fileDetailService.selectSonTask(ysMasterTaskId);
    }

    //***************************************************附件上传******************************************************


    @ApiOperation(value = "文档管理 - 上传附件:上传一次调一次",tags = "文档管理API")
    @PostMapping("/upload")
    public Callback<FileVO> uploadnew(MultipartFile uploadFiles) {
        return fileDetailService.uploadFile(uploadFiles);
    }

    @ApiOperation(value = "文档管理 - 删除已上传的附件",tags = "文档管理API")
    @PostMapping("/delFiles")
    public Callback delFiles(@RequestBody FileDeleteVO fileDeleteVO) {
        return fileDetailService.delFiles(fileDeleteVO);
    }

    @ApiOperation(value = "文档管理 - 保存上传文档/经验建议/工作日报",tags = "文档管理API")
    @PostMapping("/addMasterFile")
    public Callback addMasterFile(@RequestBody ModifyFileVO modifyFileVO) {
        return fileDetailService.addMasterFile(modifyFileVO);
    }

    @ApiOperation(value = "文档管理  - 根据ID查询要修改的数据",tags = "文档管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文档ID", required = true)
    })
    @GetMapping("/selectDetail")
    public Callback<ModifyFileVO> selectDetailOne(Integer id){
        return fileDetailService.selectDetailOne(id);
    }

    @ApiOperation(value = "文档管理  - 修改经验建议/文档/工作日报",tags = "文档管理API")
    @PostMapping("/modifyfile")
    public Callback modifyfile(@RequestBody ModifyFileVO modifyFileVO){
        return fileDetailService.modifyfile(modifyFileVO);
    }

    @ApiOperation(value = "文档管理  - 附件下载",tags = "文档管理API")
    @GetMapping("/netDownLoadFile")
    public void netDownLoadFile(String netAddress, String filename,HttpServletResponse response) throws Exception {
        fileDetailService.netDownLoadFile(netAddress, filename, response);
    }
}
