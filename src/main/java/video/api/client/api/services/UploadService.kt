package video.api.client.api.services

import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import video.api.client.api.ApiClient
import video.api.client.api.R
import video.api.client.api.clients.VideosApi
import video.api.client.api.models.Environment
import video.api.client.api.models.Video
import video.api.client.api.upload.IProgressiveUploadSession
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Service that manages multiple video upload (with a queue) on background and its notifications.
 *
 * You might want to extent this service to customize notification icon, colors, messages.
 *
 * To add a service in your application you need to add in your `AndroidManifest.xml`:
 *  <application>
 *     <service android:name=".services.UploadService" />
 *     ...
 *  </application>
 *
 * and adds the `android.permission.FOREGROUND_SERVICE` permission to your `AndroidManifest.xml`:
 *  <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 *
 * @param notificationId The notification id for the foreground notification
 * @param channelId The id of the channel. Must be unique per package. The value may be truncated if it is too long.
 * @param channelNameResourceId The user visible name of the channel. The recommended maximum length is 40 characters; the value may be truncated if it is too long.
 * @param channelDescriptionResourceId A string resource identifier for the user visible description of the notification channel, or 0 if no description is provided.
 * @param notificationIconResourceId The resource id of the notification icon.
 * @param notificationColorResourceId The color id of the notification.
 */
