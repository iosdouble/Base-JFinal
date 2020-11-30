package com.demo.common;


import com.demo.common.model._MappingKit;

import com.demo.route.ApiRoutes;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import live.autu.plugin.jfinal.swagger.config.SwaggerPlugin;
import live.autu.plugin.jfinal.swagger.config.routes.SwaggerRoutes;
import live.autu.plugin.jfinal.swagger.model.SwaggerApiInfo;
import live.autu.plugin.jfinal.swagger.model.SwaggerDoc;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * API 引导式配置
 */
public class DemoConfig extends JFinalConfig {
	
	static Prop p;
	
	/**
	 * 启动入口，运行此 main 方法可以启动项目，此 main 方法可以放置在任意的 Class 类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
//		UndertowServer.start(DemoConfig.class);
		UndertowServer.create(DemoConfig.class,"undertow.properties").start();
	}
	
	/**
	 * PropKit.useFirstFound(...) 使用参数中从左到右最先被找到的配置文件
	 * 从左到右依次去找配置，找到则立即加载并立即返回，后续配置将被忽略
	 */
	static void loadConfig() {
		if (p == null) {
			p = PropKit.useFirstFound("demo-config-pro.txt", "demo-config-dev.txt");
		}
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		loadConfig();
		
		me.setDevMode(p.getBoolean("devMode"));
		//设置默认上传文件保存路径 getFile等使用
		me.setBaseUploadPath("upload/temp/");
		
		/**
		 * 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
		 * 注入动作支持任意深度并自动处理循环注入
		 */
		me.setInjectDependency(true);

		// 配置对超类中的属性进行注入
		me.setInjectSuperClass(true);

		//设置上传最大限制尺寸
		//me.setMaxPostSize(1024*1024*10);
		//设置默认下载文件路径 renderFile使用
		me.setBaseDownloadPath("download");
		//设置默认视图类型
		me.setViewType(ViewType.JFINAL_TEMPLATE);
		//设置404渲染视图
		//me.setError404View("404.html");

		//设置启用依赖注入
		me.setInjectDependency(true);
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		//推荐拆分方式 如果需要就解开注释 创建对应的 Routes
		//me.add(new AdminRoutes());//配置后台管理系统路由
		//me.add(new FrontRoutes());//配置网站前台路由
		me.add(new ApiRoutes());//配置API访问路由
		//me.add(new WechatRoutes());//配置微信端访问路由

//		me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
//		me.add("/blog", BlogController.class);			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"

		me.add(new SwaggerRoutes());
	}


	
	public void configEngine(Engine me) {
		//配置模板支持热加载
		me.setDevMode(p.getBoolean("engineDevMode", false));
		//这里只有选择JFinal TPL的时候才用
		//配置共享函数模板
		//me.addSharedFunction("/view/common/layout.html")
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置 druid 数据库连接池插件
		DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
		me.add(druidPlugin);

		me.add(new SwaggerPlugin(new SwaggerDoc().setBasePath("/").setHost("127.0.0.1").setSwagger("2.0")
				.setInfo(new SwaggerApiInfo("jfinal swagger demo", "1.0", "jfinal swagger", ""))));
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 所有映射在 MappingKit 中自动化搞定
		_MappingKit.mapping(arp);
		me.add(arp);
	}
	
	public static DruidPlugin createDruidPlugin() {
		loadConfig();
		
		return new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
}
