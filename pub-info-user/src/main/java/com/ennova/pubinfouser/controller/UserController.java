package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfouser.dto.UserDTO;
import com.ennova.pubinfouser.service.UserService;
import com.ennova.pubinfouser.vo.CheckCodeVO;
import com.ennova.pubinfouser.vo.DeptNumVO;
import com.ennova.pubinfouser.vo.LoginLogVO;
import com.ennova.pubinfouser.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "用户API")
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Callback login(String account, String password) {
        return userService.login(account,password);
    }

    @PostMapping("/loginAll")
    public Callback loginAll(String account, String password) {
        return userService.loginAll(account,password);
    }
//
//    @GetMapping("/info")
//    public String info(HttpSession session) {
//        return "当前登录的是：" + session.getAttribute("login_user");
//    }

    @ApiOperation(value = "刷新token", tags = "用户API")
    @GetMapping("/refreshToken")
    public Callback<String>  refreshToken(@RequestParam String refreshToken){
        Callback<String>  callback = userService.refreshToken(refreshToken);
        return callback;
    }

    @ApiOperation(value = "用户管理-添加用户",tags = "用户API")
    @PostMapping("/addUser")
    public Callback addUser(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @ApiOperation(value = "用户管理-根据用户id获取用户信息",  tags = "用户API")
    @GetMapping("/getUserById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true),
    })
    public Callback<UserVO> getUserById(Integer id) {
        return userService.getUserById(id);
    }

    @ApiOperation(value = "用户管理-修改用户",tags = "用户API")
    @PostMapping("/updateUser")
    public Callback updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @ApiOperation(value = "用户管理-删除用户", tags = "用户API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true)
    })
    @PostMapping("/deleteUser")
    public Callback deleteUser(Integer id) {
        return userService.deleteUser(id);
    }

    @ApiOperation(value = "用户管理-获取用户分页列表", tags = "用户API")
    @GetMapping("/listUsers")
    public Callback listUsers(Integer page, Integer pageSize, Integer company,Integer roleId,Integer department, String searchKey) {
        return userService.listUsers(page,pageSize, company,roleId,department, searchKey);
    }

    @ApiOperation(value = "用户管理-根据部门统计",  tags = "用户API")
    @GetMapping("/getCouPerDept")
    public Callback<DeptNumVO> getCouPerDept() {
        return userService.getCouPerDept();
    }

    @ApiOperation(value = "用户管理-根据用户手机获取用户信息",  tags = "用户API")
    @GetMapping("/getUserByMobile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true),
    })
    public Callback<UserVO> getUserByMobile(String mobile) {
        return userService.getUserByMobile(mobile);
    }

    @ApiOperation(value = "用户管理-校验验证码",  tags = "用户API")
    @PostMapping("/checkCode")
    public Callback checkCode(@RequestBody CheckCodeVO checkCodeVO) {
        return userService.checkCode(checkCodeVO);
    }

    @ApiOperation(value = "登入重设密码", tags = "用户API")
    @PostMapping("/resetPassword")
    public Callback resetPassword(@RequestBody UserDTO userDTO){
        return userService.resetPassword(userDTO);
    }

    @ApiOperation(value = "网站总访问记录数",  tags = "用户API")
    @GetMapping("/getTotalVisit")
    public Callback<Integer> getTotalVisit() {
        return userService.getTotalVisit();
    }


    @ApiOperation(value = "集成页面-用户管理-获取用户列表", tags = "用户API")
    @GetMapping("/listAllUsers")
    public Callback listAllUsers(Integer company,Integer roleId,Integer department, String searchKey) {
        return userService.listAllUsers(company, roleId, department, searchKey);
    }

    @ApiOperation(value = "日志访问记录",  tags = "用户API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "loginDate", value = "yyyy-MM"),

    })
    @GetMapping("/loginLogList")
    public Callback<BaseVO<LoginLogVO>> loginLogList(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     Integer userId, String loginDate){
        return userService.loginLogList(page,pageSize,userId,loginDate);

    }

}
