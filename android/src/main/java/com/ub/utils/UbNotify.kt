package com.ub.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

@Suppress("DEPRECATION", "UNUSED")
object UbNotify {

    private const val DEFAULT_ID = 101

    /**
     * Данные значения нужны для того, чтобы создать канал уведомлений, если он не задан явно
     * Изменять их целесообразно только если не вызывается метод [Builder.setChannelParams] для создания уведомления
     */
    var defaultChannelId = "101"
    var defaultChannelName = "Main"

    fun create(context: Context, @DrawableRes icon: Int, title: String, message: String): Builder {
        return Builder(context, icon, title, message)
    }

    class Builder(private val context: Context,
                  @DrawableRes private val smallIcon: Int,
                  private val title: String,
                  private val message: String) {

        private lateinit var channel: NotificationChannel
        private var params: (NotificationCompat.Builder.() -> Unit)? = null
        private val manager: NotificationManager? by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager? }

        fun setChannelParams(id: String, name: String, channelParams: (NotificationChannel.() -> Unit)?): Builder {
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
        fun setParams(params: NotificationCompat.Builder.() -> Unit): Builder {
            this.params = params

            return this
        }

        /**
         *
         * Во время исполнения создаст канал уведомлений, который был проинициализирован.
         * Если этого не было сделано явно, создаст канал по-умолчанию [defaultChannelId], [defaultChannelName]
         * @return собранное уведомление
         */
        fun build(): android.app.Notification {
            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!this::channel.isInitialized) {
                    channel = NotificationChannel(defaultChannelId, defaultChannelName, NotificationManager.IMPORTANCE_HIGH)
                }

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

        fun show(tag: String? = null, id: Int = DEFAULT_ID) {
            manager?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (it.areNotificationsEnabled()) {
                        it.notify(tag, id, build())
                    }
                } else {
                    it.notify(tag, id, build())
                }
            }
        }
    }

    class ChannelCreator(private val context: Context) {

        private val manager: NotificationManager? by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager? }
        private val initializer = ChannelInitializer()

        @RequiresApi(Build.VERSION_CODES.O)
        fun create(builder: (ChannelInitializer.() -> Unit)) {
            builder.invoke(initializer)
            manager?.createNotificationChannels(initializer.channelsToCreate)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun clearAllChannels() {
            val channels = manager?.notificationChannels ?: return
            channels.forEach {
                manager?.deleteNotificationChannel(it.id)
            }
        }

        class ChannelInitializer {

            val channelsToCreate = ArrayList<NotificationChannel>()

            @RequiresApi(Build.VERSION_CODES.O)
            fun addNewChannel(id: String, name: String, priority: Int = NotificationManager.IMPORTANCE_HIGH, channelConfigurator: (NotificationChannel.() -> Unit)? = null): ChannelInitializer {
                val channel = NotificationChannel(id, name, priority).apply {
                    channelConfigurator?.invoke(this)
                }
                channelsToCreate.add(channel)

                return this
            }
        }
    }
}