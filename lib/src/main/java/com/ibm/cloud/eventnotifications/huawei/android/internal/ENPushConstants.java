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
 * SDK constants
 */
public interface ENPushConstants {
	String FROM_NOTIFICATION_BAR = "notificationBar";
	String DEFAULT_SERVICE_NAME = "event_notifications";
	String TOKEN = "token";
	String PLATFORM = "platform";
	String DEVICE_ID = "device_id";
	String DEVICE_ID_RES = "id";
	String USER_NAME = "user_name";
	String ENPUSH_AUTHORIZATION  = "Authorization";
	String TAG_NAME = "tag_name";
	String TAGS = "tags";
	String SUBSCRIPTIONS = "subscriptions";
	String ID = "id";
	String SENDER_ID = "senderId";
	double MIN_SUPPORTED_ANDRIOD_VERSION = 4.0;
	String PRIORITY_HIGH = "high";
	String PRIORITY_LOW = "low";
	String PRIORITY_MAX = "max";
	String PRIORITY_MIN = "min";
	String VISIBILITY_PUBLIC = "public";
	String VISIBILITY_PRIVATE = "private";
	String VISIBILITY_SECRET = "secret";
	String DISMISS_NOTIFICATION = "com.ibm.cloud.eventnotifications.destination.android.sdk.DISMISS_NOTIFICATION";
	String NOTIFICATIONID = "notificationId";
	String NOTIFICATION_ID= "notification_id";
	String URL = "url";
	String TITLE = "title";
	String TYPE = "type";
	String TEXT = "text";
	String LINES = "lines";
	String PICTURE_NOTIFICATION = "picture_notification";
	String BIGTEXT_NOTIFICATION = "bigtext_notification";
	String INBOX_NOTIFICATION = "inbox_notification";
	String NID = "nid";
	String ACTION = "action";
	String RAW = "raw";
	String DRAWABLE = "drawable";
	String STATUS = "status";
	String OPEN = "OPEN";
	String SEEN = "SEEN";
	String PREFS_CLOUD_REGION = "IBMRegion";
	String LEDARGB = "ledARGB";
	String LEDONMS = "ledOnMS";
	String LEDOFFMS = "ledOffMS";
	String DEFAULT_CHANNEL_ID = "bms_notification_channel";
	String MESSAGE_TYPE = "silent";
	String TEMPLATE_OPTIONS = "variables";

	Integer REQUEST_SUCCESS_200 = 200;
	Integer REQUEST_SUCCESS_299 = 299;
	Integer REQUEST_ERROR_AUTH = 401;
	Integer REQUEST_ERROR = 400;
	Integer REQUEST_ERROR_NOT_SUPPORTED = 405;

	String DEVICE_GET_API_ERROR = "Error while fetching the device details";
	String DEVICE_POST_API_ERROR = "Error while registering the device details";
	String DEVICE_PUT_API_ERROR = "Error while updating the device details";
	String DEVICE_DEL_API_ERROR = "Error while deleting the device details";

	String SUBSCRIPTION_GET_API_ERROR = "Error while fetching the subscription details";
	String SUBSCRIPTION_POST_API_ERROR = "Error while registering the subscription details";
	String SUBSCRIPTION_PUT_API_ERROR = "Error while updating the subscription details";
	String SUBSCRIPTION_DEL_API_ERROR = "Error while deleting the subscription details";

	String LIBRARY_PACKAGE_NAME = "com.ibm.cloud.eventnotifications.destination.android";
	String VERSION_NAME = "0.0.1";
}
