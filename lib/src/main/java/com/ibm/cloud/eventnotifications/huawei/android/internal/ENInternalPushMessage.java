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

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Represents message received from Push Server via GCM
 * Its visibility is restricted to package level since it will be used by the sdk internally
 * to store the push received before it can be passed to Jane.
 */
public class ENInternalPushMessage implements Parcelable, ENPushMessage {

	private static final String GCM_EXTRA_ID = "nid";
	private static final String GCM_EXTRA_ALERT = "alert";
	private static final String GCM_EXTRA_PAYLOAD = "payload";
	private static final String GCM_EXTRA_URL = "url";
	private static final String GCM_EXTRA_SOUND = "sound";
	private static final String GCM_EXTRA_BRIDGE = "bridge";
	private static final String GCM_EXTRA_VISIBILITY = "visibility";
	private static final String GCM_EXTRA_PRIORITY = "priority";
	private static final String GCM_EXTRA_REDACT = "redact";
	private static final String GCM_EXTRA_CATEGORY = "interactiveCategory";
	private static final String GCM_EXTRA_KEY = "key";
	private static final String GCM_EXTRA_NOTIFICATIONID = "notificationId";
	private static final String GCM_EXTRA_STYLE = "style";
	private static final String GCM_EXTRA_ICONNAME = "icon";
	private static final String GCM_EXTRA_LIGHTS = "lights";
	private static final String GCM_MESSAGE_TYPE = "type";
	private static final String GCM_HAS_TEMPLATE = "has-template";
	private static final String FCM_TITLE = "androidTitle";
	private static final String FCM_CHANNEL = "channel";

	public static final String LOG_TAG = "PushMessage";

	private String id = null;
	private String url = null;
	private String alert = null;
	private String payload = null;
	private String sound = null;
	private boolean bridge = true;
	private String priority = null;
	private String visibility = null;
	private String redact = null;
	private String key = null;
	private String category = null;
	private String messageType = null;

	private int notificationId;
	private String gcmStyle = null;
	private String iconName = null;
	private String lights = null;
	private int hasTemplate = 0;

	private String androidTitle = null;
	private JSONObject channelJson = null;

