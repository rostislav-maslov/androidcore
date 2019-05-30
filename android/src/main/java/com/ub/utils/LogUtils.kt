package com.ub.utils

import androidx.annotation.Keep
import android.util.Log

class LogUtils {

    companion object {
        private var consumerThrowable : ((throwable : Throwable) -> Unit)? = null
        private var consumerString : ((error : String) -> Unit)? = null
        private var isDebug = true
        private var defaultMessage = "Undefined error"

        @JvmStatic
        fun setThrowableLogger(consumer: (throwable : Throwable) -> Unit) {
            this.consumerThrowable = consumer
        }

        @JvmStatic
        fun setMessageLogger(consumer: (message : String) -> Unit) {
            this.consumerString = consumer
        }

        @JvmStatic
        fun setDefaultMessage(message: String) {
            this.defaultMessage = message
        }

        @JvmStatic
        fun init(isDebug: Boolean) {
            this.isDebug = isDebug
        }

        @Keep
        @JvmStatic
        fun e(tag: String, message: String?, throwable: Throwable) {
            if (isDebug) {
                Log.e(tag, message ?: defaultMessage, throwable)
            } else {
                consumerThrowable?.invoke(throwable)
            }
        }

        @Keep
        @JvmStatic
        fun e(tag: String, message: String?) {
            if (isDebug) {
                Log.e(tag, message ?: defaultMessage)
            } else {
                consumerString?.invoke(message ?: defaultMessage)
            }
        }

        @Keep
        @JvmStatic
        fun i(tag: String, message: String?) {
            if (isDebug) {
                Log.i(tag, message ?: defaultMessage)
            }
        }

        @Keep
        @JvmStatic
        fun d(tag: String, message: String?) {
            if (isDebug) {
                Log.d(tag, message ?: defaultMessage)
            }
        }

        @Keep
        @JvmStatic
        fun w(tag: String, message: String?) {
            if (isDebug) {
                Log.w(tag, message ?: defaultMessage)
            } else {
                consumerString?.invoke(message ?: defaultMessage)
            }
        }

        @Keep
        @JvmStatic
        fun w(tag: String, throwable: Throwable) {
            if (isDebug) {
                Log.w(tag, throwable)
            } else {
                consumerThrowable?.invoke(throwable)
            }
        }

        @Keep
        @JvmStatic
        fun v(tag: String, message: String?) {
            if (isDebug) {
                Log.v(tag, message ?: defaultMessage)
            } else {
                consumerString?.invoke(message ?: defaultMessage)
            }
        }
    }
}