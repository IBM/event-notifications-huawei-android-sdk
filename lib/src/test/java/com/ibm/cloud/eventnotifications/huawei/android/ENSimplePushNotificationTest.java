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

package com.ibm.cloud.eventnotifications.huawei.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class ENSimplePushNotificationTest {

    @Test
    public void testENSimplePushNotificationWithENInternalPushMessage() throws JSONException {
        String message = "{\"key\":\"key1\",\"priority\":\"MAX\",\"androidTitle\":\"Messagetitle\",\"payload\":\"{\\\"nid\\\":\\\"T9fNHhJY\\\",\\\"eventName\\\":\\\"pushTrigger\\\"}\",\"interactiveCategory\":\"Interactivecategory\",\"type\":\"default\",\"alert\":\"Testmessage\",\"sound\":\"Sound\",\"url\":\"test.message.ibm.com\",\"bridge\":true,\"visibility\":\"12\",\"redact\":\"redactmessage\",\"style\":\"big_picture\",\"icon\":\"icon_name\",\"lights\":\"red\",\"channel\":\"{\\\"sound\\\":\\\"soundname\\\",\\\"channelId\\\":\\\"channel2\\\",\\\"channelName\\\":\\\"channel2\\\",\\\"description\\\":\\\"description\\\",\\\"importance\\\":2,\\\"enableLights\\\":true,\\\"enableVibration\\\":true,\\\"lightColor\\\":\\\"RED\\\",\\\"lockScreenVisibility\\\":1,\\\"groupId\\\":\\\"channelgroup1\\\",\\\"bypassDND\\\":true,\\\"showBadge\\\":true,\\\"groupJson\\\":\\\"{\\\\\\\"name\\\\\\\":\\\\\\\"john\\\\\\\",\\\\\\\"age\\\\\\\":22,\\\\\\\"class\\\\\\\":\\\\\\\"mca\\\\\\\"}\\\"}\"}";
        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage enInternalPushMessage = new ENInternalPushMessage(jsonMessage);
        assertNotNull(enInternalPushMessage);

        ENSimplePushNotification sut = new ENSimplePushNotification(enInternalPushMessage);
        assertNotNull(sut);

        assertEquals(sut.getUrl(), "test.message.ibm.com");
        assertNotNull(sut.getPayload());

        sut.setActionName("New_Action");
        assertEquals(sut.getActionName(), "New_Action");

    }

    @Test
    public void testENSimplePushNotification() throws JSONException {

        ENSimplePushNotification sut = new ENSimplePushNotification("alert message", "Id_1", "test.message.ibm.com", "payload");
        assertNotNull(sut);

        assertEquals(sut.getUrl(), "test.message.ibm.com");
        assertNotNull(sut.getPayload());

        sut.setActionName("New_Action");
        assertEquals(sut.getActionName(), "New_Action");

    }
}
