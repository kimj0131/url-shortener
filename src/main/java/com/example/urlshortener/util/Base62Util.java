package com.example.urlshortener.util;

public class Base62Util {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long id) {
        if(id == 0)
            return String.valueOf(BASE62.charAt(0));

        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        }
        return sb.toString();
    }

    public static long decode(String encoded) {
        long result = 0;
        long power = 1;
        for (int i = 0; i < encoded.length(); i++) {
            result += BASE62.indexOf(encoded.charAt(i)) * power;
            power *= 62;
        }
        return result;
    }
}
