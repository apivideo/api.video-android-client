package video.api.client.api.work.stores

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import video.api.client.api.R
import video.api.client.api.notifications.NotificationUtils

/**
 * A class that holds the configuration for the notification.
 * It allows to customize the notification.
 */
object NotificationConfigurationStore {
    /**
     * The notification id for the foreground notification.
     */
    @JvmStatic
    var notificationId: Int = NotificationUtils.DEFAULT_NOTIFICATION_ID

    /**
     * The id of the channel. Must be unique per package. The value may be truncated if it is too long.
     */
    @JvmStatic
    var channelId: String = NotificationUtils.DEFAULT_NOTIFICATION_WORK_CHANNEL_ID

    /**
     * The user visible name of the channel. The recommended maximum length is 40 characters; the value may be truncated if it is too long.
     */
    @JvmStatic
    @StringRes
    var channelNameResourceId: Int = R.string.channel_name

    /**
     * A string resource identifier for the user visible description of the notification channel, or 0 if no description is provided.
     */
    @JvmStatic
    @StringRes
    var channelDescriptionResourceId: Int = 0

    /**
     * The resource id of the notification icon.
     */
    @JvmStatic
    @DrawableRes
    var notificationIconResourceId: Int = R.drawable.ic_api_video_logo

    /**
     * The color id of the notification.
     */
    @JvmStatic
    @ColorRes
    var notificationColorResourceId: Int = R.color.primary_orange
}