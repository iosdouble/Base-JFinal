package com.demo.utils.sha1;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * com.demo.common.util.sha1
 * create by admin nihui
 * create time 2020/12/4
 * version 1.0
 **/
public class Sha1 {
    /**
     * SHA1 安全加密算法
     * @param maps 参数key-value map集合
     * @return
     * @throws DigestException
     */
    public static String SHA1(Map<String, Object> maps) throws DigestException {
        //获取信息摘要 - 参数字典排序后字符串
        String decrypt = getOrderByLexicographic(maps);
        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decrypt.getBytes());
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xff);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigestException("签名错误！");
        }
    }
    /**
     * 获取参数的字典排序
     * @param maps 参数key-value map集合
     * @return String 排序后的字符串
     */
    private static String getOrderByLexicographic(Map<String, Object> maps){
        return splitParams(lexicographicOrder(getParamsName(maps)),maps);
    }
    /**
     * 获取参数名称 key
     * @param maps 参数key-value map集合
     * @return
     */
    private static List<String> getParamsName(Map<String, Object> maps){
        List<String> paramNames = new ArrayList<String>();
        for(Map.Entry<String, Object> entry : maps.entrySet()){
            paramNames.add(String.valueOf(entry.getValue()));
        }
        return paramNames;
    }
    /**
     * 参数名称按字典排序
     * @param paramNames 参数名称List集合
     * @return 排序后的参数名称List集合
     */
    private static List<String> lexicographicOrder(List<String> paramNames){
        Collections.sort(paramNames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                char[] chars1=o2.toCharArray();
                char[] chars2=o1.toCharArray();
                int i=0;
                while(i<chars1.length && i<chars2.length){
                    if(chars1[i]>chars2[i]){
                        return -1;
                    }else if(chars1[i]<chars2[i]){
                        return 1;
                    }else{
                        i++;
                    }
                }
                if(i==chars1.length){  //o1到头
                    return 1;
                }
                if(i== chars2.length){ //o2到头
                    return -1;
                }
                return 0;
            }
        });
        return paramNames;
    }

    /**
     * 拼接排序好的参数名称和参数值
     * @param paramNames 排序后的参数名称集合
     * @param maps 参数key-value map集合
     * @return String 拼接后的字符串
     */
    private static String splitParams(List<String> paramNames, Map<String, Object> maps){
        StringBuilder paramStr = new StringBuilder();
        for(String paramName : paramNames){
            paramStr.append(paramName);
//            for(Map.Entry<String,Object> entry : maps.entrySet()){
//                if(paramName.equals(entry.getKey())){
////                    paramStr.append(String.valueOf(entry.getValue()));
//                }
//            }
        }
        return paramStr.toString();
    }

//    public static void main(String[] args) throws DigestException {
//        Map  map = new HashMap();
//        map.put("api_ticket","VUkHJgW2SFNG4xhs6ZntSO9eDu8R5E44g6z8zIRmdzStRQIExPyTmjbk2MecyZajH-S8D5Eiy-NuBIIU3wpIxQ");
//        map.put("card_id","pWHBzs6atNI0pcQZG8zraWdDVd28");
//        map.put("nonce_str","aa6ea57d-6e20-4e9e-a418-f9e8be0fc7bd");
//        map.put("timestamp",1598457600);
//        String s = Sha1.SHA1(map);
//        System.out.println("SHA1加密"+s);
//        String str = "jsapi_ticket=" + "VUkHJgW2SFNG4xhs6ZntSO9eDu8R5E44g6z8zIRmdzStRQIExPyTmjbk2MecyZajH-S8D5Eiy-NuBIIU3wpIxQ" +
//                "&noncestr=" + "aa6ea57d-6e20-4e9e-a418-f9e8be0fc7bd" +
//                "&timestamp=" + 1598457600 +
//                "&card_id=" + "pWHBzs6atNI0pcQZG8zraWdDVd28";
//        System.out.println(SHA1Util.getSHA(str));
//    }
}
