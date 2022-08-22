package video.api.client.api.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes

class NotificationUtils(
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
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = service.getString(nameResourceId)

            val channel = NotificationChannel(
                channelId,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            if (descriptionResourceId != 0) {
                channel.description = service.getString(descriptionResourceId)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}