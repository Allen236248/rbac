package com.allen.rbac.exception;

import com.allen.rbac.util.ApiResult;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理优先级：
 * 1、在Controller方法上使用@ExceptionHandler
 * 2、使用@ControllerAdvice注解定义异常处理类，并在@ExceptionHandler注解的方法中处理异常
 * 3、通过实现HandlerExceptionResolver接口
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        if(e instanceof AuthorizationException || e instanceof UnauthorizedException)  {
            ApiResult<Void> apiResult = ApiResult.build();
            apiResult.error("无权限进行此操作");
            return apiResult;
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("msg", e.getMessage());
            mav.setViewName("/error");
            return mav;
        }
    }
}
