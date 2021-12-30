package com.wwt.webapp.userwebapp.security;


/**
 *
 * @author benw-at-wwt
 */
public final class PasswordHash {

    private final String passwordHash;
    private HashAlgorithm hashAlgorithm;


    private PasswordHash(String password, HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
        this.passwordHash = hashAlgorithm.hashPassword(password);
    }

    private PasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    private void setHashAlgorithm(HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }


    public boolean isPasswordHashEquals(String password) {
        return hashAlgorithm.isPasswordEquals(password,passwordHash);
    }

    public String getPasswordHash() {
        return passwordHash;
    }



    public static PasswordHash getInstance(String passwordHash) {
        PasswordHash pwh= new PasswordHash(passwordHash);
        pwh.setHashAlgorithm(HashAlgorithmFactory.getInstance());
        return pwh;
    }

    public static PasswordHash newInstance(String password) {
        return new PasswordHash(password, HashAlgorithmFactory.getInstance());
    }

}
