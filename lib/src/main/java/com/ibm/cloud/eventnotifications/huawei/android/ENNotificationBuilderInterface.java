/**
 * (C) Copyright IBM Corp. 2021.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ibm.cloud.eventnotifications.huawei.android;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.core.app.NotificationCompat;

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;

import org.json.JSONObject;

/**
 * Interface for handling the Notification object builder from the remote payload.
 */
interface ENNotificationBuilderInterface {

    void onUnhandled(Context context, ENInternalPushMessage notification, int notificationId);
    NotificationCompat.Builder buildNotificationCompatBuilder(Context context, ENInternalPushMessage message);
    void generateNotification(Context context, String alert,
                              String title, int icon, Intent intent, String sound, int notificationId, ENInternalPushMessage message);

    void dismissNotification(Context context, String nid);
    Notification setLights(Notification notification, ENInternalPushMessage message);
    Bitmap getBitMapBigPictureNotification(JSONObject gcmStyleObject);
    void setNotificationActions(Context context, Intent intent, int notificationId, String messageCategory, NotificationCompat.Builder mBuilder);
}