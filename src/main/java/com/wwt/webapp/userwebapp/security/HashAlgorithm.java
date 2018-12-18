package com.wwt.webapp.userwebapp.security;


/**
 *
 * @author benw-at-wwt
 */
@SuppressWarnings("SpellCheckingInspection")
public interface HashAlgorithm {


    /**
     * Returns the given password as hash string
     *
     * @param password unhashed password from input
     * @return hashed password for storing it
     */
    String hashPassword(String password);

    /**
     * Checks for a given password if the password is hashed equals to the given hash
     *
     * @param password unhashed password from outside
     * @param passwordHash the Password Hash  to compared against
     * @return true if the hash values are equal
     */
    boolean isPasswordEquals(String password, String passwordHash);
}
