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

import android.content.Context;

import org.json.JSONException;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class BaseDeviceIdentityTest {


    @Test
    public void testBaseDeviceInitializationWithMockContext() throws JSONException {
        Context mockContext = Mockito.mock(Context.class);

        String testDeviceId = UUID.randomUUID().toString();
        String version = "1.0.0";
        String brand = "IBM";
        String model = "IBM-1";
        String osName = "android";


        class SettingsMock implements SettingsSecureInterface {
            @Override
            public String getDeviceID(Context context) {
                return UUID.nameUUIDFromBytes(testDeviceId.getBytes()).toString();
            }

            @Override
            public String getRelease() {
                return version;
            }

            @Override
            public String getBrand() {
                return brand;
            }

            @Override
            public String getModel() {
                return model;
            }

            @Override
            public String getOs() {
                return osName;
            }
        }

        BaseDeviceIdentity.settingsSecureInterface = new SettingsMock();
        BaseDeviceIdentity sut = new BaseDeviceIdentity(mockContext);

        String expectedDeviceId = UUID.nameUUIDFromBytes(testDeviceId.getBytes()).toString();
        assertEquals(expectedDeviceId, sut.getId());
        assertEquals(brand, sut.getBrand());
        assertEquals(model, sut.getModel());
        assertEquals(osName, sut.getOS());
        assertEquals(version, sut.getOSVersion());



    }
}
