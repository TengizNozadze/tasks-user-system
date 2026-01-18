package com.fullstack.shared.security;

public class FeignSecurityConfig {

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<String>();

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static void clear() {
        tokenHolder.remove();
    }

    public static String getToken() {
        return tokenHolder.get();
    }

    public static boolean hasToken() {
        String t = tokenHolder.get();
        return t != null && !t.trim().isEmpty();
    }
}
