@file:Suppress("UNUSED")

package com.ub.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.NetworkCapabilities.*
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringDef
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Класс, инкаспулирующий в себе получение изменений состояния активной сети
 * Умеет работать с [Build.VERSION_CODES.LOLLIPOP] и выше через акутальное API [networkCallback],
 * и по обратной совместимости на [Build.VERSION_CODES.KITKAT] и ниже через устаревший [networkReceiver]
 *
 * Основной метод [startListener] возвращает [Flow] со значениями [State].
 * Значения могут дублироваться, на [Flow] желательно применить [kotlinx.coroutines.flow.distinctUntilChanged],
 * но пока он в статусе [kotlinx.coroutines.FlowPreview], то смотрите сами
 *
 * Сценарии, которые были учтены в работе:
 * 1. [TRANSPORT_WIFI] - соединение установлено и может передавать данные
 * 2. [TRANSPORT_WIFI] - соединение установлено и не может передавать данные (создать WiFi-точку на телефоне без SIM-карты)
 * 3. [TRANSPORT_WIFI] - соединение установлено и при любом запросе происходит редирект на [NET_CAPABILITY_CAPTIVE_PORTAL] -
 * страницу регистрации, например. ДомРу такое практикует с публичными WiFi-точками доступа
 * 4. No transport - соединение любого типа не установлено, передача данных недоступна
 * 5. [TRANSPORT_CELLULAR] - соединение установлено, передача данных доступна
 * 6. [TRANSPORT_CELLULAR] - соединение установлено, передача данных недоступна (не подключена услуга интернета, прочие сбои у оператора)
 * 7. [TRANSPORT_CELLULAR] (в роуминге) - соединение установлено, но ограничено системной защитой от случайной активации роуминга
 * 8. [TRANSPORT_BLUETOOTH] - соединение установлено, передача данных доступна
 * 9. [TRANSPORT_BLUETOOTH] - соединение установлено, передача данных недоступна (bluetooth-доступ без SIM-карты, либо сценарий из п.6)
 * 10. Прочий доступ (не тестировался) - теоретически должен работать, если на [Build.VERSION_CODES.LOLLIPOP] и выше
 * в [NetworkCapabilities] будет среди прочего [NetworkCapabilities.NET_CAPABILITY_VALIDATED].
 * На [Build.VERSION_CODES.KITKAT] работать тоже, скорее всего, будет из-за топорности API, там и ломаться нечему
 *
 * При завершении работы с классом важно закрыть с ним работу через [stopListener].
 * Иначе возможны утечки памяти и прочая Сотона, вплоть до крашей
 *
 * Известные проблемы:
 * 1. На некоторых устройствах (Asus API 21) не срабатывает [ConnectivityManager.NetworkCallback.onCapabilitiesChanged],
 * из-за чего не корректно отрабатывают сценарии 2 и 3
 * 2. А на API <= [Build.VERSION_CODES.KITKAT] сценарии 2 и 3 вообще не работают, так как у [NetworkInfo] нет соответсвующего API
 */
class CNetwork(
    private val context: Context
) {
    private val networkReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            @Suppress("DEPRECATION")
            @SuppressLint("MissingPermission")
            override fun onReceive(context: Context, intent: Intent) {
                networkStateChannel.offer(
                    if (manager?.activeNetworkInfo?.isAvailable == true) State.ACTIVE else State.DISABLE
                )
            }
        }
    }
    private val manager: ConnectivityManager? by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }
    private val networkStateChannel: Channel<String> by lazy {
        Channel<String>()
    }
    private val networkCallback: ConnectivityManager.NetworkCallback by lazy {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkStateChannel.offer(State.ACTIVE)
            }

            override fun onLost(network: Network) {
                networkStateChannel.offer(State.DISABLE)
            }

            override fun onUnavailable() {
                networkStateChannel.offer(State.DISABLE)
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                networkStateChannel.offer(
                    when {
                        networkCapabilities.hasTransport(TRANSPORT_CELLULAR) && networkCapabilities.hasCapability(NET_CAPABILITY_NOT_CONGESTED) && networkCapabilities.hasCapability(NET_CAPABILITY_NOT_SUSPENDED) -> State.ACTIVE
                        networkCapabilities.hasTransport(TRANSPORT_WIFI) && networkCapabilities.hasCapability(NET_CAPABILITY_CAPTIVE_PORTAL) -> State.CAPTIVE
                        networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED) -> State.ACTIVE
                        else -> State.DISABLE
                    }
                )
            }
        }
    }

    @State
    @Suppress("DEPRECATION")
    fun startListener(): Flow<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val request = NetworkRequest.Builder().build()
            manager?.requestNetwork(request, networkCallback)
        } else {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            context.registerReceiver(networkReceiver, filter)
        }
        return flow {
            for (networkState in networkStateChannel) {
                emit(networkState)
            }
        }
    }

    fun stopListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager?.unregisterNetworkCallback(networkCallback)
        } else {
            context.unregisterReceiver(networkReceiver)
        }
    }

    @StringDef(State.ACTIVE, State.DISABLE, State.CAPTIVE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class State {
        companion object {
            const val ACTIVE = "ACTIVE"
            const val DISABLE = "DISABLE"
            const val CAPTIVE = "CAPTIVE"
        }
    }
}

val Context.cNetwork: CNetwork
    get() = CNetwork(this)