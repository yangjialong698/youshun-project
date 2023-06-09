package com.ennova.pubinfostore.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfostore.dao.ScProblemFeedbackMapper;
import com.ennova.pubinfostore.dao.ScProblemFileMapper;
import com.ennova.pubinfostore.dto.UserDTO;
import com.ennova.pubinfostore.entity.AppNotice;
import com.ennova.pubinfostore.entity.ScProblemFeedback;
import com.ennova.pubinfostore.entity.ScProblemFile;
import com.ennova.pubinfostore.service.feign.PubInfoUserClient;
import com.ennova.pubinfostore.utils.ApiContext;
import com.ennova.pubinfostore.utils.BeanConvertUtils;
import com.ennova.pubinfostore.vo.AppUserVO;
import com.ennova.pubinfostore.vo.SaveCountVO;
import com.ennova.pubinfostore.vo.ScProblemFeedbackVO;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.CommonEnum;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.AudienceDTO;
import com.getui.push.v2.sdk.dto.req.Settings;
import com.getui.push.v2.sdk.dto.req.Strategy;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AppService {

    private PushApi pushApi;
    private ApiContext apiContext;
    private String cid = null;
    private final PubInfoUserClient pubInfoUserClient;
    private final HttpServletRequest req;
    private final ScProblemFeedbackMapper scProblemFeedbackMapper;
    private final ScProblemFileMapper scProblemFileMapper;


    public void pushToSingleByCid(AppNotice appNotice) throws InterruptedException {

        apiContext = ApiContext.build();
        apiContext.configuration.setAnalyseStableDomainInterval(500);
        apiContext.configuration.setCheckHealthInterval(500);
        apiContext.configuration.setOpenAnalyseStableDomainSwitch(false);  //关闭
        ApiHelper apiHelper2 = ApiHelper.build(apiContext.configuration);
        pushApi = apiHelper2.creatApi(PushApi.class);
        PushDTO<Audience> pushDTO = pushDTO(appNotice);
        int num = 0;
        if (appNotice.getCid() == null) {
            return;
        }
        fullCid(pushDTO, appNotice.getCid());
        ApiResult<Map<String, Map<String, String>>> apiResult;
        while (true) {
            pushDTO.setRequestId(System.currentTimeMillis() + "");
            apiResult = pushApi.pushToSingleByCid(pushDTO);
            log.info("apiResult: " + apiResult.toString());
            System.err.println(apiResult);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (num++ == 50) {
                ApiHelper.close(apiContext.configuration);
            }
            break;
        }
    }

    private PushDTO<Audience> pushDTO(AppNotice appNotice) {
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        pushDTO.setGroupName("g-name1");

        Settings settings = new Settings();
        settings.setTtl(3600000);
        Strategy strategy = new Strategy();
        strategy.setSt(1);
        settings.setStrategy(strategy);

        pushDTO.setSettings(settings);

        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setLogoUrl("https://url");
        notification.setTitle(appNotice.getTitle());
        notification.setBody(appNotice.getContent());
        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_URL.type);
        notification.setUrl("https//:www.getui.com");

        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);

        PushChannel pushChannel = new PushChannel();
        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();
        ThirdNotification thirdNotification = new ThirdNotification();
        thirdNotification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type);
        thirdNotification.setTitle(appNotice.getTitle());
        thirdNotification.setBody(appNotice.getContent());
        ups.setNotification(thirdNotification);


        //设置options 方式一
        ups.addOption("HW", "badgeAddNum", 3);
        ups.addOption("HW", "badgeClass", "com.getui.demo.GetuiSdkDemoActivity");
        ups.addOption("OP", "app_message_id", 11);
        ups.addOption("VV", "message_sort", 1);
        ups.addOptionAll("channel", "default");


        //设置options 方式二
        Map<String, Map<String, Object>> options = new HashMap<String, Map<String, Object>>();
        Map<String, Object> all = new HashMap<String, Object>();
        all.put("channel", "default");
        options.put("ALL", all);
        Map<String, Object> hw = new HashMap<String, Object>();
        all.put("badgeAddNum", 3);
        all.put("badgeClass", "com.getui.demo.GetuiSdkDemoActivity");
        options.put("HW", hw);
        ups.setOptions(options);


        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);

        IosDTO iosDTO = new IosDTO();
        Aps aps = new Aps();
        Alert alert = new Alert();
        alert.setTitle(appNotice.getTitle());
        alert.setBody(appNotice.getContent());
        aps.setAlert(alert);
        iosDTO.setAps(aps);
        pushChannel.setIos(iosDTO);
        pushDTO.setPushChannel(pushChannel);

        return pushDTO;
    }

    private void fullCid(PushDTO<Audience> pushDTO, String cidstr) {
        Audience audience = new Audience();
        audience.addCid(cidstr);
        pushDTO.setAudience(audience);
    }


    public Callback pushAll(AppNotice appNotice) {
        apiContext = ApiContext.build();
        apiContext.configuration.setAnalyseStableDomainInterval(500);
        apiContext.configuration.setCheckHealthInterval(500);
        apiContext.configuration.setOpenAnalyseStableDomainSwitch(false);  //关闭
        ApiHelper apiHelper = ApiHelper.build(apiContext.configuration);
        cid = apiContext.cid;
        pushApi = apiHelper.creatApi(PushApi.class);

        long startTimeStamp = System.currentTimeMillis();
        GTNotification gtNotification = new GTNotification();
        gtNotification.setTitle(appNotice.getTitle());
        gtNotification.setBody(appNotice.getContent());
        gtNotification.setChannelId("Default");
        gtNotification.setChannelName("Default");
        gtNotification.setChannelLevel("4");
        gtNotification.setClickType(CommonEnum.ClickTypeEnum.TYPE_URL.type);
        gtNotification.setUrl("https://service.dcloud.net.cn/build/download/74d381e0-8538-11eb-a83f-5986bd00ab62");

        PushMessage pushMessage = new PushMessage();
        pushMessage.setNetworkType(0);
        pushMessage.setNotification(gtNotification);

        PushDTO<String> pushDTO = pushDTOAll();
        pushDTO.setRequestId(String.valueOf(startTimeStamp));
        pushDTO.setGroupName("groupname");
        pushDTO.setPushMessage(pushMessage);
        ApiResult<TaskIdDTO> apiResult = pushApi.pushAll(pushDTO);
        if (apiResult.getCode() == 0) {
            String id = IdUtil.randomUUID().replace("-", "");
            appNotice.setId(id);
            appNotice.setCreateTime(new Date());
//            appNoticeMapper.insertSelective(appNotice);
            return Callback.success(apiResult);
        } else {
            return Callback.error(apiResult.getMsg());
        }

    }

    private PushDTO<String> pushDTOAll() {
        PushDTO<String> pushDTO = new PushDTO<String>();
        pushDTO.setRequestId(UUID.randomUUID().toString().substring(0, 16));
        pushDTO.setGroupName("g-name");

        Settings settings = new Settings();
        settings.setTtl(3600000);

        pushDTO.setSettings(settings);
        pushDTO.setAudience("all");

        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setTitle("title" + System.currentTimeMillis());
        notification.setBody("body" + System.currentTimeMillis());
        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_URL.type);
        notification.setUrl("https//:www.getui.com");
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);
        return pushDTO;
    }

    @Async
    public Callback<ApiResult> pushToSingleByCidAsync(AppNotice appNotice) throws InterruptedException {
        apiContext = ApiContext.build();
        apiContext.configuration.setAnalyseStableDomainInterval(500);
        apiContext.configuration.setCheckHealthInterval(500);
        apiContext.configuration.setOpenAnalyseStableDomainSwitch(false);  //关闭
        ApiHelper apiHelper2 = ApiHelper.build(apiContext.configuration);
        pushApi = apiHelper2.creatApi(PushApi.class);
        PushDTO<Audience> pushDTO = pushDTO(appNotice);
        int num = 0;
        fullCid(pushDTO, appNotice.getCid());
        ApiResult<Map<String, Map<String, String>>> apiResult;
        while (true) {
            pushDTO.setRequestId(System.currentTimeMillis() + "");
            apiResult = pushApi.pushToSingleByCid(pushDTO);
            System.err.println(apiResult);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (num++ == 50) {
                ApiHelper.close(apiContext.configuration);
            }
            break;
        }
        if (apiResult.isSuccess()) {
            return Callback.success(apiResult);
        } else {
            return Callback.error(apiResult.getMsg());
        }
    }

    public Callback<ApiResult> alarmNotification(String title, String content, Integer userId) {

        List<AppUserVO> appUserVOList = pubInfoUserClient.listAppUsers().getData();
        Set<String> cids = appUserVOList.stream().filter(appUserVO -> StringUtils.isNotEmpty(appUserVO.getCid())).map(appUserVO -> appUserVO.getCid()).collect(Collectors.toSet());

        apiContext = ApiContext.build();
        apiContext.configuration.setAnalyseStableDomainInterval(500);
        apiContext.configuration.setCheckHealthInterval(500);
        apiContext.configuration.setOpenAnalyseStableDomainSwitch(false);  //关闭
        ApiHelper apiHelper = ApiHelper.build(apiContext.configuration);
        cid = apiContext.cid;
        pushApi = apiHelper.creatApi(PushApi.class);

        //创建Message
        PushDTO<Audience> pushDTO = this.pushDTO(title, content);
        //任务组名
        pushDTO.setGroupName("group1");
        ApiResult<TaskIdDTO> apiResult = pushApi.createMsg(pushDTO);
        //创建消息接口返回的TaskId
        String taskId = apiResult.getData().getTaskId();
        ApiResult<Map<String, Map<String, String>>> mapApiResult;
        while (true) {
            AudienceDTO audienceDTO = new AudienceDTO();
            audienceDTO.setTaskid(taskId);
            //推送目标用户
            Audience audience = new Audience();
            for (String cidtemp : cids) {
                audience.addCid(cidtemp);
            }
            audienceDTO.setAudience(audience);
            mapApiResult = pushApi.pushListByCid(audienceDTO);
            System.out.println(mapApiResult);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (mapApiResult.isSuccess()) {
                return Callback.success(mapApiResult);
            } else {
                return Callback.error(mapApiResult.getMsg());
            }
        }
    }

    private PushDTO<Audience> pushDTO(String title, String content) {
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        pushDTO.setGroupName("g-name1");

        //推送条件设置
        Settings settings = new Settings();
        //消息离线时间设置
        settings.setTtl(3600000);
        //厂商通道策略
        Strategy strategy = new Strategy();
        //锤子/坚果 通道策略1-4，表示含义该消息在用户在线时推送个推通道，用户离线时推送厂商通道，需要开通ups厂商使用该通道推送消息
        strategy.setIos(4);
        strategy.setSt(1);
        strategy.setDef(1);
        settings.setStrategy(strategy);
        pushDTO.setSettings(settings);

        //消息推送参数（包含ios，android）
        PushMessage pushMessage = new PushMessage();
        //安卓系统
        GTNotification notification = new GTNotification();
        //通知图标url地址
        notification.setLogoUrl("https://url");
        //通知消息标题
        notification.setTitle(title);
        //通知消息内容
        notification.setBody(content);
        //点击通知后续动作--打开网页地址
        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_URL.type);
        //点击通知栏消息时，唤起系统默认浏览器打开此链接
        notification.setUrl("https//:www.getui.com");

        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);

        //离线厂商通道消息内容
        PushChannel pushChannel = new PushChannel();
        //android通道推送消息内容
        AndroidDTO androidDTO = new AndroidDTO();
        //通知消息内容
        Ups ups = new Ups();
        ThirdNotification thirdNotification = new ThirdNotification();
        thirdNotification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type);
        thirdNotification.setTitle(title);
        thirdNotification.setBody(content);
        ups.setNotification(thirdNotification);


        //设置options 方式一
        ups.addOption("HW", "badgeAddNum", 3);
        ups.addOption("HW", "badgeClass", "com.getui.demo.GetuiSdkDemoActivity");
        ups.addOption("OP", "app_message_id", 11);
        ups.addOption("VV", "message_sort", 1);
        ups.addOptionAll("channel", "default");


        //设置options 方式二
        Map<String, Map<String, Object>> options = new HashMap<String, Map<String, Object>>();
        Map<String, Object> all = new HashMap<String, Object>();
        all.put("channel", "default");
        options.put("ALL", all);
        Map<String, Object> hw = new HashMap<String, Object>();
        all.put("badgeAddNum", 3);
        all.put("badgeClass", "com.getui.demo.GetuiSdkDemoActivity");
        options.put("HW", hw);
        ups.setOptions(options);


        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);

        IosDTO iosDTO = new IosDTO();
        //ios通道推送消息内容
        Aps aps = new Aps();
        //通知消息
        Alert alert = new Alert();
        alert.setTitle(title);
        alert.setBody(content);
        aps.setAlert(alert);
        iosDTO.setAps(aps);
        pushChannel.setIos(iosDTO);
        pushDTO.setPushChannel(pushChannel);

        return pushDTO;
    }

    public Callback pushFeedback(@NonNull ScProblemFeedbackVO scProblemFeedbackVO) throws InterruptedException {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        ScProblemFeedback scProblemFeedback = BeanConvertUtils.convertTo(scProblemFeedbackVO, ScProblemFeedback::new);
        scProblemFeedback.setBackUserId(userVo.getId());
        UserDTO userDTO = scProblemFeedbackMapper.selectById(userVo.getId());
        String dutyPersonId = scProblemFeedbackMapper.selectByUserId(scProblemFeedbackVO.getDutyPersonId()).getId().toString();
        UserDTO dto = scProblemFeedbackMapper.selectById(Integer.valueOf(dutyPersonId));
        log.info("dto: " + dto.toString());

        log.debug("dutyPersonId: " + dutyPersonId);
        scProblemFeedback.setBackPerson(userDTO.getUserName());
        scProblemFeedback.setBackDepartment(userDTO.getDepartment());
        scProblemFeedback.setDutyPersonId(dutyPersonId);
        scProblemFeedback.setCreateTime(new Date());
        scProblemFeedback.setDelFlag(0);
        scProblemFeedback.setBackStatus("3");
        scProblemFeedbackMapper.insertSelective(scProblemFeedback);
        Integer id = scProblemFeedback.getId();
        if (CollectionUtil.isNotEmpty(scProblemFeedbackVO.getScProblemFileId())) {
            for (Integer scProblemFileId : scProblemFeedbackVO.getScProblemFileId()) {
                ScProblemFile scProblemFile = scProblemFileMapper.selectByPrimaryKey(scProblemFileId);
                scProblemFile.setProblemFeedbackId(id);
                scProblemFile.setFileType(0);
                scProblemFileMapper.updateByPrimaryKeySelective(scProblemFile);
            }
        }
        AppNotice appNotice = AppNotice.builder().title("问题反馈消息通知").content(scProblemFeedback.getBackDepartment() + userDTO.getUserName() + "给你反馈一条异常信息请及时处理")
                .userid(userVo.getId().toString()).createTime(new Date()).cid(dto.getCid()).build();
        log.info("appNotice: " + appNotice.toString());
        this.pushToSingleByCid(appNotice);
        return Callback.success(true);
    }

    public Callback selectDutyDepartmentList() {
        return Callback.success(scProblemFeedbackMapper.selectDutyDepartmentList());
    }

    public Callback selectDutyPersonList(String departmentId) {
        return Callback.success(scProblemFeedbackMapper.selectDutyPersonList(departmentId));
    }

    public Callback<ScProblemFeedbackVO> getDetails(Integer id) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        if (id != null) {
            ScProblemFeedback scProblemFeedback = scProblemFeedbackMapper.selectByPrimaryKey(id);
            UserDTO userDTO = scProblemFeedbackMapper.selectById(scProblemFeedback.getBackUserId());
            ScProblemFeedbackVO scProblemFeedbackVO = BeanConvertUtils.convertTo(scProblemFeedback, ScProblemFeedbackVO::new);
            scProblemFeedbackVO.setBackPerson(userDTO.getUserName());
            if (ObjectUtil.isNotEmpty(scProblemFeedbackVO)) {
                List<ScProblemFile> scProblemFiles = scProblemFileMapper.selectFilesByProblemIds(scProblemFeedbackVO.getId());
                scProblemFeedbackVO.setFileVOList(scProblemFiles);
            }
            return Callback.success(scProblemFeedbackVO);
        }
        return Callback.error("暂无数据");
    }

    public Callback<ScProblemFeedbackVO> getDetail(Integer id) {
        if (id != null) {
            ScProblemFeedback scProblemFeedback = scProblemFeedbackMapper.selectByPrimaryKey(id);
            UserDTO userDTO = scProblemFeedbackMapper.selectById(scProblemFeedback.getBackUserId());
            ScProblemFeedbackVO scProblemFeedbackVO = BeanConvertUtils.convertTo(scProblemFeedback, ScProblemFeedbackVO::new);
            scProblemFeedbackVO.setBackPerson(userDTO.getUserName());
            if (ObjectUtil.isNotEmpty(scProblemFeedbackVO)) {
                List<ScProblemFile> scProblemFiles = scProblemFileMapper.selectFilesByProblemId(scProblemFeedbackVO.getId(), 1);
                scProblemFeedbackVO.setFileVOList(scProblemFiles);
            }
            return Callback.success(scProblemFeedbackVO);
        }
        return Callback.error("暂无数据");
    }

    public Callback deleteById(Integer id) {
        ScProblemFeedback build = ScProblemFeedback.builder().delFlag(1).id(id).build();
        int i = scProblemFeedbackMapper.updateByPrimaryKeySelective(build);
        return i > 0 ? Callback.success() : Callback.error("删除失败");
    }

    public Callback<BaseVO<ScProblemFeedback>> getMyProblemFeedbackList(Integer page, Integer pageSize,String backStatus, String searchKey) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        Page<ScProblemFeedback> startPage = PageHelper.startPage(page, pageSize);
        List<ScProblemFeedback> myProblemFeedbackList = scProblemFeedbackMapper.getMyProblemFeedbackList(backStatus,searchKey, userVo.getId());
        BaseVO<ScProblemFeedback> baseVO = new BaseVO<>(myProblemFeedbackList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<ScProblemFeedbackVO> getMyProblemsStatus() {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        ScProblemFeedbackVO myProblemsStatus = scProblemFeedbackMapper.getMyProblemsStatus(userVo.getId());
        return Callback.success(myProblemsStatus);
    }

    public Callback<BaseVO<ScProblemFeedback>> getMyHandleProblemList(Integer page, Integer pageSize, String backStatus,String searchKey) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        Page<ScProblemFeedback> startPage = PageHelper.startPage(page, pageSize);
        List<ScProblemFeedback> myProblemFeedbackList = scProblemFeedbackMapper.getMyHandleProblemList(backStatus,searchKey, userVo.getId());
        BaseVO<ScProblemFeedback> baseVO = new BaseVO<>(myProblemFeedbackList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<ScProblemFeedbackVO> getMyHandleProblemsStatus() {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        ScProblemFeedbackVO myProblemsStatus = scProblemFeedbackMapper.getMyHandleProblemsStatus(userVo.getId());
        return Callback.success(myProblemsStatus);
    }

    public Callback<ScProblemFeedbackVO> getMyHandleDetail(Integer id) {
        if (id != null) {
            ScProblemFeedback scProblemFeedback = scProblemFeedbackMapper.selectByPrimaryKey(id);
            UserDTO userDTO = scProblemFeedbackMapper.selectById(scProblemFeedback.getBackUserId());
            ScProblemFeedbackVO scProblemFeedbackVO = BeanConvertUtils.convertTo(scProblemFeedback, ScProblemFeedbackVO::new);
            scProblemFeedbackVO.setBackPerson(userDTO.getUserName());
            if (ObjectUtil.isNotEmpty(scProblemFeedbackVO)) {
                List<ScProblemFile> scProblemFiles = scProblemFileMapper.selectFilesByProblemId(scProblemFeedbackVO.getId(), 0);
                scProblemFeedbackVO.setFileVOList(scProblemFiles);
            }
            return Callback.success(scProblemFeedbackVO);
        }
        return Callback.error("暂无数据");
    }

    public Callback solveProblem(@NonNull ScProblemFeedbackVO scProblemFeedbackVO) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        ScProblemFeedback scProblemFeedback = BeanConvertUtils.convertTo(scProblemFeedbackVO, ScProblemFeedback::new);
        if (scProblemFeedback.getId() == null) {
            return Callback.error("id不能为空");
        }
        scProblemFeedback.setProblemReason(scProblemFeedback.getProblemReason());
        scProblemFeedback.setProblemDescription(scProblemFeedback.getProblemDescription());
        scProblemFeedback.setUpdateTime(new Date());
        scProblemFeedback.setBackStatus("2");
        scProblemFeedbackMapper.updateByPrimaryKeySelective(scProblemFeedback);
        Integer id = scProblemFeedback.getId();
        List<ScProblemFile> scProblemFiles = scProblemFileMapper.selectFilesByProblemIds(id);
        if (CollectionUtil.isNotEmpty(scProblemFiles)){
            for (ScProblemFile scProblemFile : scProblemFiles) {
                if (ObjectUtil.isNotEmpty(scProblemFile)){
                    scProblemFileMapper.deleteByPrimaryKey(scProblemFile.getId());
                }
            }
        }
        if (CollectionUtil.isNotEmpty(scProblemFeedbackVO.getScProblemFileId())) {
            for (Integer scProblemFileId : scProblemFeedbackVO.getScProblemFileId()) {
                ScProblemFile scProblemFile = scProblemFileMapper.selectByPrimaryKey(scProblemFileId);
                scProblemFile.setProblemFeedbackId(id);
                scProblemFile.setFileType(1);
                scProblemFileMapper.updateByPrimaryKeySelective(scProblemFile);
            }
        }
        return Callback.success(true);
    }

    public Callback solveProblemFeedback(@NonNull ScProblemFeedbackVO scProblemFeedbackVO) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        ScProblemFeedback scProblemFeedback = BeanConvertUtils.convertTo(scProblemFeedbackVO, ScProblemFeedback::new);
        scProblemFeedback.setSolveTime(new Date());
        scProblemFeedback.setBackStatus("1");
        int i = scProblemFeedbackMapper.updateByPrimaryKeySelective(scProblemFeedback);
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error("确认失败");
    }

    public Callback NoSolveProblemFeedback(@NonNull ScProblemFeedbackVO scProblemFeedbackVO) {

        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;

        ScProblemFeedback scProblemFeedback = BeanConvertUtils.convertTo(scProblemFeedbackVO, ScProblemFeedback::new);
        scProblemFeedback.setBackStatus("0");
        scProblemFeedback.setSolveTime(new Date());
        int i = scProblemFeedbackMapper.updateByPrimaryKeySelective(scProblemFeedback);
        if (i > 0) {
            return Callback.success(true);
        }
        return Callback.error("未确认失败");
    }


    public Callback<BaseVO<ScProblemFeedbackVO>> getSfbDetailList(Integer page, Integer pageSize, String searchKey, Integer status) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        int index = (page - 1) * pageSize;
        BaseVO<ScProblemFeedbackVO> baseVO;
        ArrayList<ScProblemFeedbackVO> scProblemFeedbackVOS = new ArrayList<>();
        List<ScProblemFeedback> scProblemFeedbackCount = scProblemFeedbackMapper.selectAllByBackStatusOrDutyPerson(searchKey, null);
        if (CollectionUtil.isNotEmpty(scProblemFeedbackCount)){
        long totalProblem = scProblemFeedbackCount.size();
        long toDoProblem = scProblemFeedbackCount.stream().filter(s -> s.getBackStatus().equals("3")).count();
        long doneProblem = scProblemFeedbackCount.stream().filter(s -> s.getBackStatus().equals("1")).count();
        long doingProblem = scProblemFeedbackCount.stream().filter(s -> s.getBackStatus().equals("2")).count();
        long unDoneProblem = scProblemFeedbackCount.stream().filter(s -> s.getBackStatus().equals("0")).count();
        List<ScProblemFeedback> scProblemFeedbacks = scProblemFeedbackMapper.selectAllByBackStatusOrDutyPerson(searchKey, status);
        scProblemFeedbacks.forEach(e->{
                ScProblemFeedbackVO scProblemFeedbackVO = new ScProblemFeedbackVO();
                BeanUtils.copyProperties(e,scProblemFeedbackVO);
                scProblemFeedbackVO.setTotalProblem(totalProblem);
                scProblemFeedbackVO.setToDoProblem(toDoProblem);
                scProblemFeedbackVO.setDoneProblem(doneProblem);
                scProblemFeedbackVO.setDoingProblem(doingProblem);
                scProblemFeedbackVO.setUnDoneProblem(unDoneProblem);
                if(!e.getBackStatus().equals("1")){
                    long betweenHour = DateUtil.between(e.getCreateTime(), new Date(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }else {
                    long betweenHour = DateUtil.between(e.getCreateTime(), e.getSolveTime(), DateUnit.HOUR);
                    scProblemFeedbackVO.setGqTime(betweenHour);
                }
                scProblemFeedbackVOS.add(scProblemFeedbackVO);
            });
            baseVO = new BaseVO<>(pageing(index,pageSize,scProblemFeedbackVOS), new PageUtil(pageSize, scProblemFeedbackVOS.size(), page));
        } else {
            baseVO = new BaseVO<>(scProblemFeedbackVOS, new PageUtil(pageSize, 0, page));
        }
        return Callback.success(baseVO);
    }

    public List<ScProblemFeedbackVO> pageing(int index, int pageSize, List<ScProblemFeedbackVO> list){
        if (index < 0 || index >= list.size() || pageSize <= 0) {
            return null;
        }
        int lastIndex = index + pageSize;
        if (lastIndex > list.size()) {
            lastIndex = list.size();
        }
        list = list.subList(index, lastIndex);
        return list;
    }

    public Callback<SaveCountVO> countMyBack(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (org.apache.commons.lang.StringUtils.isEmpty(token)) {
            return Callback.error("无权限token");
        }
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        Integer userId = userVo.getId();
        List<ScProblemFeedback> scProblemFeedbackFk = scProblemFeedbackMapper.selectAllByBackUserId(userId);
        SaveCountVO saveCountVOFk = new SaveCountVO();
        if (CollectionUtil.isNotEmpty(scProblemFeedbackFk)){
            long toDoProblemFk = scProblemFeedbackFk.stream().filter(s -> s.getBackStatus().equals("3")).count();
            long doneProblemFk = scProblemFeedbackFk.stream().filter(s -> s.getBackStatus().equals("1")).count();
            long doingProblemFk = scProblemFeedbackFk.stream().filter(s -> s.getBackStatus().equals("2")).count();
            long unDoneProblemFk = scProblemFeedbackFk.stream().filter(s -> s.getBackStatus().equals("0")).count();
            saveCountVOFk.setDoingProblem(doingProblemFk);
            saveCountVOFk.setToDoProblem(toDoProblemFk);
            saveCountVOFk.setDoneProblem(doneProblemFk);
            saveCountVOFk.setUnDoneProblem(unDoneProblemFk);
            return Callback.success(saveCountVOFk) ;
        }else {
            return Callback.error(1,"无数据统计") ;
        }
    }

    public Callback<SaveCountVO> countMyJb(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (org.apache.commons.lang.StringUtils.isEmpty(token)) {
            return Callback.error("无权限token");
        }
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        Integer userId = userVo.getId();
        List<ScProblemFeedback> scProblemFeedbackJb = scProblemFeedbackMapper.selectAllByDutyPersonId(userId.toString());
        SaveCountVO saveCountVOJb = new SaveCountVO();
        if (CollectionUtil.isNotEmpty(scProblemFeedbackJb)){
            long toDoProblemJb = scProblemFeedbackJb.stream().filter(s -> s.getBackStatus().equals("3")).count();
            long doneProblemJb = scProblemFeedbackJb.stream().filter(s -> s.getBackStatus().equals("1")).count();
            long doingProblemJb = scProblemFeedbackJb.stream().filter(s -> s.getBackStatus().equals("2")).count();
            long unDoneProblemJb = scProblemFeedbackJb.stream().filter(s -> s.getBackStatus().equals("0")).count();
            saveCountVOJb.setDoingProblem(doingProblemJb);
            saveCountVOJb.setToDoProblem(toDoProblemJb);
            saveCountVOJb.setDoneProblem(doneProblemJb);
            saveCountVOJb.setUnDoneProblem(unDoneProblemJb);
            return Callback.success(saveCountVOJb) ;
        }else {
            return Callback.error(1,"无数据统计") ;
        }
    }
}
