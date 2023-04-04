package video.api.client.api.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import video.api.client.api.upload.IProgressiveUploadSession
import video.api.client.api.work.UploadWorkerHelper.upload
import video.api.client.api.work.stores.ProgressiveUploadSessionStore
import video.api.client.api.work.workers.ProgressiveUploadWorker
import video.api.client.api.work.workers.UploadWorker
import java.io.File

/**
 * Helper class to initialize the WorkManager and enqueue upload works.
 */
object UploadWorkerHelper {
    /**
     * Enqueues a work to upload a file.
     *
     * @param context The application context
     * @param videoId The video id
     * @param file The file to upload
     * @param tags The custom tags to add to identify the work
     */
    @JvmStatic
    fun upload(
        context: Context,
        videoId: String,
        file: File,
        tags: List<String> = emptyList()
    ) =
        upload(WorkManager.getInstance(context), videoId, file, tags)

    /**
     * Enqueues a work to upload a file.
     *
     * @param context The application context
     * @param videoId The video id
     * @param file The file to upload
     * @param tags The custom tags to add to identify the work
     * @param workerClass The worker class to use. Default is [UploadWorker].
     */
    @JvmStatic
    fun upload(
        context: Context,
        videoId: String,
        file: File,
        tags: List<String> = emptyList(),
        workerClass: Class<out UploadWorker> = UploadWorker::class.java
    ) =
        upload(WorkManager.getInstance(context), videoId, file, tags, workerClass)

    /**
     * Enqueues a work to upload a file.
     *
     * @param workManager The WorkManager instance
     * @param videoId The video id
     * @param file The file to upload
     * @param tags The custom tags to add to identify the work
     */
    @JvmStatic
    fun upload(
        workManager: WorkManager,
        videoId: String,
        file: File,
        tags: List<String> = emptyList()
    ) =
        upload(workManager, videoId, file, tags, UploadWorker::class.java)

    /**
     * Enqueues a work to upload a file.
     *
     * @param workManager The WorkManager instance
     * @param videoId The video id
     * @param file The file to upload
     * @param tags The custom tags to add to identify the work
     * @param workerClass The worker class to use. Default is [UploadWorker].
     */
    fun upload(
        workManager: WorkManager,
        videoId: String,
        file: File,
        tags: List<String> = emptyList(),
        workerClass: Class<out UploadWorker>
    ) =
        upload(workManager, file, null, videoId, tags, workerClass)

    /**
     * Enqueues a work to upload a file with upload token.
     *
     * @param context The application context
     * @param token The upload token
     * @param file The file to upload
     * @param videoId The video id. Can be null if the video is not created yet.
     * @param tags The custom tags to add to identify the work
     */
    @JvmStatic
    fun uploadWithUploadToken(
        context: Context,
        token: String,
        file: File,
        videoId: String? = null,
        tags: List<String> = emptyList()
    ) = uploadWithUploadToken(
        WorkManager.getInstance(context),
        token,
        file,
        videoId,
        tags,
        UploadWorker::class.java
    )

    /**
     * Enqueues a work to upload a file with upload token.
     *
     * @param context The application context
     * @param token The upload token
     * @param file The file to upload
     * @param videoId The video id. Can be null if the video is not created yet.
     * @param workerClass The worker class to use. Default is [UploadWorker].
     * @param tags The custom tags to add to identify the work
     */
    @JvmStatic
    fun uploadWithUploadToken(
        context: Context,
        token: String,
        file: File,
        videoId: String? = null,
        tags: List<String> = emptyList(),
        workerClass: Class<out UploadWorker>
    ) = uploadWithUploadToken(
        WorkManager.getInstance(context),
        token,
        file,
        videoId,
        tags,
        workerClass
    )

    /**
     * Enqueues a work to upload a file with upload token.
     *
     * @param workManager The WorkManager instance
     * @param token The upload token
     * @param file The file to upload
     * @param videoId The video id. Can be null if the video is not created yet.
     * @param tags The custom tags to add to identify the work
     */
    @JvmStatic
    fun uploadWithUploadToken(
        workManager: WorkManager,
        token: String,
        file: File,
        videoId: String? = null,
        tags: List<String> = emptyList()
    ) = uploadWithUploadToken(
        workManager,
        token,
        file,
        videoId,
        tags,
        UploadWorker::class.java
    )

