package com.demo.utils;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import org.jboss.logging.Logger;

/**
 * com.demo.utils
 * create by admin nihui
 * create time 2021/1/28
 * version 1.0
 * 跨域解决方案
 **/
public class GlobalActionInterceptor implements Interceptor {
    static Logger logger = Logger.getLogger("GlobalActionInterceptor");
    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        controller.getResponse().addHeader("Access-Control-Allow-Origin", "*");
        inv.invoke();
    }
}