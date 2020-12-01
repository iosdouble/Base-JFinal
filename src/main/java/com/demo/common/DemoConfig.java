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
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
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
		
		me.setDevMode(p.getBoolean("devMode",false));
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


	@Override
	public void afterJFinalStart() {
		// 1.5 之后支持redis存储access_token、js_ticket，需要先启动RedisPlugin
		// ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache());
		// 1.6新增的2种初始化
		// ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache(Redis.use("weixin")));
		// ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache("weixin"));

		ApiConfig ac = new ApiConfig();
		// 配置微信 API 相关参数
		//ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));

		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		//ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		//ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));

		/**
		 * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
		 */
		ApiConfigKit.putApiConfig(ac);

		/**
		 * 1.9 新增LocalTestTokenCache用于本地和线上同时使用一套appId时避免本地将线上AccessToken冲掉
		 *
		 * 设计初衷：https://www.oschina.net/question/2702126_2237352
		 *
		 * 注意：
		 * 1. 上线时应保证此处isLocalDev为false，或者注释掉该不分代码！
		 *
		 * 2. 为了安全起见，此处可以自己添加密钥之类的参数，例如：
		 * http://localhost/weixin/api/getToken?secret=xxxx
		 * 然后在WeixinApiController#getToken()方法中判断secret
		 *
		 * @see WeixinApiController#getToken()
		 */
		//if (isLocalDev) {
		//    String onLineTokenUrl = "http://localhost/weixin/api/getToken";
		//    ApiConfigKit.setAccessTokenCache(new LocalTestTokenCache(onLineTokenUrl));
		//}

		WxaConfig wc = new WxaConfig();
		wc.setAppId("wxf4cb2eb2702bbf8f");
		wc.setAppSecret("a6c6ac54a3a31841dfb534fd5dee6849");
		WxaConfigKit.setWxaConfig(wc);
	}
}
