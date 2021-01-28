package com.demo.appUser;

import com.alibaba.fastjson.JSONObject;
import com.demo.common.model.AppUsers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: appUserService
 * @Description: ****
 * @author: yux
 * @date: 2020/11/29  10:01
 */
public class AppUserService {

    private AppUsers appUsersDao = new AppUsers().dao();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public List<AppUsers> getByOrgId(String orgId){
        return appUsersDao.find("select *  from dh_app_users where lt_org_sync_id = '"+orgId+"' and lt_account_type = '0'");
    }

    /**
     * 根据openid查询用户信息
     */
    public AppUsers getAppUserByOpenid(String openid){
      return  appUsersDao.findFirst("select * from dh_app_users where wx_open_id = '"+openid+"'");
    }

    /**
     * 注册用户
     */
    public boolean add(AppUsers user) {

        String openid = user.getWxOpenId();
        AppUsers one = this.getAppUserByOpenid(openid);

        if (one == null) {
            // 保存用户信息
            AppUsers newuser = new AppUsers();
            newuser = user;
            return newuser.save();
        } else {
            // 更新用户信息
            one.setWxNickName(user.getWxNickName());
            one.setWxAvatarurl(user.getWxAvatarurl());
            one.setSex(user.getSex());// 性别 0：未知、1：男、2：女
            one.setWxProvince(user.getWxProvince());
            one.setWxCity(user.getWxCity());
            one.setWxCountry(user.getWxCountry());
            one.setLastLoginTime(dateFormat.format(new Date()));
            return one.update();
        }
    }

    public Object getPhoneNumber(String encryptedData, String session_key, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(session_key);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
