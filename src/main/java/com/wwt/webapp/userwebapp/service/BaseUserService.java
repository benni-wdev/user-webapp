package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.relational.UserRepository;
import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author benw-at-wwt
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
abstract class BaseUserService  {

    private final static Logger logger = LoggerFactory.getLogger(BaseUserService.class);

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    boolean isEmailUnique(String emailAddress, UserRepository userRepository) {
        Optional<UserEntity> userOpt = userRepository.getOperationalUsersByEmailAddress(emailAddress);
        return !userOpt.isPresent();
    }

    boolean isValidLoginId(String loginId) {
        if(loginId.length()<4) return false;
        return loginId.matches("[a-zA-Z0-9]+([_-]?[a-zA-Z0-9])*");
    }

    boolean isValidEmailAddress(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailAddress);
        return matcher.find();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected boolean isUserValid(Optional<UserEntity> userOpt) {
        if (!userOpt.isPresent()) {
            logger.warn("checkUserConstraints: Not exactly one user found");
            return false;
        }
        UserEntity userEntity = userOpt.get();
        if(!userEntity.getActivationStatus().equals( ActivationStatus.ACTIVE)) {
            logger.warn("checkUserConstraints: User not active");
            return false;
        }
        return true;
    }

}
