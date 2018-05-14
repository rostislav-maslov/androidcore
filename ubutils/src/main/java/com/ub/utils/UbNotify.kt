package com.ub.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.DrawableRes

@SuppressWarnings("unused")
object UbNotify {

    const val CHANNEL_ID = "101"
    const val CHANNEL_NAME = "Main"
    const val DEFAULT_ID = 101

    class Builder(private val context: Context) {

        fun fromLocal(icon: Int, title: String, message: String) : LocalBuilder {
            return LocalBuilder(context, icon, title, message)
        }

    }

    class LocalBuilder(private val context: Context, @DrawableRes private val smallIcon: Int, private val title: String, private val message: String) {

        private lateinit var channel: NotificationChannel
        private var params: (Notification.Builder.() -> Unit)? = null

        fun setChannelParams(id: String, name: String, channelParams : (NotificationChannel.() -> Unit)?) : LocalBuilder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
                channelParams?.invoke(channel)
            }

            return this
        }

        fun setParams(params : Notification.Builder.() -> Unit) : LocalBuilder {
            this.params = params

            return this
        }

        fun build() : android.app.Notification {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!this::channel.isInitialized) {
                    channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                }

                manager?.createNotificationChannel(channel)

                Notification.Builder(context, channel.id)
            } else {
                Notification.Builder(context)
            }

            builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(smallIcon)

            params?.invoke(builder)

            return builder.build()
        }

        fun show(id: Int = DEFAULT_ID) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (manager.areNotificationsEnabled()) {
                        manager.notify(id, build())
                    }
                } else {
                    manager.notify(id, build())
                }
            }
        }
    }
}