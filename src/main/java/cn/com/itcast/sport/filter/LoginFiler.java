package cn.com.itcast.sport.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @description: 登录拦截器
 * @author: JIAWEI
 * @date: Created in 2020/3/16 14:52
 * @version:
 * @modified By:
 */
public class LoginFiler implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoginFiler.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o) throws Exception {
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        log.info("---------------------地址拦截:"+requestURL+"----------------------------");
        HttpSession session = httpServletRequest.getSession();
        Object user = session.getAttribute("userCode");
        if (user == null) {
            try {
                response.sendRedirect("/loginPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
