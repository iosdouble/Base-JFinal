package com.demo.interceptor;

import com.demo.base.AjaxResult;
import com.demo.interceptor.exception.SystemException;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * com.demo.exception
 * create by admin nihui
 * create time 2021/1/28
 * version 1.0
 * 异常处理拦截器
 **/
public class ExceptionInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        try {
            invocation.invoke();
        } catch (SystemException e) {
            e.printStackTrace();
            Controller controller = invocation.getController();
            controller.renderJson(AjaxResult.error(e.getMessage()));
        }
    }
}
