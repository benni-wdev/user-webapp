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
