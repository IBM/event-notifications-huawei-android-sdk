/**
 * Copyright 2021 IBM Corp. All Rights Reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageConstantInterface;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageKeys;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

public class ENNotificationBuilderDefaultTest {

    private String message = "{\"key\":\"key1\",\"priority\":\"MAX\",\"androidTitle\":\"Messagetitle\",\"payload\":\"{\\\"nid\\\":\\\"T9fNHhJY\\\",\\\"eventName\\\":\\\"pushTrigger\\\"}\",\"interactiveCategory\":\"Interactivecategory\",\"type\":\"default\",\"alert\":\"Testmessage\",\"sound\":\"Sound\",\"url\":\"test.message.ibm.com\",\"bridge\":true,\"visibility\":\"12\",\"redact\":\"redactmessage\",\"style\":\"big_picture\",\"icon\":\"icon_name\",\"lights\":\"{\\\"ledARGB\\\":\\\"transparent\\\",\\\"ledOnMS\\\":\\\"100\\\",\\\"ledOffMS\\\":\\\"100\\\"}\"}";


    class ENMessageManagerTest implements ENMessageManagerInterface {

        @Override
        public void saveInSharedPreferences(Context context, ENInternalPushMessage message) {

        }

        @Override
        public String getNotificationSound(Context context, ENInternalPushMessage message) {
            return "test";
        }

        @Override
        public int getPriorityOfMessage(Context context, ENInternalPushMessage message) {
            return 1;
        }
    }

    class ENMessageConstantTest implements ENMessageConstantInterface {

        @Override
        public String getStringForKey(ENMessageKeys key) {
            return "test_constant";
        }
    }

    class ENResourceManagerTest implements ENResourceManagerInterface {

        @Override
        public int getResourceIdForCustomIcon(Context context, String resourceCategory, String resourceName) {
            return 1;
        }

        @Override
        public int getResourceId(Context context, String resourceCategory, String resourceName) {
            return 1;
        }

        @Override
        public int getCustomNotificationIcon(Context context, String resourceName) {
            return 1;
        }

        @Override
        public Uri getNotificationSoundUri(Context context, String sound) {
            return null;
        }

        @Override
        public String getNotificationDefaultTitle(Context context) {
            return "default title test";
        }

        @Override
        public String getNotificationTitle(Context context, String title) {
            return "title test";
        }
    }

    class ENMessageNotifierTest implements ENMessageNotifierInterface {

        @Override
        public void notify(NotificationManager notificationManager, Notification notification, int notificationId) {

        }

        @Override
        public void cancel(NotificationManager notificationManager, int notificationId) {

        }
    }

    @Test
    public void testBuildNotificationCompatBuilder() throws JSONException {

        ENMessageManagerInterface enMessageManager = new ENMessageManagerTest();
        ENMessageConstantInterface enMessageConstants = new ENMessageConstantTest();
        ENResourceManagerInterface enResourceManager = new ENResourceManagerTest();
        ENMessageNotifierInterface enMessageNotifier = new ENMessageNotifierTest();

        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage message = new ENInternalPushMessage(jsonMessage);
        Context mockContext = Mockito.mock(Context.class);
        NotificationChannel notificationChannel = Mockito.mock(NotificationChannel.class);

        when(mockContext.getPackageName()).thenReturn("package");
        ENNotificationBuilderDefault sut = new ENNotificationBuilderDefault();
        sut.enMessageManager = enMessageManager;
        sut.enMessageConstants = enMessageConstants;
        sut.enResourceManager = enResourceManager;
        sut.enMessageNotifier = enMessageNotifier;

        NotificationCompat.Builder builder = sut.buildNotificationCompatBuilder(mockContext, message);
        assertNotNull(builder);


        Notification notification =Mockito.mock(Notification.class);
        notification = sut.setLights(notification, message);

        assertEquals(notification.ledARGB, Color.TRANSPARENT);
        assertEquals(notification.ledOffMS, 100);
        assertEquals(notification.ledOnMS, 100);


    }


}
