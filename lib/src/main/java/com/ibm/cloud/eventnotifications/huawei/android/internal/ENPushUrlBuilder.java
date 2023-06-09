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

import com.ibm.cloud.eventnotifications.huawei.android.ENPush;

/**
 * Class for building the APIs for the network communication.
 */
public class ENPushUrlBuilder {

	private static final String FORWARDSLASH = "/";
	private static final String ENPUSH = "event-notifications";
	private static final String V1 = "v1";
	private static final String V2 = "v2";
	private static final String INSTANCES = "instances";
	private static final String DESTINATIONS = "destinations";
	private static final String DELIVERY = "delivery";

	private static final String AMPERSAND = "&";
	private static final String QUESTIONMARK = "?";
	private static final String EQUALTO = "=";
	private static final String SUBSCRIPTIONS = "tag_subscriptions";
	private static final String TAGS = "tags";
	private static final String DEVICES = "devices";
	private static final String MESSAGES = "messages";
	private static final String TAGNAME = "tag_name";
	private static final String DEVICEID = "device_id";
	public static final String DEVICE_ID_NULL = "nullDeviceId";

	private static final String HOST = "cloud.ibm.com";

	public final static String HTTPS_SCHEME = "https://";

	private String defaultProtocol = HTTPS_SCHEME;
	private static final String PRIVATE = "private";



	String reWriteDomain = null;
	private final StringBuilder pwUrl_ = new StringBuilder();

	/**
	 * Init method
	 * @param instanceId appguid of the Event Notifications service
	 * @param destinationId destinationId of the Event Notifications service
	 */
	public ENPushUrlBuilder(String instanceId, String destinationId) {
		if (ENPush.overrideServerHost != null){
			pwUrl_.append(ENPush.overrideServerHost);
			reWriteDomain = ENPush.overrideServerHost;
		}
		else {
			pwUrl_.append(defaultProtocol);
			if(ENPush.getInstance().getIsPrivateEndPoint()){
				pwUrl_.append(PRIVATE);
				pwUrl_.append(".");
			}
			String region = ENPush.getInstance().getCloudRegionSuffix();
			pwUrl_.append(region);
			pwUrl_.append(".");
			pwUrl_.append(ENPUSH);
			pwUrl_.append(".");
			pwUrl_.append(HOST);
			reWriteDomain = "";
		}
		pwUrl_.append(FORWARDSLASH);
		pwUrl_.append(ENPUSH);
		pwUrl_.append(FORWARDSLASH);
		pwUrl_.append(V2);
		pwUrl_.append(FORWARDSLASH);
		pwUrl_.append(INSTANCES);
		pwUrl_.append(FORWARDSLASH);
		pwUrl_.append(instanceId);
		pwUrl_.append(FORWARDSLASH);
		pwUrl_.append(DESTINATIONS);
		pwUrl_.append(FORWARDSLASH);
		pwUrl_.append(destinationId);
		pwUrl_.append(FORWARDSLASH);
	}

	/**
	 * Get the device url
	 * @return device url
	 */
	public String getDevicesUrl() {
		return getBaseUrl(DEVICES).toString();
	}

	/**
	 * Get subscriptions url
	 * @return subscriptions url
	 */
	public String getSubscriptionsUrl() {
		return getBaseUrl(SUBSCRIPTIONS).toString();
	}

	/**
	 * Get ListTagsubscriptions url
	 * @param deviceId deviceId for the Event Notifications service
	 * @param tagID tagID for the subscription
	 * @return return subscriptions url
	 */
	public String getSubscriptionsUrl(String deviceId, String tagID) {
		StringBuilder subscriptionsUrl = new StringBuilder(pwUrl_);
		if (deviceId != null) {
			if(tagID == null){
				subscriptionsUrl.append(DEVICES);
				subscriptionsUrl.append(FORWARDSLASH);
				subscriptionsUrl.append(deviceId);
				subscriptionsUrl.append(FORWARDSLASH);
			}
		} else {
			return DEVICE_ID_NULL;
		}
		subscriptionsUrl.append(SUBSCRIPTIONS);
		if (tagID != null) {
			subscriptionsUrl.append(FORWARDSLASH);
			subscriptionsUrl.append(tagID);
		}

		return subscriptionsUrl.toString();
	}

	/**
	 * Get device url.
	 * @param deviceId deviceId for the Event Notifications service
	 * @return return the device url.
	 */
	public String getDeviceIdUrl(String deviceId) {
		StringBuilder deviceIdUrl = new StringBuilder(getDevicesUrl());
		deviceIdUrl.append(FORWARDSLASH).append(deviceId);
		return deviceIdUrl.toString();
	}

	public String getStatusUrl(String deviceId) {
		StringBuilder statusUrl = new StringBuilder(getDeviceIdUrl(deviceId));
		statusUrl.append(FORWARDSLASH).append(DELIVERY);
		return statusUrl.toString();
	}

	/**
	 * Get unregister url.
	 * @param deviceId deviceId for the Event Notifications service
	 * @return unregister url.
	 */
	public String getUnregisterUrl(String deviceId){
		StringBuilder deviceUnregisterUrl = new StringBuilder(
				getDevicesUrl());
		deviceUnregisterUrl.append(FORWARDSLASH);
		deviceUnregisterUrl.append(deviceId);

		return deviceUnregisterUrl.toString();
	}

	/**
	 * Get rewrite domain
	 * @return rewrite domain url
	 */
	public String getRewriteDomain(){

		return reWriteDomain;
	}

	/**
	 * Get the base url
	 * @param base base name
	 * @return base url
	 */
	private StringBuilder getBaseUrl(String base) {
		StringBuilder collectionUrl = new StringBuilder(pwUrl_);
		collectionUrl.append(base);

		return collectionUrl;
	}
}
