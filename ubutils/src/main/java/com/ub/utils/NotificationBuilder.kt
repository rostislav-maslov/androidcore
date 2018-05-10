package com.ub.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat

@SuppressWarnings("unused")
object Notification {

    class Builder(private val context: Context) {

        fun fromLocal(title: String, message: String) : LocalBuilder {
            return LocalBuilder(context, title, message)
        }

        fun fromFirebase() {

        }
    }

    class FirebaseBuilder {

    }

    class LocalBuilder(private val context: Context, title: String, message: String) {

        private var channelId : String? = null
        private var color: Int? = null
        var intents: Array<out Intent>? = null

        fun setColorRes(@ColorRes color: Int) : LocalBuilder {
            this.color = ContextCompat.getColor(context, color)

            return this
        }

        fun setColorInt(@ColorInt color: Int) : LocalBuilder {
            this.color = color

            return this
        }

        fun setNotificationChannel(channelId : String) : LocalBuilder {
            this.channelId = channelId

            return this
        }

        fun setIntentss(vararg intents : Intent) : LocalBuilder {
            this.intents = intents

            return this
        }

        /*fun build() : android.app.Notification {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mainChannel = if (channelId != null) {
                    NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
                } else {
                    NotificationChannel("01", "01", NotificationManager.IMPORTANCE_HIGH)
                }
                mainChannel.enableLights(true)
                mainChannel.enableVibration(true)
                mainChannel.vibrationPattern = longArrayOf(500, 500)
                mainChannel.lightColor = ContextCompat.getColor(context, R.color.cherry_red)
                mainChannel.name = context.resources.getString(R.string.notification_channel_main_name)
                mainChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

                manager?.createNotificationChannel(mainChannel)

                Notification.Builder(context, channelId ?: "01")
            } else {
                Notification.Builder(context)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setColor(ContextCompat.getColor(context, R.color.cherry_red))
            }

            builder.setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(longArrayOf(500, 500))
                .setLights(Color.RED, 1000, 500)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(Notification.BigTextStyle().bigText(message))
                .setAutoCancel(true)

            val intent: Intent

            return builder.build()
        }*/
    }
}