package video.api.client.api.work

import androidx.work.Data
import androidx.work.WorkManager
import video.api.client.api.JSON
import video.api.client.api.models.Video
import video.api.client.api.upload.IProgressiveUploadSession
import video.api.client.api.work.stores.ProgressiveUploadSessionStore
import video.api.client.api.work.workers.AbstractUploadWorker
import video.api.client.api.work.workers.ProgressiveUploadWorker
import video.api.client.api.work.workers.UploadWorker
import java.io.File

/**
 * Extension functions for [WorkManager] to enqueue upload works.
 *
 * @param videoId The video id
 * @param file The file to upload
 * @param workerClass The worker class to use. Default is [UploadWorker].
 */
fun WorkManager.upload(
    videoId: String,
    file: File,
    workerClass: Class<out UploadWorker> = UploadWorker::class.java
) =
    UploadWorkerHelper.upload(this, videoId, file, workerClass)

/**
 * Extension functions for [WorkManager] to enqueue upload works.
 *
 * @param videoId The video id
 * @param filePath The path of the file to upload
 * @param workerClass The worker class to use. Default is [UploadWorker].
 */
fun WorkManager.upload(
    videoId: String,
    filePath: String,
    workerClass: Class<out UploadWorker> = UploadWorker::class.java
) =
    UploadWorkerHelper.upload(this, videoId, File(filePath), workerClass)

/**
 * Extension functions for [WorkManager] to enqueue upload works with upload token.
 *
 * @param token The upload token
 * @param file The file to upload
 * @param videoId The video id. Can be null if the video is not created yet.
 * @param workerClass The worker class to use. Default is [UploadWorker].
 */
fun WorkManager.uploadWithUploadToken(
    token: String,
    file: File,
    videoId: String? = null,
    workerClass: Class<out UploadWorker> = UploadWorker::class.java
) =
    UploadWorkerHelper.uploadWithUploadToken(this, token, file, videoId, workerClass)

/**
 * Extension functions for [WorkManager] to enqueue upload works with upload token.
 *
 * @param token The upload token
 * @param filePath The path of the file to upload
 * @param videoId The video id. Can be null if the video is not created yet.
 * @param workerClass The worker class to use. Default is [UploadWorker].
 */
fun WorkManager.uploadWithUploadToken(
    token: String,
    filePath: String,
    videoId: String? = null,
    workerClass: Class<out UploadWorker> = UploadWorker::class.java
) =
    UploadWorkerHelper.uploadWithUploadToken(
        this,
        token,
        File(filePath),
        videoId,
        workerClass
    )

/**
 * Extension functions for [WorkManager] to cancel upload works that was added with [WorkManager.upload].
 *
 * @param videoId The video id
 */
fun WorkManager.cancel(videoId: String) = UploadWorkerHelper.cancel(this, videoId)

/**
 * Extension functions for [WorkManager] to cancel upload works that was added with [WorkManager.uploadWithUploadToken].
 *
 * @param token The upload token
 * @param videoId The video id. Must be the same as the one used in [WorkManager.uploadWithUploadToken].
 */
fun WorkManager.cancelWithUploadToken(token: String, videoId: String? = null) =
    UploadWorkerHelper.cancelWithUploadToken(this, token, videoId)

/**
 * Extension functions for [WorkManager] to enqueue upload works for progressive upload.
 *
 * @param session The progressive upload session
 * @param file The file to upload
 * @param isLastPart True if this is the last part of the upload
 * @param workerClass The worker class to use. Default is [ProgressiveUploadWorker].
 */
fun WorkManager.uploadPart(
    session: IProgressiveUploadSession,
    file: File,
    isLastPart: Boolean,
    workerClass: Class<out ProgressiveUploadWorker> = ProgressiveUploadWorker::class.java
) =
    UploadWorkerHelper.uploadPart(this, session, file, isLastPart, workerClass)

/**
 * Extension functions for [WorkManager] to enqueue upload works for progressive upload.
 *
 * @param session The progressive upload session
 * @param filePath The path of the file to upload
 * @param isLastPart True if this is the last part of the upload
 * @param workerClass The worker class to use. Default is [ProgressiveUploadWorker].
 */
fun WorkManager.uploadPart(
    session: IProgressiveUploadSession,
    filePath: String,
    isLastPart: Boolean,
    workerClass: Class<out ProgressiveUploadWorker> = ProgressiveUploadWorker::class.java
) =
    UploadWorkerHelper.uploadPart(this, session, File(filePath), isLastPart, workerClass)

/**
 * Extension functions for [WorkManager] to cancel upload works that was added with [WorkManager.uploadPart].
 *
 * @param session The progressive upload session
 */
fun WorkManager.cancelProgressiveUploadSession(session: IProgressiveUploadSession) =
    UploadWorkerHelper.cancelProgressiveUploadSession(this, session)

/**
 * Extension functions for [Data] to deserialize the [Video].
 *
 * @return The [Video] object
 */
fun Data.toVideo() = JSON().deserialize(
    this.getString(
        AbstractUploadWorker.VIDEO_KEY
    ), Video::class.java
) as Video

/**
 * Extension functions for [Data] to deserialize the [Video].
 *
 * @return The [Video] object
 */
fun Data.toProgress(): Int = this.getInt(AbstractUploadWorker.PROGRESS_KEY, 0)