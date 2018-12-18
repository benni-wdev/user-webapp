package com.wwt.webapp.userwebapp.security;

/**
 * @author benw-at-wwt
 */
final class HashAlgorithmFactory {

    private HashAlgorithmFactory() {}

    public static HashAlgorithm getInstance() {
        return BCryptHashAlgorithm.getInstance();
    }
}
