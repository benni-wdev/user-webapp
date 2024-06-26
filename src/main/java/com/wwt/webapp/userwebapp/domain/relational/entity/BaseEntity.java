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
package com.wwt.webapp.userwebapp.domain.relational.entity;

import com.wwt.webapp.userwebapp.domain.ObjectWithUuid;
import com.wwt.webapp.userwebapp.helper.TimestampHelper;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author benw-at-wwt
 */
@SuppressWarnings({"unused", "FieldMayBeFinal", "CanBeFinal"})
@MappedSuperclass
@Getter
@ToString
public class BaseEntity implements ObjectWithUuid {

    @Id
    @Column(name = "UUID",length = 36,nullable = false)
    private String uuid;

    @Column(name = "CREATED_AT",nullable = false)
    private Timestamp createdAt;

    @Column(name= "VERSION",columnDefinition = "integer default 1")
    private int version;

    @Column(name = "LAST_MODIFIED_AT")
    private Timestamp lastModifiedAt;


    protected BaseEntity() {
        this.createdAt = TimestampHelper.getNowAsUtcTimestamp();
        this.uuid = UUID.randomUUID().toString();
        this.version = 1;
    }


    void updateBase() {
        version++;
        lastModifiedAt = TimestampHelper.getNowAsUtcTimestamp();
    }


    public static <E extends BaseEntity> List<String> keyList(List<E> list) {
        if(list == null) throw new IllegalArgumentException("list must not be null");
        List<String> returnList = new ArrayList<>();
        for(ObjectWithUuid e:list) {
            returnList.add(e.getUuid());
        }
        return returnList;
    }
}
