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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoggerTest implements LoggerInterface {

    private int calledLogger = 0;
    @Before
    public void setUp() {
        calledLogger = 0;
    }

    @After
    public void tearDown() {
        calledLogger = 0;
    }

    @Test
    public void testLogger() {
        Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENPushIntentService.class.getSimpleName());
        logger.loggerInterface = this;
        assertEquals(0, calledLogger);
        logger.error("ENPushIntentService: test fail");
        assertEquals(1, calledLogger);
        logger.info("ENPushIntentService: test info");
        assertEquals(2, calledLogger);
        logger.debug("ENPushIntentService: test debug");
        assertEquals(3, calledLogger);
        logger.fatal("ENPushIntentService: test fatal");
        assertEquals(4, calledLogger);

    }

    @Override
    public void doLog(String level, String message, long timestamp, Throwable t, JSONObject additionalMetadata) {
        calledLogger++;
    }
}
