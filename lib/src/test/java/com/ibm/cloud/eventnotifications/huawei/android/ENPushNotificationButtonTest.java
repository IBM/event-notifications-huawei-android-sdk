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

public class ENPushNotificationButtonTest {

    @Test
    public void testButtons() {
        ENPushNotificationButton acceptButton = new ENPushNotificationButton.Builder("Accept Button")
                .setIcon("check_circle_icon")
                .setLabel("Accept")
                .build();
        ENPushNotificationButton declineButton = new ENPushNotificationButton.Builder("Decline Button")
                .setIcon("extension_circle_icon")
                .setLabel("Decline")
                .setPerformsInForeground(true)
                .build();

        assertNotNull(acceptButton);
        assertNotNull(declineButton);

        assertEquals(acceptButton.getButtonName(), "Accept Button");
        assertEquals(acceptButton.getLabel(), "Accept");
        assertEquals(acceptButton.getIcon(), "check_circle_icon");

        assertEquals(declineButton.getButtonName(), "Decline Button");
        assertEquals(declineButton.getLabel(), "Decline");
        assertEquals(declineButton.getIcon(), "extension_circle_icon");

    }
}
