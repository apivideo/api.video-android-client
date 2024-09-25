package video.api.client.api.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import video.api.client.api.upload.IProgressiveUploadSession
import video.api.client.api.work.UploadWorkerHelper.upload
import video.api.client.api.work.stores.ProgressiveUploadSessionStore
import video.api.client.api.work.utils.md5
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
            .addTag(ARTIFACT_ID)
            .apply {
                videoId?.let { addTag(getTagForVideoId(it)) }
                token?.let { addTag(getTagForUploadToken(it)) }
                tags.forEach { addTag(it) }
            }
            .build()
        return OperationWithRequest(workManager.enqueue(workRequest), workRequest)
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
            .addTag(ARTIFACT_ID)
            .addTag("progressive")
            .apply {
                session.videoId?.let { addTag(getTagForVideoId(it)) }
                session.token?.let { addTag(getTagForUploadToken(it)) }
                tags.forEach { addTag(it) }
            }
            .build()
        return OperationWithRequest(workManager.enqueue(workRequest), workRequest)
    }

    /**
     * Cancels all upload works.
     *
     * @param context The application context
     */
    @JvmStatic
    fun cancelAll(context: Context) =
        cancelAll(WorkManager.getInstance(context))

    /**
     * Cancels all upload works.
     *
     * @param workManager The WorkManager instance
     */
    @JvmStatic
    fun cancelAll(workManager: WorkManager) =
        workManager.cancelAllWorkByTag(ARTIFACT_ID)

    /**
     * Cancels all works related to a video id.
     *
     * @param context The application context
     * @param videoId The video id
     */
    @JvmStatic
    fun cancel(context: Context, videoId: String) =
        cancel(WorkManager.getInstance(context), videoId)

    /**
     * Cancels all works related to a video id.
     *
     * @param workManager The WorkManager instance
     * @param videoId The video id
     */
    @JvmStatic
    fun cancel(workManager: WorkManager, videoId: String) =
        workManager.cancelAllWorkByTag(getTagForVideoId(videoId))

    /**
     * Cancels all works related to an upload token that was added with [uploadWithUploadToken].
     *
     * @param context The application context
     * @param token The upload token
     */
    @JvmStatic
    fun cancelWithUploadToken(context: Context, token: String) =
        cancelWithUploadToken(WorkManager.getInstance(context), token)

    /**
     * Cancels all works related to an upload token that was added with [uploadWithUploadToken].
     *
     * @param workManager The WorkManager instance
     * @param token The upload token
     */
    @JvmStatic
    fun cancelWithUploadToken(workManager: WorkManager, token: String) =
        workManager.cancelAllWorkByTag(getTagForUploadToken(token))

    /**
     * Returns the tag used to identify works related to a video id.
     *
     * @param videoId The video id
     * @return The tag
     */
    fun getTagForVideoId(videoId: String): String {
        return "($PREFIX_VIDEO_ID${videoId.md5()})"
    }

    /**
     * Returns the tag used to identify works related to an upload token.
     *
     * @param token The upload token
     * @return The tag
     */
    fun getTagForUploadToken(token: String): String {
        return "($PREFIX_TOKEN${token.md5()})"
    }

    private const val PREFIX_VIDEO_ID = "videoId="
    private const val PREFIX_TOKEN = "token="
    private const val ARTIFACT_ID = "android-api-client"
}
