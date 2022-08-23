package com.ennova.pubinfodaily.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfodaily.service.YsDailyRepService;
import com.ennova.pubinfodaily.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dailyrep")
public class YsDailyRepController {
    @Autowired
    private YsDailyRepService ysDailyRepService;

    @ApiOperation(value = "非任务日报管理 - 新增/修改非任务工作日报",tags = "非任务日报管理API")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody YsDailyRepVO ysDailyRepVO) {
        return ysDailyRepService.insertOrUpdate(ysDailyRepVO);
    }

    @DeleteMapping("/deleteDailyRep")
    @ApiOperation(value = "非任务日报管理 - 非任务日报删除",tags = "非任务日报管理API")
    public Callback deleteDailyRep(Integer id) {
        return ysDailyRepService.deleteDailyRep(id);
    }

    @ApiOperation(value = "非任务日报管理 - 查看+反馈按钮查非任务日报详情",tags = "非任务日报管理API")
    @GetMapping("/selectOne")
    public Callback<YsDailyRepVO> selectOne(Integer id) {
        return ysDailyRepService.selectByPrimaryKey(id);
    }

    @ApiOperation(value = "非任务日报管理 - 非任务工作日报反馈",tags = "非任务日报管理API")
    @PostMapping("/addFeedContent")
    public Callback addFeedContent(@RequestBody FeedBackVO feedBackVO) {
        return ysDailyRepService.addFeedContent(feedBackVO);
    }

    @ApiOperation(value = "非任务日报管理 - 查看附件详情",tags = "非任务日报管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "日报ID", required = true)
    })
    @GetMapping("/fileDetail")
    public Callback<List<FileDownVO>> fileDetail(Integer id){
        return ysDailyRepService.fileDetail(id);
    }

    @ApiOperation(value = "非任务日报管理 - 非任务工作日报列表",tags = "非任务日报管理API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "fileName", value = "日报名称")
    })
    @GetMapping("/getDailyyRepDetails")
    public Callback<BaseVO<DailyRepDetailVO>> getDailyyRepDetails(Integer page, Integer pageSize,
                                                                  String fileName, String startTime, String endTime){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return ysDailyRepService.getDailyyRepDetails(page,pageSize,fileName,startTime,endTime,req);
    }

    @ApiOperation(value = "非任务日报管理 - 上传非任务工作日报附件",tags = "非任务日报管理API")
    @PostMapping("/uploadFile")
    public Callback<FileVO> uploadFile(MultipartFile uploadFiles) {
        return ysDailyRepService.uploadFile(uploadFiles);
    }

    @ApiOperation(value = "非任务日报管理 - 删除非任务工作日报附件",tags = "非任务日报管理API")
    @PostMapping("/delFiles")
    public Callback delFiles(@RequestBody FileRepDelVO fileRepDelVO) {
        return ysDailyRepService.delFiles(fileRepDelVO);
    }
}
