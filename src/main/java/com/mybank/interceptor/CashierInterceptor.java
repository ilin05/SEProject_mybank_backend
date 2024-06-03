package com.mybank.interceptor;

import com.google.gson.Gson;
import com.mybank.utils.ApiResult;
import com.mybank.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CashierInterceptor implements HandlerInterceptor {

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
        if(!"cashier".equals(role)) {
            ApiResult notCashier = ApiResult.failure("NOT_CASHIER");
            String notCashierString = new Gson().toJson(notCashier);
            response.getWriter().write(notCashierString);
            return false;
        }
        String privilege = (String) claims.get("privilege");
        String url = request.getRequestURL().toString();
        /*
            A: 存取款、转账、挂失补发、冻结解冻、开户
            B: 存取款、转账、挂失补发、冻结解冻
            C: 存取款、转账
         */
        if("A".equals(privilege)) {
            return true;
        } else if("B".equals(privilege)) {
            ;
        } else if("C".equals(privilege)) {
            ;
        }
        return false;
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
