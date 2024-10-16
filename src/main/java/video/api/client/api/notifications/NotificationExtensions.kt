package video.api.client.api.notifications

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

fun NotificationCompat.Builder.setStyle(
    context: Context,
    @DrawableRes notificationIconResourceId: Int,
    @ColorRes notificationColorResourceId: Int
): NotificationCompat.Builder = apply {
    setSmallIcon(notificationIconResourceId)
    color = ContextCompat.getColor(context, notificationColorResourceId)
}