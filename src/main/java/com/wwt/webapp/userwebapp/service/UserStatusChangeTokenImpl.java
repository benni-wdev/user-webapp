package com.wwt.webapp.userwebapp.service;


import com.wwt.webapp.userwebapp.helper.ConfigProvider;
import com.wwt.webapp.userwebapp.helper.TimestampHelper;
import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;
import java.sql.Timestamp;

/**
 * @author benw-at-wwt
 */
public class UserStatusChangeTokenImpl implements UserStatusChangeToken {


    private final String token;
    private final Timestamp tokenExpiresAt;

    private UserStatusChangeTokenImpl(String token, Timestamp tokenExpiresAt) {
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Timestamp getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public static UserStatusChangeTokenImpl newInstance() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new UserStatusChangeTokenImpl(Hex.encodeHexString(bytes), TimestampHelper.getNowPlusMinsAsUtcTimestamp( ConfigProvider.getConfigIntValue("minutesUntilTokenExpiry")));
    }

    public static UserStatusChangeTokenImpl getInstance(String token, Timestamp tokenExpiresAt) {
        return new UserStatusChangeTokenImpl(token,tokenExpiresAt);
    }
}
