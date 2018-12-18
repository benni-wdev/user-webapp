package com.wwt.webapp.userwebapp.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author benw-at-wwt
 */
public final class BCryptHashAlgorithm implements HashAlgorithm {


    private static class BCryptHashAlgorithmHolder {
        private static final BCryptHashAlgorithm INSTANCE = new BCryptHashAlgorithm();
    }

    /**
     * not instantiatable -> singleton because object is stateless
     */
    private BCryptHashAlgorithm() {}

    /**
     * Singleton instance
     *
     * @return the one and only instance
     */
    static BCryptHashAlgorithm getInstance() {
        return BCryptHashAlgorithmHolder.INSTANCE;
    }


    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean isPasswordEquals(String password, String passwordHash) {

        return BCrypt.checkpw(password, passwordHash);
    }


}
