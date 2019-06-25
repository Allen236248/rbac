package com.allen.rbac.exception;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        ModelMap model = new ModelMap();
        model.addAttribute("code", "-1");
        if(isRestful(o)) {
            if(e instanceof AuthorizationException || e instanceof UnauthorizedException)  {
                model.addAttribute("msg", "无权限进行此操作");
            } else {
                model.addAttribute("msg", e.getMessage());
            }
            response.setContentType("application/json; charset=UTF-8");
            try {
                response.getWriter().write(JSON.toJSONString(model));
            } catch (IOException ioe) {
            }
            return new ModelAndView();
        } else {
            model.addAttribute("msg", "系统异常");
            return new ModelAndView("/error", model);
        }
    }

    private boolean isRestful(Object o) {
        if(o instanceof HandlerMethod) {
            HandlerMethod handler = (HandlerMethod) o;
            ResponseBody rb = handler.getMethodAnnotation(ResponseBody.class);
            if(null != rb) {
                return true;
            } else if(isAnnotationPresent(handler.getBeanType(), ResponseBody.class)) {
                return true;
            } else if(isAnnotationPresent(handler.getBeanType(), RestController.class)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotationType) {
        if(clazz.isAnnotationPresent(annotationType)) {
            return true;
        } else {
            clazz = clazz.getSuperclass();
            if(null != clazz) {
                return isAnnotationPresent(clazz, annotationType);
            }
        }
        return false;
    }
}
