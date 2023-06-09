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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class ENAbstractPushMessageTest {

    private String message = "{\"priority\":\"MAX\",\"androidTitle\":\"Message title\",\"payload\":\"{\\\"nid\\\":\\\"T9fNHhJY\\\",\\\"eventName\\\":\\\"pushTrigger\\\"}\",\"interactiveCategory\":\"Interactive category\",\"icon\":\"Icon\",\"type\":\"default\",\"alert\":\"Test message\",\"sound\":\"Sound\"}";


    @Test
    public void testENAbstractPushMessageInitialization() throws JSONException {

        JSONObject jsonMessage = new JSONObject(message);
        ENInternalPushMessage recMessage = new ENInternalPushMessage(jsonMessage);

        class ENAbstractPushMessageDummy extends ENAbstractPushMessage {

            public ENAbstractPushMessageDummy(String alert, String id) {
                super(alert, id);
            }

            public ENAbstractPushMessageDummy(ENInternalPushMessage message) {
                super(message);
            }
        }
        ENAbstractPushMessage sut = new ENAbstractPushMessageDummy(recMessage);

        assertEquals(recMessage.getAlert() , sut.getAlert());
        assertEquals(recMessage.getId() , sut.getId());

        ENAbstractPushMessage sut2 = new ENAbstractPushMessageDummy("new alert", "id_13_rt");
        assertEquals("new alert" , sut2.getAlert());
        assertEquals("id_13_rt" , sut2.getId());

    }
}
