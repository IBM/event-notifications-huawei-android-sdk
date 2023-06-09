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

import org.json.JSONObject;

import java.util.Date;
import java.util.WeakHashMap;
/**
 * Levels supported in this Logger class.
 */
public class Logger {


    /**
     * Levels supported in this Logger class.
     */
    public enum LEVEL {
        /**
         * @exclude
         */
        FATAL {
            protected int getLevelValue() {
                return 50;
            }
        },
        ERROR {
            protected int getLevelValue() {
                return 100;
            }
        },
        WARN {
            protected int getLevelValue() {
                return 200;
            }
        },
        INFO {
            protected int getLevelValue() {
                return 300;
            }
        },
        DEBUG {
            protected int getLevelValue() {
                return 400;
            }
        }
    }


    public static final String INTERNAL_PREFIX = "eventnotifications.sdk.";
    public LoggerInterface loggerInterface;

    //Defaulting to DEBUG. SDK internal logs are hidden by default.
    private static LEVEL level = LEVEL.DEBUG;

    private static WeakHashMap<String, Logger> instances = new WeakHashMap<>();
    private final String name;


    // private, for factory creation of Logger objects
    private Logger(final String name) {
        this.name = (name == null || name.trim().equals ("")) ? "NONE" : name.trim();
    }

    /**
     * Get the Logger for the given name.
     *
     * @param name the tag that should be printed with log messages.  The value is passed
     *        through to android.util.Log and persistently recorded when log capture is enabled.
     * @return the Logger for the given name
     */
    static synchronized public Logger getLogger(final String name) {
        Logger logger = instances.get(name); {
            if (null == logger) {
                logger = new Logger(name);
                instances.put (name,  logger);
            }
        }
        return logger;
    }


    /**
     * Log at FATAL level.
     *
     * @param message the message to log
     */
    public void fatal(final String message) {
        fatal(message, null);
    }

    /**
     * Log at FATAL level.
     *
     * @param message the message to log
     * @param t       a Throwable whose stack trace is converted to string and logged, and passed through as-is to android.util.Log
     */
    public void fatal(final String message, final Throwable t) {
        doLog(LEVEL.FATAL, message, (new Date()).getTime(), t);
    }

    /**
     * Log at ERROR level.
     *
     * @param message the message to log
     */
    public void error(final String message) {
        error(message, null);
    }

    /**
     * Log at ERROR level.
     *
     * @param message the message to log
     * @param t       a Throwable whose stack trace is converted to string and logged, and passed through as-is to android.util.Log
     */
    public void error(final String message, final Throwable t) {
        doLog(LEVEL.ERROR, message, (new Date()).getTime(), t);
    }

    /**
     * Log at WARN level.
     *
     * @param message
     */
    public void warn(final String message) {
        warn(message, null);
    }

    /**
     * Log at WARN level.
     *
     * @param message the message to log
     * @param t       a Throwable whose stack trace is converted to string and logged, and passed through as-is to android.util.Log
     */
    public void warn(final String message, final Throwable t) {
        doLog(LEVEL.WARN, message, (new Date()).getTime(), t);
    }

    /**
     * Log at INFO level.
     *
     * @param message the message to log
     */
    public void info(final String message) {
        info(message, null);
    }

    /**
     * Log at INFO level.
     *
     * @param message the message to log
     * @param t       a Throwable whose stack trace is converted to string and logged, and passed through as-is to android.util.Log
     */
    public void info(final String message, final Throwable t) {
        doLog(LEVEL.INFO, message, (new Date()).getTime(), t);
    }

    /**
     * Log at DEBUG level.
     *
     * @param message the message to log
     */
    public void debug(final String message) {
        debug(message, null);
    }

    /**
     * Log at DEBUG level.
     *
     * @param message the message to log
     * @param t       a Throwable whose stack trace is converted to string and logged, and passed through as-is to android.util.Log
     */
    public void debug(final String message, final Throwable t) {
        doLog(LEVEL.DEBUG, message, (new Date()).getTime(), t);
    }

    /**
     * @param calledLevel specify the Logger.LEVEL (a null parameter results in no log entry)
     * @param message     (optional) the data for the log entry
     * @param timestamp   the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * @param t           (optional) an Exception or Throwable, may be null
     * @exclude All log calls flow through here.  Use this method when you want to control the timestamp, attach additional metadata,
     * and attach a Throwable's call stack to the log output.
     */
    protected void doLog(final LEVEL calledLevel, String message, final long timestamp, final Throwable t) {
        doLog(calledLevel, message, timestamp, t, null);
    }

    protected void doLog(final LEVEL calledLevel, String message, final long timestamp, final Throwable t, JSONObject additionalMetadata) {
        if (loggerInterface != null) {
            loggerInterface.doLog(calledLevel.name(), message, timestamp, t, additionalMetadata);
        }
    }
}

interface LoggerInterface {
    void doLog(final String level, final String message, long timestamp, final Throwable t, JSONObject additionalMetadata);
}
