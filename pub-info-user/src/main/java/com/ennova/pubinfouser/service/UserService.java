package com.ennova.pubinfouser.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfouser.dao.*;
import com.ennova.pubinfouser.dto.BaseDTO;
import com.ennova.pubinfouser.dto.UserDTO;
import com.ennova.pubinfouser.entity.*;
import com.ennova.pubinfouser.service.feign.PubInfoTaskClient;
import com.ennova.pubinfouser.utils.AddressUtil;
import com.ennova.pubinfouser.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Service
@Slf4j
public class UserService extends BaseService<UserEntity> {

    private String taskSystem = "1001";
    private String purchaseSystem = "1002";
    private String scanSystem = "1003";
    private String newsSystem = "1004";
    private String reportSystem = "1005";
    private String AFFICHE = "1006";
    private String ERP = "1007";
    private String OPINIONBOX = "1008";

    @Resource
    private UserDao userDao;

    @Resource
    private RoleService roleService;

    @Autowired
    private PubInfoTaskClient pubInfoTaskClient;

    @Autowired
    private BaseDao baseDao;

//    @Resource
//    private RoleDao roleDao;

//    @Resource
//    private PermissionDao permissionDao;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserDeptMapper userDeptMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private HttpServletRequest request;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private TUserSystemMapper tUserSystemMapper;

    @Autowired
    private DeptService deptService;


    public Callback<UserVO> login(String account, String password) {
        System.out.println("*****************路由测试***************"+account+" -- "+password);
        if(account == null || "".equals(account)) {
            return Callback.error("请输入账号");
        }
        if(password == null || "".equals(password)) {
            return Callback.error("请输入密码");
        }
        UserVO userVO = null;
        if (Validator.isMobile(account)) {
            userVO = userDao.getUserInfoByMobile(account);
            if (userVO == null) {
                return Callback.error("手机号码未注册");
            }
        }else if (account.startsWith("U") && account.length() == 8){
            userVO = userDao.getUserInfoByJobNum(account);
            if (userVO == null) {
                return Callback.error("工号未注册");
            }
        }
        List<UserRole>  userRoleList = userRoleMapper.selectByUserId(userVO.getId());
        UserRole userRole = null ;
        if (CollectionUtil.isNotEmpty(userRoleList)){
            userRole = userRoleList.get(0);
        }else {
            return Callback.error("用户无角色,无法登入");
        }
        // 验证密码是否正确
        try {
            password = md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!password.equals(userVO.getPassword())) {
            return Callback.error("手机号或密码错误，请重试");
        }
        if(userVO.getStatus().equals("1")) {
            return Callback.error("账号已被禁用，请确认");
        }
        //生成token
        String token = JWTUtil.generateTokenForLog(account,userVO.getId(), userVO.getCompany().toString());
        //生成刷新token
        String refreshToken = JWTUtil.generateRefToken(account,userVO.getId(), userVO.getCompany().toString());
        userVO.setToken(token);
        userVO.setRefreshToken(refreshToken);
        userVO.setPassword("");
        userVO.setMenu(roleService.getMenu(userRole.getRoleId()));
        //登陆成功，记录登陆日志
        String ua = StrUtil.sub(this.request.getHeader("user-agent"), 0, 500);
        String ip = ServletUtil.getClientIP(this.request);
        String location = AddressUtil.getRegion(ip);

        LoginLog loginLog = LoginLog.builder().requestIp(ip).userId(userVO.getId()).userName(userVO.getUsername()).account(userVO.getMobile())
                .description("用户登陆").loginDate(new Date()).ua(ua).location(location).createTime(new Date()).build();

        loginLogMapper.insert(loginLog);
        return Callback.success(userVO);
    }


