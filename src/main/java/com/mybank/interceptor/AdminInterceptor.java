package com.mybank.interceptor;

import com.google.gson.Gson;
import com.mybank.utils.ApiResult;
import com.mybank.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Claims claims = null;
        try {
            String jwt = request.getHeader("Authorization");
            claims = JwtUtils.parseJwt(jwt);
        } catch (Exception e) {
            ApiResult notLogin = ApiResult.failure("NOT_LOGIN");
            String notLoginString = new Gson().toJson(notLogin);
            response.getWriter().write(notLoginString);
            return false;
        }
        String role = (String) claims.get("role");
        if(!"admin".equals(role)) {
            ApiResult notAdmin = ApiResult.failure("NOT_ADMIN");
            String notAdminString = new Gson().toJson(notAdmin);
            response.getWriter().write(notAdminString);
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
