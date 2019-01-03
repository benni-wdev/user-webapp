/* Copyright 2018-2019 Wehe Web Technologies
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
