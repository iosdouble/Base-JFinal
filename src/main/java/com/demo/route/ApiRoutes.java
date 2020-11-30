package com.demo.route;


import com.demo.index.TestController;
import com.jfinal.config.Routes;

/**
 * com.demo.route
 * create by admin nihui
 * create time 2020/11/30
 * version 1.0
 **/
public class ApiRoutes extends Routes {
    @Override
    public void config() {
        //针对一组路由配置baseViewPath
        //this.setBaseViewPath("/_view/_admin");
        //针对一组路由配置单独的拦截器
        //this.addInterceptor(new AdminAuthInterceptor());
        //针对后台管理系统配置路由+controller
        //this.add("/admin", AdminIndexController.class,"/index");
        add("/api/test", TestController.class);
    }
}
