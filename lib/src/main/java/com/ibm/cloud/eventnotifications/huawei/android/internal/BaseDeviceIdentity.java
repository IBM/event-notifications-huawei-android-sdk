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

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Holds the device identity json
 */
public class BaseDeviceIdentity extends JSONObject implements DeviceIdentity {

    static public SettingsSecureInterface settingsSecureInterface = new DefaultSettingsSecure();

    /**
     * Init the data using context
     * @param context android application context
     */
    public BaseDeviceIdentity (Context context) {
        try {
            put(ID, settingsSecureInterface.getDeviceID(context));
            put(OS, settingsSecureInterface.getOs());
            put(OS_VERSION, settingsSecureInterface.getRelease());
			put(BRAND, settingsSecureInterface.getBrand());
            put(MODEL, settingsSecureInterface.getModel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return device unique id
     */
    public String getId() {
        return optString(ID);
    }

    /**
     * @return device OS
     */
    public String getOS() {
        return optString(OS);
    }

    /**
     * @return device model
     */
    public String getModel() {
        return optString(MODEL);
    }

	/**
	 * @return OS version
	 */
	public String getOSVersion(){
		return optString(OS_VERSION);
	}

	/**
	 * @return device brand
	 */
	public String getBrand(){
		return optString(BRAND);
	}
}

