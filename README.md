# Android Huawei destination SDK for IBM Cloud Event Notifications service Version 1.1.0
Android destination client library to interact with various [IBM Cloud Event Notifications Service](https://cloud.ibm.com/apidocs?category=event-notifications).

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
    * [Gradle](#gradle)
- [Using the SDK](#using-the-sdk)
- [Questions](#questions)
- [Issues](#issues)
- [Open source @ IBM](#open-source--ibm)
- [Contributing](#contributing)
- [License](#license)

<!-- tocstop -->

## Overview

The IBM Cloud Event Notifications Service Huawei Android destination SDK allows developers to register for Huawei destination of Event Notifications service in IBM cloud.

Service Name | Artifact Coordinates
--- | ---
[Event Notifications Service](https://cloud.ibm.com/apidocs/event-notifications) | com.ibm.cloud:eventnotifications-huawei-android:1.1.0

## Prerequisites

[ibm-cloud-onboarding]: https://cloud.ibm.com/registration

* An [IBM Cloud][ibm-cloud-onboarding] account.
* An Event Notifications Instance
* An IAM API key to allow the SDK to access your account. Create one [here](https://cloud.ibm.com/iam/apikeys).

## Installation
The current version of this SDK is: 1.1.0

Each service's artifact coordinates are listed in the table above.

The project artifacts are published on the public [Maven Central](https://repo1.maven.org/maven2/)
artifact repository. This is the default public repository used by maven when searching for dependencies.
To use this repository within a gradle build, please see
[this link](https://docs.gradle.org/current/userguide/declaring_repositories.html).

To use the Event Notifications Huawei Android destination SDK, define a dependency that contains the artifact coordinates (group id, artifact id and version) for the service, like this:


### Gradle
```gradle
compile 'com.ibm.cloud:eventnotifications-huawei-android:1.1.0'
```

## Using the SDK

SDK Methods to consume

- [Installation](#installation)
- [Initialize SDK](#initialize-sdk)
    - [Include SDK with Gradle](#include-sdk-with-gradle)
        - [Initialize SDK](#initialize-sdk)
- [Register for notifications](#register-for-notifications)
    - [Receiving notifications](#receiving-notifications)
    - [Unregistering from notifications](#unregistering-from-notifications)
- [Event Notifications destination tags subscriptions](#event-notifications-destination-tags-subscriptions)
    - [Subscribe to tags](#subscribe-to-tags)
    - [Retrieve subscribed tags](#retrieve-subscribed-tags)
    - [Unsubscribe from tags](#unsubscribe-from-tags)
- [Notification options](#notification-options)
    - [Adding custom DeviceId for registration](#adding-custom-deviceid-for-registration)
    - [Sending Delivery Status for notifications](#sending-delivery-status-for-notifications)
## Installation

### Include SDK with Gradle

Configure the Module level `build.gradle` and Project level `build.gradle` files.

1. Add the following dependencies to your Project level `build.gradle` file.

   ```groovy
   buildscript {
   	repositories {
   	        google()
                   mavenCentral()
   	}
   	dependencies {
   	  classpath "com.android.tools.build:gradle:8.0.2"
      classpath 'com.huawei.agconnect:agcp:1.6.0.300'
   	}
   }
   allprojects {
        repositories {
   	  google()
         mavenCentral()
   	}
   }
   ```

2. Add SDK dependency to your Module level `build.gradle` file.

   ```groovy
   dependencies {
       ........
       implementation 'com.huawei.hms:push:6.9.0.300'
       implementation 'com.ibm.cloud:eventnotifications-huawei-android:1.1.0'
       .......
   }
   ```
   >**Note**: Use the latest build tools (API 33).


3. Add the AppGallery Connect plugin configuration

   ```
   plugins {
    id 'com.huawei.agconnect'
    }
   ```

4. Configure the `AndroidManifest.xml` file. Add the following permissions inside application's `AndroidManifest.xml` file.

    ```
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    ```

5. Add the `agconnect-services.json` in Android application module root directory. For more information on how to add this file, see [Setup the SDK on HMS](https://developer.huawei.com/consumer/en/codelab/HMSPushKit/index.html#3).

6. You need to register service in application in the AndroidManifest.xml file to receive data messages or obtain tokens. service inherits from the HmsMessageService class and implements the methods in the class. The following uses the service with the DemoHmsMessageService class name as an example. You can customize the class name as needed.
    ```
    <manifest ...>
        ...
        <application ...>
            <service android:name=".DemoHmsMessageService" android:exported="false">
                <intent-filter>
                    <action android:name="com.huawei.push.action.MESSAGING_EVENT"/>
                </intent-filter>
            </service>
        </application>
        ...
    </manifest>
    ```
### Initialize SDK
Initialize the sdk to connect with your Event Notifications service instance.

```java
import com.ibm.cloud.eventnotifications.destination.android.ENPush;

String instanceGUID = "<instance_guid>>";
String destinationID = "<instance_destination_id>";
String apiKey = "<instance_apikey>";

ENPush enPush = ENPush.getInstance();
enPush.setCloudRegion(ENPush.REGION_US_SOUTH); // Set your region

enPush.initialize(getApplicationContext(),instanceGUID,destinationID, apiKey);
```
- region : Region of the Event Notifications Instance.
- `ENPush.REGION_US_SOUTH`
- `ENPush.REGION_UK`
- `ENPush.REGION_SYDNEY`
- `ENPush.REGION_FRANKFURT`
- `ENPush.REGION_MADRID`
- `ENPush.REGION_BNPP`
- `ENPush.REGION_OSAKA`
- `ENPush.REGION_TOKYO`
- `ENPush.REGION_TORONTO`
- `ENPush.REGION_SAO_PAULO`


## Register for notifications

Use the `ENPush.registerDevice()` API to register the device with Huawei destination in Event 
Notifications service.

The following options are supported:

- Register without userName: method will accept one parameter `deviceToken` which is received from 
HMS core.

  ```java
  //Register Android devices
  enPush.registerDevice("deviceToken", new ENPushResponseListener<String>() {
      
      @Override
      public void onSuccess(String deviceId) {
         //handle successful device registration here
      }

      @Override
      public void onFailure(ENPushException ex) {
         //handle failure in device registration here
      }
  });
  ```

- Register with UserName. For `userName` based notification, the register method will accept 2 parameters - `userName` and `deviceToken`'.

  ```java
  // Register the device to Event Notifications
  enPush.registerDeviceWithUserName("userName", "deviceToken", new ENPushResponseListener<String>() {
      
      @Override	
      public void onSuccess(String deviceId) {
          //handle successful device registration here
      }

      @Override	
      public void onFailure(ENPushException ex) {
          //handle failure in device registration here
      }
  });
  ```
The userName is used to pass the unique userName value for registering for Event notifications.
The deviceToken is used to pass the device token received from HMS Core for registering for Event notifications.
### Receiving notifications

To recieve notification when app is in foreground, implement `onMessageReceived` of `HmsMessageService`. Refer the [sample code](https://developer.huawei.com/consumer/en/doc/development/HMS-Plugin-Guides-V1/receivedatamessage-0000001050136502-V1) for example usage.


### Unregistering from notifications

Use the following code snippets to un-register from Event Notifications.
```java
enPush.unregister(new ENPushResponseListener<String>() {
	
	@Override
	public void onSuccess(String s) {
		// Handle success
	}

	@Override
	public void onFailure(ENPushException e) {
		// Handle Failure
	}
});
```
>**Note**: To unregister from the `UserName` based registration, you have to call the registration method. See the `Register without userId option` in [Register for notifications](#register-for-notifications).

## Event Notifications destination tags subscriptions

### Subscribe to tags

The `subscribe` API will subscribe the device for a given tag. After the device is subscribed to a particular tag, the device can receive notifications that are sent for that tag.

Add the following code snippet to your Android mobile application to subscribe to a list of tags.

```java
// Subscribe to the given tag
enPush.subscribe(tagName, new ENPushResponseListener<String>() {
	
	@Override
	public void onSuccess(String arg) {
		System.out.println("Succesfully Subscribed to: "+ arg);
	}

	@Override
	public void onFailure(ENPushException ex) {
		System.out.println("Error subscribing to Tag1.." + ex.getMessage());
	}
});
```

### Retrieve subscribed tags

The `getSubscriptions` API will return the list of tags to which the device is subscribed. Use the following code snippets in the mobile application to get the subscription list.

```java
// Get a list of tags that to which the device is subscribed.
enPush.getSubscriptions(new ENPushResponseListener<List<String>>() {
	
	@Override
	public void onSuccess(List<String> tags) {
		System.out.println("Subscribed tags are: "+tags);
	}

	@Override
	public void onFailure(ENPushException ex) {
		System.out.println("Error getting subscriptions.. " + ex.getMessage());
	}
})
```

### Unsubscribe from tags

The `unsubscribeFromTags` API will remove the device subscription from the list tags. Use the following code snippets to allow your devices to get unsubscribe from a tag.

```java
// unsubscibe from the given tag ,that to which the device is subscribed.
push.unsubscribe(tagName, new ENPushResponseListener<String>() {
	
	@Override
	public void onSuccess(String s) {
		System.out.println("Successfully unsubscribed from tag . "+ tag);
	}

	@Override
	public void onFailure(ENPushException e) {
		System.out.println("Error while unsubscribing from tags. "+ e.getMessage());
	}	
});
```

## Notification options

### Adding custom DeviceId for registration

To send `DeviceId` use the `setDeviceId` method of `ENPushNotificationOptions` class.

```java
	ENPushNotificationOptions options = new ENPushNotificationOptions();
	options.setDeviceid("YOUR_DEVICE_ID");
```

>**Note**: Remember to keep custom DeviceId `unique` for each device.

### Sending Delivery Status for notifications

You can send notification status (SEEN/OPEN) back to Event Notifiation service, implement sendStatusEvent function to use this.

Based on when notification was received by intercepting the notification, you can send back SEEN and when its opened you can send back OPEN.

To use this function example is given below

For status open -
ENPush.getInstance().sendStatusEvent("en_nid", ENStatus.OPEN, listener);

For status seen -
ENPush.getInstance().sendStatusEvent("en_nid", ENStatus.SEEN, listener);

## Multidex support prior to Android 5.0
Versions of the platform prior to Android 5.0 (API level 21) use the Dalvik runtime for executing app code.
1. Add the following in your gradle file,

```groovy

android {
...
   defaultConfig{
    ....
      multiDexEnabled true
    ....
   }
...
}
...
dependencies {
  .....
  compile 'com.android.support:multidex:1.0.1'
  ....
}
```
2. In the `manifest.xml` file add teh following,
   A
```xml
<application 
    android:name="android.support.multidex.MultiDexApplication"
```


## Questions

If you are having difficulties using this SDK or have a question about the IBM Cloud services,
please ask a question at
[Stack Overflow](http://stackoverflow.com/questions/ask?tags=ibm-cloud).

## Issues
If you encounter an issue with the project, you are welcome to submit a
[bug report](https://github.com/IBM/event-notifications-huawei-android-sdk/issues).
Before that, please search for similar issues. It's possible that someone has already reported the problem.

## Open source @ IBM
Find more open source projects on the [IBM Github Page](http://ibm.github.io/)

## Contributing
See [CONTRIBUTING](CONTRIBUTING.md).

## License

The IBM Cloud Event Notifications Service Android destination SDK is released under the Apache 2.0 license.
The license's full text can be found in [LICENSE](LICENSE).
