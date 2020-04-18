package com.github.bishoybasily.springframework.commons.core.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author bishoybasily
 * @since 3/19/20
 */
public class CryptoUtils {

    public static String md5(String value) {
        return Hashing.md5().hashString(value, Charset.defaultCharset()).toString();
    }

    public static String sha1(String value) {
        return Hashing.sha1().hashString(value, Charset.defaultCharset()).toString();
    }

    public static String sha256(String value) {
        return Hashing.sha256().hashString(value, Charset.defaultCharset()).toString();
    }

    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
