package com.ub.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.res.Resources
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.annotation.StringRes
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStream
import java.net.NetworkInterface
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@SuppressLint("StaticFieldLeak")
object UbUtils {
    private var context : Context? = null

    fun init(context : Context) {
        this.context = context
    }

    /**
     * Получение строки с переданными параметрами для форматирования
     */
    @JvmStatic
    fun getString(@StringRes id: Int, vararg parameters: Any) : String {
        context?.let {
            return it.getString(id, *parameters)
        }

        throw IllegalStateException("Context in UbUtils not initialized. Please call UbUtils.init in your Application instance")
    }

    /**
     * Получение ресурсов
     */
    @JvmStatic
    fun getResources(): Resources {
        context?.let {
            return it.resources
        }

        throw IllegalStateException("Context in UbUtils not initialized. Please call UbUtils.init in your Application instance")
    }

    /**
     * Копирование текста в буфер обмена
     * @return успешность операции
     */
    @JvmStatic
    fun copyTextToClipboard(text: String): Boolean {
        context?.let {
            val clipManager = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData = ClipData.newPlainText("text", text)
            clipManager?.let { manager ->
                manager.setPrimaryClip(clipData)

                return true
            }

            return false
        }

        return false
    }

    /**
     * Получение высоты статус-бара в пикселях
     * @return высота статус-бара в пикселях
     */
    @JvmStatic
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }
}

/**
 * Ленивая последовательность элементов
 * Идеально подходит для таймера
 */
val timer = sequence {
    var cur = 1
    while (true) {
        yield(cur)
        cur += 1
    }
}

/**
 * Сравнение двух коллекций на предмет совпадения содержимого
 * Содержимое может быть не по порядку
 */
fun <T> haveSameElements(col1: Collection<T>?, col2: Collection<T>?): Boolean {
    if (col1 === col2)
        return true

    // If either list is null, return whether the other is empty
    if (col1 == null)
        return col2!!.isEmpty()
    if (col2 == null)
        return col1.isEmpty()

    // If lengths are not equal, they can't possibly match
    if (col1.size != col2.size)
        return false

    // Helper class, so we don't have to do a whole lot of autoboxing
    class Count {
        // Initialize as 1, as we would increment it anyway
        var count = 1
    }

    val counts = HashMap<T, Count>()

    // Count the items in list1
    for (item in col1) {
        val count = counts[item]
        if (count != null)
            count.count++
        else
        // If the map doesn't contain the item, put a new count
            counts[item] = Count()
    }

    // Subtract the count of items in list2
    for (item in col2) {
        val count = counts[item]
        // If the map doesn't contain the item, or the count is already reduced to 0, the lists are unequal
        if (count == null || count.count == 0)
            return false
        count.count--
    }

    // If any count is nonzero at this point, then the two lists don't match
    for (count in counts.values)
        if (count.count != 0)
            return false

    return true
}

/**
 * Проверка на валидность введенного номера
 */
fun isValidPhoneNumber(number: String): Boolean {
    return android.util.Patterns.PHONE.matcher(number).matches()
}

/**
 * Проверка на валидность введенного почтового адреса
 */
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

/**
 * Получение текущего IP-адреса устройства
 */
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
                            return if (delim < 0) sAddr.toUpperCase(Locale.getDefault()) else sAddr.substring(0, delim).toUpperCase(Locale.getDefault())
                        }
                    }
                }
            }
        }
    } catch (ex: Exception) {
        LogUtils.e("IpAddress", ex.message ?: "IP Error", ex)
    }
    // for now eat exceptions
    return ""
}

/**
 * Определение, является ли ошибка сетевой
 */
fun isNetworkException(error: Throwable): Boolean {
    return error is IOException || error is HttpException
}

/**
 * Check, if device is Samsung for showng DatePicker without bugs
 */
fun isBrokenSamsungDevice(): Boolean {
    return (Build.MANUFACTURER.equals("samsung", ignoreCase = true)
        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1)
}

/**
 * Прячет клавиатуру
 */
fun hideSoftKeyboard(context: Context) {
    try {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow((context as Activity).currentFocus!!.windowToken, 0)
        context.currentFocus!!.clearFocus()
    } catch (e: NullPointerException) {
        LogUtils.e("KeyBoard", "NULL point exception in input method service")
    }

}

/**
 * Открывает клавиатуру
 */
fun openSoftKeyboard(context: Context, view: View) {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Открывает страницу приложения в Google Play Market
 * @return - успешность операции: true - ссылка открыта, false - не открыта
 */
fun openMarket(context: Context) : Boolean {
    val uri = Uri.parse("market://details?id=" + context.packageName)
    val market = Intent(Intent.ACTION_VIEW, uri)
    return if (market.resolveActivity(context.packageManager) != null) {
        context.startActivity(market)
        true
    } else {
        val browser = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
        if (browser.resolveActivity(context.packageManager) != null) {
            context.startActivity(browser)
            true
        } else {
            false
        }
    }
}

/**
 * Определение, включена ли геолокация на устройстве
 */
fun isGpsIsEnabled(context: Context): Boolean {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    val networkLocationEnabled = manager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
    val gpsLocationEnabled = manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true

    return networkLocationEnabled || gpsLocationEnabled
}

/**
 * Coroutine-cancellable-реализация загрузки объекта из сети
 *
 * Процесс работы:
 * 1. Загрузка по переданному [url] объекта [okhttp3.Response] с помощью [OkHttpClient]
 * 2. Преобразование с помощью [objectMapper] в объект типа [T] с учетом возможного пустого ответа
 * 3. Отдача в существующую корутину результата операции
 *
 * В случае отмены операции с помощью [okhttp3.Call.cancel] сетевой запрос отменяется, если возможно
 */
suspend inline fun <T> OkHttpClient.download(url: String, crossinline objectMapper: (byteStream: InputStream?) -> T?) =
    suspendCancellableCoroutine<T?> { continuation ->
        val request = Request.Builder().url(url).build()
        val call = newCall(request)
        call.enqueue(object : Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!continuation.isCompleted) {
                    continuation.resume(objectMapper.invoke(response.body()?.byteStream()))
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                if (!continuation.isCompleted) {
                    continuation.resumeWithException(e)
                }
            }
        })

        continuation.invokeOnCancellation {
            call.cancel()
        }
    }