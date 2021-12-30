package com.wwt.webapp.userwebapp.domain.relational;

import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query( "select u from UserEntity u where u.emailAddress = :emailAddress and u.activationStatus <> 'ARCHIVED'" )
    Optional<UserEntity> getOperationalUsersByEmailAddress(@Param("emailAddress") String emailAddress);

    @Query( "select u from UserEntity u where u.activationToken = :activationToken")
    Optional<UserEntity> getUserByActivationToken(@Param("activationToken") String activationToken);

    @Query( "select u from UserEntity u where u.loginId = :loginId")
    Optional<UserEntity> getUserByLoginId(@Param("loginId") String loginId);

    @Query( "select u from UserEntity u where u.refreshToken = :refreshToken")
    Optional<UserEntity> getUserByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query( "select u from UserEntity u where u.passwordRecoveryToken = :passwordRecoveryToken")
    Optional<UserEntity> getUserByPasswordRecoveryToken(@Param("passwordRecoveryToken") String passwordRecoveryToken);

}
