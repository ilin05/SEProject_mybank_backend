package com.mybank.interceptor;

import com.google.gson.Gson;
import com.mybank.utils.ApiResult;
import com.mybank.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public class CashierInterceptor implements HandlerInterceptor {

    public void writeFailure(HttpServletResponse response, String message) throws IOException {
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
        if(!"cashier".equals(role)) {
            writeFailure(response, "NOT_CASHIER");
            return false;
        }

        String privilege = (String) claims.get("privilege");
        String url = request.getRequestURL().toString();
        System.out.println(url);
        /*
            A: 存取款、转账、挂失补发、冻结解冻、开户销户、更改支付密码
            B: 存取款、转账、挂失补发、冻结解冻
            C: 存取款、转账
         */
        if("A".equals(privilege)) {
            return true;
        } else if("B".equals(privilege)) {
            if(url.contains("openAccount")||url.contains("closeAccount")) {
                writeFailure(response, "NOT_PRIVILEGE");
                return false;
            }
            return true;
        } else if("C".equals(privilege)) {
            if(url.contains("openAccount") || url.contains("freeze") || url.contains("unfreeze")
                    || url.contains("reportLoss") || url.contains("reissue")) {
                writeFailure(response, "NOT_PRIVILEGE");
                return false;
            }
            return true;
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
