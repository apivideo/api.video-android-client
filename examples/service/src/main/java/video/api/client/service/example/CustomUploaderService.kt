package video.api.client.service.example

import android.app.Notification
import androidx.core.app.NotificationCompat
import video.api.client.api.models.Video
import video.api.client.api.notifications.setStyle
import video.api.client.api.services.UploadService

class CustomUploaderService : UploadService(
    notificationId = DEFAULT_NOTIFICATION_ID,
    channelId = DEFAULT_NOTIFICATION_CHANNEL_ID,
    channelNameResourceId = R.string.upload_notification_channel_name,
) {
    companion object {
        const val DEFAULT_NOTIFICATION_CHANNEL_ID =
            "video.api.client.example.CustomUploaderService"
        const val DEFAULT_NOTIFICATION_ID = 3535 // Unique ID for the notification
    }

    /**
     * You can override each of [UploadService] notification methods to customize notifications.
     */
    override fun onUploadSuccessNotification(id: String, video: Video): Notification {
        // Return your custom the notification
        return NotificationCompat.Builder(this, channelId)
            .setStyle(this, notificationIconResourceId, notificationColorResourceId)
            .setContentTitle(getString(R.string.upload_success_notification_text))
            .build()
    }
}