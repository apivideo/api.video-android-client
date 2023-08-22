package video.api.client.api.work.workers

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import kotlinx.coroutines.*
import video.api.client.api.R
import video.api.client.api.notifications.NotificationUtils
import video.api.client.api.notifications.setStyle
import video.api.client.api.work.stores.NotificationConfigurationStore
import video.api.client.api.work.stores.NotificationConfigurationStore.channelId
import video.api.client.api.work.stores.NotificationConfigurationStore.notificationColorResourceId
import video.api.client.api.work.stores.NotificationConfigurationStore.notificationIconResourceId
import java.util.concurrent.Executors

/**
 * Worker that uploads a video to api.video.
 *
 * For progressive upload with upload token, you need to limit the number of executors to a single
 * thread executor in your [androidx.work.Configuration].
 *
 * Input arguments depends on the implementation.
 *
 * @param context The application context
 * @param workerParams The parameters of the work
 */
abstract class AbstractUploadWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)
    private var previousProgress = 0

    protected fun updateProgress(progress: Int) {
        if (isStopped) return
        if (previousProgress == progress) return

        previousProgress = progress
        CoroutineScope(Dispatchers.IO).launch {
            onUploadProgressNotification(progress)?.let {
                notify(it)
            }

            setProgress(
                workDataOf(
                    PROGRESS_KEY to progress
                )
            )
        }
    }

    protected fun createNotificationChannel() =
        NotificationUtils.createNotificationChannel(
            applicationContext,
            channelId,
            NotificationConfigurationStore.channelNameResourceId,
            NotificationConfigurationStore.channelDescriptionResourceId
        )

    @SuppressLint("MissingPermission")
    protected fun notify(notification: Notification) {
        notificationManager.notify(NotificationConfigurationStore.notificationId, notification)
    }

    fun createErrorNotification(e: Exception) {
        onUploadError(e)?.let { notify(it) }
    }

    /**
     * Called when the upload starts.
     *
     * You can customize the notification by overriding this method.
     *
     * @return The notification to be displayed or null
     */
    open fun onUploadStarted(): Notification {
        return NotificationCompat.Builder(applicationContext, channelId)
            .setStyle(applicationContext, notificationIconResourceId, notificationColorResourceId)
            .setContentTitle(applicationContext.getString(R.string.upload_notification_upload_started_title))
            .build()
    }

    /**
     * Called when an exception occurred during upload
     *
     * You can customize the notification by overriding this method.
     *
     * @param e The exception that occurred
     * @return The notification to be displayed or null
     */
    open fun onUploadError(e: Exception): Notification? {
        return NotificationCompat.Builder(applicationContext, channelId)
            .setStyle(applicationContext, notificationIconResourceId, notificationColorResourceId)
            .setContentTitle(applicationContext.getString(R.string.upload_notification_error_title))
            .setContentText(e.localizedMessage)
            .build()
    }

    /**
     * Called when upload progress on a file has changed
     *
     * You can customize the notification by overriding this method.
     *
     * @param progress The progress of the upload
     * @return The notification to be displayed or null
     */
    open fun onUploadProgressNotification(progress: Int): Notification? {
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        return NotificationCompat.Builder(applicationContext, channelId)
            .setStyle(
                applicationContext,
                notificationIconResourceId,
                notificationColorResourceId
            )
            .setOngoing(true)
            .setContentTitle(applicationContext.getString(R.string.upload_notification_progress_title))
            .setContentText(
                applicationContext.getString(
                    R.string.upload_notification_current_progress_text,
                    progress
                )
            )
            .setProgress(100, progress, false)
            .addAction(
                android.R.drawable.ic_delete,
                applicationContext.getString(R.string.upload_notification_cancel),
                intent
            )
            .build()
    }

    protected fun createForegroundInfo(notification: Notification): ForegroundInfo {
        return ForegroundInfo(NotificationConfigurationStore.notificationId, notification)
    }

    companion object {
        const val PROGRESS_KEY = "progress"
        const val VIDEO_KEY = "video"
        const val ERROR_KEY = "error"
        const val FILE_PATH_KEY = "filePath"

        /**
         * A single thread executor to be used for upload to avoid parallel uploads.
         */
        internal val uploaderExecutor = Executors.newSingleThreadExecutor()
    }
}