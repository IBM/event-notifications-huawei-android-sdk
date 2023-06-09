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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ibm.cloud.eventnotifications.huawei.android.ENResourceManagerDefault;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for creating the Android Notifications channel.
 */
public class ENInternalPushChannel {

    private static final String GROUP_CHANNEL_ID = "channelId";
    private static final String GROUP_CHANNEL_NAME = "channelName";
    private static final String GROUP_CHANNEL_IMPORTANCE = "importance";
    private static final String GROUP_CHANNEL_ENABLE_LIGHTS = "enableLights";
    private static final String GROUP_CHANNEL_ENABLE_VIBRATION = "enableVibration";
    private static final String GROUP_CHANNEL_LIGHT_COLOR = "lightColor";
    private static final String GROUP_CHANNEL_LOCKSCREEN_VISIBILITY = "lockScreenVisibility";
    private static final String GROUP_CHANNEL_GROUP = "groupJson";
    private static final String GROUP_CHANNEL_BYPASS_DND = "bypassDND";
    private static final String GROUP_CHANNEL_DESCRIPTION = "description";
    private static final String GROUP_CHANNEL_SHOWBADGE = "showBadge";
    private static final String GROUP_CHANNEL_SOUND = "sound";
    private static final String GROUP_CHANNEL_VIBRATIONPATTERN = "vibrationPattern";


    private String channelId = null;
    private String channelName = null;
    private int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private boolean enableLights = false;
    private boolean enableVibration = false;
    private String lightColor = "black";
    private int lockScreenVisibility = Notification.VISIBILITY_PUBLIC;
    private JSONObject groupJson = null;
    private boolean bypassDND = false;
    private String description = null;
    private boolean showBadge = true;
    private String sound = null;
    private JSONArray vibrationPattern = new JSONArray();

