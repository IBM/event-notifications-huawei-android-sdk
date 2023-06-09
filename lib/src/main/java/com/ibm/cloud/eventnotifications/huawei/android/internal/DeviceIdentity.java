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
 * Interface for the device, platform, os etc.
 */
public interface DeviceIdentity {
	String ID = "id";
	String OS = "platform";
	String OS_VERSION = "osVersion";
	String MODEL = "model";
	String BRAND = "brand";

	/**
	 * @return deviceId
	 */
	String getId();

	/**
	 * @return device OS
	 */
	String getOS();

	/**
	 * @return OS version
	 */
	String getOSVersion();

	/**
	 * @return device model
	 */
	String getModel();

	/**
	 * @return device brand
	 */
	String getBrand();
}
