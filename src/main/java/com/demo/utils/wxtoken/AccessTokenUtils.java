package com.demo.utils.wxtoken;

import com.demo.utils.http.HttpUtil;
import com.demo.utils.json.JsonUtil;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import java.util.HashMap;
import java.util.Map;

/**
 * com.demo.utils
 * create by admin nihui
 * create time 2021/1/28
 * version 1.0
 **/
public class AccessTokenUtils {

    private static Prop p = PropKit.use("demo-config-dev.txt");

    public static String getToken(){
        Map params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", p.get("appId"));
        params.put("secret",p.get("appSecret"));
        String doGet = HttpUtil.doGet("https://api.weixin.qq.com/cgi-bin/token", params);
        AccessToken accessToken = JsonUtil.toObject(doGet, AccessToken.class);
        return accessToken.getAccess_token();
    }
}
