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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

public class ENInternalPushMessageTest {

    private String message = "{\"key\":\"key1\",\"priority\":\"MAX\",\"androidTitle\":\"Messagetitle\",\"payload\":\"{\\\"nid\\\":\\\"T9fNHhJY\\\",\\\"eventName\\\":\\\"pushTrigger\\\"}\",\"interactiveCategory\":\"Interactivecategory\",\"type\":\"default\",\"alert\":\"Testmessage\",\"sound\":\"Sound\",\"url\":\"test.message.ibm.com\",\"bridge\":true,\"visibility\":\"12\",\"redact\":\"redactmessage\",\"style\":\"big_picture\",\"icon\":\"icon_name\",\"lights\":\"red\",\"channel\":\"{\\\"sound\\\":\\\"soundname\\\",\\\"channelId\\\":\\\"channel2\\\",\\\"channelName\\\":\\\"channel2\\\",\\\"description\\\":\\\"description\\\",\\\"importance\\\":2,\\\"enableLights\\\":true,\\\"enableVibration\\\":true,\\\"lightColor\\\":\\\"RED\\\",\\\"lockScreenVisibility\\\":1,\\\"groupId\\\":\\\"channelgroup1\\\",\\\"bypassDND\\\":true,\\\"showBadge\\\":true,\\\"groupJson\\\":\\\"{\\\\\\\"name\\\\\\\":\\\\\\\"john\\\\\\\",\\\\\\\"age\\\\\\\":22,\\\\\\\"class\\\\\\\":\\\\\\\"mca\\\\\\\"}\\\"}\"}";

    @Test
    public void testENMessageJSON() throws JSONException {
        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage sut = new ENInternalPushMessage(jsonMessage);
        JSONObject converted = sut.toJson();
        assertEquals(converted.getString("alert"), "Testmessage");
        assertEquals(converted.getString("androidTitle"), "Messagetitle");
    }

    @Test
    public void testENMessage() throws JSONException {
        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage sut = new ENInternalPushMessage(jsonMessage);
        assertEquals(sut.getAlert(), "Testmessage");
        sut.setAlert("Test message");
        assertEquals(sut.getAlert(), "Test message");

        assertEquals(sut.getAndroidTitle(), "Messagetitle");
        sut.setAndroidTitle("Message title");
        assertEquals(sut.getAndroidTitle(), "Message title");

        Context mockContext = Mockito.mock(Context.class);
        NotificationManager mNotificationManager = Mockito.mock(NotificationManager.class);;

        assertNotNull(sut.getChannelJson());
        NotificationChannel channelObject = sut.getChannel(mockContext, mNotificationManager);
        assertNotNull(channelObject);

        String channelString = "{\"sound\":\"sound name\",\"channelId\":\"channel3\",\"channelName\":\"channel 3\",\"description\":\"description\",\"importance\":2,\"enableLights\":true,\"enableVibration\":true,\"lightColor\":\"RED\",\"lockScreenVisibility\":1,\"groupId\":\"channelgroup1\",\"bypassDND\":true,\"showBadge\":true,\"groupJson\":\"{\\\"name\\\":\\\"john\\\",\\\"age\\\":22,\\\"class\\\":\\\"mca\\\"}\"}";
        sut.setChannelJson(new JSONObject(channelString));
        assertNotNull(sut.getChannelJson());

        assertNotNull(sut.getPayload());
        assertEquals(sut.getUrl(), "test.message.ibm.com");

        assertEquals(sut.getPriority(), "MAX");
        sut.setPriority("MIN");
        assertEquals(sut.getPriority(), "MIN");

        assertEquals(sut.getVisibility(), "12");
        sut.setVisibility("2");
        assertEquals(sut.getVisibility(), "2");

        assertEquals(sut.getRedact(), "redactmessage");
        sut.setRedact("new redact");
        assertEquals(sut.getRedact(), "new redact");

        assertEquals(sut.getCategory(), "Interactivecategory");
        sut.setCategory("new category");
        assertEquals(sut.getCategory(), "new category");

        assertEquals(sut.getKey(), "key1");
        sut.setKey("new key");
        assertEquals(sut.getKey(), "new key");

        assertEquals(sut.getGcmStyle(), "big_picture");
        sut.setGcmStyle("new style");
        assertEquals(sut.getGcmStyle(), "new style");

        assertEquals(sut.getLights(), "red");
        sut.setLights("new lights");
        assertEquals(sut.getLights(), "new lights");

        assertEquals(sut.getMessageType(), "default");
        sut.setMessageType("new type");
        assertEquals(sut.getMessageType(), "new type");

        assertNotNull(sut.toString());

        assertEquals(sut.getId(), "T9fNHhJY");
        sut.setId("new id");
        assertEquals(sut.getId(), "new id");

        assertEquals(sut.getSound(), "Sound");
        sut.setSound("new sound");
        assertEquals(sut.getSound(), "new sound");

        assertEquals(sut.getBridge(),true);
        assertEquals(sut.getPriority(),"MIN");
        assertEquals(sut.getVisibility(),"2");
        assertEquals(sut.getNotificationId(),0);
        sut.setNotificationId(784);
        assertEquals(sut.getNotificationId(),784);

        assertEquals(sut.getIcon(),"icon_name");
        sut.setIcon("new icon_name");
        assertEquals(sut.getIcon(),"new icon_name");
    }

    @Test
    public void testENInternalPushMessageIntent() throws JSONException {

        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage sut = new ENInternalPushMessage(jsonMessage);
        assertNotNull(sut);
        Intent intent = Mockito.mock(Intent.class);
        Bundle bundle = Mockito.mock(Bundle.class);
        intent.putExtra("message", sut);
        when(intent.getExtras()).thenReturn(bundle);
        try {
            ENInternalPushMessage sutFromIntent = new ENInternalPushMessage(intent);
        } catch (Exception e) {

        }

    }

    @Test
    public void testENInternalPushMessageWriteToParcel() throws JSONException {

        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage sut = new ENInternalPushMessage(jsonMessage);
        assertNotNull(sut);

        Parcel parcel = Mockito.mock(Parcel.class);
        sut.writeToParcel(parcel, 1);
        assertNotNull(parcel);

        ENInternalPushMessage sutSecond = ENInternalPushMessage.CREATOR.createFromParcel(parcel);
        assertNotNull(sutSecond);

    }
}
