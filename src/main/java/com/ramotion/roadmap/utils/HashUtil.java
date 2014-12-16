package com.ramotion.roadmap.utils;

import java.security.MessageDigest;
import java.util.Formatter;

public class HashUtil {

    public static String getSHA1(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            if ((salt != null) && (salt.length > 0)) digest.update(salt);
            byte[] bytes = digest.digest(password.getBytes("UTF-8"));
            Formatter formatter = new Formatter();

            for (byte b : bytes) formatter.format("%02x", b);

            String result = formatter.toString();
            formatter.close();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
