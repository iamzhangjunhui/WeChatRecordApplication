package com.example.kaylee.wechatrecordapplication;

/**
 * Created by kaylee on 2018/3/15.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author:
 * date: 2016/10/25 20:59
 */
public class MD5Util {

    public static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte[] b) {  //String to  byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}