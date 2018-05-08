package com.ub.utils

import android.util.Log
import com.ub.ubutils.BuildConfig

object LogUtils {

    private var consumerThrowable : ((throwable : Throwable) -> Void)? = null
    private var consumerString : ((error : String) -> Void)? = null

    fun setThrowableLogger(consumer: (throwable : Throwable) -> Void) {
        this.consumerThrowable = consumer
    }

    fun setMessageLogger(consumer: (message : String) -> Void) {
        this.consumerString = consumer
    }

    fun logError(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        } else {
            consumerThrowable?.invoke(throwable)
        }
    }

    fun logError(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        } else {
            consumerString?.invoke(message)
        }
    }

    fun logInfo(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun logDebug(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun logWarning(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        } else {
            consumerString?.invoke(message)
        }
    }

    fun logWarning(tag: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, throwable)
        } else {
            consumerThrowable?.invoke(throwable)
        }
    }

    fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message)
        } else {
            consumerString?.invoke(message)
        }
    }
}