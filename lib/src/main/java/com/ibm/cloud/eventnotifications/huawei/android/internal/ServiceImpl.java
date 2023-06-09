/**
 * Copyright 2021 IBM Corp. All Rights Reserved.
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.cloud.sdk.core.http.HttpHeaders;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.http.RequestBuilder;
import com.ibm.cloud.sdk.core.http.ResponseConverter;
import com.ibm.cloud.sdk.core.http.ServiceCall;
import com.ibm.cloud.sdk.core.http.ServiceCallback;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.BaseService;
import com.ibm.cloud.sdk.core.util.ResponseConverterUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper class consisting of methods that perform API request/response handling of the Event Notifications SDK
 * by extending the {@link BaseService}.
 */
public class ServiceImpl extends BaseService implements NetworkInterface {
    protected static Logger logger = Logger.getLogger(Logger.INTERNAL_PREFIX + ServiceImpl.class.getSimpleName());
    private static final String DEFAULT_IAM_DEV_STAGE_URL = "iam.test.cloud.ibm.com";
    private static ServiceImpl instance;
    private static IamAuthenticator iamAuthenticator = null;
    private static String apikey = "";
    private static String overrideServerHost = null;


    private ServiceImpl() {}

    /**
     * @param apikey the apikey
     * @param overrideServerHost Server Host. Use for testing purpose.
     * @return instance of {@link ServiceImpl}
     */
    public static ServiceImpl getInstance(String apikey, String overrideServerHost) {
        if (instance == null) {
            instance = init(apikey, overrideServerHost);
        }
        return instance;
    }

    public ServiceImpl(String serviceName, Authenticator authenticator) {
        super(serviceName, authenticator);
    }

    private static ServiceImpl init(String apikey, String overrideServerHost) {
        ServiceImpl.apikey = apikey;
        ServiceImpl.overrideServerHost = overrideServerHost;
        IamAuthenticator iamAuthenticator = ServiceImpl.getIamAuthenticator();
        ServiceImpl service = new ServiceImpl(ENPushConstants.DEFAULT_SERVICE_NAME, iamAuthenticator);
        service.configureService(ENPushConstants.DEFAULT_SERVICE_NAME);
        return service;
    }

    /**
     * Returns the IAM Authenticator, which will be used to authenticate requests to Event Notifications service.
     *
     * @return iam authenticator object
     */
    public static IamAuthenticator getIamAuthenticator() {
        if (iamAuthenticator == null) {
            createIamAuth();
        }
        return iamAuthenticator;
    }

    private static IamAuthenticator createIamAuth() {
        if (overrideServerHost != null && !overrideServerHost.contains("https://private")) {
            iamAuthenticator = new IamAuthenticator.Builder().
            url(ENPushUrlBuilder.HTTPS_SCHEME + DEFAULT_IAM_DEV_STAGE_URL).
            apikey(apikey).
            build();
        } else {
            //this automatically calls iam prod url
            iamAuthenticator = new IamAuthenticator.Builder()
            .apikey(apikey).build();
        }
        return iamAuthenticator;
    }


    private HashMap<String, String> getServiceHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.USER_AGENT, ENPushConstants.LIBRARY_PACKAGE_NAME+ "/" + ENPushConstants.VERSION_NAME);
        headers.put(HttpHeaders.CONTENT_TYPE, HttpMediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Execute GET API request.
     *
     * @param url url of the source
     * @return the HTTP response
     */
    public ServiceCall getData(String url, ServiceCallback callback) {
        try {
            RequestBuilder builder = RequestBuilder.get(RequestBuilder.resolveRequestUrl(url, null, null));
            for (Map.Entry<String, String> header : this.getServiceHeaders().entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }

            ResponseConverter<String> responseConverter = ResponseConverterUtils.getString();
            ServiceCall call = createServiceCall(builder.build(), responseConverter);
            call.enqueue(callback);
            return call;

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Execute GET API request.
     *
     * @param url url of the source
     * @return the HTTP response
     */
    public ServiceCall delete(String url, ServiceCallback callback) {
        try {
            RequestBuilder builder = RequestBuilder.delete(RequestBuilder.resolveRequestUrl(url, null, null));
            for (Map.Entry<String, String> header : this.getServiceHeaders().entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }

            ResponseConverter<String> responseConverter = ResponseConverterUtils.getString();
            ServiceCall call = createServiceCall(builder.build(), responseConverter);
            call.enqueue(callback);
            return call;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }
    /**
     * Execute POST API request.
     *
     * @param url url of the source
     * @param data data to send
     * @return the HTTP response
     */
    public ServiceCall postData(String url, JSONObject data, ServiceCallback callback) {
        try {
            RequestBuilder builder = RequestBuilder.
                    post(RequestBuilder.resolveRequestUrl(url, null, null));
            for (Map.Entry<String, String> header : this.getServiceHeaders().entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
            JsonObject jsonData = new Gson().fromJson(String.valueOf(data), JsonObject.class);
            builder.bodyJson(jsonData);
            ResponseConverter<String> responseConverter = ResponseConverterUtils.getString();
            ServiceCall call = createServiceCall(builder.build(), responseConverter);
            call.enqueue(callback);
            return call;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Execute PUT API request.
     *
     * @param url url to connect
     * @param data data to send
     * @return the HTTP response
     */
    public ServiceCall putData(String url, JSONObject data, ServiceCallback callback) {
        try {
            RequestBuilder builder = RequestBuilder.put(RequestBuilder.resolveRequestUrl(url, null, null));

            for (Map.Entry<String, String> header : this.getServiceHeaders().entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
            JsonObject jsonData = new Gson().fromJson(String.valueOf(data), JsonObject.class);
            builder.bodyJson(jsonData);
            ResponseConverter<String> responseConverter = ResponseConverterUtils.getString();
            ServiceCall call = createServiceCall(builder.build(), responseConverter);
            call.enqueue(callback);
            return call;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Execute PUT API request.
     *
     * @param url url to connect
     * @param data data to send
     * @return the HTTP response
     */
    public ServiceCall patchData(String url, JSONObject data, ServiceCallback callback) {
        try {
            RequestBuilder builder = RequestBuilder.patch(RequestBuilder.resolveRequestUrl(url, null, null));

            for (Map.Entry<String, String> header : this.getServiceHeaders().entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
            JsonObject jsonData = new Gson().fromJson(String.valueOf(data), JsonObject.class);
            builder.bodyJson(jsonData);
            ResponseConverter<String> responseConverter = ResponseConverterUtils.getString();
            ServiceCall call = createServiceCall(builder.build(), responseConverter);
            call.enqueue(callback);
            return call;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }
}
