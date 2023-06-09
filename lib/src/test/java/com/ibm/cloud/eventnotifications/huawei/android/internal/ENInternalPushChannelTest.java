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

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

public class ENInternalPushChannelTest {


    private String channelString = "{\"sound\":\"sound name\",\"channelId\":\"channel2\",\"channelName\":\"channel 2\",\"description\":\"description\",\"importance\":2,\"enableLights\":true,\"enableVibration\":true,\"lightColor\":\"RED\",\"lockScreenVisibility\":1,\"groupId\":\"channelgroup1\",\"bypassDND\":true,\"showBadge\":true,\"groupJson\":\"{\\\"name\\\":\\\"john\\\",\\\"age\\\":22,\\\"class\\\":\\\"mca\\\"}\"}";
    @Test
    public void testChannel() throws JSONException {
        JSONObject jsonMessage = new JSONObject(channelString);
        ENInternalPushChannel channel = new ENInternalPushChannel(jsonMessage);
        assertNotNull(channel);
        assertNotNull(channel.toString());
        assertEquals(channel.getChannelId(), "channel2");
        assertEquals(channel.getChannelName(), "channel 2");
        assertEquals(channel.getImportance(), 2);
        assertEquals(channel.getEnableLights(), true);
        assertEquals(channel.getEnableVibration(), true);
        assertEquals(channel.getLightColor(), "RED");
        assertEquals(channel.getLockScreenVisibility(), 1);
        assertNotNull(channel.getGroupJson());
        assertEquals(channel.getBypassDND(), true);
        assertEquals(channel.getDescription(), "description");
        assertEquals(channel.getShowBadge(), true);
        assertEquals(channel.getSound(), "sound name");
        assertNotNull(channel.getVibrationPattern());

        channel.setChannelId("Channel_id_1");
        channel.setChannelName("Channel_name_2");
        channel.setImportance(3);
        channel.setEnableLights(false);
        channel.setEnableVibration(false);
        channel.setLightColor("Green");
        channel.setLockScreenVisibility(3);
        channel.setGroupsJSON(new JSONObject("{\"groupId\": \"QweRrcER\", \"groupName\": \"Test Group 2\"}"));
        channel.setBypassDND(false);
        channel.setShowBadge(false);
        channel.setDescription("new test description");
        channel.setSound("test_sound");
        channel.setVibrationPattern(new JSONArray("[100,100]"));

        assertEquals(channel.getChannelId(), "Channel_id_1");

        Context mockContext = Mockito.mock(Context.class);
        NotificationChannel notificationChannel = Mockito.mock(NotificationChannel.class);

        NotificationManager mNotificationManager = Mockito.mock(NotificationManager.class);;
        NotificationChannel channelObject = channel.getChannel(mockContext, mNotificationManager);

        assertNotNull(channelObject);


    }
}
