package com.itheima.health.security;


import com.alibaba.fastjson.JSON;
import com.itheima.health.entity.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * security认证失败处理类
 * authentication-failure-handler-ref="authenticationFailureHandler"
 */

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        //根据异常类型 提示
        if (exception instanceof InternalAuthenticationServiceException) {
            response.setContentType("text/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(new Result(false, "账号错误！请检查后重试")));
        } else if (exception instanceof BadCredentialsException) {
            response.setContentType("text/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(new Result(false, "密码错误！请检查后重试")));
        } else {
            logger.debug("login未知异常！", exception);
            super.onAuthenticationFailure(request,response,exception);
        }

    }

}
