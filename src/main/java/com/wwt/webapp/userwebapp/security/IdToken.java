package com.wwt.webapp.userwebapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

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

    public String getId() {
        return id;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getSubject() {
        return subject;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public String getAdminRole() { return adminRole; }

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

    @Override
    public String toString() {
        return "IdToken{" +
                "id='" + id + '\'' +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", expiresAt=" + expiresAt +
                ", issuedAt=" + issuedAt +
                ", adminRole=" + adminRole +
                '}';
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
