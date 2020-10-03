package com.developer.phimtatnhanh.ads.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

class EncryptionUtils {

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encodeMd5(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] data = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            return md5Hex(data);
        } catch (Exception e) {
            e.printStackTrace();
            return string;
        }
    }

    private static String md5Hex(byte[] data) {
        return new String(encodeHex(data));
    }

    private static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }

}
