/**
 * Copyright 2021 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.cloud.eventnotifications.huawei.android.internal;

import com.ibm.cloud.sdk.core.http.ServiceCall;
import com.ibm.cloud.sdk.core.http.ServiceCallback;

import org.json.JSONObject;

/**
 * Interface for handling API service.
 */
interface NetworkInterface {
    ServiceCall getData(String url, ServiceCallback callback);
    ServiceCall delete(String url, ServiceCallback callback);
    ServiceCall postData(String url, JSONObject data, ServiceCallback callback);
    ServiceCall putData(String url, JSONObject data, ServiceCallback callback);
}
