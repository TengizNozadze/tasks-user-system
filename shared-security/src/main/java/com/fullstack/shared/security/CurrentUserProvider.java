package com.fullstack.shared.security;

/**
 * Simple holder for current request's token/username. Services can set it when handling incoming requests
 * so FeignSecurityConfig can propagate the Authorization header on outgoing Feign calls.
 */
public class CurrentUserProvider {
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void setToken(String t) {
        token.set(t);
        FeignSecurityConfig.setToken(t);
    }

    public static String getToken() {
        return token.get();
    }

    public static void clear() {
        token.remove();
        FeignSecurityConfig.clear();
    }
}