    protected static Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENInternalPushChannel.class.getSimpleName());

    /**
     * Convert the channel to string.
     * @return string representation of the channel.
     */
    @Override
    public String toString() {
        return "ENInternalPushChannel [channelId=" + channelId + ", channelName=" + channelName +
                ",importance="+ importance +", enableLights=" +enableLights+", enableVibration ="+enableVibration+"" +
                " lightColor="+lightColor+",lockScreenVisibility="+lockScreenVisibility+", " +
                "groupJson="+groupJson+", bypassDND="+bypassDND+", description="+description+", showBadge="+showBadge+", " +
                "vibrationPattern="+vibrationPattern+" ]";
    }

    /**
     * get channel id
     * @return channel id
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * get channel name
     * @return channel name
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * get channel importance
     * @return channel importance
     */
    public int getImportance() {
        return importance;
    }

    /**
     * get channel enable lights value
     * @return channel enable lights value
     */
    public boolean getEnableLights() {return enableLights;}

    /**
     * get channel enable vibration value
     * @return channel enable vibration value
     */
    public boolean getEnableVibration() {return enableVibration;}

    /**
     * get channel light color value
     * @return channel light color value
     */
    public String getLightColor() {return lightColor;}

    /**
     * get channel lock screen visibility
     * @return channel lock screen visibility
     */
    public int getLockScreenVisibility() {
        return lockScreenVisibility;
    }

    /**
     * get channel group json object
     * @return channel group json object
     */
    public JSONObject getGroupJson() { return groupJson;}

    /**
     * get channel bypass DND value
     * @return channel bypass DND value
     */
    public boolean getBypassDND() { return  bypassDND;}

    /**
     * get channel description value
     * @return channel description value
     */
    public String getDescription() { return description;}

    /**
     * get channel show badge value
     * @return channel show badge value
     */
    public boolean getShowBadge() {return showBadge;}

    /**
     * get channel sound value
     * @return channel sound value
     */
    public String getSound() { return sound;}

    /**
     * get channel vibration pattern value
     * @return channel vibration pattern value
     */
    public JSONArray getVibrationPattern() {return  vibrationPattern;}


    /**
     * Set channel id value
     * @param channelId
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * Set channel Name
     * @param channelName
     */
    public void setChannelName(String channelName) { this.channelName = channelName;}

    /**
     * Set channel importance value
     * @param importance
     */
    public void setImportance(int importance) { this.importance = importance;}

    /**
     * Set channel lights enable value
     * @param enableLights
     */
    public void setEnableLights(boolean enableLights) { this.enableLights = enableLights;}

    /**
     * Set channel vibration enable value
     * @param enableVibration
     */
    public void setEnableVibration(boolean enableVibration) { this.enableVibration = enableVibration;}

    /**
     * Set channel light color value
     * @param lightColor
     */
    public void setLightColor(String lightColor) { this.lightColor = lightColor;}

    /**
     * Set channel lock screen visibility value
     * @param lockScreenVisibility
     */
    public void setLockScreenVisibility(int lockScreenVisibility) { this.lockScreenVisibility = lockScreenVisibility;}

    /**
     * Set channel group json
     * @param groupJson
     */
    public void setGroupsJSON(JSONObject groupJson) { this.groupJson = groupJson;}

    /**
     * Set channel bypass DND value
     * @param bypassDND
     */
    public void setBypassDND(boolean bypassDND) { this.bypassDND = bypassDND;}

    /**
     * Set channel badge show boolean
     * @param showBadge
     */
    public void setShowBadge(boolean showBadge) { this.showBadge = showBadge;}

    /**
     * Set channel description value
     * @param description
     */
    public void setDescription(String description) { this.description = description;}

    /**
     * Set channel sound value
     * @param sound
     */
    public void setSound(String sound) { this.sound = sound;}

    /**
     * Set channel vibration pattern
     * @param vibrationPattern
     */
    public void setVibrationPattern(JSONArray vibrationPattern) { this.vibrationPattern = vibrationPattern;}

    /**
     * Init method
     * @param json channel json
     */
    public ENInternalPushChannel(JSONObject json) {

        try {
            channelId = json.getString(GROUP_CHANNEL_ID);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            channelName = json.getString(GROUP_CHANNEL_NAME);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            importance = json.getInt(GROUP_CHANNEL_IMPORTANCE);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            enableLights = json.getBoolean(GROUP_CHANNEL_ENABLE_LIGHTS);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            enableVibration = json.getBoolean(GROUP_CHANNEL_ENABLE_VIBRATION);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            lightColor = json.getString(GROUP_CHANNEL_LIGHT_COLOR);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            lockScreenVisibility = json.getInt(GROUP_CHANNEL_LOCKSCREEN_VISIBILITY);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            String group =json.getString(GROUP_CHANNEL_GROUP);
            groupJson = new JSONObject(group);
            //groupJson = json.getJSONObject(GROUP_CHANNEL_GROUP);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            bypassDND = json.getBoolean(GROUP_CHANNEL_BYPASS_DND);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            description = json.getString(GROUP_CHANNEL_DESCRIPTION);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            showBadge = json.getBoolean(GROUP_CHANNEL_SHOWBADGE);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            sound = json.getString(GROUP_CHANNEL_SOUND);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }

        try {
            vibrationPattern = json.getJSONArray(GROUP_CHANNEL_VIBRATIONPATTERN);
        } catch (JSONException e) {
            logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
        }
    }

    /**
     * Get the channel group object
     * @return  channel group object
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannelGroup getChannelGroup() {

        if (groupJson != null) {

            ENInternalPushChannelGroup group = new ENInternalPushChannelGroup(this.groupJson);
            NotificationChannelGroup channelGroup = new NotificationChannelGroup(group.getGroupId(),group.getChannelGroupName());
            return  channelGroup;
        }
        return null;
    }

    /**
     * Get the channel object
     * @param context app context
     * @param mNotificationManager NotificationManager object
     * @return channel object
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel getChannel(Context context, NotificationManager mNotificationManager) {

        NotificationChannel mChannel = new NotificationChannel(this.channelId, this.channelName,this.importance);
        mChannel.enableLights(this.enableLights);
        mChannel.enableVibration(this.enableVibration);
        mChannel.setLightColor(getLightColor(this.lightColor));
        mChannel.setLockscreenVisibility(this.lockScreenVisibility);
        mChannel.setBypassDnd(this.bypassDND);
        mChannel.setShowBadge(this.showBadge);
        if (this.description != null) {
            mChannel.setDescription(this.description);
        }
        if (this.sound != null) {
            ENResourceManagerDefault enPushIntentService = new ENResourceManagerDefault();
            mChannel.setSound(enPushIntentService.getNotificationSoundUri(context,this.sound),null);
        }

        if(this.vibrationPattern != null) {
            int len = this.vibrationPattern.length();
            long[] list = new long[len];
            for (int i=0;i<len;i++){
                try {
                    list[i] = this.vibrationPattern.getLong(i);
                } catch (JSONException e) {
                    logger.error("ENInternalPushChannel: ENInternalPushChannel() - Exception while parsing JSON, get alert.  "+ e.toString());
                }
            }
            if(list.length > 0) {
                mChannel.setVibrationPattern(list);
            }
        }

        if(this.groupJson != null ) {
            NotificationChannelGroup channelGroup = getChannelGroup();
            mNotificationManager.createNotificationChannelGroup(channelGroup);
            mChannel.setGroup(channelGroup.getId());
        }
        return  mChannel;
    }

    /**
     * Construct the light colors for channel
     * @param ledARGB color values
     * @return return color value.
     */
    private int getLightColor(String ledARGB) {
        if (ledARGB!=null && ledARGB.equalsIgnoreCase("black")) {
            return Color.BLACK;
        } else if (ledARGB.equalsIgnoreCase("darkgray")) {
            return Color.DKGRAY;
        } else if (ledARGB.equalsIgnoreCase("gray")) {
            return Color.GRAY;
        } else if (ledARGB.equalsIgnoreCase("lightgray")) {
            return Color.LTGRAY;
        } else if (ledARGB.equalsIgnoreCase("white")) {
            return Color.WHITE;
        } else if (ledARGB.equalsIgnoreCase("red")) {
            return Color.RED;
        } else if (ledARGB.equalsIgnoreCase("green")) {
            return Color.GREEN;
        } else if (ledARGB.equalsIgnoreCase("blue")) {
            return Color.BLUE;
        } else if (ledARGB.equalsIgnoreCase("yellow")) {
            return Color.YELLOW;
        } else if (ledARGB.equalsIgnoreCase("cyan")) {
            return Color.CYAN;
        } else if (ledARGB.equalsIgnoreCase("magenta")) {
            return Color.MAGENTA;
        } else if (ledARGB.equalsIgnoreCase("transparent")) {
            return Color.TRANSPARENT;
        } else {
            return Color.BLACK;
        }
    }
}