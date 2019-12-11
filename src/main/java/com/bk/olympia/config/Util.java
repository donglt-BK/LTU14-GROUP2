package com.bk.olympia.config;

public class Util {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() < 1;
    }

    public static int parseIntOrZero(String str) {
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (Exception ex) {
            System.out.print(ex.getStackTrace());
        }
        return result;
    }
}
