/* Copyright 2018-2021 Wehe Web Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
