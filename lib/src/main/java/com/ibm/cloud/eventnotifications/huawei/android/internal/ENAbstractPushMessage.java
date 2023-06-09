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
 * An abstract class for ENPushMessage. Contains basic methods for alert and message Id.
 */
public abstract class ENAbstractPushMessage implements ENPushMessage {

	protected String alert;
	protected String id;

	/**
	 * init method
	 * @param alert alert message
	 * @param id message id
	 */
	public ENAbstractPushMessage(String alert, String id) {
		this.alert = alert;
		this.id = id;
	}

	/**
	 * init method
	 * @param message ENInternalPushMessage object
	 */
	public ENAbstractPushMessage(ENInternalPushMessage message) {
		this(message.getAlert(), message.getId());
	}

	/**
	 *
	 * @return alert string
	 */
	@Override
	public String getAlert() {
		return alert;
	}

	/**
	 *
	 * @return message id
	 */
	@Override
	public String getId() {
		return id;
	}

}
