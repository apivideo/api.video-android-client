package video.api.client.api.work.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.withContext
import video.api.client.api.JSON
import video.api.client.api.upload.UploadPartProgressListener
import video.api.client.api.work.stores.ProgressiveUploadSessionStore
import java.io.File
import java.io.IOException


/**
 * Worker that uploads a video to api.video.
 *
 * For regular upload, see [UploadWorker].
 *
 * Input arguments are:
 * - [SESSION_ID_KEY]: The progressive session id in [ProgressiveUploadSessionStore]
 * - [AbstractUploadWorker.FILE_PATH_KEY]: The file path
 * - [IS_LAST_PART_KEY]: Weather or not this is the last part
 * - [PART_ID_KEY]: The part id. Optional.
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
class ProgressiveUploadWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    AbstractUploadWorker(context, workerParams), UploadPartProgressListener {

    override suspend fun doWork(): Result {
        createNotificationChannel()
        setForeground(createForegroundInfo(onUploadStarted()))

        val sessionIndex = inputData.getInt(SESSION_ID_KEY, DEFAULT_SESSION_ID)
        val filePath = inputData.getString(FILE_PATH_KEY)
        val isLastPart = inputData.getBoolean(IS_LAST_PART_KEY, false)
        val partId = inputData.getInt(PART_ID_KEY, DEFAULT_PART_ID)

        if (sessionIndex == DEFAULT_SESSION_ID) {
            createErrorNotification(IOException("Missing session id"))
            return Result.failure()
        }
        if (filePath == null) {
            createErrorNotification(IOException("File path not set"))
            return Result.failure()
        }

        val file = File(filePath)

        return try {
            val video = withContext(limitedCoroutineContext) {
                if (partId != DEFAULT_PART_ID) {
                    ProgressiveUploadSessionStore.get(sessionIndex)!!
                        .uploadPart(file, partId, isLastPart, this@ProgressiveUploadWorker)
                } else {
                    ProgressiveUploadSessionStore.get(sessionIndex)!!
                        .uploadPart(file, isLastPart, this@ProgressiveUploadWorker)
                }
            }
            Result.success(
                workDataOf(
                    FILE_PATH_KEY to filePath,
                    VIDEO_KEY to JSON().serialize(video)
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Upload failed", e)
            createErrorNotification(e)
            Result.failure(workDataOf(ERROR_KEY to e.message))
        }
    }

    override fun onProgress(bytesWritten: Long, totalBytes: Long) {
        updateProgress((bytesWritten * 100 / totalBytes).toInt())
    }

    companion object {
        private const val TAG = "ProgressiveUploadWorker"

        private const val SESSION_ID_KEY = "sessionId"
        private const val IS_LAST_PART_KEY = "isLastPart"
        private const val PART_ID_KEY = "partId"

        private const val DEFAULT_SESSION_ID = -1
        private const val DEFAULT_PART_ID = -1

        /**
         * Creates the input data for the worker
         *
         * @param sessionId The upload session id
         * @param file The file to upload
         * @param isLastPart Whether this is the last part of the upload
         * @param partId The part id
         * @return The input data
         */
        fun createInputData(
            sessionId: Int,
            file: File,
            isLastPart: Boolean,
            partId: Int? = null
        ): Data {
            return Data.Builder()
                .putInt(SESSION_ID_KEY, sessionId)
                .putString(FILE_PATH_KEY, file.absolutePath)
                .putBoolean(IS_LAST_PART_KEY, isLastPart)
                .putInt(PART_ID_KEY, partId ?: DEFAULT_PART_ID)
                .build()
        }
    }
}