package com.demo.appUser;

import com.alibaba.fastjson.JSONObject;
import com.demo.base.AjaxResult;
import com.demo.common.model.AppUsers;
import com.demo.utils.StrUtil;
import com.demo.utils.StringUtils;
import com.demo.utils.idgenerator.SnowflakeIdUtils;
import com.jfinal.aop.Duang;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import com.jfinal.wxaapp.api.WxaUserApi;
import live.autu.plugin.jfinal.swagger.annotation.Api;
import live.autu.plugin.jfinal.swagger.annotation.ApiImplicitParam;
import live.autu.plugin.jfinal.swagger.annotation.ApiImplicitParams;
import live.autu.plugin.jfinal.swagger.annotation.ApiOperation;
import live.autu.plugin.jfinal.swagger.config.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: userController
 * @Description: ****
 * @author: yux
 * @date: 2020/11/29  10:01
 */
@Api(value = "用户登录接口")
public class AppUserController extends Controller {
    // 微信用户接口api
    protected WxaUserApi wxaUserApi = Duang.duang(WxaUserApi.class);
    private static String jsCode2sessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
    // 获取实名信息url
    private static String realNameInfoUrl = "https://api.weixin.qq.com/cgi-bin/wxopen/getrealnameinfo?access_token=";
    private static String realNameInfoCheckUrl = "https://api.weixin.qq.com/intp/realname/checkrealnameinfo?access_token=";
    private static String categoryIdUrl = "https://api.weixin.qq.com/wxa/get_category?access_token=";

