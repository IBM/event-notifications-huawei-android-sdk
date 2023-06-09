
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
 * Exception class for the SDK.
 */
public class ENPushException extends Exception {

	private static final long serialVersionUID = 8902847782639797614L;

	private int statusCode = 0;
	private String statusLine = null;
	private String errorMessage = null;
	private String errorCode = null;
	private String docUrl = null;

	/**
	 * Constructor to create a ENPushException
	 *
	 * @param e  root cause exception
	 */
	public ENPushException(Exception e) {
		super(e);
	}

	/**
	 * Constructor to create a ENPushException
	 *
	 * @param message  exception detail message
	 */
	public ENPushException(String message) {
		super(message);
	}

	/**
	 * Create a ENPushException with the below information
	 *
	 * @param errorMessage  Error message received from Push Server
	 * @param statusCode  Error code received from Push Server
	 */
	public ENPushException(String errorMessage, int statusCode) {
		super(", Message : " + errorMessage + ", statusCode : " + statusCode );
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

    /**
     * Create a ENPushException with the below information
     *
     * @param statusCode  Status code of the HTTP response received
     * @param statusLine  Status line of the HTTP response received
     * @param errorMessage  Error message received from Push Server
     * @param errorCode  Error code received from Push Server
     * @param docUrl  URL at which information of the error related information could be found
     */
    public ENPushException(int statusCode, String statusLine, String errorMessage,
						   String errorCode, String docUrl) {
    	super("Status Code : " + statusCode + ", Status Line : " + statusLine + ", Message : " +
    			errorMessage + ", Error Code : " + errorCode + " , Doc URL : " + docUrl);

    	this.statusCode = statusCode;
    	this.statusLine = statusLine;
    	this.errorMessage = errorMessage;
    	this.errorCode = errorCode;
    	this.docUrl = docUrl;
    }

    /**
     * Create a ENPushException with the below information
     *
     * @param statusCode  Status code of the HTTP response received
     * @param statusLine  Status line of the HTTP response received
     */

    public ENPushException(int statusCode, String statusLine) {
        super("Status Code : " + statusCode + ", Status Line : " + statusLine);

       this.statusCode = statusCode;
       this.statusLine = statusLine;
    }

	/**
	 * Return the HTTP status code from the HTTP response
	 *
	 * @return status code as int
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Return the HTTP status line from the HTTP response
	 *
	 * @return status line as string
	 */
	public String getStatusLine() {
		return statusLine;
	}

	/**
	 * Return the error message
	 *
	 * @return error message as string
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Return the error code
	 *
	 * @return error code as string
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Return the documentation url
	 *
	 * @return documentation url as string
	 */
	public String getDocUrl() {
		return docUrl;
	}
}
