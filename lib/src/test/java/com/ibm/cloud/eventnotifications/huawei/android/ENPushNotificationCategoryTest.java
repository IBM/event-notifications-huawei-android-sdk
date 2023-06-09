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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ENPushNotificationCategoryTest {

    @Test
    public void testCategory() {

        ENPushNotificationButton acceptButton = new ENPushNotificationButton.Builder("Accept Button")
                .setIcon("check_circle_icon")
                .setLabel("Accept")
                .build();
        List<ENPushNotificationButton> buttonGroup_1 =  new ArrayList<ENPushNotificationButton>();
        buttonGroup_1.add(acceptButton);
        ENPushNotificationCategory category = new ENPushNotificationCategory.Builder("First_Button_Group1").setButtons(buttonGroup_1).build();

        assertNotNull(acceptButton);
        assertNotNull(category);

        assertEquals("First_Button_Group1", category.getCategoryName());
        assertEquals(1, category.getButtons().size());
        assertEquals("Accept Button", category.getButtons().get(0).getButtonName());

    }
}
