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

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushConstants;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushUtils;

/**
 * Class to Resource access in SDK. This class implements the ENMessageManagerInterface interface.
 */
class ENMessageManagerDefault implements ENMessageManagerInterface {

    /**
     * Method to save data in SharedPreferences.
     * @param context current context.
     * @param message data to be stored.
     */
    @Override
    public void saveInSharedPreferences(Context context, ENInternalPushMessage message) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ENPush.PREFS_NAME, Context.MODE_PRIVATE);
        String msgString = message.toJson().toString();
        //PREFS_NOTIFICATION_COUNT value provides the count of number of undelivered notifications stored in the sharedpreferences
        int count = sharedPreferences.getInt(ENPush.PREFS_NOTIFICATION_COUNT, 0);
        //Increment the count and use it for the next notification
        count++;
        ENPushUtils.storeContentInSharedPreferences(sharedPreferences, ENPush.PREFS_NOTIFICATION_MSG + count, msgString);

        ENPushUtils.storeContentInSharedPreferences(sharedPreferences, ENPush.PREFS_NOTIFICATION_COUNT, count);
    }

    /**
     * Method to get the notifications sound from options.
     * @param context current context.
     * @param message Message object.
     * @return returns the sound value
     */
    @Override
    public String getNotificationSound(Context context, ENInternalPushMessage message) {
        String soundFromServer = message.getSound();
        String soundPreSet = null;
        ENPushNotificationOptions options = ENPush.getInstance().getNotificationOptions(context.getApplicationContext());

        if (options != null && options.getSound() != null) {
            soundPreSet = options.getSound();
        }
        if (soundFromServer != null) {
            return soundFromServer;
        } else if (soundPreSet != null) {
            return soundPreSet;
        }
        return null;
    }

    /**
     * Method to get the priority of the message.
     * @param context current context.
     * @param message Message object.
     * @return returns the priority value
     */
    @Override
    public int getPriorityOfMessage(Context context, ENInternalPushMessage message) {
        String priorityFromServer = message.getPriority();
        ENPushNotificationOptions options = ENPush.getInstance().getNotificationOptions(context.getApplicationContext());
        int priorityPreSetValue = 0;

        if (options != null && options.getPriority() != null) {
            priorityPreSetValue = options.getPriority().getValue();
        }

        if (priorityFromServer != null) {
            if (priorityFromServer.equalsIgnoreCase(ENPushConstants.PRIORITY_MAX)) {
                return Notification.PRIORITY_MAX;
            } else if (priorityFromServer.equalsIgnoreCase(ENPushConstants.PRIORITY_MIN)) {
                return Notification.PRIORITY_MIN;
            } else if (priorityFromServer.equalsIgnoreCase(ENPushConstants.PRIORITY_HIGH)) {
                return Notification.PRIORITY_HIGH;
            } else if (priorityFromServer.equalsIgnoreCase(ENPushConstants.PRIORITY_LOW)) {
                return Notification.PRIORITY_LOW;
            }
        } else if (priorityPreSetValue != 0) {
            return priorityPreSetValue;
        }
        return Notification.PRIORITY_DEFAULT;
    }
}