    public Callback<UserVO> loginAll(String account, String password) {
        System.out.println("*****************路由测试***************"+account+" -- "+password);
        if(account == null || "".equals(account)) {
            return Callback.error("请输入账号");
        }
        if(password == null || "".equals(password)) {
            return Callback.error("请输入密码");
        }
        UserVO userVO = null;
        if (Validator.isMobile(account)) {
            userVO = userDao.getUserInfoByMobile(account);
            if (userVO == null) {
                return Callback.error("手机号码未注册");
            }
        }else if (account.startsWith("U") && account.length() == 8){
            userVO = userDao.getUserInfoByJobNum(account);
            if (userVO == null) {
                return Callback.error("工号未注册");
            }
        }
        List<UserRole>  userRoleList = userRoleMapper.selectByUserId(userVO.getId());
        UserRole userRole = null ;
        if (CollectionUtil.isNotEmpty(userRoleList)){
            userRole = userRoleList.get(0);
        }
//        else {
//            return Callback.error("用户无角色,无法登入");
//        }
        // 验证密码是否正确
        try {
            password = md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!password.equals(userVO.getPassword())) {
            return Callback.error("手机号或密码错误，请重试");
        }
        if(userVO.getStatus().equals("1")) {
            return Callback.error("账号已被禁用，请确认");
        }
        List<TUserSystem> tUserSystems = tUserSystemMapper.queryByUserId(userVO.getId());
        //生成token
        String token = JWTUtil.generateTokenForLog(account,userVO.getId(), userVO.getCompany().toString());
        //生成刷新token
        String refreshToken = JWTUtil.generateRefToken(account,userVO.getId(), userVO.getCompany().toString());
        userVO.setToken(token);
        userVO.setRefreshToken(refreshToken);
        userVO.setPassword("");
        ArrayList<NewMenuVO> newMenuVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(tUserSystems)){
            for (TUserSystem tUserSystem : tUserSystems) {
                NewMenuVO newMenuVO = new NewMenuVO();
                String sysNum = tUserSystem.getSysNum();
                if (sysNum.equals(taskSystem)){
                    // List<MenuVO> menuList = roleService.getMenu(userRole.getRoleId());
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1001");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
                if (sysNum.equals(purchaseSystem)){
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1002");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
                if (sysNum.equals(scanSystem)){
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1003");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
                if (sysNum.equals(newsSystem)){
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1004");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
                if (sysNum.equals(reportSystem)){
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1005");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
                if (sysNum.equals(AFFICHE)){
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1006");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
                if (sysNum.equals(ERP)){
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1007");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
                if (sysNum.equals(OPINIONBOX)){
                    List<MenuVO> menuList = roleService.getMenuBySysNum("1008");
                    newMenuVO.setSysNum(sysNum);
                    newMenuVO.setSysName(tUserSystem.getSysName());
                    newMenuVO.setMenu(menuList);
                    newMenuVOS.add(newMenuVO);
                }
            }
        }
        userVO.setNewMenu(newMenuVOS);
        //登陆成功，记录登陆日志
        String ua = StrUtil.sub(this.request.getHeader("user-agent"), 0, 500);
        String ip = ServletUtil.getClientIP(this.request);
        String location = AddressUtil.getRegion(ip);

        LoginLog loginLog = LoginLog.builder().requestIp(ip).userId(userVO.getId()).userName(userVO.getUsername()).account(userVO.getMobile())
                .description("用户登陆").loginDate(new Date()).ua(ua).location(location).createTime(new Date()).build();

        loginLogMapper.insert(loginLog);
        return Callback.success(userVO);
    }

    public Callback<String> refreshToken(String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)){
            return Callback.error("refreshToken不能为空");
        }
        if (!JWTUtil.verify(refreshToken)){
            return  new Callback(1004, "refreshToken已过期", null);
        }
        com.ennova.pubinfocommon.vo.UserVO userVO = JWTUtil.getUserVOByToken(refreshToken);
        String username = userVO.getUsername();
        Integer userId = userVO.getId();
        String company = userVO.getCompany();
        String newToken = JWTUtil.generateTokenForLog(username, userId, company);
        return Callback.success(newToken);
    }

    @Transactional
    public Callback<Boolean> addUser(UserDTO userDTO) {
        UserEntity userEntity=new UserEntity();
        BeanUtils.copyProperties(userDTO,userEntity);
        if (!userDTO.getPassword().equals(userDTO.getConPassWord())) {
            return Callback.error("密码不一致！请重新输入");
        }
        UserVO tempUserByMo = userDao.getUserInfoByMobile(userEntity.getMobile());
        if(tempUserByMo != null) {
            return Callback.error("手机号已经存在！请确认");
        }
        UserVO tempUserByJn =userDao.getUserInfoByJobNum(userEntity.getJobNum());
        if(tempUserByJn != null) {
            return Callback.error("工号已经存在！请确认");
        }
        List<UserVO> userVOList = userDao.getUserInfoByUname(userEntity.getUsername());
        if(CollectionUtil.isNotEmpty(userVOList)) {
            return Callback.error("该用户名已经存在！请确认");
        }
        Integer roleId = userDTO.getRoleId();
        Integer department = userDTO.getDepartment();
        if(StringUtils.isNotEmpty(userEntity.getPassword())) {
            userEntity.setPassword(md5DigestAsHex(userEntity.getPassword().getBytes(StandardCharsets.UTF_8)));
        }
        //保存用户表
        userDao.insert(userEntity);
        //保存用户部门表
        if(null != department){
            UserDept userDept=new UserDept();
            userDept.setUserId(userEntity.getId());
            userDept.setDeptId(department);
            userDept.setCreateTime(new Date());
            userDeptMapper.insertSelective(userDept);
        }
        //保存用户角色表
        if(roleId !=null){
            UserRole userRole=new UserRole();
            userRole.setUserId(userEntity.getId());
            userRole.setRoleId(roleId);
            userRole.setCreateTime(new Date());
            userRoleMapper.insertSelective(userRole);
        }
        return Callback.success(true);
    }


    public Callback<UserVO> getUserById(Integer id) {

        if(id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        return Callback.success(userDao.getUserById(id));

    }


    public Callback deleteUser(Integer id) {
        if (id == null || id <= 0) {
            return Callback.error("参数错误");
        }
        Callback callback = pubInfoTaskClient.selectByHaveUserId(id);
        Integer count = (Integer) callback.data;
        if (count > 0){
            return Callback.error("该用户已创建任务无法删除");
        }
        //删除该用户对应的角色
        userRoleMapper.deleteByUserId(id);
        //删除该用户对应的角色
        userDeptMapper.deleteByUserId(id);
        //删除用户
        int result = userDao.delete(id);
        if (result < 1) {
            return Callback.error("操作失败");
        }
        return Callback.success("删除成功");
    }

    public Callback<BaseVO<UserVO>> listUsers(Integer page, Integer pageSize, Integer company,Integer roleId,Integer department, String searchKey) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        Page<UserVO> startPage = PageHelper.startPage(page, pageSize);
        List<UserVO> userVOList = userDao.listUsers(company, roleId,department,searchKey).stream().filter(userVO -> StringUtils.isNotEmpty(userVO.getUserId())).collect(Collectors.toList());
        List<UserVO> userVOS = userDao.listManagerUsers();
        List<DeptVO> deptVOList = deptService.listDeptList(53).getData();
        if (CollectionUtil.isNotEmpty(deptVOList)){
            List<String> deptManageIds = deptVOList.stream().filter(p -> StringUtils.isNotEmpty(p.getManageId())).map(deptVO -> deptVO.getManageId()).collect(Collectors.toList());
            for (UserVO userVO : userVOList) {
                userVO.setIsBold(0);
                for (Object deptManageId : deptManageIds) {
                    if (userVO.getUserId().equals(deptManageId)){
                        for (UserVO vo : userVOS) {
                            if (vo.getUserId().equals(userVO.getUserId())) {
                                userVO.setIsBold(1);
                            }
                        }
                    }
                }
            }
        }
        BaseVO<UserVO> baseVO = new BaseVO<>(userVOList, new PageUtil(pageSize, (int)startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    @Transactional
    public Callback updateUser(UserDTO userDTO) {
        UserEntity userEntity = BaseDTO.convertBean(userDTO);
        if (StringUtils.isNotEmpty(userDTO.getPassword()) || StringUtils.isNotEmpty(userDTO.getConPassWord())){
            if (!userDTO.getPassword().equals(userDTO.getConPassWord())) {
                return Callback.error("密码不一致！请重新输入");
            }
        }
        if(StringUtils.isNotEmpty(userEntity.getPassword())) {
            userEntity.setPassword(md5DigestAsHex(userEntity.getPassword().getBytes(StandardCharsets.UTF_8)));
        }
        UserVO userIn = userDao.getUserById(userEntity.getId());
        if (!userIn.getMobile().equals(userEntity.getMobile())){
            UserVO tempUser = userDao.getUserInfoByMobile(userEntity.getMobile());
            if(tempUser != null) {
                return Callback.error("手机号已经存在！请确认");
            }
        }
        //更新用户表
        userDao.update(userEntity);
        //更新用户部门表
        List<UserDept> userDepts = userDeptMapper.selectByUserId(userEntity.getId());
        if (CollectionUtil.isNotEmpty(userDepts)){
            UserDept userDept = userDepts.get(0);
            userDept.setUserId(userDTO.getId());
            userDept.setDeptId(userDTO.getDepartment());
            userDeptMapper.updateByPrimaryKeySelective(userDept);
        }
        //更新用户角色表
        List<UserRole> userRoles = userRoleMapper.selectByUserId(userEntity.getId());
        if (CollectionUtil.isNotEmpty(userRoles)){
            UserRole userRole = userRoles.get(0);
            userRole.setUserId(userDTO.getId());
            userRole.setRoleId(userDTO.getRoleId());
            userRoleMapper.updateByPrimaryKeySelective(userRole);
        }else {
            UserRole userRole = new UserRole();
            userRole.setUserId(userDTO.getId());
            userRole.setRoleId(userDTO.getRoleId());
            userRole.setCreateTime(new Date());
            userRoleMapper.insertSelective(userRole);
        }
        return Callback.success("用户修改成功");
    }

    public Callback<DeptNumVO> getCouPerDept() {
        DeptNumVO deptNumVO = new DeptNumVO();
        List<PerDeptNumVO> perDeptNumVOS = userDao.getCouPerDept();
        deptNumVO.setDeptNum(perDeptNumVOS);
        int total = perDeptNumVOS.stream().mapToInt(PerDeptNumVO::getDeptCou).sum();
        deptNumVO.setTotalNum(total);
        return Callback.success(deptNumVO);
    }

    public Callback resetPassword(UserDTO userDTO) {
        UserEntity userEntity = BaseDTO.convertBean(userDTO);
        if (!userDTO.getPassword().equals(userDTO.getConPassWord())) {
            return Callback.error("密码不一致！请重新输入");
        }
        if(StringUtils.isNotEmpty(userEntity.getPassword())) {
            userEntity.setPassword(md5DigestAsHex(userEntity.getPassword().getBytes(StandardCharsets.UTF_8)));
        }
        userEntity.setIsUpdate(1);
        //更新用户表
        int i = userDao.update(userEntity);
        if (i>0){
            return Callback.success("重设密码成功");
        }
        return Callback.error("重设密码失败");
    }

    public Callback<UserVO> getUserByMobile(String mobile) {
        if(StringUtils.isEmpty(mobile)) {
            return Callback.error("参数错误");
        }
        UserVO userVO = userDao.getUserByMobile(mobile);
        if (null != userVO){
            return Callback.success(userVO);
        }
        return Callback.error("手机号暂未注册");
    }

    public Callback checkCode(CheckCodeVO checkCodeVO) {
        //校验短信验证码
        String verificationCode = checkCodeVO.getVerificationCode();
        String verificationCodeService = redisTemplate.opsForValue().get("reset_password_sms_code:" + checkCodeVO.getMobile());
        if (!verificationCode.equals(verificationCodeService)){
            return Callback.error("验证码错误,请重新输入");
        }
        return Callback.success("验证成功");
    }

    public Callback<Integer> getTotalVisit() {
        Integer totalVisit = loginLogMapper.getTotalVisit();
        return Callback.success(totalVisit);
    }

    public Callback<BaseVO<LoginLogVO>> loginLogList(Integer page, Integer pageSize, Integer userId, String loginDate){
        Page<LoginLogVO> startPage = PageMethod.startPage(page, pageSize);
        List<LoginLogVO> loginLogVOList = loginLogMapper.loginLogList(userId,loginDate);
        BaseVO<LoginLogVO> baseVO = new BaseVO<>(loginLogVOList, new PageUtil(pageSize, (int)startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

}