	protected static Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENInternalPushMessage.class.getSimpleName());

	/**
	 * Init method
	 * @param intent intent object with value in extras.
	 */
	public ENInternalPushMessage(Intent intent) {

		Bundle info = intent.getExtras();
		ENPushUtils.dumpIntent(intent);

		alert = info.getString(GCM_EXTRA_ALERT);
		androidTitle = info.getString(FCM_TITLE);
		url = info.getString(GCM_EXTRA_URL);
		channelJson = (JSONObject) info.get (FCM_CHANNEL);
		payload = info.getString(GCM_EXTRA_PAYLOAD);
		sound = info.getString(GCM_EXTRA_SOUND);
		bridge = info.getBoolean(GCM_EXTRA_BRIDGE);
		priority = info.getString(GCM_EXTRA_PRIORITY);
		visibility = info.getString(GCM_EXTRA_VISIBILITY);
		redact = info.getString(GCM_EXTRA_REDACT);
		key = info.getString(GCM_EXTRA_KEY);
		category = info.getString(GCM_EXTRA_CATEGORY);
		gcmStyle = info.getString(GCM_EXTRA_STYLE);
		iconName = info.getString(GCM_EXTRA_ICONNAME);
		notificationId = info.getInt(GCM_EXTRA_NOTIFICATIONID);
		lights = info.getString(GCM_EXTRA_LIGHTS);
		messageType = info.getString(GCM_MESSAGE_TYPE);

		try {
			hasTemplate = Integer.parseInt(info.getString(GCM_HAS_TEMPLATE));
			JSONObject towers = new JSONObject(payload);
			id = towers.getString(GCM_EXTRA_ID);
		}
		catch (JSONException e){
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get id.  "+ e.toString());
		}
	}

	/**
	 * Init method
	 * @param source parcel with values of ENInternalPushMessage
	 */
	private ENInternalPushMessage(Parcel source) {
		id = source.readString();
		alert = source.readString();
		androidTitle = source.readString();
		url = source.readString();
		try {
			String value = source.readString();
			if (value != null) {
				channelJson = new JSONObject(value);
			}
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception with Parcel.  "+ e.toString());
		}
		payload = source.readString();
		sound = source.readString();
		bridge = Boolean.valueOf(source.readString());
		priority = source.readString();
		visibility = source.readString();
		redact = source.readString();
		category = source.readString();
		key = source.readString();
		gcmStyle = source.readString();
		iconName = source.readString();
		notificationId = source.readInt();
		lights = source.readString();
		messageType = source.readString();
		hasTemplate = source.readInt();
	}

	/**
	 * Init method
	 * @param json json object with ENInternalPushMessage values.
	 */
	public ENInternalPushMessage(JSONObject json) {
		try {
			alert = json.getString(GCM_EXTRA_ALERT);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get alert.  "+ e.toString());
		}
		try {
			androidTitle = json.getString(FCM_TITLE);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get androidTitle.  "+ e.toString());
		}
		try {
			String channel =json.getString(FCM_CHANNEL);
			channelJson = new JSONObject(channel);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get androidTitle.  "+ e.toString());
		}

		try {
			url = json.getString(GCM_EXTRA_URL);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get url .  "+ e.toString());
		}
		try {
			payload = json.getString(GCM_EXTRA_PAYLOAD);
			JSONObject towers = new JSONObject(payload);
			id = towers.getString(GCM_EXTRA_ID);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get payload  "+ e.toString());
		}
		try {
			sound = json.getString(GCM_EXTRA_SOUND);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get sound.  "+ e.toString());
		}
		try {
			bridge = json.getBoolean(GCM_EXTRA_BRIDGE);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get bridge.  "+ e.toString());
		}
		try {
			priority = json.getString(GCM_EXTRA_PRIORITY);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get priority.  "+ e.toString());
		}
		try {
			visibility = json.getString(GCM_EXTRA_VISIBILITY);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get visibility.  "+ e.toString());
		}
		try {
			redact = json.getString(GCM_EXTRA_REDACT);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get redact.  "+ e.toString());
		}
		try {
			category = json.getString(GCM_EXTRA_CATEGORY);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get category.  "+ e.toString());
		}
		try {
			key = json.getString(GCM_EXTRA_KEY);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get key.  "+ e.toString());
		}
		try {
			gcmStyle = json.getString(GCM_EXTRA_STYLE);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get style.  "+ e.toString());
		}
		try {
			iconName = json.getString(GCM_EXTRA_ICONNAME);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get iconName.  "+ e.toString());
		}
		try {
			notificationId = json.getInt(GCM_EXTRA_NOTIFICATIONID);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get notificationId.  "+ e.toString());
		}
		try {
			lights = json.getString(GCM_EXTRA_LIGHTS);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get lights. "+ e.toString());
		}
		try {
			messageType = json.getString(GCM_MESSAGE_TYPE);
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get messageType. "+ e.toString());
		}
		try {
			hasTemplate = Integer.parseInt(json.getString(GCM_HAS_TEMPLATE));
		} catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON, get hasTemplate. "+ e.toString());
		}
	}

	/**
	 * Convert ENInternalPushMessage object to json
	 * @return json of ENInternalPushMessage object
	 */
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(GCM_EXTRA_ID, id);
			json.put(GCM_EXTRA_ALERT, alert);
			json.put(FCM_TITLE,androidTitle);
			json.put(FCM_CHANNEL,channelJson);
			json.put(GCM_EXTRA_URL, url);
			json.put(GCM_EXTRA_PAYLOAD, payload);
			json.put(GCM_EXTRA_SOUND,sound);
			json.put(GCM_EXTRA_BRIDGE, bridge);
			json.put(GCM_EXTRA_PRIORITY, priority);
			json.put(GCM_EXTRA_VISIBILITY, visibility);
			json.put(GCM_EXTRA_REDACT, redact);
			json.put(GCM_EXTRA_CATEGORY, category);
			json.put(GCM_EXTRA_KEY, key);
			json.put(GCM_EXTRA_ICONNAME, iconName);
            json.put(GCM_EXTRA_NOTIFICATIONID, notificationId);
			json.put(GCM_EXTRA_LIGHTS, lights);
			json.put(GCM_MESSAGE_TYPE, messageType);
			json.put(GCM_HAS_TEMPLATE,hasTemplate);

    } catch (JSONException e) {
			logger.error("ENInternalPushMessage: ENInternalPushMessage() - Exception while parsing JSON.  "+ e.toString());
		}
		return json;
	}

	/**
	 * Get alert of ENInternalPushMessage onject
	 * @return alert value.
	 */
	@Override
	public	String getAlert() {
		return alert;
	}

	/**
	 * Set ENInternalPushMessage alert value
	 * @param alert alert value
	 */
	public void setAlert(String alert) {
		this.alert = alert;
	}

	/**
	 * Get Android Title of ENInternalPushMessage object
	 * @return Android title value
	 */
	public  String getAndroidTitle() {
		return androidTitle;
	}

	/**
	 * Set ENInternalPushMessage object android title
	 * @param androidTitle android title value
	 */
	public void  setAndroidTitle(String androidTitle) {
		this.androidTitle = androidTitle;
	}

	/**
	 * Get ENInternalPushMessage channel json
	 * @return channel json
	 */
	public JSONObject getChannelJson() {
		return channelJson;
	}

	/**
	 * Set ENInternalPushMessage object channel json
	 * @param channelJson channel json value.
	 */
	public void  setChannelJson(JSONObject channelJson) {
		this.channelJson = channelJson;
	}

	/**
	 * Get the NotificationChannel from ENInternalPushMessage object
	 * @param context app context
	 * @param mNotificationManager NotificationManager object
	 * @return NotificationChannel object
	 */
	@RequiresApi(api = Build.VERSION_CODES.O)
	public NotificationChannel getChannel(Context context, NotificationManager mNotificationManager) {

		if (channelJson != null) {
			ENInternalPushChannel channel = new ENInternalPushChannel(channelJson);
			return channel.getChannel(context,mNotificationManager);
		}
		return null;
	}
	/**
	 * Returns the payload as string
	 *
	 * @return payload as string
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * Gets the URL that is part of the notification
	 *
	 * @return url as String
	 */
	public String getUrl() {
		return url;
	}

	public void setPriority(String priority) { this.priority = priority; }

	public void setVisibility (String visibility) { this.visibility = visibility; }

	public void setRedact(String redact) {this.redact = redact; }

	public void setCategory (String category) {this.category = category; }

	public void setKey (String key) { this.key = key; }

	public void setGcmStyle (String gcmStyle) { this.gcmStyle = gcmStyle; }

	public String getGcmStyle () {return gcmStyle;}

	public void setLights(String lights) { this.lights = lights; }

	public String getLights() { return lights; }
	public void setMessageType(String messageType) { this.messageType = messageType; }
	public String getMessageType() {
		if (messageType != null && !messageType.isEmpty() && !messageType.equals("null")) {
			return messageType;
		} else {
			return "";
		}
	}
	public void setHastemplate(int hasTemplate) { this.hasTemplate = hasTemplate; }
	public int getHastemplate() { return hasTemplate; }

	@Override
	public String toString() {
		return "ENPushMessage [url=" + url + ", alert=" + alert + ", title=" + androidTitle + ", payload="
				+ payload  + ",sound="+ sound+ ",priority="+ priority + ",visibility="+ visibility + ",redact=" + redact + ",category="+category + ",key="+key + ",notificationId="+notificationId + ",type="+messageType+" ,hasTemplate="+hasTemplate+", channelJson="+channelJson+"]";
	}

	/* (non-Javadoc)
	 * @see com.ibm.mobile.services.push.IBMMessage#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.mobile.services.push.IBMMessage#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(alert);
		dest.writeString(androidTitle);
		dest.writeString(url);
		if (channelJson != null) {
			dest.writeString(channelJson.toString());
		} else {
			dest.writeString(null);
		}
		dest.writeString(payload);
		dest.writeString(sound);
		dest.writeString(String.valueOf(bridge));
		dest.writeString(priority);
		dest.writeString(visibility);
		dest.writeString(redact);
		dest.writeString(category);
		dest.writeString(key);
		dest.writeString(gcmStyle);
		dest.writeString(iconName);
		dest.writeInt(notificationId);
		dest.writeString(lights);
		dest.writeString(messageType);
		dest.writeInt(hasTemplate);
	}

	public static final Creator<ENInternalPushMessage> CREATOR = new Creator<ENInternalPushMessage>() {

		@Override
		public ENInternalPushMessage[] newArray(int size) {
			return new ENInternalPushMessage[size];
		}

		@Override
		public ENInternalPushMessage createFromParcel(Parcel source) {
			return new ENInternalPushMessage(source);
		}
	};

	/* (non-Javadoc)
	 * @see com.ibm.mobile.services.push.IBMMessage#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public boolean getBridge() { return bridge; }

	public String getPriority() { return priority; }

	public String getVisibility() { return visibility; }

	public String getRedact() { return redact; }

	public String getCategory() { return category; }

	public String getKey() { return key; }

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getIcon() { return iconName; }

	public void setIcon(String iconName) { this.iconName = iconName;}
}
