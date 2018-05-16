package com.ub.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat

@Suppress("DEPRECATION")
@SuppressWarnings("unused")
object UbNotify {

    private const val DEFAULT_ID = 101

    /**
     * Данные значения нужны для того, чтобы создать канал уведомлений, если он не задан явно
     * Изменять их целесообразно только если не вызывается метод [LocalBuilder.setChannelParams] для создания уведомления
     */
    var defaultChannelId = "101"
    var defaultChannelName = "Main"

    class Builder(private val context: Context) {

        fun fromLocal(@DrawableRes icon: Int, title: String, message: String) : LocalBuilder {
            return LocalBuilder(context, icon, title, message)
        }

    }

    class LocalBuilder(private val context: Context, @DrawableRes private val smallIcon: Int, private val title: String, private val message: String) {

        private lateinit var channel: NotificationChannel
        private var params: (NotificationCompat.Builder.() -> Unit)? = null

        fun setChannelParams(id: String, name: String, channelParams: (NotificationChannel.() -> Unit)?) : LocalBuilder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
                channelParams?.invoke(channel)
            }

            return this
        }

        /**
         * Исполняется после [show]
         * Т.е. расчитывать значение id в этом методе - не самая лучшая идея
         * Метод [show] захватит значение переменной id раньше, чем он расчитается тут
         */
        fun setParams(params : NotificationCompat.Builder.() -> Unit) : LocalBuilder {
            this.params = params

            return this
        }

        /**
         *
         * Во время исполнения создаст канал уведомлений, который был проинициализирован.
         * Если этого не было сделано явно, создаст канал по-умолчанию [defaultChannelId], [defaultChannelName]
         * @return собранное уведомление
         */
        fun build() : android.app.Notification {
            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!this::channel.isInitialized) {
                    channel = NotificationChannel(defaultChannelId, defaultChannelName, NotificationManager.IMPORTANCE_HIGH)
                }

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                manager?.createNotificationChannel(channel)

                NotificationCompat.Builder(context, channel.id)
            } else {
                NotificationCompat.Builder(context)
            }

            builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(smallIcon)

            params?.invoke(builder)

            return builder.build()
        }

        fun show(id: Int = DEFAULT_ID) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            manager?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (it.areNotificationsEnabled()) {
                        it.notify(id, build())
                    }
                } else {
                    it.notify(id, build())
                }
            }
        }
    }
}