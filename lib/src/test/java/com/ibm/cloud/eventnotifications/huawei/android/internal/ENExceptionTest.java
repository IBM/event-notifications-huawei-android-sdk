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

import com.ibm.cloud.eventnotifications.huawei.android.ENPushIntentService;

import org.json.JSONObject;
import org.junit.Test;

public class ENExceptionTest {

    int exceptionCalled = 0;
    String messageSut = "";

    class LoggerDummy implements LoggerInterface {

        @Override
        public void doLog(String level, String message, long timestamp, Throwable t, JSONObject additionalMetadata) {
            exceptionCalled += 1;
            messageSut = message;
        }
    }

    @Test
    public void testENExceptionInitialization() {

        Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENPushIntentService.class.getSimpleName());
        logger.loggerInterface = new LoggerDummy();
        ENException.logger = logger;

        Exception e = new Exception("Exception");
        ENException.logException("ENExceptionTest", "testENExceptionInitialization", e);
        assertEquals(exceptionCalled, 1);

        ENException.logException("ENExceptionTest", "testENExceptionInitialization", e, new Object[]{});
        assertEquals(exceptionCalled, 2);
    }
}
