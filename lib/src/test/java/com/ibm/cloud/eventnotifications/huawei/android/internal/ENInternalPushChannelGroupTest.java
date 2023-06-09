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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class ENInternalPushChannelGroupTest {
    private String groupString = "{\"groupId\": \"QweRrc\", \"groupName\": \"Test Group\"}";

    @Test
    public void testGroup() throws JSONException {

        JSONObject jsonMessage = new JSONObject(groupString);

        ENInternalPushChannelGroup group = new ENInternalPushChannelGroup(jsonMessage);
        assertNotNull(group);
        assertEquals(group.getGroupId(), "QweRrc");
        assertEquals(group.getChannelGroupName(), "Test Group");

        JSONObject getJSON = group.toJson();

        assertEquals(getJSON.getString("groupId"), "QweRrc");
        assertEquals(getJSON.getString("groupName"), "Test Group");

        group.setGroupId("QweRrc12");
        group.setChannelGroupName("Test Group name");
        assertEquals(group.getGroupId(), "QweRrc12");
        assertEquals(group.getChannelGroupName(), "Test Group name");

    }

}
