package com.github.bishoybasily.springframework.commons.core.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
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

    public static byte[] base64Decode(String value) {
        return Base64.getDecoder().decode(value);
    }

    public static boolean isSignatureVerified(
            String fullAlgorithm, String shortAlgorithm,
            String base64Key, String base64TokenSignature,
            String token
    ) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        final var encodedPublicKey = base64Decode(base64Key);
        final var keyFactory = KeyFactory.getInstance(shortAlgorithm);
        final var publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        final var publicKey = keyFactory.generatePublic(publicKeySpec);

        final var encodedSignature = base64Decode(base64TokenSignature);

        final var signature = Signature.getInstance(fullAlgorithm);
        signature.initVerify(publicKey);
        signature.update(token.getBytes());

        return signature.verify(encodedSignature);

    }

}
