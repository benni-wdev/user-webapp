package com.wwt.webapp.userwebapp.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashAlgorithmTest {


    @Test
    public void testCheckPassword() {
        @SuppressWarnings("SpellCheckingInspection") HashAlgorithm algo = BCryptHashAlgorithm.getInstance();
        assertTrue(algo.isPasswordEquals ("test1",
                algo.hashPassword("test1")
        ));
    }


    @Test
    public void testPwAndSaltLength() {
        HashAlgorithm algo = BCryptHashAlgorithm.getInstance();
        assertEquals(60,algo.hashPassword("test").length());
    }

}
