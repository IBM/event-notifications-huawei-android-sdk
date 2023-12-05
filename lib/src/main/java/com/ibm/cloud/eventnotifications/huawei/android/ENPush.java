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

import static com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageKeys.GCM_EXTRA_MESSAGE;
import static com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageKeys.GCM_MESSAGE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ibm.cloud.eventnotifications.huawei.android.internal.BaseDeviceIdentity;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENException;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushConstants;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushUrlBuilder;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushUtils;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENStatus;
import com.ibm.cloud.eventnotifications.huawei.android.internal.Logger;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ServiceImpl;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCall;
import com.ibm.cloud.sdk.core.http.ServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <class>ENPush</class> provides methods required by an android application to
 * be able to receive Event Notifications push notifications.
 */
public class ENPush {

  public final static String REGION_US_SOUTH = "us-south";
  public final static String REGION_UK = "eu-gb";
  public final static String REGION_SYDNEY = "au-syd";
  public final static String REGION_FRANKFURT = "eu-de";
  public final static String REGION_MADRID = "eu-es";
  public final static String REGION_BNPP = "eu-fr2";

  public static final String PREFS_NAME = "com.ibm.cloud.eventnotifications.huawei.android";
  static final String PREFS_NOTIFICATION_MSG = "LatestNotificationMsg";
  static final String PREFS_NOTIFICATION_COUNT = "NotificationCount";

  static final int INITIALISATION_ERROR = 403;
  static final String PREFS_MESSAGES_OPTIONS = "MessageOptions";
  static  String enPushActionName = null;

  private static ENPush instance;
  private static Context appContext = null;

  private String deviceId = null;
  private String deviceToken = null;
  private String regId = null;
  private int statusCode = 0;

  private String destinationId = null;
  private String instanceId = null;
  private String apiKey;

  private String region = null;
  private BaseDeviceIdentity deviceIdentity = null;

  private boolean isInitialized = false;

  private List<ENInternalPushMessage> pending = new ArrayList<ENInternalPushMessage>();

  private ENPushNotificationListener notificationListener = null;
  private ENPushResponseListener<String> registerResponseListener = null;

