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
package com.wwt.webapp.userwebapp.domain;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author benw-at-wwt
 */
public interface ObjectWithUuid {

    String getUuid();


    static Set<String> getUuidsForObjectList(List<? extends ObjectWithUuid> objectList) {
        Set<String> result = new HashSet<>(objectList.size());
        for(ObjectWithUuid objectWithUuid:objectList) {
            result.add( objectWithUuid.getUuid() );
        }
        return result;
    }


}
