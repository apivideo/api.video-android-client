package video.api.client.api.work

import androidx.work.WorkManager
import video.api.client.api.upload.IProgressiveUploadSession
import java.io.File

/**
 * Extension functions for [WorkManager] to enqueue upload works.
 *
 * @param videoId The video id
 * @param file The file to upload
 */
fun WorkManager.upload(videoId: String, file: File) =
    UploadWorkerManagerHelper.upload(this, videoId, file)

/**
 * Extension functions for [WorkManager] to enqueue upload works.
 *
 * @param videoId The video id
 * @param filePath The path of the file to upload
 */
fun WorkManager.upload(videoId: String, filePath: String) =
    UploadWorkerManagerHelper.upload(this, videoId, File(filePath))

/**
 * Extension functions for [WorkManager] to enqueue upload works with upload token.
 *
 * @param token The upload token
 * @param file The file to upload
 * @param videoId The video id. Can be null if the video is not created yet.
 */
fun WorkManager.uploadWithUploadToken(token: String, file: File, videoId: String? = null) =
    UploadWorkerManagerHelper.uploadWithUploadToken(this, token, file, videoId)

/**
 * Extension functions for [WorkManager] to enqueue upload works with upload token.
 *
 * @param token The upload token
 * @param filePath The path of the file to upload
 * @param videoId The video id. Can be null if the video is not created yet.
 */
fun WorkManager.uploadWithUploadToken(token: String, filePath: String, videoId: String? = null) =
    UploadWorkerManagerHelper.uploadWithUploadToken(this, token, File(filePath), videoId)

/**
 * Extension functions for [WorkManager] to cancel upload works that was added with [WorkManager.upload].
 *
 * @param videoId The video id
 */
fun WorkManager.cancel(videoId: String) = UploadWorkerManagerHelper.cancel(this, videoId)

/**
 * Extension functions for [WorkManager] to cancel upload works that was added with [WorkManager.uploadWithUploadToken].
 *
 * @param token The upload token
 * @param videoId The video id. Must be the same as the one used in [WorkManager.uploadWithUploadToken].
 */
fun WorkManager.cancelWithUploadToken(token: String, videoId: String? = null) =
    UploadWorkerManagerHelper.cancelWithUploadToken(this, token, videoId)

/**
 * Extension functions for [WorkManager] to enqueue upload works for progressive upload.
 *
 * @param session The progressive upload session
 * @param file The file to upload
 * @param isLastPart True if this is the last part of the upload
 */
fun WorkManager.uploadPart(
    session: IProgressiveUploadSession,
    file: File,
    isLastPart: Boolean
) =
    UploadWorkerManagerHelper.uploadPart(this, session, file, isLastPart)

/**
 * Extension functions for [WorkManager] to enqueue upload works for progressive upload.
 *
 * @param session The progressive upload session
 * @param filePath The path of the file to upload
 * @param isLastPart True if this is the last part of the upload
 */
fun WorkManager.uploadPart(
    session: IProgressiveUploadSession,
    filePath: String,
    isLastPart: Boolean
) =
    UploadWorkerManagerHelper.uploadPart(this, session, File(filePath), isLastPart)

/**
 * Extension functions for [WorkManager] to cancel upload works that was added with [WorkManager.uploadPart].
 *
 * @param session The progressive upload session
 */
fun WorkManager.cancelProgressiveUploadSession(session: IProgressiveUploadSession) =
    UploadWorkerManagerHelper.cancelProgressiveUploadSession(this, session)
