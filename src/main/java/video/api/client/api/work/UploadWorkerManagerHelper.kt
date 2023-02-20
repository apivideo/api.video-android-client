package video.api.client.api.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import video.api.client.api.upload.IProgressiveUploadSession
import video.api.client.api.work.UploadWorkerManagerHelper.upload
import video.api.client.api.work.stores.ProgressiveUploadSessionStore
import video.api.client.api.work.worker.ProgressiveUploadWorker
import video.api.client.api.work.worker.UploadWorker
import java.io.File

/**
 * Helper class to initialize the WorkManager and enqueue upload works.
 */
object UploadWorkerManagerHelper {
    /**
     * Enqueues a work to upload a file.
     *
     * @param context The application context
     * @param videoId The video id
     * @param file The file to upload
     */
    @JvmStatic
    fun upload(context: Context, videoId: String, file: File) =
        upload(WorkManager.getInstance(context), videoId, file)

    /**
     * Enqueues a work to upload a file.
     *
     * @param workManager The WorkManager instance
     * @param videoId The video id
     * @param file The file to upload
     */
    @JvmStatic
    fun upload(workManager: WorkManager, videoId: String, file: File) =
        upload(workManager, file, null, videoId)

    /**
     * Enqueues a work to upload a file with upload token.
     *
     * @param context The application context
     * @param token The upload token
     * @param file The file to upload
     * @param videoId The video id. Can be null if the video is not created yet.
     */
    @JvmStatic
    fun uploadWithUploadToken(
        context: Context,
        token: String,
        file: File,
        videoId: String? = null
    ) = uploadWithUploadToken(WorkManager.getInstance(context), token, file, videoId)

    /**
     * Enqueues a work to upload a file with upload token.
     *
     * @param workManager The WorkManager instance
     * @param token The upload token
     * @param file The file to upload
     * @param videoId The video id. Can be null if the video is not created yet.
     */
    @JvmStatic
    fun uploadWithUploadToken(
        workManager: WorkManager,
        token: String,
        file: File,
        videoId: String? = null
    ) = upload(workManager, file, token, videoId)

    private fun upload(
        workManager: WorkManager,
        file: File,
        token: String? = null,
        videoId: String? = null,
    ): OperationWithRequest {
        require((token != null) || (videoId != null)) {
            "You must provide either a token or a videoId"
        }

        val workRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
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
            .addTag(generateTag(videoId, token))
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
        workManager.cancelAllWorkByTag(generateTag(videoId, null))

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
        workManager.cancelAllWorkByTag(generateTag(videoId, token))

    private const val PREFIX_VIDEO_ID = "videoId="
    private const val PREFIX_TOKEN = "token="
    private fun generateTag(videoId: String?, token: String?) =
        "($PREFIX_VIDEO_ID$videoId, $PREFIX_TOKEN$token)"

    /**
     * Enqueues a work to upload a part of a file.
     *
     * @param context The application context
     * @param session The progressive upload session
     * @param file The file to upload
     * @param isLastPart True if this is the last part of the file
     */
    @JvmStatic
    private fun uploadPart(
        context: Context,
        session: IProgressiveUploadSession,
        file: File,
        isLastPart: Boolean,
    ) = uploadPart(WorkManager.getInstance(context), session, file, isLastPart)

    /**
     * Enqueues a work to upload a part of a file.
     *
     * @param workManager The WorkManager instance
     * @param session The progressive upload session
     * @param file The file to upload
     * @param isLastPart True if this is the last part of the file
     */
    @JvmStatic
    fun uploadPart(
        workManager: WorkManager,
        session: IProgressiveUploadSession,
        file: File,
        isLastPart: Boolean,
    ): OperationWithRequest {
        val sessionIndex = ProgressiveUploadSessionStore.add(session)

        val workRequest = OneTimeWorkRequest.Builder(ProgressiveUploadWorker::class.java)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setInputData(
                ProgressiveUploadWorker.createInputData(
                    sessionIndex,
                    file,
                    isLastPart
                )
            )
            .addTag(generateSessionTag(sessionIndex))
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
        val sessionIndex = ProgressiveUploadSessionStore.indexOf(session)
        workManager.cancelAllWorkByTag(generateSessionTag(sessionIndex))
    }

    private const val PREFIX_SESSION_ID = "sessionIndex="
    private fun generateSessionTag(sessionIndex: Int) =
        "($PREFIX_SESSION_ID$sessionIndex)"
}
