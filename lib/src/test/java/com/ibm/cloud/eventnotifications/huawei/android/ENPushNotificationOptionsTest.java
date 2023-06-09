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

import static org.junit.Assert.assertNotNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ENPushNotificationOptionsTest {

    @Test
    public void testENPushNotificationOptions() throws JSONException {

        ENPushNotificationOptions options = new ENPushNotificationOptions();
        ENPushNotificationButton acceptButton = new ENPushNotificationButton.Builder("Accept Button")
                .setIcon("check_circle_icon")
                .setLabel("Accept")
                .build();
        ENPushNotificationButton declineButton = new ENPushNotificationButton.Builder("Decline Button")
                .setIcon("extension_circle_icon")
                .setLabel("Decline")
                .build();
        ENPushNotificationButton viewButton = new ENPushNotificationButton.Builder("View Button")
                .setIcon("extension_circle_icon")
                .setLabel("view")
                .build();
        List<ENPushNotificationButton> buttonGroup_1 =  new ArrayList<ENPushNotificationButton>();
        buttonGroup_1.add(acceptButton);
        buttonGroup_1.add(declineButton);
        buttonGroup_1.add(viewButton);
        List<ENPushNotificationButton> buttonGroup_2 =  new ArrayList<ENPushNotificationButton>();
        buttonGroup_2.add(acceptButton);
        buttonGroup_2.add(declineButton);
        List<ENPushNotificationButton> buttonGroup_3 =  new ArrayList<ENPushNotificationButton>();
        buttonGroup_3.add(acceptButton);
        ENPushNotificationCategory category = new ENPushNotificationCategory.Builder("First_Button_Group1").setButtons(buttonGroup_1).build();
        ENPushNotificationCategory category1 = new ENPushNotificationCategory.Builder("First_Button_Group2").setButtons(buttonGroup_2).build();
        ENPushNotificationCategory category2 = new ENPushNotificationCategory.Builder("First_Button_Group3").setButtons(buttonGroup_3).build();
        List<ENPushNotificationCategory> categoryList =  new ArrayList<ENPushNotificationCategory>();
        categoryList.add(category);
        categoryList.add(category1);
        categoryList.add(category2);
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

        assertNotNull(options);
        assertNotNull(options.getInteractiveNotificationCategories());
        assertNotNull(options.getIcon());
        assertNotNull(options.getPriority());
        assertNotNull(options.getRedact());
        assertNotNull(options.getSound());
        assertNotNull(options.getTemplateValues());
        assertNotNull(options.getVisibility());
    }
}
