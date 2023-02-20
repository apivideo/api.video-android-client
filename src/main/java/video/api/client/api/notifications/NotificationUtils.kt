package video.api.client.api.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes

object NotificationUtils {
    const val DEFAULT_NOTIFICATION_SERVICE_CHANNEL_ID =
        "video.api.client.api.clients.service"
    const val DEFAULT_NOTIFICATION_WORK_CHANNEL_ID =
        "video.api.client.api.clients.work"
    const val DEFAULT_NOTIFICATION_ID = 3333

    fun createNotificationChannel(
        context: Context,
        channelId: String,
        @StringRes nameResourceId: Int,
        @StringRes descriptionResourceId: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(nameResourceId)

            val channel = NotificationChannel(
                channelId,
                name,
                NotificationManager.IMPORTANCE_LOW
            )
            if (descriptionResourceId != 0) {
                channel.description = context.getString(descriptionResourceId)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}