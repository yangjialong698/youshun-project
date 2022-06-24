package com.ennova.pubinfofile.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfofile.entity.YsDayRep;
import com.ennova.pubinfofile.service.YsDayRepService;
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
import java.util.List;

/**
 * dayrep
 * @author wangwei
 */
@RestController
@RequestMapping("/dayrep")
public class YsDayRepController {

    @Resource
    private YsDayRepService ysDayRepService;

    @ApiOperation(value = "日报管理 - 新增/修改工作日报",tags = "日报管理API")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody YsDayRepVO ysDayRepVO) {
        return ysDayRepService.insertOrUpdate(ysDayRepVO);
    }

    @DeleteMapping("/deleteDayRep")
    @ApiOperation(value = "日报管理 - 日报删除",tags = "日报管理API")
    public Callback deleteDayRep(Integer id) {
        return ysDayRepService.deleteDayRep(id);
    }

    @ApiOperation(value = "日报管理 - 查看+反馈按钮查日报详情",tags = "日报管理API")
    @GetMapping("/selectOne")
    public Callback<YsDayRepVO> selectOne(Integer id) {
        return ysDayRepService.selectByPrimaryKey(id);
    }

    @ApiOperation(value = "日报管理 - 工作日报反馈",tags = "日报管理API")
    @PostMapping("/addFeedContent")
    public Callback addFeedContent(@RequestBody FeedBackVO feedBackVO) {
        return ysDayRepService.addFeedContent(feedBackVO);
    }

    @ApiOperation(value = "日报管理 - 查看附件详情",tags = "日报管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "日报ID", required = true)
    })
    @GetMapping("/fileDetail")
    public Callback<List<FileDownVO>> fileDetail(Integer id){
        return ysDayRepService.fileDetail(id);
    }

    @ApiOperation(value = "日报管理 - 工作日报列表",tags = "日报管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID"),
            @ApiImplicitParam(name = "fileName", value = "日报名称")
    })
    @GetMapping("/getDayRepDetails")
    public Callback<BaseVO<DayRepDetailVO>> getDayRepDetails(Integer page, Integer pageSize,
                                                             Integer ysMasterTaskId, String fileName, String startTime, String endTime){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return ysDayRepService.getDayRepDetails(page,pageSize,ysMasterTaskId,fileName,startTime,endTime,req);
    }

    @ApiOperation(value = "日报管理 - 上传工作日报附件",tags = "日报管理API")
    @PostMapping("/uploadFile")
    public Callback<FileVO> uploadFile(MultipartFile uploadFiles) {
        return ysDayRepService.uploadFile(uploadFiles);
    }

    @ApiOperation(value = "日报管理 - 删除工作日报附件",tags = "日报管理API")
    @PostMapping("/delFiles")
    public Callback delFiles(@RequestBody FileRepDelVO fileRepDelVO) {
        return ysDayRepService.delFiles(fileRepDelVO);
    }

}
