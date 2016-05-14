package com.lib.common;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by angcyo on 2016-05-14 18:03.
 */
public class RLog {
    public static int LOG_LEVEL = Log.VERBOSE;
    private Logger logger;

    private RLog(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    private RLog(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public static RLog getLog(String name) {
        return new RLog(name);
    }

    public static RLog getLog(Class clazz) {
        return new RLog(clazz);
    }

    /**
     * @see org.slf4j.Logger#trace(String)
     */
    public void trace(String msg) {
        if (LOG_LEVEL > 0) {
            logger.trace(msg);
        }
    }

    /**
     * @see org.slf4j.Logger#trace(String, Object)
     */
    public void trace(String format, Object arg) {
        if (LOG_LEVEL > 0) {
            logger.trace(format, arg);
        }
    }


    /**
     * @see org.slf4j.Logger#trace(String, Object...)
     */
    public void trace(String format, Object... arguments) {
        if (LOG_LEVEL > 0) {
            logger.trace(format, arguments);
        }
    }


    /**
     * @see org.slf4j.Logger#trace(String, Throwable)
     */
    public void trace(String msg, Throwable t) {
        if (LOG_LEVEL > 0) {
            logger.trace(msg, t);
        }
    }


    /**
     * @see org.slf4j.Logger#debug(String)
     */
    public void debug(String msg) {
        if (LOG_LEVEL >= Log.DEBUG) {
            logger.debug(msg);
        }
    }


    /**
     * @see org.slf4j.Logger#debug(String, Object)
     */
    public void debug(String format, Object arg) {
        if (LOG_LEVEL >= Log.DEBUG) {
            logger.debug(format, arg);
        }
    }


    /**
     * @see org.slf4j.Logger#debug(String, Object, Object)
     */
    public void debug(String format, Object arg1, Object arg2) {
        if (LOG_LEVEL >= Log.DEBUG) {
            logger.debug(format, arg1, arg2);
        }
    }


    /**
     * @see org.slf4j.Logger#debug(String, Object...)
     */
    public void debug(String format, Object... arguments) {
        if (LOG_LEVEL >= Log.DEBUG) {
            logger.debug(format, arguments);
        }
    }


    /**
     * @see org.slf4j.Logger#debug(String, Throwable)
     */
    public void debug(String msg, Throwable t) {
        if (LOG_LEVEL >= Log.DEBUG) {
            logger.debug(msg, t);
        }
    }

    /**
     * @see org.slf4j.Logger#info(String)
     */
    public void info(String msg) {
        if (LOG_LEVEL >= Log.INFO) {
            logger.info(msg);
        }
    }


    /**
     * @see org.slf4j.Logger#info(String, Object)
     */
    public void info(String format, Object arg) {
        if (LOG_LEVEL >= Log.INFO) {
            logger.info(format, arg);
        }
    }


    /**
     * @see org.slf4j.Logger#info(String, Object, Object)
     */
    public void info(String format, Object arg1, Object arg2) {
        if (LOG_LEVEL >= Log.INFO) {
            logger.info(format, arg1, arg2);
        }
    }


    /**
     * @see org.slf4j.Logger#info(String, Object...)
     */
    public void info(String format, Object... arguments) {
        if (LOG_LEVEL >= Log.INFO) {
            logger.info(format, arguments);
        }
    }


    /**
     * @see org.slf4j.Logger#info(String, Throwable)
     */
    public void info(String msg, Throwable t) {
        if (LOG_LEVEL >= Log.INFO) {
            logger.info(msg, t);
        }
    }

    /**
     * @see org.slf4j.Logger#warn(String)
     */
    public void warn(String msg) {
        if (LOG_LEVEL >= Log.WARN) {
            logger.warn(msg);
        }
    }


    /**
     * @see org.slf4j.Logger#warn(String, Object)
     */
    public void warn(String format, Object arg) {
        if (LOG_LEVEL >= Log.WARN) {
            logger.warn(format, arg);
        }
    }


    /**
     * @see org.slf4j.Logger#warn(String, Object...)
     */
    public void warn(String format, Object... arguments) {
        if (LOG_LEVEL >= Log.WARN) {
            logger.warn(format, arguments);
        }
    }


    /**
     * @see org.slf4j.Logger#warn(String, Object, Object)
     */
    public void warn(String format, Object arg1, Object arg2) {
        if (LOG_LEVEL >= Log.WARN) {
            logger.warn(format, arg1, arg2);
        }
    }


    /**
     * @see org.slf4j.Logger#warn(String, Throwable)
     */
    public void warn(String msg, Throwable t) {
        if (LOG_LEVEL >= Log.WARN) {
            logger.warn(msg, t);
        }
    }

    /**
     * @see org.slf4j.Logger#error(String)
     */
    public void error(String msg) {
        if (LOG_LEVEL >= Log.ERROR) {
            logger.error(msg);
        }
    }

    /**
     * @see org.slf4j.Logger#error(String, Object)
     */
    public void error(String format, Object arg) {
        if (LOG_LEVEL >= Log.ERROR) {
            logger.error(format, arg);
        }
    }

    /**
     * @see org.slf4j.Logger#error(String, Object, Object)
     */
    public void error(String format, Object arg1, Object arg2) {
        if (LOG_LEVEL >= Log.ERROR) {
            logger.error(format, arg1, arg2);
        }
    }

    /**
     * @see org.slf4j.Logger#error(String, Object...)
     */
    public void error(String format, Object... arguments) {
        if (LOG_LEVEL >= Log.ERROR) {
            logger.error(format, arguments);
        }
    }


    /**
     * @see org.slf4j.Logger#error(String, Throwable)
     */
    public void error(String msg, Throwable t) {
        if (LOG_LEVEL >= Log.ERROR) {
            logger.error(msg, t);
        }
    }

}
