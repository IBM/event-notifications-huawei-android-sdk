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

package com.ibm.cloud.eventnotifications.huawei.android;

/** This interface defines the listener methods used for Push notifications.
 */
public interface ENPushNotificationListener {

	/**
	 * This method is called when a notification arrives on the device. This
	 * method is called from the main activity (UI) thread. Hence any long
	 * pending operations from within this method must be done from a separate
	 * thread to keep the UI responsive.
	 *
	 * @param message
	 *            An ENPushMessage object that contains the data / properties
	 *            received as part of the notification
	 */
	public void onReceive(ENSimplePushNotification message);

}
