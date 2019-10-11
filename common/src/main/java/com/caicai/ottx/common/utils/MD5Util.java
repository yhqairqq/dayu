package com.caicai.ottx.common.utils;

import java.security.MessageDigest;

/**
 * MD5工具类
 * Created by oneape<oneape15@163.com>
 * Created 2019-06-18 18:25.
 * Modify:
 */
public class MD5Util {

    // 盐值, 用于混淆md5
    private static final String slat = "*I_*_Need_some_slat*";

    public static String getMD5(String str) {
        return getMD5(str, false);
    }

    public static String getMD5(String str, boolean needSlat) {
        String base = str;
        if (needSlat) {
            base += slat;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(base.getBytes("UTF8"));
            byte s[] = md.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result.toUpperCase();
        } catch (Exception e) {
            //
        }
        return "";
    }

    /**
     * 对拉冬系统用户进行特殊md5加密
     * --> md5( ( md5(password + salt) ).toLowerCase()  + username ).toUpperCase()
     *
     * @param username String
     * @param password String
     * @param salt     String
     * @return String
     */
    public static String ladonUserPassword(String username, String password, String salt) {
        String md5_1 = getMD5(password + salt);
        return getMD5(md5_1.toLowerCase() + username);
    }

    public static void main(String[] args) {
        System.out.println(ladonUserPassword("test", "test", "Z_00544-L"));
    }

}