    /**
     * Enqueues a work to upload a file with upload token.
     *
     * @param workManager The WorkManager instance
     * @param token The upload token
     * @param file The file to upload
     * @param videoId The video id. Can be null if the video is not created yet.
     * @param tags The custom tags to add to identify the work
     * @param workerClass The worker class to use. Default is [UploadWorker].
     */
    @JvmStatic
    fun uploadWithUploadToken(
        workManager: WorkManager,
        token: String,
        file: File,
        videoId: String? = null,
        tags: List<String> = emptyList(),
        workerClass: Class<out UploadWorker>,
    ) = upload(workManager, file, token, videoId, tags, workerClass)

    private fun upload(
        workManager: WorkManager,
        file: File,
        token: String? = null,
        videoId: String? = null,
        tags: List<String> = emptyList(),
        workerClass: Class<out UploadWorker>
    ): OperationWithRequest {
        require((token != null) || (videoId != null)) {
            "You must provide either a token or a videoId"
        }

        val workRequest = OneTimeWorkRequest.Builder(workerClass)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setInputData(
                UploadWorker.createInputData(
                    token,
                    videoId,
                    file
                )
            )
            .addTag("android-api-client")
            .addTag(getTagForUpload(videoId, token))
            .apply {
                tags.forEach { addTag(it) }
            }
            .build()
        return OperationWithRequest(workManager.enqueue(workRequest), workRequest)
    }

    /**
     * Cancels all works related to a video id that was added with [upload].
     * Works with upload token are not cancelled.
     *
     * @param context The application context
     * @param videoId The video id
     */
    @JvmStatic
    fun cancel(context: Context, videoId: String) =
        cancel(WorkManager.getInstance(context), videoId)

    /**
     * Cancels all works related to a video id that was added with [upload].
     * Works with upload token are not cancelled.
     *
     * @param workManager The WorkManager instance
     * @param videoId The video id
     */
    @JvmStatic
    fun cancel(workManager: WorkManager, videoId: String) =
        workManager.cancelAllWorkByTag(getTagForUpload(videoId, null))

    /**
     * Cancels all works related to an upload token and possibly a video id that was added with [uploadWithUploadToken].
     * Works without upload token are not cancelled.
     *
     * @param context The application context
     * @param token The upload token
     * @param videoId The video id.Must be the same as the one used in [uploadWithUploadToken].
     */
    @JvmStatic
    fun cancelWithUploadToken(context: Context, token: String, videoId: String? = null) =
        cancelWithUploadToken(WorkManager.getInstance(context), token, videoId)

    /**
     * Cancels all works related to an upload token and possibly a video id that was added with [uploadWithUploadToken].
     * Works without upload token are not cancelled.
     *
     * @param workManager The WorkManager instance
     * @param token The upload token
     * @param videoId The video id. Must be the same as the one used in [uploadWithUploadToken].
     */
    @JvmStatic
    fun cancelWithUploadToken(workManager: WorkManager, token: String, videoId: String? = null) =
        workManager.cancelAllWorkByTag(getTagForUpload(videoId, token))

    private const val PREFIX_VIDEO_ID = "videoId="
    private const val PREFIX_TOKEN = "token="

    /**
     * Returns the tag used to identify works related to a video id or an upload token.
     *
     * @param videoId The video id
     * @param token The upload token
     * @return The tag
     */
    fun getTagForUpload(videoId: String?, token: String?): String {
        require((token != null) || (videoId != null)) {
            "You must provide either a token or a videoId"
        }
        return "($PREFIX_VIDEO_ID$videoId, $PREFIX_TOKEN$token)"
    }

    /**
     * Enqueues a work to upload a part of a file.
     *
     * @param context The application context
     * @param session The progressive upload session
     * @param file The file to upload
     * @param isLastPart True if this is the last part of the file
     * @param partId The part id. If null, the part id will be manage automatically.
     * @param tags The custom tags to add to identify the work
     */
    @JvmStatic
    private fun uploadPart(
        context: Context,
        session: IProgressiveUploadSession,
        file: File,
        isLastPart: Boolean,
        partId: Int? = null,
        tags: List<String> = emptyList(),
    ) = uploadPart(
        WorkManager.getInstance(context),
        session,
        file,
        isLastPart,
        partId,
        tags,
        ProgressiveUploadWorker::class.java
    )

