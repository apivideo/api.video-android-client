package video.api.client.api.work.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import video.api.client.api.JSON
import video.api.client.api.upload.UploadProgressListener
import video.api.client.api.work.stores.VideosApiStore
import video.api.client.api.work.workers.UploadWorker.Companion.TOKEN_KEY
import video.api.client.api.work.workers.UploadWorker.Companion.VIDEO_ID_KEY
import java.io.File
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resumeWithException

/**
 * Worker that uploads a video to api.video.
 * It requires that [VideosApiStore] is initialized.
 *
 * For progressive upload, see [ProgressiveUploadWorker].
 *
 * Input arguments are:
 * - [TOKEN_KEY]: The upload token
 * - [VIDEO_ID_KEY]: The video id
 * - [AbstractUploadWorker.FILE_PATH_KEY]: The file path
 *
 * Progress is reported through a notification and listening to [WorkInfo] progress [AbstractUploadWorker.PROGRESS_KEY].
 *
 * Output arguments are:
 * - [AbstractUploadWorker.VIDEO_KEY]: The upload progress
 * - [AbstractUploadWorker.FILE_PATH_KEY]: The file path
 *
 * @param context The application context
 * @param workerParams The parameters of the work
 */
class UploadWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    AbstractUploadWorker(context, workerParams), UploadProgressListener {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun doWork(): Result {
        createNotificationChannel()
        setForeground(createForegroundInfo(onUploadStarted()))

        val videosApi = VideosApiStore.getInstance()

        val token = inputData.getString(TOKEN_KEY)
        val videoId = inputData.getString(VIDEO_ID_KEY)
        val filePath = inputData.getString(FILE_PATH_KEY)

        if ((token == null) && (videoId == null)) {
            createErrorNotification(IOException("Missing token or video id"))
            return Result.failure()
        }
        if (filePath == null) {
            createErrorNotification(IOException("File path not set"))
            return Result.failure()
        }

        val file = File(filePath)

        /**
         * Use an executor to make the coroutine cancellable.
         */
        return try {
            val video = suspendCancellableCoroutine { continuation ->
                val future = uploaderExecutor.submit {
                    try {
                        val video = if (token == null) {
                            videosApi.upload(
                                videoId, file, this@UploadWorker
                            )
                        } else {
                            videosApi.uploadWithUploadToken(
                                token,
                                file,
                                videoId,
                                this@UploadWorker
                            )
                        }
                        continuation.resume(video, null)
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    }
                }
                continuation.invokeOnCancellation {
                    future.cancel(true)
                }
            }
            Result.success(
                workDataOf(
                    FILE_PATH_KEY to filePath,
                    VIDEO_KEY to JSON().serialize(video)
                )
            )
        } catch (e: CancellationException) {
            Log.i(TAG, "Upload cancelled")
            Result.failure(workDataOf(ERROR_KEY to e.message))
        } catch (e: Exception) {
            Log.e(TAG, "Upload failed", e)
            createErrorNotification(e)
            Result.failure(workDataOf(ERROR_KEY to e.message))
        }
    }

    override fun onProgress(
        bytesWritten: Long,
        totalBytes: Long,
        chunkCount: Int,
        chunkNum: Int
    ) {
        updateProgress((bytesWritten * 100 / totalBytes).toInt())
    }

    companion object {
        private const val TAG = "UploadWorker"

        private const val TOKEN_KEY = "token"
        private const val VIDEO_ID_KEY = "videoId"

        /**
         * Creates the input data for the worker
         *
         * @param token The upload token
         * @param videoId The video id
         * @param file The file to upload
         * @return The input data
         */
        fun createInputData(
            token: String?,
            videoId: String?,
            file: File
        ): Data {
            require((token != null) || (videoId != null)) { "Either token or videoId must be provided" }

            return Data.Builder()
                .putString(TOKEN_KEY, token)
                .putString(VIDEO_ID_KEY, videoId)
                .putString(FILE_PATH_KEY, file.absolutePath)
                .build()
        }
    }
}