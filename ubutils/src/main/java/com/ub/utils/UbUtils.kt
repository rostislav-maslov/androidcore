package com.ub.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes
import android.view.inputmethod.InputMethodManager
import java.net.NetworkInterface
import java.util.*

@SuppressLint("StaticFieldLeak")
object UbUtils {
    lateinit var context : Context

    /**
     * Получение строки с переданными параметрами для форматирования
     */
    @JvmStatic
    fun getString(@StringRes id: Int, vararg parameters: Any) : String {
        return context.getString(id, parameters)
    }

    /**
     * Прячет клавиатуру
     */
    @JvmStatic
    fun hideSoftKeyboard(context: Context) {
        try {
            val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow((context as Activity).currentFocus!!.windowToken, 0)
            context.currentFocus!!.clearFocus()
        } catch (e: NullPointerException) {
            LogUtils.logError("KeyBoard", "NULL point exception in input method service")
        }

    }

    /**
     * Проверка на валидность введенного номера
     */
    @JvmStatic
    fun isValidPhoneNumber(number: String): Boolean {
        return android.util.Patterns.PHONE.matcher(number).matches()
    }

    /**
     * Получение текущего IP-адреса устройства
     */
    @JvmStatic
    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            LogUtils.logError("IpAddress", ex.message ?: "IP Error", ex)
        }
        // for now eat exceptions
        return ""
    }
}