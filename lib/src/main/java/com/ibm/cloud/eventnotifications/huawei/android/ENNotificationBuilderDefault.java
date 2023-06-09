/**
 * (C) Copyright IBM Corp. 2021.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ibm.cloud.eventnotifications.huawei.android;

import static com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageKeys.CANCEL_IBM_PUSH_NOTIFICATION;
import static com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageKeys.IBM_PUSH_NOTIFICATION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.ibm.cloud.eventnotifications.huawei.android.internal.ENInternalPushMessage;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageConstantInterface;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENMessageDefaultConstant;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushConstants;
import com.ibm.cloud.eventnotifications.huawei.android.internal.ENPushUtils;
import com.ibm.cloud.eventnotifications.huawei.android.internal.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Class for handling the Notification object builder from the remote payload. This class implements ENNotificationBuilderInterface.
 */
class ENNotificationBuilderDefault implements ENNotificationBuilderInterface {

    private static Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ENNotificationBuilderDefault.class.getSimpleName());

    public static ENMessageManagerInterface enMessageManager = new ENMessageManagerDefault();
    public static ENMessageConstantInterface enMessageConstants = new ENMessageDefaultConstant();
    public static ENResourceManagerInterface enResourceManager = new ENResourceManagerDefault();
    public static ENMessageNotifierInterface enMessageNotifier = new ENMessageNotifierDefault();

    /**
     * Build NotificationCompatBuilder object
     * @param context app current context
     * @param message ENInternalPushMessage object
     * @return returns NotificationCompatBuilder object
     */
    @Override
    public NotificationCompat.Builder buildNotificationCompatBuilder(Context context, ENInternalPushMessage message) {

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String id = context.getPackageName();
            NotificationChannel channel = message.getChannel(context, mNotificationManager);
            if (channel != null) {
                mNotificationManager.createNotificationChannel(channel);
            } else {

                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                channel = new NotificationChannel(id, enResourceManager.getNotificationDefaultTitle(context), importance);
                channel.enableLights(true);
                mNotificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(context, id);
            builder.setChannelId(channel.getId());


        } else {
            builder = new NotificationCompat.Builder(context);
        }

        return builder;
    }

    /**
     * Method to handle the FCM notifications
     * @param context app current context
     * @param notification Notification object
     * @param notificationId notification ID
     */
    @Override
    public void onUnhandled(Context context, ENInternalPushMessage notification, int notificationId) {

        ENInternalPushMessage message = notification;


        message.setNotificationId(notificationId);
        enMessageManager.saveInSharedPreferences(context.getApplicationContext(), message);

        if (message.getMessageType() != null && message.getMessageType().equalsIgnoreCase(ENPushConstants.MESSAGE_TYPE)) {
            logger.info("ENNotificationBuilderDefault:onUnhandled() - Received silent push notification");
        } else {
            Intent intent = new Intent(ENPushUtils.getIntentPrefix(context)
                    + enMessageConstants.getStringForKey(IBM_PUSH_NOTIFICATION));
            intent.setClass(context, ENPushNotificationHandler.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra(ENPushConstants.NOTIFICATIONID, message.getNotificationId());

            generateNotification(context, message.getAlert(),
                    enResourceManager.getNotificationTitle(context, message.getAndroidTitle()),
                    enResourceManager.getCustomNotificationIcon(context, message.getIcon()), intent, enMessageManager.getNotificationSound(context, message), notificationId, message);
        }
    }

    /**
     * Generate notification when app is in the foreground
     * @param context app current context
     * @param alert message alert value
     * @param title message title value
     * @param icon message icon value
     * @param intent intent for the message
     * @param sound message sound value
     * @param notificationId message notification ID
     * @param message message object.
     */
    @Override
    public void generateNotification(Context context, String alert, String title, int icon, Intent intent, String sound, int notificationId, ENInternalPushMessage message) {

        int androidSDKVersion = Build.VERSION.SDK_INT;
        long when = System.currentTimeMillis();
        Notification notification = null;
        NotificationCompat.Builder builder = buildNotificationCompatBuilder(context, message);

        Intent deleteIntent = new Intent(ENPushUtils.getIntentPrefix(context)
                + enMessageConstants.getStringForKey(CANCEL_IBM_PUSH_NOTIFICATION));
        deleteIntent.putExtra(ENPushConstants.ID, message.getId());
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, notificationId, deleteIntent, 0);

        if (message.getGcmStyle() != null && androidSDKVersion > 15) {
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            try {
                JSONObject gcmStyleObject = new JSONObject(message.getGcmStyle());
                String type = gcmStyleObject.getString(ENPushConstants.TYPE);

                if (type != null && type.equalsIgnoreCase(ENPushConstants.PICTURE_NOTIFICATION)) {
                    Bitmap remote_picture = null;
                    NotificationCompat.BigPictureStyle notificationStyle = new NotificationCompat.BigPictureStyle();
                    notificationStyle.setBigContentTitle(alert);
                    notificationStyle.setSummaryText(gcmStyleObject.getString(ENPushConstants.TITLE));

                    remote_picture = getBitMapBigPictureNotification(gcmStyleObject);

                    if (remote_picture != null) {
                        notificationStyle.bigPicture(remote_picture);
                    }

                    builder.setSmallIcon(icon)
                            .setLargeIcon(remote_picture)
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentIntent(PendingIntent
                                    .getActivity(context, notificationId, intent,
                                            PendingIntent.FLAG_UPDATE_CURRENT))
                            .setDeleteIntent(deletePendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(alert)
                            .setStyle(notificationStyle);
                } else if (type != null && type.equalsIgnoreCase(ENPushConstants.BIGTEXT_NOTIFICATION)) {
                    NotificationCompat.BigTextStyle notificationStyle = new NotificationCompat.BigTextStyle();
                    notificationStyle.setBigContentTitle(alert);
                    notificationStyle.setSummaryText(gcmStyleObject.getString(ENPushConstants.TITLE));
                    notificationStyle.bigText(gcmStyleObject.getString(ENPushConstants.TEXT));

                    builder.setSmallIcon(icon)
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentIntent(PendingIntent
                                    .getActivity(context, notificationId, intent,
                                            PendingIntent.FLAG_UPDATE_CURRENT))
                            .setDeleteIntent(deletePendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(alert)
                            .setStyle(notificationStyle);

                } else if (type != null && type.equalsIgnoreCase(ENPushConstants.INBOX_NOTIFICATION)) {
                    NotificationCompat.InboxStyle notificationStyle = new NotificationCompat.InboxStyle();
                    notificationStyle.setBigContentTitle(alert);
                    notificationStyle.setSummaryText(gcmStyleObject.getString(ENPushConstants.TITLE));

                    String lines = gcmStyleObject.getString(ENPushConstants.LINES).replaceAll("\\[", "").replaceAll("\\]", "");
                    String[] lineArray = lines.split(",");

                    for (String line : lineArray) {
                        notificationStyle.addLine(line);
                    }

                    builder.setSmallIcon(icon)

                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentIntent(PendingIntent
                                    .getActivity(context, notificationId, intent,
                                            PendingIntent.FLAG_UPDATE_CURRENT))
                            .setDeleteIntent(deletePendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(alert)
                            .setStyle(notificationStyle);
                }

                this.setNotificationActions(context, intent, notificationId, message.getCategory(), builder);
                notification = builder.build();

                notification = setLights(notification, message);

                notification.flags = Notification.FLAG_AUTO_CANCEL;
                enMessageNotifier.notify(notificationManager, notification, notificationId);
            } catch (JSONException e) {
                logger.error("ENNotificationBuilderDefault:generateNotification() - Error while parsing JSON.");
            }

        } else {
            if (androidSDKVersion > 10) {
                builder.setContentIntent(PendingIntent
                        .getActivity(context, notificationId, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                        .setDeleteIntent(deletePendingIntent)
                        .setSmallIcon(icon).setTicker(alert).setWhen(when)
                        .setAutoCancel(true).setContentTitle(title)
                        .setContentText(alert).setSound(enResourceManager.getNotificationSoundUri(context, sound));

                if (androidSDKVersion > 15) {
                    int priority = enMessageManager.getPriorityOfMessage(context, message);
                    builder.setPriority(priority);

                    this.setNotificationActions(context, intent, notificationId, message.getCategory(), builder);
                    notification = builder.build();
                }

                if (androidSDKVersion > 19) {
                    //As new material theme is very light, the icon is not shown clearly
                    //hence setting the background of icon to black
                    builder.setColor(Color.BLACK);
                    Boolean isBridgeSet = message.getBridge();
                    if (!isBridgeSet) {
                        // show notification only on current device.
                        builder.setLocalOnly(true);
                    }

                    notification = builder.build();
                    int receivedVisibility = 1;
                    String visibility = message.getVisibility();
                    if (visibility != null && visibility.equalsIgnoreCase(ENPushConstants.VISIBILITY_PRIVATE)) {
                        receivedVisibility = 0;
                    }
                    if (receivedVisibility == Notification.VISIBILITY_PRIVATE && message.getRedact() != null) {
                        builder.setContentIntent(PendingIntent
                                .getActivity(context, notificationId, intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT))
                                .setSmallIcon(icon).setTicker(alert).setWhen(when)
                                .setAutoCancel(true).setContentTitle(title)
                                .setContentText(message.getRedact()).setSound(enResourceManager.getNotificationSoundUri(context, sound));

                        notification.publicVersion = builder.build();
                    }
                }

                if (androidSDKVersion > 21) {
                    String setPriority = message.getPriority();
                    if (setPriority != null && setPriority.equalsIgnoreCase(ENPushConstants.PRIORITY_MAX)) {
                        //heads-up notification
                        builder.setContentText(alert)
                                .setFullScreenIntent(PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT), true);
                        notification = builder.build();
                    }
                }

            } else {
                notification = builder.setContentIntent(PendingIntent
                        .getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setDeleteIntent(deletePendingIntent)
                        .setSmallIcon(icon).setTicker(alert).setWhen(when)
                        .setAutoCancel(true).setContentTitle(title)
                        .setContentText(alert).setSound(enResourceManager.getNotificationSoundUri(context, sound))
                        .build();
            }

            notification = setLights(notification, message);

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            enMessageNotifier.notify(notificationManager, notification, notificationId);
        }

    }

    /**
     * Set the lights for Notification object.
     * @param notification Notification object
     * @param message ENInternalPushMessage object
     * @return Notification object
     */
    @Override
    public Notification setLights(Notification notification, ENInternalPushMessage message) {
        if (message.getLights() != null) {
            try {
                JSONObject lightsObject = new JSONObject(message.getLights());
                String ledARGB = lightsObject.getString(ENPushConstants.LEDARGB);
                if (ledARGB != null && ledARGB.equalsIgnoreCase("black")) {
                    notification.ledARGB = Color.BLACK;
                } else if (ledARGB.equalsIgnoreCase("darkgray")) {
                    notification.ledARGB = Color.DKGRAY;
                } else if (ledARGB.equalsIgnoreCase("gray")) {
                    notification.ledARGB = Color.GRAY;
                } else if (ledARGB.equalsIgnoreCase("lightgray")) {
                    notification.ledARGB = Color.LTGRAY;
                } else if (ledARGB.equalsIgnoreCase("white")) {

                    notification.ledARGB = Color.WHITE;
                } else if (ledARGB.equalsIgnoreCase("red")) {
                    notification.ledARGB = Color.RED;
                } else if (ledARGB.equalsIgnoreCase("green")) {
                    notification.ledARGB = Color.GREEN;
                } else if (ledARGB.equalsIgnoreCase("blue")) {
                    notification.ledARGB = Color.BLUE;
                } else if (ledARGB.equalsIgnoreCase("yellow")) {
                    notification.ledARGB = Color.YELLOW;
                } else if (ledARGB.equalsIgnoreCase("cyan")) {
                    notification.ledARGB = Color.CYAN;
                } else if (ledARGB.equalsIgnoreCase("magenta")) {
                    notification.ledARGB = Color.MAGENTA;
                } else if (ledARGB.equalsIgnoreCase("transparent")) {
                    notification.ledARGB = Color.TRANSPARENT;
                }
                int ledOnMS = lightsObject.getInt(ENPushConstants.LEDONMS);
                int ledOffMS = lightsObject.getInt(ENPushConstants.LEDOFFMS);

                if (ledOnMS != 0 && ledOffMS != 0) {
                    notification.ledOnMS = ledOnMS;
                    notification.ledOffMS = ledOffMS;
                    notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                }
            } catch (Exception e) {
                logger.error("ENNotificationBuilderDefault:setLights() - Error while parsing JSON");
            }
        } else {
            notification.defaults |= Notification.DEFAULT_LIGHTS;
        }

        return notification;
    }

    /**
     * Dismiss the Notification
     * @param context app current context
     * @param nid message ID
     */
    @Override
    public void dismissNotification(Context context, String nid) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(ENPush.PREFS_NAME, Context.MODE_PRIVATE);
        int countOfStoredMessages = sharedPreferences.getInt(ENPush.PREFS_NOTIFICATION_COUNT, 0);

        if (countOfStoredMessages > 0) {
            for (int index = 1; index <= countOfStoredMessages; index++) {

                String key = ENPush.PREFS_NOTIFICATION_MSG + index;
                try {
                    String msg = sharedPreferences.getString(key, null);
                    if (msg != null) {
                        JSONObject messageObject = new JSONObject(msg);
                        if (messageObject != null && !messageObject.isNull(ENPushConstants.NID)) {
                            String id = messageObject.getString(ENPushConstants.NID);
                            if (id != null && id.equals(nid)) {
                                ENPushUtils.removeContentFromSharedPreferences(sharedPreferences, key);
                                ENPushUtils.storeContentInSharedPreferences(sharedPreferences, ENPush.PREFS_NOTIFICATION_COUNT, countOfStoredMessages - 1);
                                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                //mNotificationManager.cancel(messageObject.getInt(ENPushConstants.NOTIFICATIONID));
                                enMessageNotifier.cancel(mNotificationManager, messageObject.getInt(ENPushConstants.NOTIFICATIONID));
                            }
                        }
                    }
                } catch (JSONException e) {
                    logger.error("ENNotificationBuilderDefault: dismissNotification() - Failed to dismiss notification.");
                }
            }
        }
    }

    /**
     * Get the image from url
     * @param gcmStyleObject JSONObject of FCM style.
     * @return return bitmap of the image
     */
    @Override
    public Bitmap getBitMapBigPictureNotification(JSONObject gcmStyleObject) {

        Bitmap remote_picture = null;
        try {
            remote_picture = new BitMapBigPictureNotificationDefault().execute(gcmStyleObject.getString(ENPushConstants.URL)).get();
        } catch (Exception e) {
            logger.error("ENNotificationBuilderDefault:getBitMapBigPictureNotification() - Error while fetching image file.");
        }

        return remote_picture;
    }

    /**
     * Set the Notification actions.
     * @param context app current context
     * @param intent Intent for the notification
     * @param notificationId Message ID
     * @param messageCategory Message category value
     * @param mBuilder NotificationCompat.Builder object
     */
    @Override
    public void setNotificationActions(Context context, Intent intent, int notificationId, String messageCategory, NotificationCompat.Builder mBuilder) {

        ENPushNotificationOptions options = ENPush.getInstance().getNotificationOptions(context);
        if (options != null && options.getInteractiveNotificationCategories() != null) {

            List<ENPushNotificationCategory> categoryList = options.getInteractiveNotificationCategories();

            for (ENPushNotificationCategory category : categoryList) {
                if (category.getCategoryName().equals(messageCategory)) {
                    for (ENPushNotificationButton newButton : category.getButtons()) {
                        intent.setAction(newButton.getButtonName());
                        mBuilder.addAction(enResourceManager.getResourceIdForCustomIcon(context, ENPushConstants.DRAWABLE, newButton.getIcon()), newButton.getLabel(),
                                PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                    }

                }

            }
        }
    }

    /**
     * Class for fetching the image from url.
     */
    class BitMapBigPictureNotificationDefault extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                return null;
            }
        }

    }
}


