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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import org.junit.Test;
import org.mockito.Mockito;

public class ENResourceManagerDefaultTest {

    @Test
    public void testGetResourceIdForCustomIcon() {

        Context mockContext = Mockito.mock(Context.class);
        Resources resources = Mockito.mock(Resources.class);

        int expectedResourceId = 1;
        when(mockContext.getResources()).thenReturn(resources);
        when(mockContext.getPackageName()).thenReturn("package");

        when(resources.getIdentifier(anyString(), anyString(), anyString())).thenReturn(expectedResourceId);

        ENResourceManagerDefault sut = new ENResourceManagerDefault();

        int result = sut.getResourceIdForCustomIcon(mockContext, "drawable", "icon");

        assertEquals(result, expectedResourceId);

    }

    @Test
    public void testGetResourceId() {

        Context mockContext = Mockito.mock(Context.class);
        Resources resources = Mockito.mock(Resources.class);

        int expectedResourceId = 1;
        when(mockContext.getResources()).thenReturn(resources);
        when(mockContext.getPackageName()).thenReturn("package");

        when(resources.getIdentifier(anyString(), anyString(), anyString())).thenReturn(expectedResourceId);

        ENResourceManagerDefault sut = new ENResourceManagerDefault();

        int result = sut.getResourceId(mockContext, "drawable", "icon");

        assertEquals(result, expectedResourceId);

    }

    @Test
    public void testGetCustomNotificationIcon() {

        Context mockContext = Mockito.mock(Context.class);
        Resources resources = Mockito.mock(Resources.class);

        int expectedResourceId = 1;
        when(mockContext.getResources()).thenReturn(resources);
        when(mockContext.getPackageName()).thenReturn("package");

        when(resources.getIdentifier(anyString(), anyString(), anyString())).thenReturn(expectedResourceId);

        ENResourceManagerDefault sut = new ENResourceManagerDefault();

        int result = sut.getCustomNotificationIcon(mockContext, "drawable");

        assertEquals(result, expectedResourceId);

    }

    @Test
    public void testGetNotificationSoundUri() {

        Context mockContext = Mockito.mock(Context.class);
        Resources resources = Mockito.mock(Resources.class);


        String expectedUri = "android.resource://packaged/1";

        int expectedResourceId = 1;

        when(mockContext.getResources()).thenReturn(resources);
        when(mockContext.getPackageName()).thenReturn("package");

        when(resources.getIdentifier(anyString(), anyString(), anyString())).thenReturn(expectedResourceId);
        ENResourceManagerDefault sut = new ENResourceManagerDefault();

        Uri result = sut.getNotificationSoundUri(mockContext, "drawable");

    }

}
