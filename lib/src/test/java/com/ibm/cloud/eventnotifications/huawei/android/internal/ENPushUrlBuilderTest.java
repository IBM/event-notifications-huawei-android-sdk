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

import com.ibm.cloud.eventnotifications.huawei.android.ENPush;

import org.junit.Test;

public class ENPushUrlBuilderTest {

    @Test
    public void testENPushUrlBuilder() {
        ENPush enPush = ENPush.getInstance();
        enPush.setCloudRegion(ENPush.REGION_US_SOUTH);

        // Use only for development purpose.

        ENPushUrlBuilder sut = new ENPushUrlBuilder("instance_id", "destination_id");

        assertNotNull(sut.getDevicesUrl());
        assertNotNull(sut.getDeviceIdUrl("device_id"));
        assertNotNull(sut.getSubscriptionsUrl());
        assertNotNull(sut.getSubscriptionsUrl("device_id", "tag_name"));
        assertNotNull(sut.getUnregisterUrl("device_id"));
    }

    @Test
    public void testENPushUrlBuilderOverrride() {

        ENPush enPush = ENPush.getInstance();
        enPush.setCloudRegion(ENPush.REGION_US_SOUTH);

        ENPush.overrideServerHost = "https://notifications.appdomain.cloud";

        ENPushUrlBuilder sut = new ENPushUrlBuilder("instance_id", "destination_id");

        assertNotNull(sut.getDevicesUrl());
        assertNotNull(sut.getDeviceIdUrl("device_id"));
        assertNotNull(sut.getSubscriptionsUrl());
        assertNotNull(sut.getSubscriptionsUrl("device_id", "tag_name"));
        assertNotNull(sut.getUnregisterUrl("device_id"));
        assertEquals(sut.getRewriteDomain(), "https://notifications.appdomain.cloud");


    }
}