  private boolean onMessageReceiverRegistered = false;
  private boolean isNewRegistration = false;
  private boolean hasRegisterParametersChanged = false;
  public static ENPushNotificationOptions options = null;
  private boolean isFromNotificationBar = false;
  private ENInternalPushMessage messageFromBar = null;
  private Intent pushNotificationIntent = null;
  protected static Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENPush.class.getSimpleName());
  private boolean isPrivateEndPoint= false;
  public static String overrideServerHost = null;

  private JSONObject templateJson = new JSONObject();
  private String tagSubscriptionID = null;
  AndroidContextInterface androidContextInterface = new AndroidContextDefault();

  public synchronized static ENPush getInstance() {
    if (instance == null) {
      instance = new ENPush();
    }
    return instance;
  }

  /**
   * @exclude
   * @return IBM Cloud region suffix for SDK components to build URLs
   */
  public String getCloudRegionSuffix(){ return region;}

  public void usePrivateEndpoint(boolean isPrivateEndPoint){
    this.isPrivateEndPoint = isPrivateEndPoint;
  }

  public boolean getIsPrivateEndPoint(){
    return this.isPrivateEndPoint;
  }

  public void setCloudRegion( String region){ this.region = region;}

  /**
   * ENPush Initialisation method with instanceGUID and DestinationID.
   * <p/>
   *
   * @param context          this is the Context of the application from getApplicationContext()
   * @param guid          The unique ID of the Event Notifications service instance that the application must connect to.
   * @param destinationID DestinationID from the Event Notifications service.
   * @param apikey API Key from the Event Notifications service.
   */
  public void initialize(Context context, String guid, String destinationID, String apikey) {
    this.initialize(context,guid,destinationID,apikey, null);
  }

  /**
   * ENPush Initialisation method with instanceGUID and DestinationID.
   * <p/>
   *
   * @param context          this is the Context of the application from getApplicationContext()
   * @param guid          The unique ID of the Event Notifications service instance that the application must connect to.
   * @param destinationID DestinationID from the Event Notifications service.
   * @param apikey API Key from the Event Notifications service.
   * @param options - The ENPushNotificationOptions with the default parameters
   *
   */
  public void initialize(Context context, String guid, String destinationID, String apikey, ENPushNotificationOptions options) {

    try {
      if (ENPushUtils.validateString(guid) && ENPushUtils.validateString(destinationID) && ENPushUtils.validateString(apikey)) {
        // Get the applicationId and backend route from core
        this.destinationId = destinationID;
        this.instanceId = guid;
        this.appContext = context.getApplicationContext();
        this.isInitialized = true;
        this.apiKey = apikey;
        androidContextInterface.validateAndroidContext(appContext);

        if (options != null){
          setNotificationOptions(context,options);
          this.regId = options.getDeviceid();
        }
        deviceIdentity = new BaseDeviceIdentity(context);
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences != null){
          ENPushUtils.storeContentInSharedPreferences(sharedPreferences, "instanceId", guid);
          ENPushUtils.storeContentInSharedPreferences(sharedPreferences, "destinationId", destinationID);
          ENPushUtils.storeContentInSharedPreferences(sharedPreferences, "apiKey", apikey);
          ENPushUtils.storeContentInSharedPreferences(sharedPreferences, "isInitialized", "true");
        }
      } else {
        logger.error("ENPush:initialize() - An error occured while initializing ENPush service. Add a valid ClientSecret and Event Notifications service instance ID Value");
        throw new ENPushException("ENPush:initialize() - An error occured while initializing ENPush service. Add a valid ClientSecret and Event Notifications service instance ID Value", INITIALISATION_ERROR);
      }

    } catch (Exception e) {
      logger.error("ENPush:initialize() - An error occured while initializing ENPush service.");
      throw new RuntimeException(e);
    }
  }

  /**
   * Request ENPush to deliver incoming Event Notifications push messages to listener.onReceive()
   * method.
   * <p/>
   * This method is typically called from the onResume() method of the
   * activity that is handling Event Notifications push notifications.
   *
   * @param notificationListener ENPushNotificationListener object whose onReceive() method
   *                             will be called upon receipt of a Event Notifications push message.
   */
  public void listen(ENPushNotificationListener notificationListener) {

    if (!onMessageReceiverRegistered) {
      appContext.registerReceiver(onMessage, new IntentFilter(
              ENPushUtils.getIntentPrefix(appContext) + ENPushIntentService.enMessageConstants.getStringForKey(GCM_MESSAGE)));
      onMessageReceiverRegistered = true;

      this.notificationListener = notificationListener;
      ENPushIntentService.setAppForeground(true);

      boolean gotSavedMessages;

      if (pushNotificationIntent != null) {
        gotSavedMessages = getMessagesFromSharedPreferences(pushNotificationIntent.getIntExtra("notificationId", 0));
        pushNotificationIntent = null;
      } else {
        gotSavedMessages = getMessagesFromSharedPreferences(0);
      }

      if (!isFromNotificationBar) {
        if (gotSavedMessages) {
          dispatchPending();
        }
        cancelAllNotification();
      } else {
        if (messageFromBar != null) {
          isFromNotificationBar = false;
          sendNotificationToListener(messageFromBar);
          cancelNotification(messageFromBar);
          messageFromBar = null;
        }
      }
    } else {
      logger.info("ENPush:listen() - onMessage broadcast listener has already been registered.");
    }
  }

  /**
   * Request ENPush to stop delivering incoming push messages to
   * notificationListener.onReceive() method. After hold(), ENPush will store
   * the latest push message in private shared preference and deliver that
   * message during the next {@link #listen(ENPushNotificationListener)}.
   * <p/>
   * This method is typically called from the onPause() method of the activity
   * that is handling push notifications.
   */
  public void hold() {
    notificationListener = null;
    ENPushIntentService.setAppForeground(false);
    if (onMessageReceiverRegistered) {
      try {
        appContext.unregisterReceiver(onMessage);
      } catch (Exception e) {
        logger.warn("ENPush:hold() - Exception while unregistering receiver. " + e.getMessage());
      }
      onMessageReceiverRegistered = false;
    }
  }

  /**
   * Checks whether push notification is supported.
   *
   * @return true if push is supported, false otherwise.
   */
  public boolean isPushSupported() {
    String version = android.os.Build.VERSION.RELEASE.substring(0, 3);
    return (Double.valueOf(version) >= ENPushConstants.MIN_SUPPORTED_ANDRIOD_VERSION);
  }

  /**x
   * Registers the device for Push notifications with the given alias and
   * consumerId
   *
   * @param listener - Mandatory listener class. When the device is successfully
   *                 registered with Push service the
   *                 {@link ENPushResponseListener}.onSuccess method is called
   *                 with the deviceId. {@link ENPushResponseListener}.onFailure
   *                 method is called otherwise
   * @param userName   -  The UserName for registration.
   */
  public void registerDeviceWithUserName(String userName, String huaweiDeviceToken, ENPushResponseListener<String> listener) {

    if (isInitialized) {
      this.registerResponseListener = listener;
      ENPushIntentService.setAppForeground(true);
      logger.info("ENPush:register() - Retrieving senderId from ENPush server.");
      registerInBackground(userName, huaweiDeviceToken);
    } else {
      logger.error("ENPush:register() - An error occurred while registering for ENPush service. Event Notifications not initialized with call to initialize()");
    }

  }

  /**
   * Registers the device for Push notifications with the given alias and
   * consumerId
   *
   * @param listener - Mandatory listener class. When the device is successfully
   *                 registered with Push service the
   *                 {@link ENPushResponseListener}.onSuccess method is called
   *                 with the deviceId. {@link ENPushResponseListener}.onFailure
   *                 method is called otherwise
   */
  public void registerDevice(String deviceToken, ENPushResponseListener<String> listener) {

    this.registerDeviceWithUserName(null,deviceToken, listener);
  }

  /**
   * Subscribes to the given tag
   *
   * @param tagName  name of the tag
   * @param listener Mandatory listener class. When the subscription is created
   *                 successfully the {@link ENPushResponseListener}.onSuccess
   *                 method is called with the tagName for which subscription is
   *                 created. {@link ENPushResponseListener}.onFailure method is
   *                 called otherwise
   */
  public void subscribe(final String tagName, final ENPushResponseListener<String> listener) {
    ENPushUrlBuilder builder = new ENPushUrlBuilder(instanceId, destinationId);
    if (this.deviceId == null) {
      this.deviceId = ENPushUtils.getContentFromSharedPreferences(appContext, instanceId+ destinationId + ENPushConstants.ID);
    }
    String path = builder.getSubscriptionsUrl();
    logger.debug("ENPush:subscribe() - The tag subscription path is: " + path);

    try {
      JSONObject data = buildSubscription(tagName);

      ServiceCall response =
              this.getServiceImplementation().postData(path, data, new ServiceCallback() {
                @Override
                public void onResponse(Response response) {
                  if (response.getStatusCode() >= ENPushConstants.REQUEST_SUCCESS_200
                          && response.getStatusCode() <= ENPushConstants.REQUEST_SUCCESS_299) {

                    logger.info("ENPush:subscribe() - Tag subscription successfully created.  The response is: " + response.toString());
                    listener.onSuccess(tagName);
                  } else {
                    logger.error("ENPush: Error while getSubscriptions");
                    listener.onFailure(new ENPushException("ENPush: Error while getSubscriptions"));
                  }
                }
                @Override
                public void onFailure(Exception e) {
                  logger.error("ENPush: Error while subscribing");
                  listener.onFailure(new ENPushException(e));
                }
              });
    } catch (Exception e) {
      logger.error("ENPush: Error while subscribing to tags");
      listener.onFailure(new ENPushException("ENPush: Error while subscribing to tags", 400));
    }
  }

  public void getTagSubscriptionId(String tagName, String path, final ENPushResponseListener<String> listener) {
    try {
      ServiceCall response =
              this.getServiceImplementation().getData(path, new ServiceCallback() {
                @Override
                public void onResponse(Response response) {
                  if (response.getStatusCode() >= ENPushConstants.REQUEST_SUCCESS_200
                          && response.getStatusCode() <= ENPushConstants.REQUEST_SUCCESS_299) {

                    try {
                      String tagSubscrID = null;
                      JSONArray tagSubscriptions = (JSONArray) (new JSONObject((String) response.getResult()))
                              .get(ENPushConstants.SUBSCRIPTIONS);
                      int tagsCnt = tagSubscriptions.length();
                      for (int tagsIdx = 0; tagsIdx < tagsCnt; tagsIdx++) {
                        if(tagName.equals(tagSubscriptions.getJSONObject(tagsIdx)
                                .getString(ENPushConstants.TAG_NAME))){
                          tagSubscrID = tagSubscriptions.getJSONObject(tagsIdx)
                                  .getString(ENPushConstants.ID);
                        }
                      }
                      listener.onSuccess(tagSubscrID);
                    } catch (JSONException e) {
                      logger.error("ENPush: getTagSubscriptionId() - Failure while getting Tag subscriptions ID.  Failure response is: " + e.getMessage());
                      listener.onFailure(new ENPushException(e));
                    }
                  } else {
                    logger.error("ENPush: Error while getTagSubscriptionId");
                    listener.onFailure(new ENPushException("ENPush: Error while getTagSubscriptionId"));
                  }
                }

                @Override
                public void onFailure(Exception e) {
                  listener.onFailure(new ENPushException(e));
                }
              });
    } catch (Exception e) {
      logger.error("ENPush: Error while getSubscriptions");
      listener.onFailure(new ENPushException("ENPush: Error while getSubscriptions", 400));
    }
  }

  /**
   * Unsubscribes to the given tag
   *
   * @param tagName  name of the tag
   * @param listener Mandatory listener class. When the subscription is deleted
   *                 successfully the {@link ENPushResponseListener}.onSuccess
   *                 method is called with the tagName for which subscription is
   *                 deleted. {@link ENPushResponseListener}.onFailure method is
   *                 called otherwise
   */
  public void unsubscribe(final String tagName,
                          final ENPushResponseListener<String> listener) {
    ENPushUrlBuilder builder = new ENPushUrlBuilder(instanceId, destinationId);
    if (this.deviceId == null) {
      this.deviceId = ENPushUtils.getContentFromSharedPreferences(appContext, instanceId+ destinationId + ENPushConstants.ID);
    }
    String path = builder.getSubscriptionsUrl(deviceId, null);
    if (path == ENPushUrlBuilder.DEVICE_ID_NULL) {
      listener.onFailure(new ENPushException("The device is not registered yet. Please register device before calling subscriptions API"));
      return;
    }
    logger.debug("ENPush:unsubscribe() - The tag un-subscription path is: " + path);

    getTagSubscriptionId(tagName, path, new ENPushResponseListener<String>() {
      @Override
      public void onSuccess(String response) {
        tagSubscriptionID = response;
        logger.info("ENPush:getTagSubscriptionId() - Get TagSubscription Id is successful");
        if (tagSubscriptionID !=null) {
          try {
            String url = builder.getSubscriptionsUrl(deviceId, tagSubscriptionID);
            getServiceImplementation().delete(url, new ServiceCallback() {
              @Override
              public void onResponse(Response response) {
                if (response.getStatusCode() >= ENPushConstants.REQUEST_SUCCESS_200
                        && response.getStatusCode() <= ENPushConstants.REQUEST_SUCCESS_299) {
                  logger.info("ENPush:unsubscribe() - Tag un-subscription successful.  The response is: " + response.toString());
                  listener.onSuccess(tagName);
                } else {
                  logger.error("ENPush: Error while getSubscriptions");
                  listener.onFailure(new ENPushException("ENPush: Error while getSubscriptions"));
                }
              }

              @Override
              public void onFailure(Exception e) {
                logger.error("ENPush: Error while getSubscriptions");
                listener.onFailure(new ENPushException(e));
              }
            });

          } catch (Exception e) {
            logger.error("ENPush: Error while subscribing to tags");
            listener.onFailure(new ENPushException("ENPush: Error while Unsubscribing to tags", 400));
          }
        }
      }

      @Override
      public void onFailure(ENPushException exception) {
        logger.error("ENPush: Error while getTagSubscriptionId()");
      }
    });
  }

  /**
   * Unregister the device from Push Server
   *
   * @param listener Mandatory listener class. When the subscription is deleted
   *                 successfully the {@link ENPushResponseListener}.onSuccess
   *                 method is called with the tagName for which subscription is
   *                 deleted. {@link ENPushResponseListener}.onFailure method is
   *                 called otherwise
   */
  public void unregister(final ENPushResponseListener<String> listener) {
    ENPushUrlBuilder builder = new ENPushUrlBuilder(instanceId, destinationId);
    if (this.deviceId == null) {
      this.deviceId = ENPushUtils.getContentFromSharedPreferences(appContext, instanceId+ destinationId + ENPushConstants.ID);
    }
    String path = builder.getUnregisterUrl(deviceId);
    logger.debug("ENPush:unregister() - The device unregister url is: " + path);

    try {
      ServiceCall response =
              this.getServiceImplementation().delete(path, new ServiceCallback() {
                @Override
                public void onResponse(Response response) {
                  if (response.getStatusCode() >= ENPushConstants.REQUEST_SUCCESS_200
                          && response.getStatusCode() <= ENPushConstants.REQUEST_SUCCESS_299) {

                    logger.info("ENPush:unregister() - Successfully unregistered device. Response is: " + response.toString());
                    listener.onSuccess("Device Successfully unregistered from receiving push notifications.");
                  } else {
                    logger.error("ENPush: Error while getSubscriptions");
                    listener.onFailure(new ENPushException("ENPush: Error while getSubscriptions"));
                  }
                }

                @Override
                public void onFailure(Exception e) {
                  logger.error("ENPush: Error while getSubscriptions");
                  listener.onFailure(new ENPushException(e));

                }
              });
    } catch (Exception e) {
      logger.error("Response object is " + e.getLocalizedMessage());
      listener.onFailure(new ENPushException(e));
    }
  }


  /**
   * Get the list of tags subscribed to
   *
   * @param listener Mandatory listener class. When the list of tags subscribed to
   *                 are successfully retrieved the {@link ENPushResponseListener}
   *                 .onSuccess method is called with the list of tagNames
   *                 {@link ENPushResponseListener}.onFailure method is called
   *                 otherwise
   */
  public void getSubscriptions(final ENPushResponseListener<List<String>> listener) {

    ENPushUrlBuilder builder = new ENPushUrlBuilder(instanceId, destinationId);
    String path = builder.getSubscriptionsUrl(deviceId, null);

    if (path == ENPushUrlBuilder.DEVICE_ID_NULL) {
      listener.onFailure(new ENPushException("The device is not registered yet. Please register device before calling subscriptions API"));
      return;
    }

    try {
      ServiceCall response =
              this.getServiceImplementation().getData(path, new ServiceCallback() {
                @Override
                public void onResponse(Response response) {

                  if (response.getStatusCode() >= ENPushConstants.REQUEST_SUCCESS_200
                          && response.getStatusCode() <= ENPushConstants.REQUEST_SUCCESS_299) {

                    try {
                      List<String> tagNames = new ArrayList<String>();
                      JSONArray tags = (JSONArray) (new JSONObject((String) response.getResult()))
                              .get(ENPushConstants.SUBSCRIPTIONS);
                      int tagsCnt = tags.length();
                      for (int tagsIdx = 0; tagsIdx < tagsCnt; tagsIdx++) {
                        tagNames.add(tags.getJSONObject(tagsIdx)
                                .getString(ENPushConstants.TAG_NAME));
                      }
                      listener.onSuccess(tagNames);
                    } catch (JSONException e) {
                      logger.error("ENPush: getSubscriptions() - Failure while getting subscriptions.  Failure response is: " + e.getMessage());
                      listener.onFailure(new ENPushException(e));
                    }
                  } else {
                    logger.error("ENPush: Error while getSubscriptions");
                    listener.onFailure(new ENPushException("ENPush: Error while getSubscriptions"));
                  }
                }

                @Override
                public void onFailure(Exception e) {
                  listener.onFailure(new ENPushException(e));
                }
              });
    } catch (Exception e) {
      logger.error("ENPush: Error while getSubscriptions");
      listener.onFailure(new ENPushException("ENPush: Error while getSubscriptions", 400));
    }
  }


  /**
   * Set the default push notification options for notifications.
   *
   * @param context - this is the Context of the application from getApplicationContext()
   * @param options - The ENPushNotificationOptions with the default parameters
   */
  private void setNotificationOptions(Context context, ENPushNotificationOptions options) {

    if (this.appContext == null) {
      this.appContext = context.getApplicationContext();
    }
    this.options = options;
    Gson gson = new Gson();
    String json = gson.toJson(options);
    SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    ENPushUtils.storeContentInSharedPreferences(sharedPreferences, ENPush.PREFS_MESSAGES_OPTIONS, json);
  }

  public ENPushNotificationOptions getNotificationOptions(Context context) {
    if (options != null ) {
      return this.options;
    }else {
      //SharedPreferences sharedPreferences = context.getSharedPreferences(ENPush.PREFS_NAME, Context.MODE_PRIVATE);
      String optionsString = ENPushUtils.getContentFromSharedPreferences(context,ENPush.PREFS_MESSAGES_OPTIONS);
      Gson gson = new Gson();
      ENPushNotificationOptions options = gson.fromJson(optionsString, ENPushNotificationOptions.class);
      return options;
    }
  }

