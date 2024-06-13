package com.mybank.interceptor;

import com.google.gson.Gson;
import com.mybank.utils.ApiResult;
import com.mybank.utils.JwtUtils;
import com.mybank.utils.UserUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;


public class InternetInterceptor implements HandlerInterceptor {

    public void writeFailure(HttpServletResponse response, String message) throws Exception {
        ApiResult notLogin = ApiResult.failure(message);
        String notLoginString = new Gson().toJson(notLogin);
        response.getWriter().write(notLoginString);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Claims claims = null;
        try {
            String jwt = request.getHeader("Authorization");
            claims = JwtUtils.parseJwt(jwt);
        } catch (Exception e) {
            writeFailure(response,"NOT_LOGIN");
            return false;
        }
        String role = (String) claims.get("role");
        if(!"internet".equals(role)) {
            writeFailure(response, "NOT_INTERNET");
            return false;
        }

        HashMap<String,String> params = new HashMap<>();
        params.put("customerId",claims.get("customerId") + "");
        params.put("customerAccountId",(String) claims.get("customerAccountId"));
        UserUtils.set(params);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserUtils.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
