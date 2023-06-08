package com.mcrmb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 这个类提供了使用 MD5 算法加密字符串的实用方法。
 */
public class EncryptionUtil {

    /**
     * 对字符串进行MD5加密
     *
     * @param originalString 原始字符串
     * @return 加密后的字符串
     */
    public static String encryptString(String originalString) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(originalString.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();

            for (byte byteValue : digest) {
                int intValue = byteValue;
                if (intValue < 0) {
                    intValue += 256;
                }

                if (intValue < 16) {
                    hexString.append("0");
                }

                hexString.append(Integer.toHexString(intValue));
            }

            String encryptedString = hexString.toString();
            return encryptedString.toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


}
