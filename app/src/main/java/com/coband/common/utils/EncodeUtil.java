package com.coband.common.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by mqh on 11/1/16.
 */

public class EncodeUtil
{

    // 密钥
    public final static String secretKey = "6k7q2x5qz148x29w36780l2A";
    // 向量
//    public final static String iv = "12345678";
    // 加解密统一使用的编码方式
    public final static String encoding = "utf-8";
    // 3des加密
    public static final String algorithm = "desede";

    /**
     * @param str 需要加密的文字
     * @return 加密后的文字
     * @throws Exception 加密失败
     */
    public static String get3DES(String str) throws Exception
    {
        if (null == str || str.isEmpty()) return null;
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory
                .getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] encryptData = cipher.doFinal(str.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return 解密后的文字
     * @throws Exception
     */
    public static String decode3DES(String encryptText) throws Exception {
        if (null == encryptText || encryptText.isEmpty()) return null;
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        cipher.init(Cipher.DECRYPT_MODE, deskey);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

        return new String(decryptData, encoding);
    }
}
