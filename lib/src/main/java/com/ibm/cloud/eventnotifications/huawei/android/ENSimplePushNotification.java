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

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENAbstractPushMessage;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushMessage;

/**
 * ENSimplePushNotification class for showing the simplest for Notification object.
 */
public class ENSimplePushNotification extends ENAbstractPushMessage implements ENPushMessage {
	private String url;
	private String payload;
    private String actionName = "";

	/**
	 * Init method
	 * @param alert alert message
	 * @param id message id
	 * @param url message url
	 * @param payload message payload object
	 */
	ENSimplePushNotification(String alert, String id,
							 String url, String payload) {
		super(alert, id);
		this.url = url;
		this.payload = payload;
	}

	/**
	 * Init method using ENInternalPushMessage
	 * @param message ENInternalPushMessage object
	 */
	ENSimplePushNotification(ENInternalPushMessage message) {
		super(message);
		this.url = message.getUrl();
		this.payload = message.getPayload();
	}

	/**
	 * Get the url property specified in the notification
	 *
	 * @return the url property
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get the additional payload defined in the notification message
	 *
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "ENSimplePushNotification :{url:" + url + ", payload:"
				+ payload + ", alert:" + alert + "}";
	}

	/**
	 * Set the action name for the notification
	 * @param actionName
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * Get the action name of the notification
	 * @return actionName
	 */
	public String getActionName() {
		return actionName;
	}
}
