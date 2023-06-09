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

public class ENPushExceptionTest {

    @Test
    public void testENPushExceptionWithException() {

        ENPushException exception = new ENPushException(new Exception("Error test"));
        assertNotNull(exception);
    }

    @Test
    public void testENPushExceptionWithMessage() {

        ENPushException exception = new ENPushException("Error test");
        assertNotNull(exception);
    }

    @Test
    public void testENPushExceptionWithErrorMessage() {

        ENPushException exception = new ENPushException("Error test", 400);
        assertNotNull(exception);
        assertEquals("Error test", exception.getErrorMessage());
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testENPushExceptionWithErrorLine() {

        ENPushException exception = new ENPushException(400, "Error new line", "Error test", "400", "/var/error.txt");
        assertNotNull(exception);
        assertEquals("Error test", exception.getErrorMessage());
        assertEquals("Error new line", exception.getStatusLine());
        assertEquals("400", exception.getErrorCode());
        assertEquals("/var/error.txt", exception.getDocUrl());
        assertEquals(400, exception.getStatusCode());
    }
}
