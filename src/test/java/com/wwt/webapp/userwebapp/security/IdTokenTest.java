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


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class IdTokenTest {



    @Test
    public void checkTokenEncryption()  {
        String issuer = "TestIssuer";
        String subject = "TestUser";
        IdToken t1 = IdToken.newInstance(issuer,subject,10000);
        IdToken t2 = IdToken.parse(t1.convertToSignedJwt());
        assertTrue(t1.getId().equals(t2.getId())
                        && 	t1.getIssuer().equals(t2.getIssuer())
                        &&  t1.getSubject().equals(t2.getSubject())
                        &&  t1.getExpiresAt().equals(t2.getExpiresAt())
                        &&  t1.getIssuedAt().equals(t2.getIssuedAt())
        );

    }

    @Test
    public void checkTokenValidity()  {
        String issuer = "TestIssuer";
        String subject = "TestUser";
        int secondsValid = 10;
        Instant compare1 = Instant.now().plusSeconds(secondsValid-1);
        Instant compare2 = Instant.now().plusSeconds(secondsValid*2);
        IdToken t1 = IdToken.newInstance(issuer,subject,secondsValid);
        assertTrue(t1.getExpiresAt().isAfter(compare1)
                && t1.getExpiresAt().isBefore(compare2)
        );
        assertTrue(t1.getExpiresAt().isAfter(compare1)
                && t1.getExpiresAt().isBefore(compare2)
        );

    }

    @Test(expected=ExpiredJwtException.class)
    public void expiryException()  {
        String issuer = "TestIssuer";
        String subject = "TestUser";
        IdToken t1 = IdToken.newInstance(issuer,subject,0);
        IdToken t2 = IdToken.parse(t1.convertToSignedJwt());
        System.out.println(t1);
        System.out.println(t2);

    }

    @Test(expected=SignatureException.class)
    public void signatureException()  {
        String issuer = "TestIssuer";
        String subject = "TestUser";
        KeyPair keyPair = null;
        try {
            keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyPair save = Keys.keyPair;
        Keys.keyPair = keyPair;
        IdToken t1 = IdToken.newInstance(issuer,subject,1000);
        String jwt = t1.convertToSignedJwt();
        Keys.keyPair = save;
        IdToken t2 = IdToken.parse(jwt);
        System.out.println(t1);
        System.out.println(t2);
    }


}
