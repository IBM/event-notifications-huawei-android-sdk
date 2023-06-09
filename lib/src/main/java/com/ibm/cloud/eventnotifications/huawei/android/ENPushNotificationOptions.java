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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * ENPushNotificationOptions for creating the Notifications options available in the SDK.
 */
public class ENPushNotificationOptions {

    private Visibility visibility;
    private String redact;
    private Priority priority;
    private String sound;
    private String icon;
    private List<ENPushNotificationCategory> categories = new ArrayList<ENPushNotificationCategory>();
    private String deviceId;
    private JSONObject templateValues = new JSONObject();

    /**
     * Get the visibility of NotificationOptions
     * @return NotificationOptions visibility value
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Set NotificationOptions visibility value
     * @param visibility visibility value
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Get NotificationOptions redact value.
     * @return redact value
     */
    public String getRedact() {
        return redact;
    }

    /**
     * set NotificationOptions redact value
     * @param redact redact value.
     */
    public void setRedact(String redact) {
        this.redact = redact;
    }

    /**
     * Get NotificationOptions priority value
     * @return priority value
     */
    public Priority getPriority() { return priority; }

    /**
     * Set NotificationOptions priority value.
     * @param priority priority value
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Get NotificationOptions sound value
     * @return sound value
     */
    public String getSound() {
        return sound;
    }

    /**
     * Set NotificationOptions sound value
     * @param sound sound value
     */
    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * Get NotificationOptions icon
     * @return icon value
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Set NotificationOptions icon value
     * @param icon icon value
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Init method
     */
    public ENPushNotificationOptions() {}

    /**
     * Enum for message priority
     */
    public static enum Priority {
        MAX(2), HIGH(1), DEFAULT(0), LOW(-1), MIN(-2);

        private final int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Enum for message visibility
     */
    public static enum Visibility {
        PUBLIC(1), PRIVATE(0), SECRET(-1);

        private final int value;

        Visibility(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Set interactive notification category
     * @param category ENPushNotificationCategory object
     */
    public void  setInteractiveNotificationCategory(ENPushNotificationCategory category) {
        categories.add(category);
    }

    /**
     * Set interactive notification categories
     * @param categories list of ENPushNotificationCategory object
     */
    public void setInteractiveNotificationCategories(List<ENPushNotificationCategory> categories) {
        this.categories = categories;
    }

    /**
     * Set the device id value
     * @param withDeviceId device id value
     */
    public void setDeviceid(String withDeviceId) {
        this.deviceId = withDeviceId;
    }

    /**
     * Get interactive notification categories
     * @return interactive notification categories
     */
    public List<ENPushNotificationCategory> getInteractiveNotificationCategories() {
        return categories;
    }

    /**
     * Get device id
     * @return device id
     */
    public String getDeviceid() {
        return deviceId;
    }

    public JSONObject getTemplateValues() {
        return templateValues;
    }

    public void setPushVariables(JSONObject templateValues) {
        this.templateValues = templateValues;
    }

    }
