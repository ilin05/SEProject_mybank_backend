package com.mybank.interceptor;

import com.google.gson.Gson;
import com.mybank.utils.ApiResult;
import com.mybank.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String url = request.getRequestURL().toString();
//        String ip = request.getRemoteAddr();

        String jwt = request.getHeader("Authorization");

        if (jwt==null || jwt.isEmpty() || !JwtUtils.validateJwt(jwt)) {
            ApiResult notLogin = ApiResult.failure("NOT_LOGIN");
            String notLoginString = new Gson().toJson(notLogin);
            response.getWriter().write(notLoginString);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
