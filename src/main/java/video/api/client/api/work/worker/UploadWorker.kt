package video.api.client.api.work.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.withContext
import video.api.client.api.JSON
import video.api.client.api.upload.UploadProgressListener
import video.api.client.api.work.stores.VideosApiStore
import video.api.client.api.work.worker.UploadWorker.Companion.FILE
import video.api.client.api.work.worker.UploadWorker.Companion.TOKEN
import video.api.client.api.work.worker.UploadWorker.Companion.VIDEO_ID
import java.io.File
import java.io.IOException

/**
 * Worker that uploads a video to api.video.
 * It requires that [VideosApiStore] is initialized.
 *
 * For progressive upload, see [ProgressiveUploadWorker].
 *
 * Input arguments are:
 * - [TOKEN]: The upload token
 * - [VIDEO_ID]: The video id
 * - [FILE]: The file path
 *
 * Progress is reported through a notification and listening to [WorkInfo] progress [AbstractUploadWorker.PROGRESS_KEY].
 *
 * Output arguments are:
 * - [AbstractUploadWorker.VIDEO_KEY]: The upload progress
 *
 * @param context The application context
 * @param workerParams The parameters of the work
 */
class UploadWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    AbstractUploadWorker(context, workerParams), UploadProgressListener {

    override suspend fun doWork(): Result {
        createNotificationChannel()
        setForeground(createForegroundInfo(onUploadStarted()))

        val videosApi = VideosApiStore.getInstance()

        val token = inputData.getString(TOKEN)
        val videoId = inputData.getString(VIDEO_ID)
        val filePath = inputData.getString(FILE)

        if ((token == null) && (videoId == null)) {
            createErrorNotification(IOException("Missing token or video id"))
            return Result.failure()
        }
        if (filePath == null) {
            createErrorNotification(IOException("File path not set"))
            return Result.failure()
        }

        val file = File(filePath)

        return try {
            val video = withContext(limitedCoroutineContext) {
                if (token == null) {
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
            }
            Result.success(workDataOf(VIDEO_KEY to JSON().serialize(video)))
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

        private const val TOKEN = "token"
        private const val VIDEO_ID = "videoId"
        private const val FILE = "file"

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
                .putString(TOKEN, token)
                .putString(VIDEO_ID, videoId)
                .putString(FILE, file.absolutePath)
                .build()
        }
    }
}