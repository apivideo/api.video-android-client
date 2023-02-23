package video.api.client.api.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import video.api.client.api.notifications.NotificationUtils

class ServiceNotificationUtils(
    private val service: Service,
    private val channelId: String,
    private val notificationId: Int
) {
    private val notificationManager: NotificationManager by lazy {
        service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private var hasNotified = false

    fun cancel() {
        service.stopForeground(true)
        hasNotified = false
    }

    fun notify(notification: Notification) {
        if (!hasNotified) {
            service.startForeground(
                notificationId,
                notification
            )
            hasNotified = true
        } else {
            notificationManager.notify(notificationId, notification)
        }
    }

    fun createNotificationChannel(
        @StringRes nameResourceId: Int,
        @StringRes descriptionResourceId: Int
    ) = NotificationUtils.createNotificationChannel(
        service,
        channelId,
        nameResourceId,
        descriptionResourceId
    )
}