//  @Override
//  public void onNewToken(@NonNull String s) {
//    //super.onNewToken(s);
//    logger.debug("registerInBackground() - " + s);
//  }

  /**
   * Based on the PREFS_NOTIFICATION_COUNT value, fetch all the stored notifications in the same order from
   * the shared preferences and save it onto the list.
   * This method will ensure that the notifications are sent to the Application in the same order in which they arrived.
   */
  public boolean getMessagesFromSharedPreferences(int notificationId) {
    boolean gotMessages = false;
    SharedPreferences sharedPreferences = appContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE);

    int countOfStoredMessages = sharedPreferences.getInt(ENPush.PREFS_NOTIFICATION_COUNT, 0);

    if (countOfStoredMessages > 0) {
      String key = null;
      try {
        Map<String, ?> allEntriesFromSharedPreferences = sharedPreferences.getAll();
        Map<String, String> notificationEntries = new HashMap<String, String>();

        for (Map.Entry<String, ?> entry : allEntriesFromSharedPreferences.entrySet()) {
          String rKey = entry.getKey();
          if (entry.getKey().startsWith(PREFS_NOTIFICATION_MSG)) {
            notificationEntries.put(rKey, entry.getValue().toString());
          }
        }

        for (Map.Entry<String, String> entry : notificationEntries.entrySet()) {
          String nKey = entry.getKey();
          key = nKey;
          String msg = sharedPreferences.getString(nKey, null);

          if (msg != null) {
            gotMessages = true;
            logger.debug("ENPush:getMessagesFromSharedPreferences() - Messages retrieved from shared preferences.");
            ENInternalPushMessage pushMessage = new ENInternalPushMessage(
                    new JSONObject(msg));

            if (notificationId != 0) {
              if (notificationId == pushMessage.getNotificationId()) {
                isFromNotificationBar = true;
                messageFromBar = pushMessage;
                ENPushUtils.removeContentFromSharedPreferences(sharedPreferences, nKey);
                ENPushUtils.storeContentInSharedPreferences(sharedPreferences, ENPush.PREFS_NOTIFICATION_COUNT, countOfStoredMessages - 1);
                break;
              }
            } else {
              synchronized (pending) {
                pending.add(pushMessage);
              }
              ENPushUtils.removeContentFromSharedPreferences(sharedPreferences, nKey);
            }
          }
        }

      } catch (JSONException e) {
        ENPushUtils.removeContentFromSharedPreferences(sharedPreferences, key);
      }
      if (notificationId == 0) {
        ENPushUtils.storeContentInSharedPreferences(sharedPreferences, ENPush.PREFS_NOTIFICATION_COUNT, 0);
      }
    }

    return gotMessages;
  }

  public void setIntent(Intent pushNotificationIntent) {
    this.pushNotificationIntent = pushNotificationIntent;
  }

  public ENPushNotificationListener getNotificationListener() {
    return notificationListener;
  }

  public static void openMainActivityOnNotificationClick(Context ctx) {
    Intent intentToLaunch = ctx.getPackageManager().getLaunchIntentForPackage(ctx.getPackageName());
    enPushActionName = getInstance().pushNotificationIntent.getAction();

    if (intentToLaunch != null) {
      intentToLaunch.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
      ctx.startActivity(intentToLaunch);
    }
  }

  private void registerInBackground(final String userName, String huaweiDeviceToken) {
    if(!ENPushUtils.validateString(huaweiDeviceToken)){
      logger.debug("registerInBackground() - deviceToken should not be empty!");
      registerResponseListener.onFailure((new ENPushException("Device Token is empty!")));
      return;
    }
    deviceToken = huaweiDeviceToken;
    logger.info("ENPush:registerInBackground() - Successfully registered with Huawei Returned deviceToken is: " + deviceToken);
    computeRegId();
    new Thread(new Runnable() {
      @Override
      public void run() {
        registerWithUserName(userName);
      }
    }).start();
  }

  private void computeRegId() {
    logger.debug("ENPush:computeRegId() Computing device's registrationId");

    if (regId == null && deviceIdentity != null) {
      regId = deviceIdentity.getId();
      logger.debug("ENPush:computeRegId() - DeviceId obtained from AuthorizationManager is : " + regId);
    }
  }

  public void sendStatusEvent(String notificationId, ENStatus status, ENPushResponseListener<String> listener){
    String isInitialized = ENPushUtils.getContentFromSharedPreferences(appContext.getApplicationContext(), "isInitialized");
    if("true".equals(isInitialized)) {
      String instanceId = ENPushUtils.getContentFromSharedPreferences(appContext.getApplicationContext(), "instanceId");
      String destinationId = ENPushUtils.getContentFromSharedPreferences(appContext.getApplicationContext(), "destinationId");
      String regId = ENPushUtils.getContentFromSharedPreferences(appContext.getApplicationContext(), instanceId + destinationId + ENPushConstants.ID);
      this.apiKey = ENPushUtils.getContentFromSharedPreferences(appContext.getApplicationContext(), "apiKey");

      ENPushUrlBuilder builder = new ENPushUrlBuilder(instanceId, destinationId);
      String path = builder.getStatusUrl(regId);
      JSONObject data = new JSONObject();
      try {
        data.put(ENPushConstants.NOTIFICATION_ID, notificationId);
        data.put(ENPushConstants.STATUS, status);
        data.put(ENPushConstants.PLATFORM, "G");
      } catch (JSONException e) {
        logger.error("ENPush: sendStatusEvent() - Error while building JSON object.");
        listener.onFailure(new ENPushException(e));
        return;
      }

      ServiceCall call =
              this.getServiceImplementation().postData(path, data, new ServiceCallback() {
                @Override
                public void onResponse(Response response) {
                  try {
                    logger.info("sending status event is successful" + response.getStatusCode());
                    if(listener == null){
                      return;
                    }
                    listener.onSuccess(response.toString());
                  } catch (Exception e) {
                    logger.error("ENPush:sendStatusEvent() - Exception caught while parsing JSON response.");
                    if(listener == null){
                      return;
                    }
                    listener.onFailure(new ENPushException(e));
                  }
                }

                @Override
                public void onFailure(Exception e) {
                  logger.error("ENPush:sendSeenEvent() - Failure");
                  if(listener == null){
                    return;
                  }
                  listener.onFailure(new ENPushException(e));
                }
              });
    }else {
      logger.error("ENPush:sendStatusEvent() - Error while sending status -. Not initialized ENPush");
      if(listener == null){
        return;
      }
      listener.onFailure(new ENPushException("ENPush:sendStatusEvent() - Error while sending status -. Not initialized ENPush"));
    }
  }

  private boolean registerWithUserName(final String userName) {

    if (isInitialized == true) {
      ENPushUrlBuilder builder = new ENPushUrlBuilder(instanceId, destinationId);
      String path = builder.getDeviceIdUrl(regId);
      try {
        ServiceCall call =
                this.getServiceImplementation().getData(path, new ServiceCallback() {
                  @Override
                  public void onResponse(Response response) {
                    if (response.getStatusCode() >= ENPushConstants.REQUEST_SUCCESS_200
                            && response.getStatusCode() <= ENPushConstants.REQUEST_SUCCESS_299) {

                      try {
                        JSONObject map = new JSONObject((String) response.getResult());
                        String retDeviceId = map.getString(ENPushConstants.DEVICE_ID_RES);
                        String retToken = map.getString(ENPushConstants.TOKEN);
                        String userNameFromResponse = map.getString(ENPushConstants.USER_NAME);

                        Boolean hasChange = false;

                        if (!(retDeviceId.equals(regId))
                                || !(retToken.equals(deviceToken))){
                          hasChange = true;
                        }

                        if (ENPushUtils.validateString(userName) && !(userName.equals(userNameFromResponse))) {
                          hasChange = true;
                        }

                        if (hasChange) {
                          deviceId = retDeviceId;
                          ENPushUtils
                                  .storeContentInSharedPreferences(
                                          appContext, instanceId + destinationId,
                                          ENPushConstants.ID, deviceId);

                          hasRegisterParametersChanged = true;
                          updateTokenCallback(deviceToken, userName);
                        } else {
                          deviceId = retDeviceId;
                          ENPushUtils
                                  .storeContentInSharedPreferences(
                                          appContext, instanceId + destinationId,
                                          ENPushConstants.ID, deviceId);
                          registerResponseListener
                                  .onSuccess(retDeviceId);
                        }

                      } catch (Exception e) {
                        ENException.logException(this.getClass().getName(), "registerWithUserName", e);
                        registerResponseListener.onFailure(new ENPushException(e));
                      }
                    } else {
                      isNewRegistration = true;
                      updateTokenCallback(deviceToken, userName);
                    }
                  }

                  @Override
                  public void onFailure(Exception e) {
                    logger.error("Response object is " + e.getLocalizedMessage());
                    isNewRegistration = true;
                    updateTokenCallback(deviceToken, userName);
                  }
                });
      } catch (Exception e) {
        logger.error("Response object is " + e.getLocalizedMessage());
        registerResponseListener.onFailure(new ENPushException(e));
      }
    } else {
      //String error = "Error while registration -. Not initialized ENPush";
      logger.error("ENPush:verifyDeviceRegistrationWithUserName() - Error while registration -. Not initialized ENPush");
      registerResponseListener.onFailure(new ENPushException("ENPush:verifyDeviceRegistrationWithUserName() - Error while registration -. Not initialized ENPush"));
    }

    return true;
  }

  private void updateTokenCallback(String deviceToken, String userName) {
    ENPushUrlBuilder builder = new ENPushUrlBuilder(instanceId, destinationId);
    JSONObject data = buildDevice(userName);
    String path = builder.getDevicesUrl();

    if (isNewRegistration) {
      logger.debug("ENPush:updateTokenCallback() - Device is registering with Event Notifications server for the first time.");

      ServiceCall call =
              this.getServiceImplementation().postData(path, data, new ServiceCallback() {
                @Override
                public void onResponse(Response response) {

                  try {
                    JSONObject map = new JSONObject((String) response.getResult());

                    String retDeviceId = map.getString(ENPushConstants.DEVICE_ID_RES);
                    deviceId = retDeviceId;
                    ENPushUtils
                            .storeContentInSharedPreferences(
                                    appContext, instanceId + destinationId,
                                    ENPushConstants.ID, retDeviceId);
                    isNewRegistration = false;
                    logger.info("ENPush:updateTokenCallback() - Successfully registered device.");
                    registerResponseListener.onSuccess(response.toString());
                  } catch (Exception e) {
                    logger.error("ENPush:updateTokenCallback() - Exception caught while parsing JSON response.");
                    registerResponseListener.onFailure(new ENPushException(e));
                  }
                }

                @Override
                public void onFailure(Exception e) {
                  logger.error("ENPush:updateTokenCallback() - Failure during device registration.");
                  registerResponseListener.onFailure(new ENPushException(e));
                }
              });

    } else if (hasRegisterParametersChanged) {
      logger.debug("EnPush:updateTokenCallback() - Device is already registered. Registration parameters have changed.");
      data = buildDeviceUpdate(userName);
      path = builder.getDeviceIdUrl(deviceId);

      ServiceCall call =
              this.getServiceImplementation().patchData(path, data, new ServiceCallback() {
                @Override
                public void onResponse(Response response) {
                  logger.debug("ENPush:updateTokenCallback() - Device registration successfully updated.");
                  isNewRegistration = false;
                  registerResponseListener.onSuccess(response.toString());
                }

                @Override
                public void onFailure(Exception e) {
                  logger.debug("ENPush:updateTokenCallback() - Failure while updating device registration details.");
                  registerResponseListener.onFailure(new ENPushException(e));
                }
              });
      hasRegisterParametersChanged = false;
    } else {
      registerResponseListener.onSuccess(deviceId);
    }
  }


  private JSONObject buildDevice(String userName) {
    JSONObject device = new JSONObject();
    try {
      device.put(ENPushConstants.ID, regId);
      device.put(ENPushConstants.TOKEN, deviceToken);
      if (ENPushUtils.validateString(userName)) {
        device.put(ENPushConstants.USER_NAME, userName);
      }
      if (registerOptions()) {
        device.put(ENPushConstants.TEMPLATE_OPTIONS, templateJson);
      }

    } catch (JSONException e) {
      logger.error("ENPush: buildDevice() - Error while building device JSON object.");
      throw new RuntimeException(e);
    }

    return device;
  }

  private JSONObject buildDeviceUpdate(String userName) {
    JSONObject device = new JSONObject();
    try {
      device.put(ENPushConstants.TOKEN, deviceToken);
      if (ENPushUtils.validateString(userName)) {
        device.put(ENPushConstants.USER_NAME, userName);
      }
      if (registerOptions()) {
        device.put(ENPushConstants.TEMPLATE_OPTIONS, templateJson);
      }

    } catch (JSONException e) {
      logger.error("ENPush: buildDeviceUpdate() - Error while building device JSON object.");
      throw new RuntimeException(e);
    }

    return device;
  }

  private boolean registerOptions() {

    ENPushNotificationOptions options = ENPush.getInstance().getNotificationOptions(appContext);
    if (options != null && options.getTemplateValues() != null && options.getTemplateValues().length() > 0) {
      templateJson = options.getTemplateValues();
      return true;
    }
    return false;
  }

  private JSONObject buildSubscription(String tagName) {
    JSONObject subscriptionObject = new JSONObject();

    try {
      subscriptionObject.put(ENPushConstants.DEVICE_ID, deviceId);
      subscriptionObject.put(ENPushConstants.TAG_NAME, tagName);
    } catch (JSONException e) {
      logger.error("ENPush: buildSubscription() - Error while building device subscription JSON object.");
      throw new RuntimeException(e);
    }

    return subscriptionObject;
  }

  private void cancelAllNotification() {
    NotificationManager notificationManager = (NotificationManager) appContext
            .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancelAll();
  }

  private void cancelNotification(ENInternalPushMessage pushMessage) {
    NotificationManager notificationManager = (NotificationManager) appContext
            .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancel(pushMessage.getNotificationId());
  }
  private void dispatchPending() {
    while (true) {
      ENInternalPushMessage message = null;
      synchronized (pending) {
        if (pending.size() > 0) {
          message = pending.remove(0);
        }
      }

      if (message == null) {
        break;
      }

      if (notificationListener != null) {
        sendNotificationToListener(message);
      }
    }
  }

  private void sendNotificationToListener(ENInternalPushMessage message) {
    ENSimplePushNotification simpleNotification = new ENSimplePushNotification(
            message);
    simpleNotification.setActionName(enPushActionName);
    notificationListener.onReceive(simpleNotification);
    enPushActionName = null;
  }

  private BroadcastReceiver onMessage = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      logger.debug("ENPush:onMessage() - Successfully received message for dispatching.");
      ENInternalPushMessage message = (ENInternalPushMessage) intent.getParcelableExtra(ENPushIntentService.enMessageConstants.getStringForKey(GCM_EXTRA_MESSAGE));
      synchronized (pending) {
        pending.add(message);
      }

      dispatchPending();
    }
  };

  private ENPushException getException(Response response, Throwable throwable, JSONObject object) {

    String errorString = null;
    statusCode = 0;
    if (response != null) {
      errorString = response.getStatusMessage();
      statusCode = response.getStatusCode();
    } else if (errorString == null && throwable != null) {
      errorString = throwable.toString();
    } else if (errorString == null && object != null) {
      errorString = object.toString();
    }
    return new ENPushException(errorString, statusCode);
  }

  ServiceImpl getServiceImplementation() {

    return ServiceImpl.getInstance(this.apiKey, overrideServerHost);
  }
}


