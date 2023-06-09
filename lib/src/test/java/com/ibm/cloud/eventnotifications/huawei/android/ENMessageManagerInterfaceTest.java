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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ENMessageManagerInterfaceTest {

    private String message = "{\"key\":\"key1\",\"priority\":\"MAX\",\"androidTitle\":\"Messagetitle\",\"payload\":\"{\\\"nid\\\":\\\"T9fNHhJY\\\",\\\"eventName\\\":\\\"pushTrigger\\\"}\",\"interactiveCategory\":\"Interactivecategory\",\"type\":\"default\",\"alert\":\"Testmessage\",\"sound\":\"Sound\",\"url\":\"test.message.ibm.com\",\"bridge\":true,\"visibility\":\"12\",\"redact\":\"redactmessage\",\"style\":\"big_picture\",\"icon\":\"icon_name\",\"lights\":\"red\",\"channel\":\"{\\\"sound\\\":\\\"soundname\\\",\\\"channelId\\\":\\\"channel2\\\",\\\"channelName\\\":\\\"channel2\\\",\\\"description\\\":\\\"description\\\",\\\"importance\\\":2,\\\"enableLights\\\":true,\\\"enableVibration\\\":true,\\\"lightColor\\\":\\\"RED\\\",\\\"lockScreenVisibility\\\":1,\\\"groupId\\\":\\\"channelgroup1\\\",\\\"bypassDND\\\":true,\\\"showBadge\\\":true,\\\"groupJson\\\":\\\"{\\\\\\\"name\\\\\\\":\\\\\\\"john\\\\\\\",\\\\\\\"age\\\\\\\":22,\\\\\\\"class\\\\\\\":\\\\\\\"mca\\\\\\\"}\\\"}\"}";

    @Test
    public void testENMessageManagerInterface() throws JSONException {

        ENMessageManagerDefault sut = new ENMessageManagerDefault();
        Context mockContext = Mockito.mock(Context.class);

        ENPushNotificationOptions options = new ENPushNotificationOptions();
        ENPushNotificationButton acceptButton = new ENPushNotificationButton.Builder("Accept Button")
                .setIcon("check_circle_icon")
                .setLabel("Accept")
                .build();
        List<ENPushNotificationButton> buttonGroup_1 =  new ArrayList<ENPushNotificationButton>();
        buttonGroup_1.add(acceptButton);
        ENPushNotificationCategory category = new ENPushNotificationCategory.Builder("First_Button_Group1").setButtons(buttonGroup_1).build();
        List<ENPushNotificationCategory> categoryList =  new ArrayList<ENPushNotificationCategory>();
        categoryList.add(category);
        options.setIcon("icon.png");
        options.setDeviceid("new_device_id");
        options.setVisibility(ENPushNotificationOptions.Visibility.PUBLIC);
        options.setPriority(ENPushNotificationOptions.Priority.HIGH);
        JSONObject variables = new JSONObject();
        variables.put("name", "test");
        options.setPushVariables(variables);
        options.setRedact("hello");
        options.setSound("hello.mp3");
        options.setInteractiveNotificationCategories(categoryList);

        Gson gson = new Gson();
        String json = gson.toJson(options);

        ENPushUtils mockUtils = Mockito.mock(ENPushUtils.class);
        final SharedPreferences sharedPrefs = Mockito.mock(SharedPreferences.class);

        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
        when(ENPushUtils.getContentFromSharedPreferences(mockContext,"MessageOptions")).thenReturn(json);

        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage message = new ENInternalPushMessage(jsonMessage);

        assertEquals(sut.getNotificationSound(mockContext, message), "Sound");
        assertEquals(sut.getPriorityOfMessage(mockContext, message), 2);

    }

}
