package com.demo.swagger;

import com.jfinal.config.Routes;

/**
 * com.demo.swagger
 * create by admin nihui
 * create time 2020/11/30
 * version 1.0
 **/
public class SwaggerRoutes extends Routes {
    @Override
    public void config() {
        setBaseViewPath("/static");
        add("/swagger", SwaggerController.class);
    }
}
