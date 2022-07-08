package com.ennova.pubinfofile.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfofile.service.YsExpSugService;
import com.ennova.pubinfofile.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/expsug")
public class YsExpSugController {

    @Resource
    private YsExpSugService ysExpSugService;

    @ApiOperation(value = "经验建议 - 新增/修改经验建议",tags = "经验建议API")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody YsExpSugVO ysExpSugVO) {
        return ysExpSugService.insertOrUpdate(ysExpSugVO);
    }

    @DeleteMapping("/deleteExpSug")
    @ApiOperation(value = "经验建议 - 经验建议删除",tags = "经验建议API")
    public Callback deleteExpSug(Integer id) {
        return ysExpSugService.deleteExpSug(id);
    }

    @ApiOperation(value = "经验建议 - 查看+评论按钮查经验建议详情",tags = "经验建议API")
    @GetMapping("/selectOne")
    public Callback<YsExpSugVO> selectOne(Integer id) {
        return ysExpSugService.selectByPrimaryKey(id);
    }

    @ApiOperation(value = "经验建议 - 经验建议评论",tags = "经验建议API")
    @PostMapping("/addSugComment")
    public Callback addSugComment(@RequestBody SugCommentVO sugCommentVO) {
        return ysExpSugService.addSugComment(sugCommentVO);
    }

    @ApiOperation(value = "经验建议 - 查看附件详情",tags = "经验建议API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "经验建议ID", required = true)
    })
    @GetMapping("/fileDetail")
    public Callback<List<FileDownVO>> fileDetail(Integer id){
        return ysExpSugService.fileDetail(id);
    }

    @ApiOperation(value = "经验建议 - 经验建议列表",tags = "经验建议API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID"),
            @ApiImplicitParam(name = "fileName", value = "经验建议名称")
    })
    @GetMapping("/getExpSugDetails")
    public Callback<BaseVO<ExpSugDetailVO>> getExpSugDetails(Integer page, Integer pageSize,
                                                             Integer ysMasterTaskId, String fileName){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return ysExpSugService.getExpSugDetails(page,pageSize,ysMasterTaskId,fileName,req);
    }

    @ApiOperation(value = "经验建议 - 上传经验建议附件",tags = "经验建议API")
    @PostMapping("/uploadFile")
    public Callback<FileVO> uploadFile(MultipartFile uploadFiles) {
        return ysExpSugService.uploadFile(uploadFiles);
    }

    @ApiOperation(value = "经验建议 - 删除经验建议附件",tags = "经验建议API")
    @PostMapping("/delFiles")
    public Callback delFiles(@RequestBody ExpSugDelVO expSugDelVO) {
        return ysExpSugService.delFiles(expSugDelVO);
    }

    @ApiOperation(value = "经验建议搜索 - 获取主任务下拉",tags = "经验建议API")
    @GetMapping("/queryMasterTask")
    public Callback<List<LinkedHashMap>> queryMasterTask(){
        return ysExpSugService.queryMasterTask();
    }
}
