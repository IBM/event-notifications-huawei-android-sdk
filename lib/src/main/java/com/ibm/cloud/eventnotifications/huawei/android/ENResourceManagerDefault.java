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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushConstants;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushUtils;
import com.ibm.cloud.eventnotifications.huawei.android.internal.Logger;

/**
 * ENResourceManagerDefault implements the ENResourceManagerInterface interface and contains the methods required for accessing the app resources.
 */
public class ENResourceManagerDefault implements ENResourceManagerInterface {

    private static Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENResourceManagerDefault.class.getSimpleName());


    /**
     * Get resource id for the custom icon
     * @param context app context
     * @param resourceCategory resource category value
     * @param resourceName resource name
     * @return return icon resource id
     */
    public int getResourceIdForCustomIcon(Context context, String resourceCategory, String resourceName) {
        int resourceId = -1;

        try {
            resourceId = context.getResources().getIdentifier(resourceName, ENPushConstants.DRAWABLE, context.getPackageName());
        } catch (Exception e) {
            logger.error("ENResourceManagerDefault: Failed to find resource R." + resourceCategory + "." + resourceName, e);
        }
        return resourceId;
    }

    /**
     * Get the resource id for an object
     * @param context app context
     * @param resourceCategory resource category value
     * @param resourceName resource name
     * @return return icon resource id
     */
    public int getResourceId(Context context, String resourceCategory, String resourceName) {
        int resourceId = -1;
        try {
            resourceId = context.getResources().getIdentifier(resourceName, ENPushConstants.RAW, context.getPackageName());
        } catch (Exception e) {
            logger.error("ENResourceManagerDefault: getResourceId() - Failed to find resource R." + resourceCategory + "." + resourceName, e);
        }
        return resourceId;
    }

    /**
     * Get custom notification icon value
     * @param context app context
     * @param resourceName resource name
     * @return return icon resource id
     */
    public int getCustomNotificationIcon(Context context, String resourceName) {
        int resourceId = -1;

        try {
            if (resourceName == null) {
                resourceName = "push_notification_icon";
            }
            resourceId = this.getResourceIdForCustomIcon(context, ENPushConstants.DRAWABLE, resourceName);
        } catch (Exception e) {
            logger.error("ENPushIntentService: getCustomNotification() - Exception while parsing icon file.");
            resourceId = android.R.drawable.btn_star;
        }

        if (resourceId == 0) {
            resourceId = android.R.drawable.btn_star;
        }
        return resourceId;
    }
    /**
     * Get notification sound value
     * @param context app context
     * @param sound sound name
     * @return return Uri of sound resource
     */
    public Uri getNotificationSoundUri(Context context, String sound) {
        Uri uri = null;

        if (sound == null) {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else if (!(sound.trim().isEmpty())) {
            String soundResourceString = sound;
            try {
                if (soundResourceString.contains(".")) {
                    soundResourceString = soundResourceString.substring(0, soundResourceString.indexOf("."));
                }
                int resourceId = getResourceId(context, ENPushConstants.RAW, soundResourceString);
                if (resourceId == -1) {
                    logger.error("ENPushIntentService:getNotificationSoundUri() - Specified sound file is not found in res/raw");
                }
                uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId);
            } catch (Exception e) {
                logger.error("ENPushIntentService:getNotificationSoundUri() - Exception while parsing sound file");
            }
        }

        return uri;
    }

    /**
     * Get the application default title
     * @param context app context
     * @return return app title
     */
    public String getNotificationDefaultTitle(Context context) {
        int notificationTitle = -1;
        try {
            notificationTitle = ENPushUtils.getResourceId(context.getApplicationContext(),
                    "string", ENPushConstants.DEFAULT_CHANNEL_ID);
            return context.getString(notificationTitle);
        } catch (Exception e) {
            // ignore the exception
            logger.error("ENPushIntentService:getNotificationSoundUri() - Exception while parsing sound file");
        }
        return ENPushConstants.DEFAULT_CHANNEL_ID;
    }

    /**
     * Get notification title
     * @param context app context
     * @param title default title value
     * @return return title
     */
    public String getNotificationTitle(Context context, String title) {
        // Check if push_notification_title is defined, if not get the
        // application name

        if (title != null && !title.equals("") && !title.isEmpty()) {
            return title;
        }
        int notificationTitle = -1;
        try {
            notificationTitle = ENPushUtils.getResourceId(context.getApplicationContext(),
                    "string", "push_notification_title");
            return context.getString(notificationTitle);
        } catch (Exception e) {
            // ignore the exception
        }

        if (notificationTitle == -1) {
            ApplicationInfo appInfo = null;
            PackageManager packManager = context.getPackageManager();
            try {
                appInfo = packManager.getApplicationInfo(
                        context.getPackageName(), 0);
            } catch (Exception e) {
                logger.warn("ENPushIntentService:getNotificationTitle() - Notification will not have a title because application name is not available.");
            }

            if (appInfo != null) {
                return (String) packManager.getApplicationLabel(appInfo);
            }
        }

        return "";
    }
}
