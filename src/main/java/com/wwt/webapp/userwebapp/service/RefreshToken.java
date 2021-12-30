package com.wwt.webapp.userwebapp.service;

import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;

@SuppressWarnings("ALL")
public class RefreshToken {

    private static RefreshToken nullInstance;

    private final String token;

    private RefreshToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }

    public static RefreshToken newInstance() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return new RefreshToken(Hex.encodeHexString(bytes));
    }

    public static RefreshToken newInstance(String token) {
        return new RefreshToken(token);
    }

    public static RefreshToken getNullInstance() {
        if(nullInstance == null) {
            nullInstance = new RefreshToken("");
        }
        return nullInstance;
    }
}
