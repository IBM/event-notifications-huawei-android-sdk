/**
 * (C) Copyright IBM Corp. 2021.
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
 * Class implementing ENMessageConstantInterface for getting the default constants.
 */
public class ENMessageDefaultConstant implements ENMessageConstantInterface {

    /**
     * Get string values for the constants.
     * @param key key for the constant.
     * @return string value for the key.
     */
    @Override
    public String getStringForKey(ENMessageKeys key) {

        switch (key) {
            case GCM_EXTRA_MESSAGE:
                return "message";
            case GCM_MESSAGE:
                return ".C2DM_MESSAGE";
            case IBM_PUSH_NOTIFICATION:
                return ".IBMPushNotification";
            case CANCEL_IBM_PUSH_NOTIFICATION:
                return ".Cancel_IBMPushNotification";
            default:
                return "";
        }
    }
}