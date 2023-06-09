/**
 * (C) Copyright IBM Corp. 2021.
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


package com.ibm.cloud.eventnotifications.huawei.android;

/** This interface defines the callback methods used for reporting the status of ENPush's async
 * methods.
 */
public interface ENPushResponseListener<T> {

    /**
     * Will be called by ENPush when the request succeeds.
     * This method is called from inside a worker thread. Hence an UI updates
     * from within this method should be done by calling runOnUiThread() method.
     *
     * @param response - The success response.
     */
    public void onSuccess(T response);

    /**
     * Will be called by ENPush when the request fails.
     * This method is called from inside a worker thread. HEnce any UI updates
     * from within this method should be done by calling runOnUiThread() method.
     *
     * @param exception - Exception thrown on failure response.
     */
    public void onFailure(ENPushException exception);
}
