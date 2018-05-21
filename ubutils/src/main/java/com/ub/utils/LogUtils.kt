package com.ub.utils

import android.util.Log
import com.ub.ubutils.BuildConfig

object LogUtils {

    private var consumerThrowable : ((throwable : Throwable) -> Unit)? = null
    private var consumerString : ((error : String) -> Unit)? = null

    @JvmStatic
    fun setThrowableLogger(consumer: (throwable : Throwable) -> Unit) {
        this.consumerThrowable = consumer
    }

    @JvmStatic
    fun setMessageLogger(consumer: (message : String) -> Unit) {
        this.consumerString = consumer
    }

    @JvmStatic
    fun e(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        } else {
            consumerThrowable?.invoke(throwable)
        }
    }

    @JvmStatic
    fun e(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        } else {
            consumerString?.invoke(message)
        }
    }

    @JvmStatic
    fun i(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    @JvmStatic
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    @JvmStatic
    fun w(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        } else {
            consumerString?.invoke(message)
        }
    }

    @JvmStatic
    fun w(tag: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, throwable)
        } else {
            consumerThrowable?.invoke(throwable)
        }
    }

    @JvmStatic
    fun v(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message)
        } else {
            consumerString?.invoke(message)
        }
    }
}