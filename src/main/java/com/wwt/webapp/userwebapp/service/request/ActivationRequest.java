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
package com.wwt.webapp.userwebapp.service.request;


@SuppressWarnings("unused")
public class ActivationRequest implements InternalRequest {
	
	private  String activationToken;


    public ActivationRequest() {
    }

    public ActivationRequest(String activationToken) {
        this.activationToken = activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public String getActivationToken() {
        return activationToken;
    }

    @Override
    public String toString() {
        return "ActivationRequest{" +
                "activationToken='" + activationToken + '\'' +
                '}';
    }
}
