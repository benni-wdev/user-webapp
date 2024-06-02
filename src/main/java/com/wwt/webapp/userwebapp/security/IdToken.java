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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author benw-at-wwt
 */
@Getter
@ToString
public class IdToken {

    private static final String adminRoleClaimType = "adminRole";


    private final String  id;
    private final String  issuer;
    private final String  subject;
    private final Instant expiresAt;
    private final Instant issuedAt;
    private final String  adminRole;

    private IdToken(String issuer, String subject, int secondsValid, String adminRole) {
        this.id = UUID.randomUUID().toString();
        this.issuer = issuer;
        this.subject = subject;
        this.issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        this.expiresAt = issuedAt.plusSeconds(secondsValid).truncatedTo(ChronoUnit.SECONDS);
        this.adminRole = (adminRole == null?"":adminRole);
    }


    private IdToken(String id, String issuer, String subject,long issuedAt, long expiresAt, String adminRole) {
        this.id = id;
        this.issuer = issuer;
        this.subject = subject;
        this.issuedAt = Instant.ofEpochMilli(issuedAt).truncatedTo(ChronoUnit.SECONDS);
        this.expiresAt = Instant.ofEpochMilli(expiresAt).truncatedTo(ChronoUnit.SECONDS);
        this.adminRole = (adminRole == null?"":adminRole);
    }


    public String convertToSignedJwt() {
        Map<String,Object> claims = new HashMap<>();
        if(!adminRole.isEmpty())
            claims.put(adminRoleClaimType,adminRole);
        return  Jwts.builder()
                .setClaims(claims)
                .setId(getId())
                .setIssuedAt(Date.from(getIssuedAt()))
                .setSubject(getSubject())
                .setExpiration(Date.from(getExpiresAt()))
                .setIssuer(getIssuer())
                .signWith( Keys.getKeyPair().getPrivate())
                .compact();
    }


    public static IdToken newInstance(String issuer,String subject, int secondsValid, String adminRole) {
        return (new IdToken(issuer,subject,secondsValid,adminRole));
    }

    public static IdToken parse(String bearer) {
        Claims claims = Jwts.parser()
                        .setSigningKey( Keys.getKeyPair().getPublic())
                        .parseClaimsJws(bearer).getBody();
        return (new IdToken(claims.getId(),
                            claims.getIssuer(),
                            claims.getSubject(),
                            claims.getIssuedAt().getTime(),
                            claims.getExpiration().getTime(),
                            ((String)claims.get(adminRoleClaimType))
                            )
        );
    }
}
