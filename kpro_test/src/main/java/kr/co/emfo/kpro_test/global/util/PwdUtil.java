package kr.co.emfo.kpro_test.global.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PwdUtil {

    public static String pwdMd5(String input) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());

            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();

            for (byte b : digest) {

                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 암호화 실패", e);
        }
    }
}