    /**
     * Enqueues a work to upload a part of a file.
     *
     * @param context The application context
     * @param session The progressive upload session
     * @param file The file to upload
     * @param isLastPart True if this is the last part of the file
     * @param partId The part id. If null, the part id will be manage automatically.
     * @param tags The custom tags to add to identify the work
     * @param workerClass The worker class to use. Default is [ProgressiveUploadWorker].
     */
    @JvmStatic
    private fun uploadPart(
        context: Context,
        session: IProgressiveUploadSession,
        file: File,
        isLastPart: Boolean,
        partId: Int? = null,
        tags: List<String> = emptyList(),
        workerClass: Class<out ProgressiveUploadWorker>
    ) = uploadPart(
        WorkManager.getInstance(context),
        session,
        file,
        isLastPart,
        partId,
        tags,
        workerClass
    )

    /**
     * Enqueues a work to upload a part of a file.
     *
     * @param workManager The WorkManager instance
     * @param session The progressive upload session
     * @param file The file to upload
     * @param isLastPart True if this is the last part of the file
     * @param partId The part id. If null, the part id will be manage automatically.
     * @param tags The custom tags to add to identify the work
     */
    @JvmStatic
    fun uploadPart(
        workManager: WorkManager,
        session: IProgressiveUploadSession,
        file: File,
        isLastPart: Boolean,
        partId: Int? = null,
        tags: List<String> = emptyList()
    ) = uploadPart(
        workManager,
        session,
        file,
        isLastPart,
        partId,
        tags,
        ProgressiveUploadWorker::class.java
    )

    /**
     * Enqueues a work to upload a part of a file.
     *
     * @param workManager The WorkManager instance
     * @param session The progressive upload session
     * @param file The file to upload
     * @param isLastPart True if this is the last part of the file
     * @param partId The part id. If null, the part id will be manage automatically.
     * @param tags The custom tags to add to identify the work
     * @param workerClass The worker class to use. Default is [ProgressiveUploadWorker].
     */
    @JvmStatic
    fun uploadPart(
        workManager: WorkManager,
        session: IProgressiveUploadSession,
        file: File,
        isLastPart: Boolean,
        partId: Int? = null,
        tags: List<String> = emptyList(),
        workerClass: Class<out ProgressiveUploadWorker>
    ): OperationWithRequest {
        val sessionIndex = ProgressiveUploadSessionStore.add(session)

        val workRequest = OneTimeWorkRequest.Builder(workerClass)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setInputData(
                ProgressiveUploadWorker.createInputData(
                    sessionIndex,
                    file,
                    isLastPart,
                    partId
                )
            )
            .addTag("android-api-client")
            .addTag("progressive")
            .addTag(getTagForProgressiveUploadSession(session))
            .apply {
                tags.forEach { addTag(it) }
            }
            .build()
        return OperationWithRequest(workManager.enqueue(workRequest), workRequest)
    }

    /**
     * Cancels all works related to a progressive upload session.
     *
     * @param context The application context
     * @param session The progressive upload session
     */
    @JvmStatic
    fun cancelProgressiveUploadSession(context: Context, session: IProgressiveUploadSession) =
        cancelProgressiveUploadSession(WorkManager.getInstance(context), session)

    /**
     * Cancels all works related to a progressive upload session.
     *
     * @param workManager The WorkManager instance
     * @param session The progressive upload session
     */
    @JvmStatic
    fun cancelProgressiveUploadSession(
        workManager: WorkManager,
        session: IProgressiveUploadSession
    ) {
        workManager.cancelAllWorkByTag(getTagForProgressiveUploadSession(session))
    }

    private const val PREFIX_SESSION_ID = "session="

    /**
     * Returns the tag used to identify works related to a progressive upload session.
     *
     * @param session The progressive upload session
     * @return The tag
     */
    fun getTagForProgressiveUploadSession(session: IProgressiveUploadSession): String {
        return "($PREFIX_SESSION_ID$session)"
    }
}
