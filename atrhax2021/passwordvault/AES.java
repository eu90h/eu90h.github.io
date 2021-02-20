package com.example.passwordvault;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static byte[] key;
    private static SecretKeySpec secretKey;

    public static void setKey(String str) {
        try {
            key = str.getBytes("UTF-8");
            key = MessageDigest.getInstance("SHA-1").digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
    }

    public static String encrypt(String str, String str2) {
        try {
            setKey(str2);
            Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
            instance.init(1, secretKey);
            return Base64.getEncoder().encodeToString(instance.doFinal(str.getBytes("UTF-8")));
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("Error while encrypting: " + e.toString());
            return null;
        }
    }

    public static String decrypt(String str, String str2) {
        try {
            setKey(str2);
            Cipher instance = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            instance.init(2, secretKey);
            return new String(instance.doFinal(Base64.getDecoder().decode(str)));
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("Error while decrypting: " + e.toString());
            return null;
        }
    }
}