open class UploadService(
    private val notificationId: Int = DEFAULT_NOTIFICATION_ID,
    protected val channelId: String = DEFAULT_NOTIFICATION_CHANNEL_ID,
    @StringRes private val channelNameResourceId: Int = R.string.channel_name,
    @StringRes private val channelDescriptionResourceId: Int = 0,
    @DrawableRes protected val notificationIconResourceId: Int = R.drawable.ic_api_video_logo,
    @ColorRes protected val notificationColorResourceId: Int = R.color.primary_orange
) : Service() {
    private val uploadFuturesMap = ConcurrentHashMap<String, Future<Video>>()
    private val binder = UploadServiceBinder()
    private lateinit var videosApi: VideosApi
    private var isBind: Boolean = false

    private val executor = Executors.newSingleThreadExecutor()
    private val listener = object : UploadServiceListener {
        override fun onUploadStarted(id: String) {
            onUploadStartedNotification(
                id,
            )?.let {
                notify(it)
            }
            listeners.forEach { it.onUploadStarted(id) }
        }

        override fun onUploadProgress(id: String, progress: Int) {
            onUploadProgressNotification(
                id,
                progress
            )?.let {
                notify(it)
            }
            listeners.forEach { it.onUploadProgress(id, progress) }
        }

        override fun onUploadError(id: String, e: Exception) {
            _numOfError++
            onUploadErrorNotification(id, e)?.let { notify(it) }
            listeners.forEach { it.onUploadError(id, e) }

            if (!hasRemaining) {
                onLastUploadNotification()?.let { notify(it) }
                listeners.forEach { it.onLastUpload() }
            }
        }

        override fun onUploadCancelled(id: String) {
            onUploadCancelledNotification(id)?.let { notify(it) }
            listeners.forEach { it.onUploadCancelled(id) }

            if (!hasRemaining) {
                onLastUploadNotification()?.let { notify(it) }
                listeners.forEach { it.onLastUpload() }
            }
        }

        override fun onUploadComplete(id: String, video: Video) {
            _numOfUploaded++
            onUploadSuccessNotification(id, video)?.let { notify(it) }
            listeners.forEach { it.onUploadComplete(id, video) }

            if (!hasRemaining) {
                onLastUploadNotification()?.let { notify(it) }
                listeners.forEach { it.onLastUpload() }
            }
        }

        override fun onLastUpload() {
           if (!isBind) {
               stopSelf()
           }
        }
    }

    protected val notificationUtils: NotificationUtils by lazy {
        NotificationUtils(this, channelId, notificationId)
    }
    private val listeners = mutableListOf<UploadServiceListener>()

    private var _totalNumOfUploads: Int = 0

    /**
     * Get number of uploads
     */
    val totalNumOfUploads: Int
        get() = _totalNumOfUploads

    private var _numOfCancelled: Int = 0

    /**
     * Get number of uploads that has been cancelled
     */
    val numOfCancelled: Int
        get() = _numOfCancelled

    /**
     * Get number of uploads that still need to be upload
     */
    val numOfRemaining: Int
        get() = totalNumOfUploads - numOfCancelled - numOfUploaded - numOfError

    private var _numOfError: Int = 0

    /**
     * Get number of failed uploads
     */
    val numOfError: Int
        get() = _numOfError

    private var _numOfUploaded: Int = 0

    /**
     * Get number of succeeded uploads
     */
    val numOfUploaded: Int
        get() = _numOfUploaded

    /**
     * Check if there is still file to upload
     */
    val hasRemaining: Boolean
        get() = numOfRemaining > 0

    /**
     * Cancel a specific upload
     *
     * @param id The unique upload id of the upload to cancel
     */
    fun cancel(id: String) {
        val future = uploadFuturesMap[id]

        future?.let {
            _numOfCancelled++
            it.cancel(true)
        } ?: throw NoSuchElementException("Failed to find upload id $id")
    }

    /**
     * Cancel all uploads.
     *
     * Also clean counter and flush internal list
     */
    fun cancelAll() {
        uploadFuturesMap.keys.forEach { cancel(it) }
        uploadFuturesMap.clear()

        _numOfUploaded = 0
        _numOfError = 0
        _numOfCancelled = 0
        _totalNumOfUploads = 0
    }

    fun addListener(listener: UploadServiceListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: UploadServiceListener) {
        listeners.remove(listener)
    }

    /**
     * Called when an error on a file happens
     *
     * You can customize the notification by overriding this method.
     *
     * @param id The video id or the token of the video
     * @param e The exception that has been throwned
     * @return The notification to be displayed or null
     */
    open fun onUploadErrorNotification(id: String, e: Exception): Notification? {
        return NotificationCompat.Builder(this, channelId)
            .setStyle(notificationIconResourceId, notificationColorResourceId)
            .setContentTitle(getString(R.string.notification_error_title))
            .setContentText(e.localizedMessage)
            .build()
    }

    /**
     * Called when upload progress on a file has changed
     *
     * You can customize the notification by overriding this method.
     *
     * @param id The video id or the token of the video
     * @param progress The progress of the upload
     * @return The notification to be displayed or null
     */
    open fun onUploadProgressNotification(id: String, progress: Int): Notification? {
        return NotificationCompat.Builder(this, channelId)
            .setStyle(notificationIconResourceId, notificationColorResourceId)
            .setOngoing(true)
            .setContentTitle(getString(R.string.notification_progress_title))
            .setContentText(
                getString(
                    R.string.notification_progress_text,
                    numOfUploaded,
                    totalNumOfUploads - numOfCancelled
                )
            )
            .setProgress(100, progress, false)
            .build()
    }

    /**
     * Called when a file will start to be uploaded
     *
     * You can customize the notification by overriding this method.
     *
     * @param id The video id or the token of the video
     * @return The notification to be displayed or null
     */
    open fun onUploadStartedNotification(id: String): Notification? = null

    /**
     * Called when the upload of the file has been cancelled
     *
     * You can customize the notification by overriding this method.
     *
     * @param id The video id or the token of the video
     * @return The notification to be displayed or null
     */
    open fun onUploadCancelledNotification(id: String): Notification? = null

    /**
     * Called when the a file was successfully uploaded
     *
     * You can customize the notification by overriding this method.
     *
     * @param video The video instance (contains info incl. the video id)
     * @return The notification to be displayed or null
     */
    open fun onUploadSuccessNotification(id: String, video: Video): Notification? = null

    /**
     * Called when the queue has no more file to upload
     */
    open fun onLastUploadNotification(): Notification? {
        cancelNotification()
        return null
    }

    /**
     * Upload a file from its video id
     *
     * @param videoId The video id or the token of the video
     * @param filePath The path of the file to upload
     * @return the unique upload id
     */
    fun upload(videoId: String, filePath: String) =
        upload(videoId, File(filePath))


    /**
     * Upload a file from its video id
     *
     * @param videoId The video id or the token of the video
     * @param file The file to upload
     * @return the unique upload id
     */
    fun upload(videoId: String, file: File): String {
        _totalNumOfUploads++

        val id = UUID.randomUUID().toString()
        val future = executor.submit(
            UploadTask(
                id,
                file,
                { fileToUpload, progressListener ->
                    videosApi.upload(
                        videoId,
                        fileToUpload,
                        progressListener
                    )
                },
                listener
            )
        )

        uploadFuturesMap[id] = future
        return id
    }

    /**
     * Upload a file from its upload token
     *
     * @param token The video id or the token of the video
     * @param filePath The path of the file to upload
     * @param videoId The optional video id if it was already created, otherwise it will be created if you pass null
     * @return the unique upload id
     */
    fun uploadWithUploadToken(token: String, filePath: String, videoId: String? = null) =
        uploadWithUploadToken(token, File(filePath), videoId)

    /**
     * Upload a file from its upload token
     *
     * @param token The video id or the token of the video
     * @param file The file to upload
     * @param videoId The optional video id if it was already created, otherwise it will be created if you pass null
     * @return the unique upload id
     */
    fun uploadWithUploadToken(token: String, file: File, videoId: String? = null): String {
        _totalNumOfUploads++

        val id = UUID.randomUUID().toString()
        val future = executor.submit(
            UploadTask(
                id,
                file,
                { fileToUpload, progressListener ->
                    videosApi.uploadWithUploadToken(
                        token,
                        fileToUpload,
                        videoId,
                        progressListener
                    )
                },
                listener
            )
        )

        uploadFuturesMap[id] = future
        return id
    }

    /**
     * Creates a [ProgressiveUploadSession] for a video id
     *
     * @param videoId The video id
     */
    fun createProgressiveUploadSession(videoId: String) =
        createProgressiveUploadSession(videosApi.createUploadProgressiveSession(videoId))

    /**
     * Creates a [ProgressiveUploadSession] for an upload token
     *
     * @param token The upload token
     * @param videoId The optional video id if it was already created, otherwise it will be created if you pass null
     */
    fun createUploadTokenProgressiveUploadSession(token: String, videoId: String? = null) =
        createProgressiveUploadSession(videosApi.createUploadWithUploadTokenProgressiveSession(token, videoId))

    /**
     * Creates a [ProgressiveUploadSession].
     *
     * @param session The progressive upload session either for upload token or video id
     */
    fun createProgressiveUploadSession(
        session: IProgressiveUploadSession
    ): ProgressiveUploadSession {
        return ProgressiveUploadSession(session)
    }

    /**
     * A decorator for progression upload session.
     *
     * @param session The progressive upload session either for upload token or video id
     */
    inner class ProgressiveUploadSession(
        private val session: IProgressiveUploadSession
    ) {
        /**
         * Upload a single part (not the last).
         *
         * @param filePath The path of the file to upload
         */
        fun uploadPart(filePath: String) =
            uploadPart(File(filePath))

        /**
         * Upload a single part (not the last).
         *
         * @param file The file to upload
         */
        fun uploadPart(file: File): String {
            _totalNumOfUploads++

            val id = UUID.randomUUID().toString()
            val future = executor.submit(
                UploadTask(
                    id,
                    file,
                    { fileToUpload, progressListener ->
                        session.uploadPart(
                            fileToUpload
                        ) { bytesWritten, totalBytes ->
                            progressListener.onProgress(
                                bytesWritten,
                                totalBytes,
                                0,
                                0
                            )
                        }
                    },
                    listener
                )
            )

            uploadFuturesMap[id] = future
            return id
        }

        /**
         * Upload a single part (not the last).
         *
         * @param filePath The path of the file to upload
         * @param partId The part id
         * @return the unique upload id
         */
        fun uploadPart(filePath: String, partId: Int) =
            uploadPart(File(filePath), partId)

        /**
         * Upload a single part (not the last).
         *
         * @param file The file to upload
         * @param partId The part id
         * @return the unique upload id
         */
        fun uploadPart(file: File, partId: Int): String {
            _totalNumOfUploads++

            val id = UUID.randomUUID().toString()
            val future = executor.submit(
                UploadTask(
                    id,
                    file,
                    { fileToUpload, progressListener ->
                        session.uploadPart(
                            fileToUpload,
                            partId
                        ) { bytesWritten, totalBytes ->
                            progressListener.onProgress(
                                bytesWritten,
                                totalBytes,
                                0,
                                0
                            )
                        }
                    },
                    listener
                )
            )

            uploadFuturesMap[id] = future
            return id
        }

        /**
         * Upload the last part.
         *
         * @param filePath The path of the file to upload
         * @return the unique upload id
         */
        fun uploadLastPart(filePath: String) =
            uploadLastPart(File(filePath))

        /**
         * Upload the last part.
         *
         * @param file The file to upload
         * @return the unique upload id
         */
        fun uploadLastPart(file: File): String {
            _totalNumOfUploads++

            val id = UUID.randomUUID().toString()
            val future = executor.submit(
                UploadTask(
                    id,
                    file,
                    { fileToUpload, progressListener ->
                        session.uploadLastPart(
                            fileToUpload
                        ) { bytesWritten, totalBytes ->
                            progressListener.onProgress(
                                bytesWritten,
                                totalBytes,
                                0,
                                0
                            )
                        }
                    },
                    listener
                )
            )

            uploadFuturesMap[id] = future
            return id
        }

        /**
         * Upload the last part.
         *
         * @param filePath The path of the file to upload
         * @param partId The part id
         * @return the unique upload id
         */
        fun uploadLastPart(filePath: String, partId: Int) =
            uploadLastPart(File(filePath), partId)

        /**
         * Upload the last part.
         *
         * @param file The file to upload
         * @param partId The part id
         * @return the unique upload id
         */
        fun uploadLastPart(file: File, partId: Int): String {
            _totalNumOfUploads++

            val id = UUID.randomUUID().toString()
            val future = executor.submit(
                UploadTask(
                    id,
                    file,
                    { fileToUpload, progressListener ->
                        session.uploadLastPart(
                            fileToUpload,
                            partId
                        ) { bytesWritten, totalBytes ->
                            progressListener.onProgress(
                                bytesWritten,
                                totalBytes,
                                0,
                                0
                            )
                        }
                    },
                    listener
                )
            )

            uploadFuturesMap[id] = future
            return id
        }
    }

    override fun onCreate() {
        super.onCreate()

        notificationUtils.createNotificationChannel(
            channelNameResourceId,
            channelDescriptionResourceId
        )
    }

    override fun onBind(intent: Intent): IBinder {
        val basePath = intent.getStringExtra(BASE_PATH_KEY) ?: Environment.SANDBOX.basePath
        val apiKey = intent.getStringExtra(API_KEY_KEY)

        val apiClient = apiKey?.let {
            ApiClient(it, basePath)
        } ?: ApiClient(basePath)

        if (intent.hasExtra(TIMEOUT_KEY)) {
            val timeout = intent.getIntExtra(TIMEOUT_KEY, 0)
            apiClient.writeTimeout = timeout
            apiClient.readTimeout = timeout
        }

        val sdkName = intent.getStringExtra(SDK_NAME_KEY)
        val sdkVersion = intent.getStringExtra(SDK_VERSION_KEY)
        if (sdkName != null && sdkVersion != null) {
            apiClient.setSdkName(sdkName, sdkVersion)
        } else {
            apiClient.setSdkName("service", "1.0.0")
        }

        val appName = intent.getStringExtra(APP_NAME_KEY)
        val appVersion = intent.getStringExtra(APP_VERSION_KEY)
        if (appName != null && appVersion != null) {
            apiClient.setSdkName(appName, appVersion)
        }

        videosApi = VideosApi(apiClient)


        isBind = true

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isBind = false
        return super.onUnbind(intent)
    }

    private fun notify(notification: Notification) {
        notificationUtils.notify(notification)
    }

    private fun cancelNotification() {
        notificationUtils.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdownNow()
        listeners.clear()
    }

    inner class UploadServiceBinder : Binder() {
        val service: UploadService = this@UploadService
    }

    companion object {
        const val TAG = "UploadService"

        const val DEFAULT_NOTIFICATION_CHANNEL_ID =
            "video.api.client.api.clients.service"
        const val DEFAULT_NOTIFICATION_ID = 3333

        const val BASE_PATH_KEY = "base_path_key"
        const val API_KEY_KEY = "api_key_key"
        const val TIMEOUT_KEY = "timeout_key"
        const val SDK_NAME_KEY = "sdk_name_key"
        const val SDK_VERSION_KEY = "sdk_version_key"
        const val APP_NAME_KEY = "app_name_key"
        const val APP_VERSION_KEY = "app_version_key"

       /**
         * Start a child of [UploadService].
         *
         * @param context The context of the application
         * @param serviceClass The class of the service to start. Must be a child of [UploadService].
         * @param apiKey The API key if you want to upload with video id
         * @param environment The targeted environment
         * @param timeout The API timeout in milliseconds
         * @param onServiceCreated The callback that returns the [UploadService] instance
         * @param onServiceDisconnected Called when service has been disconnected
         * @param appName The application name for stats
         * @param appVersion The application version for stats
         * @param sdkName The SDK name for stats (internal usage only)
         * @param sdkVersion The SDK version for stats (internal usage only)
         */
        fun startService(
            context: Context,
            serviceClass: Class<out UploadService>,
            apiKey: String?,
            environment: Environment = Environment.PRODUCTION,
            timeout: Int? = null,
            onServiceCreated: (UploadService) -> Unit,
            onServiceDisconnected: (name: ComponentName?) -> Unit,
            appName: String? = null,
            appVersion: String? = null,
            sdkName: String? = null,
            sdkVersion: String? = null
        ): ServiceConnection {
            context.startService(Intent(context, serviceClass))

            val connection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
                    if (binder is UploadService.UploadServiceBinder) {
                        onServiceCreated(binder.service)
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    onServiceDisconnected(name)
                }
            }

            val intent = Intent(context, serviceClass).apply {
                apiKey?.let { putExtra(API_KEY_KEY, it) }
                putExtra(BASE_PATH_KEY, environment.basePath)
                timeout?.let { putExtra(TIMEOUT_KEY, it) }
                sdkName?.let { putExtra(SDK_NAME_KEY, it) }
                sdkVersion?.let { putExtra(SDK_VERSION_KEY, it) }
                appName?.let { putExtra(APP_NAME_KEY, it) }
                appVersion?.let { putExtra(APP_VERSION_KEY, it) }
            }

            context.bindService(
                intent,
                connection,
                Context.BIND_AUTO_CREATE
            )

            return connection
        }

        fun unbindService(
            context: Context,
            serviceConnection: ServiceConnection
        ) {
            context.unbindService(serviceConnection)
        }

        fun stopService(
            context: Context,
            serviceClass: Class<out UploadService>
        ) {
            context.stopService(Intent(context, serviceClass))
        }
    }

    protected fun NotificationCompat.Builder.setStyle(
        @DrawableRes notificationIconResourceId: Int,
        @ColorRes notificationColorResourceId: Int
    ): NotificationCompat.Builder = apply {
        setSmallIcon(notificationIconResourceId)
        color = getColor(notificationColorResourceId)
    }
}

