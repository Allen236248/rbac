package com.allen.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.allen.rbac.dto.SysPrivilegeDto;
import com.allen.rbac.util.ApiResult;
import com.allen.rbac.util.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ApiResult<SysPrivilegeDto> login(HttpServletRequest request) {
        ApiResult<SysPrivilegeDto> apiResult = ApiResult.build();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(username);
            token.setPassword(password.toCharArray());
            LOGGER.info("token=" + JSON.toJSONString(token));
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
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

}
