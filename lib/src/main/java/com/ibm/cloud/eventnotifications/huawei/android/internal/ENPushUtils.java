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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.ibm.cloud.eventnotifications.huawei.android.ENPush;

import java.util.Iterator;
import java.util.Set;

/**
 * Utils class for resource and storage actions.
 */
public class ENPushUtils extends Activity {

	private static final String LOG_CAT = ENPush.class.getName();

	/**
	 * Get package name
	 * @param context app context
	 * @return package name
	 */
	public static String getIntentPrefix(Context context) {
		return context.getPackageName();
	}

	/**
	 * Get the resource id
	 * @param context app context
	 * @param resourceCategory resource category
	 * @param resourceName resource name
	 * @return resource id
	 */
	public static int getResourceId(Context context, String resourceCategory,
			String resourceName) {
		int resourceId = -1;
		try {
			@SuppressWarnings("rawtypes")
			Class[] classes = Class.forName(context.getPackageName() + ".R")
					.getDeclaredClasses();
			for (int i = 0; i < classes.length; i++) {
				if (classes[i].getSimpleName().equals(resourceCategory)) {
					resourceId = classes[i].getField(resourceName).getInt(null);
					break;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to find resource R."
					+ resourceCategory + "." + resourceName, e);
		}

		return resourceId;
	}

	/**
	 * Get value from shared preferences
	 * @param appContext app context
	 * @param applicationId application guid of Event Notifications
	 * @param valueType Key for the shared preferences
	 * @return value from the shared preferences
	 */
	public static String getContentFromSharedPreferences(Context appContext,
			String applicationId, String valueType) {
		SharedPreferences sharedPreferences = appContext.getSharedPreferences(
				ENPush.PREFS_NAME, 0);
		return sharedPreferences.getString(applicationId + valueType, null);
	}

	/**
	 * Get value from the shared preferences
	 * @param appContext app context
	 * @param valueType Key for the shared preferences
	 * @return value from the shared preferences
	 */
	public static String getContentFromSharedPreferences(Context appContext, String valueType) {
		SharedPreferences sharedPreferences = appContext.getSharedPreferences(
				ENPush.PREFS_NAME, 0);
		return sharedPreferences.getString(valueType, null);
	}

	/**
	 * Store value to shared preferences
	 * @param appContext app context
	 * @param applicationId application guid of Event Notifications
	 * @param valueType Key for the shared preferences
	 * @param value Value to store in the shared preferences
	 */
	public static void storeContentInSharedPreferences(Context appContext,
			String applicationId, String valueType, String value) {
		SharedPreferences sharedPreferences = appContext.getSharedPreferences(
				ENPush.PREFS_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(applicationId + valueType, value);
		editor.commit();
	}

	/**
	 * Remove value form shared preferences
	 * @param sharedPreferences shared preferences
	 * @param key Key for the shared preferences
	 */
	public static void removeContentFromSharedPreferences(SharedPreferences sharedPreferences, String key ) {

		Editor editor = sharedPreferences.edit();
		String msg = sharedPreferences.getString(key, null);
		editor.remove(key);
		editor.commit();
	}

	// Store the key, value in SharedPreferences
	public static void storeContentInSharedPreferences(SharedPreferences sharedPreferences, String key, String value ) {
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	// Store the key, value in SharedPreferences
	public static void storeContentInSharedPreferences(SharedPreferences sharedPreferences, String key, int value ) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void dumpIntent(Intent i) {

		Bundle bundle = i.getExtras();
		if (bundle != null) {
			Set<String> keys = bundle.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
			}
		}
	}
	public static Boolean validateString(String object) {
		if (object == null || object.isEmpty() || object.equals("")) {
			return false;
		} else {
			return true;
		}
	}
    
    
    private static void verifyPermission(Context context, String paramString) {
        if (!isPermissionGranted(context, paramString)) {
            throw new IllegalStateException("Android Manifest Error: Missing permission in manifest: " + paramString);
        }
    }
    
    private static boolean isPermissionGranted(Context context, String paramString) {
        PackageManager localPackageManager = context.getPackageManager();
        if (localPackageManager != null) {
            return localPackageManager.checkPermission(paramString, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

	public static final String APPLICATION_ID = "APPLICATION_ID";
}
