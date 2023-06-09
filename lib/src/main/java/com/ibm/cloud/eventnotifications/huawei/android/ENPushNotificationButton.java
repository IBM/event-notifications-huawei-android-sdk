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

/**
 * <class>ENPushNotificationButton</class> provides methods required to create interactive Button options.
 */
public class ENPushNotificationButton {
    private String buttonName;
    private String label;
    private String icon;

    /**
     *
     * @return return the button name.
     */
    public String getButtonName() {
        return buttonName;
    }

    /**
     *
     * @return return button label.
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @return return icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Builder for creating the ENPushNotificationButton object.
     */
    public static class Builder {
        private String buttonName;
        private String label;
        private String icon;
        private boolean performsInForeground;

        public Builder(String buttonName) {
            this.buttonName = buttonName;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder setPerformsInForeground(boolean performsInForeground) {
            this.performsInForeground = performsInForeground;
            return this;
        }

        public ENPushNotificationButton build() {
            return new ENPushNotificationButton(this);
        }

    }

    private ENPushNotificationButton(Builder builder) {
        buttonName = builder.buttonName;
        label = builder.label;
        icon = builder.icon;
    }

}
