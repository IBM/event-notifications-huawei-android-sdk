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

/**
 * Class containing methods to log all the exceptions.
 */
public final class ENException {

    protected static Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENException.class.getSimpleName());

    private ENException() {}
    /**
     * @param className name of class
     * @param methodName name of method
     * @param exception the exception thrown
     */
    public static void logException(String className, String methodName, Exception exception) {
        logException(className, methodName, exception, new Object[]{});

    }

    /**
     * @param className name of class
     * @param methodName name of method
     * @param exception the exception thrown
     * @param messageParams additional log message
     */
    public static void logException(String className, String methodName, Exception exception, final Object[] messageParams) {
        String message = "Class :" + className + ", Method : " + methodName + ", Reason: " + exception.toString();
        for (int i = messageParams.length - 1; i >= 0; i--) {
            message += messageParams[i];
        }
        logger.debug(message);
    }
}
