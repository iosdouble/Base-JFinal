package com.demo.index;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import live.autu.plugin.jfinal.swagger.annotation.Api;
import live.autu.plugin.jfinal.swagger.annotation.ApiImplicitParam;
import live.autu.plugin.jfinal.swagger.annotation.ApiImplicitParams;
import live.autu.plugin.jfinal.swagger.annotation.ApiOperation;
import live.autu.plugin.jfinal.swagger.config.RequestMethod;

/**
 * com.demo.index
 * create by admin nihui
 * create time 2020/11/30
 * version 1.0
 **/
@Api(description = "测试接口")
public class TestController extends Controller {

    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",required=false,description="这是学员的姓名")
    })
    public void index() {
        String name=getPara("userName");
        renderJson(Ret.ok("msg","测试成功！").set("userName", name));
    }
}
