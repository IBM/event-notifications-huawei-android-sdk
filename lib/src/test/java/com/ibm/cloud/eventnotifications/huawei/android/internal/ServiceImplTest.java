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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCallback;
import com.ibm.cloud.sdk.core.security.NoAuthAuthenticator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class ServiceImplTest {

    private NetworkInterface sut;
    private String mockAPIKey = "testApikey";
    private String path = "http://localhost:8080";
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {

        server = new MockWebServer();
        server.start(8080);
    }
    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testGetDataServiceImpl() throws IOException {

        String mockData = "data on get call";
        server.enqueue(new MockResponse().setBody(mockData));
        sut = new ServiceImpl("servicename", new NoAuthAuthenticator());
        CountDownLatch latch = new CountDownLatch(1);

        sut.getData(path, new ServiceCallback() {
            @Override
            public void onResponse(Response response) {
                assertEquals(mockData, response.getResult().toString());
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                fail("failed with exception " + e.getMessage());
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteDataServiceImpl() throws IOException {

        String mockData = "data on delete call";
        server.enqueue(new MockResponse().setBody(mockData).setStatus("HTTP/1.1 202"));
        sut = new ServiceImpl("servicename", new NoAuthAuthenticator());
        CountDownLatch latch = new CountDownLatch(1);

        sut.delete(path, new ServiceCallback() {
            @Override
            public void onResponse(Response response) {
                assertEquals(mockData, response.getResult().toString());
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                fail("failed with exception " + e.getMessage());
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPostDataServiceImpl() throws IOException, JSONException {

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("name", "postData");

        server.enqueue(new MockResponse().setBody(mockResponse.toString()));
        sut = new ServiceImpl("servicename", new NoAuthAuthenticator());
        CountDownLatch latch = new CountDownLatch(1);

        sut.postData(path, mockResponse,  new ServiceCallback() {
            @Override
            public void onResponse(Response response) {
                try {
                    JSONObject map = new JSONObject((String) response.getResult());
                    assertEquals(map.get("name"), mockResponse.get("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                fail("failed with exception " + e.getMessage());
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPutDataServiceImpl() throws IOException, JSONException {


        JSONObject mockResponse = new JSONObject();
        mockResponse.put("name", "postData");
        server.enqueue(new MockResponse().setBody(mockResponse.toString()));
        sut = new ServiceImpl("servicename", new NoAuthAuthenticator());
        CountDownLatch latch = new CountDownLatch(1);

        sut.putData(path, mockResponse, new ServiceCallback() {
            @Override
            public void onResponse(Response response) {
                try {
                    JSONObject map = new JSONObject((String) response.getResult());
                    assertEquals(map.get("name"), mockResponse.get("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                fail("failed with exception " + e.getMessage());
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}