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
import android.net.Uri;

/**
 * ENResourceManagerInterface contains the methods required for accessing the app resources.
 */
interface ENResourceManagerInterface {
    int getResourceIdForCustomIcon(Context context, String resourceCategory, String resourceName);

    int getResourceId(Context context, String resourceCategory, String resourceName);

    int getCustomNotificationIcon(Context context, String resourceName);

    Uri getNotificationSoundUri(Context context, String sound);

    String getNotificationDefaultTitle(Context context);

    String getNotificationTitle(Context context, String title);
}
