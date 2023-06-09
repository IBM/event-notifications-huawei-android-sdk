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

import java.util.List;

/**
 * <class>ENPushNotificationCategory</class> provides methods required to create interactive category options.
 */
public class ENPushNotificationCategory {

    private String categoryName;
    private List<ENPushNotificationButton> buttons;

    public List<ENPushNotificationButton> getButtons() {
        return buttons;
    }

    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Builder for creating the ENPushNotificationCategory object.
     */
    public static class Builder {

        private String categoryName;
        private List<ENPushNotificationButton> buttons;

        public Builder(String categoryName) {
            this.categoryName = categoryName;
        }

        public Builder setButtons(List<ENPushNotificationButton> buttons) {
            this.buttons = buttons;
            return this;
        }

        public ENPushNotificationCategory build() {
            return new ENPushNotificationCategory(this);
        }

    }

    private ENPushNotificationCategory(Builder builder) {
        categoryName = builder.categoryName;
        buttons = builder.buttons;
    }

}
