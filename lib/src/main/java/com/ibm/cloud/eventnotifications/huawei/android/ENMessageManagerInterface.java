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

import android.content.Context;

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;

/**
 * interface to Resource access in SDK.
 */
interface ENMessageManagerInterface {

    /**
     * Method to save data in SharedPreferences.
     * @param context current context.
     * @param message data to be stored.
     */
    void saveInSharedPreferences(Context context, ENInternalPushMessage message);

    /**
     * Method to get the notifications sound from options.
     * @param context current context.
     * @param message Message object.
     * @return returns the sound value
     */
    String getNotificationSound(Context context, ENInternalPushMessage message);

    /**
     * Method to get the priority of the message.
     * @param context current context.
     * @param message Message object.
     * @return returns the priority value
     */
    int getPriorityOfMessage(Context context, ENInternalPushMessage message);
}