    // 实名信息校验接口校验结果常量
    public static final String V_OP_NA = "用户暂未实名认证";
    public static final String V_OP_NM_MA = "用户与姓名匹配";
    public static final String V_OP_NM_UM = "用户与姓名不匹配";
    public static final String V_NM_ID_MA = "姓名与证件号匹配";
    public static final String V_NM_ID_UM = "姓名与证件号不匹配";
    @Inject
    private AppUserService appUserService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 登录
     */

    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",required=false,description="传入Code")
    })
    public void login() {
        String jsCode = getPara("code");
        if (StrKit.isBlank(jsCode)) {
            Kv data = Kv.by("errcode", 500).set("errmsg", "code is blank");
            renderJson(data);
            return;
        }
        // 获取SessionKey
        ApiResult apiResult = wxaUserApi.getSessionKey(jsCode);

        if (!apiResult.isSucceed()) {
            renderJson(apiResult.getJson());
            return;
        }
        renderJson(apiResult.getJson());
    }

    /**
     * 判断用户是否存在
     */
    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="openid",required=false,description="这是学员的姓名")
    })
    public void exitUser() {
        Kv data = null;
        String openid = getPara("openid");
        AppUsers user = appUserService.getAppUserByOpenid(openid);
        if (user != null) {
            data = Kv.by("result", 1).set("msg", "用户已存在");
        } else {
            data = Kv.by("result", 0).set("msg", "用户不存在");
        }
    }

    /**
     * 2.注册用户
     */
    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",required=false,description="这是学员的姓名")
    })
    public void register() {
        String openid = getPara("openid");
        AppUsers user = new AppUsers();
        user.setWxNickName(getPara("nickName"));
        user.setWxAvatarurl(getPara("avatarUrl"));
        user.setSex(StrUtil.genderToString(getParaToInt("gender")));
        user.setWxProvince(getPara("province"));
        user.setWxCity(getPara("city"));
        user.setWxCountry(getPara("country"));
        user.setWxOpenId(openid);
        user.setWxUnionId(getPara("unionid"));
        user.setLastLoginTime(dateFormat.format(new Date()));
        user.setTotalPoints(0);
        user.setCurrentPoints(0);
        user.setIsRegister("0");
        SnowflakeIdUtils s = new SnowflakeIdUtils();
        user.setId(String.valueOf(s.nextId()));
        Kv data = null;
        boolean flag = appUserService.add(user);
        if (flag) {
            user = appUserService.getAppUserByOpenid(openid);
            String id_card = user.getIdCard();
            String phone = user.getPhone();
            String isExistence = "0";
            if (StringUtils.isNotEmpty(id_card) && StringUtils.isNotEmpty(phone)) {
                isExistence = "1";
            }
            data = Kv.by("result", 1).set("msg", "成功").set("isExistence", isExistence);
        } else {
            data = Kv.by("result", 0).set("msg", "失败");
        }
        renderJson(data);
    }

    /**
     * 手机号解密保存
     */
    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",required=false,description="这是学员的姓名")
    })
    public void getMobile() {
        String encDataStr = get("encDataStr");
        String ivStr = get("ivStr");
        String keyStr = get("keyStr");
        String openid = get("openid");
        Object jsonResult = appUserService.getPhoneNumber(encDataStr, keyStr, ivStr);
        JSONObject json = (JSONObject) JSONObject.toJSON(jsonResult);
        String moblie = json.get("phoneNumber").toString();
        Kv data = null;
        AppUsers appUsers = null;
        appUsers = appUserService.getAppUserByOpenid(openid);
        if (appUsers == null) {
            data = Kv.by("result", 0).set("msg", "失败");
        } else {
            appUsers.setPhone(moblie);
            appUsers.update();
            data = Kv.by("result", 1).set("msg", "成功");
        }
        renderJson(data);
    }

    /**
     * 获取用户信息
     */
    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="commit_openid",required=false,description="openid ")
    })
    public void userinfo() {
        String openid = getPara("commit_openid");
        AppUsers user = appUserService.getAppUserByOpenid(openid);
        if (user == null) {
            renderJson("未找到用户");
        }else{
                String sql = "select * from dh_member_apply where commit_openid = '"+openid+"'";
//                MemberApply memberApply = memberApplyDao.findFirst(sql);
                Record memberApply = Db.findFirst("select DISTINCT(dh_member_apply.apply_crads), dh_member_apply.* ,dept.dept_name from dh_member_apply LEFT JOIN\n" +
                        "(select tmp.dept_id,sys_dept.parent_id,sys_dept.dept_name from sys_dept,(SELECT sys_dept.dept_id,sys_dept.parent_id FROM dh_member_apply LEFT JOIN sys_dept ON dh_member_apply.org_id = sys_dept.dept_id) as tmp WHERE sys_dept.dept_id = tmp.parent_id) as dept \n" +
                        " on dh_member_apply.org_id = dept.dept_id WHERE commit_openid = ?",openid);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("appUser",user);
                jsonObject.put("applyUser",memberApply);
                AjaxResult result = AjaxResult.success("操作成功", jsonObject);
                renderJson(result);
        }

    }

    /**
     * 用户是否完善资料
     */
    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",required=false,description="这是学员的姓名")
    })
    public void isData() {
        String openid = getPara("openid");
        AppUsers user = appUserService.getAppUserByOpenid(openid);
        String community_id = user.getAddressCommunity();
        String address_quarters_id = user.getAddressQuartersId();
        String floor = user.getAddressFloor();
        String result = "0";
        String msg = "未完善信息";
        if (StringUtils.isNotEmpty(community_id) && StringUtils.isNotEmpty(address_quarters_id) && StringUtils.isNotEmpty(floor)) {
            result = "1";
            msg = "已完善信息";
        }
        Kv data = Kv.by("result", result).set("msg", msg);
        renderJson(data);
    }

    /**
     * 用户是否实名
     */
    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",required=false,description="这是学员的姓名")
    })
    public void isRealName() {
        String openid = getPara("openid");
        AppUsers user = appUserService.getAppUserByOpenid(openid);
        String cardId = user.getIdCard();
        String result = "0";
        String msg = "未实名";
        if (StringUtils.isNotEmpty(cardId)) {
            result = "1";
            msg = "已实名";
        }
        Kv data = Kv.by("result", result).set("msg", msg);
        renderJson(data);
    }

    /**
     * 验证微信支付实名信息
     *
     * @throws Throwable
     */
    @ApiOperation(methods= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",required=false,description="这是学员的姓名")
    })
    public void checkrealnameinfo() {
        Kv data = null;
        String appopenid = getPara("openid");
        String name = getPara("real_name");
        String idcard = getPara("cred_id");
        String code = getPara("code");

        // 参数判空
        if (StrKit.isBlank(appopenid)) {
            data = Kv.by("result", -1).set("msg", "openid is blank");
            renderJson(data);
            return;
        }
        if (StrKit.isBlank(name)) {
            data = Kv.by("result", -1).set("msg", "real_name is blank");
            renderJson(data);
            return;
        }
        if (StrKit.isBlank(idcard)) {
            data = Kv.by("result", -1).set("msg", "cred_id is blank");
            renderJson(data);
            return;
        }
        if (StrKit.isBlank(code)) {
            data = Kv.by("result", -1).set("msg", "code is blank");
            renderJson(data);
            return;
        }

        // 传递参数拼接
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openid", appopenid);
        params.put("real_name", name);
        params.put("cred_id", idcard);
        params.put("cred_type", 1);
        params.put("code", code);

        String url = realNameInfoCheckUrl + AccessTokenApi.getAccessTokenStr();
        String jsonResult = HttpUtils.post(url, JsonUtils.toJson(params));
        JSONObject jsonObject = JSONObject.parseObject(jsonResult);
        //{"errcode":0,"errmsg":"ok","verify_openid":"V_OP_NM_MA","verify_real_name":"V_NM_ID_MA","bind_bankcard":"YES"}

        int errcode = (int) jsonObject.get("errcode");

        if (errcode == 0) {
            String errmsg = (String) jsonObject.get("errmsg");
            String verify_openid = (String) jsonObject.get("verify_openid");
            String verify_real_name = (String) jsonObject.get("verify_real_name");
            String bind_bankcard = (String) jsonObject.get("bind_bankcard");

            Map<String, Object> list = new HashMap<String, Object>();
            list.put("errcode", errcode);
            list.put("errmsg", errmsg);
            list.put("verify_openid", verify_openid);
            list.put("verify_real_name", verify_real_name);
            list.put("bind_bankcard", bind_bankcard);

            // 返回提示信息文案
            if ("V_OP_NA".equals(verify_openid)) {
                list.put("info", V_OP_NA);
            }

            if ("V_OP_NM_UM".equals(verify_openid)) {
                list.put("info", V_OP_NM_UM);
            }

            if ("V_OP_NM_MA".equals(verify_openid)) {
                if ("V_NM_ID_MA".equals(verify_real_name)) {
                    list.put("info", V_OP_NM_MA + V_NM_ID_MA);
                    //用户与姓名匹配 且 姓名与证件号匹配 两者全部满足后更新用户信息和实名状态
                    AppUsers uf = appUserService.getAppUserByOpenid(appopenid);
                    if (uf != null) {
                        uf.setWxNickName(name);
                        uf.setLtRealName(name);
                        uf.setIdCard(idcard);
                        uf.update();
                    }
                }

                if ("V_NM_ID_UM".equals(verify_real_name)) {
                    list.put("info", V_OP_NM_MA + V_NM_ID_UM);
                }
            }

            data = Kv.by("result", 1).set("msg", list);
        } else {
            data = Kv.by("result", 0).set("msg", "获取失败");
        }
        renderJson(data);
    }




}
