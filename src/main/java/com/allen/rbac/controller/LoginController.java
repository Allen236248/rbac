package com.allen.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.allen.rbac.dto.SysUserDto;
import com.allen.rbac.service.SysUserService;
import com.allen.rbac.util.ApiResult;
import com.allen.rbac.exception.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public ApiResult<SysUserDto> login(HttpServletRequest request) {
        ApiResult<SysUserDto> apiResult = ApiResult.build();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(username);
            token.setPassword(password.toCharArray());
            LOGGER.info("token=" + JSON.toJSONString(token));
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);

            //登陆成功，返回用户的所有权限
            SysUserDto sysUserDto = sysUserService.findByUsernameWithPrivilege(username);
            apiResult.setAttach(sysUserDto);
        } catch (ServiceException se) {
            LOGGER.error("登陆失败，", se);
            apiResult.error("登陆失败");
        } catch (IncorrectCredentialsException e) {
            LOGGER.error("登陆失败，", e);
            apiResult.error("用户名或密码错误");
        } catch (AuthenticationException e) {
            LOGGER.error("登陆失败，", e);
            apiResult.error("用户名或密码错误");
        } catch (Exception e) {
            LOGGER.error("登陆失败，", e);
            apiResult.error("登陆失败");
        }
        return apiResult;
    }

    @GetMapping("/login")
    public ModelAndView toLogin() {
        ModelAndView mav = new ModelAndView("/login");
        //mav.addObject("msg", "请先登录");
        return mav;
    }

    @GetMapping("/logout")
    public ApiResult<Void> logout() {
        ApiResult<Void> apiResult = ApiResult.build();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return apiResult;
    }

}
