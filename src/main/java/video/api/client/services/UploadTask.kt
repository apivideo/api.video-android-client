package video.api.client.services

import android.util.Log
import com.google.common.base.Throwables
import video.api.client.api.models.Video
import video.api.client.api.upload.UploadProgressListener
import java.io.File
import java.io.InterruptedIOException
import java.util.concurrent.Callable

class UploadTask(
    private val videoIdOrToken: String,
    private val file: File,
    private val onUploadTask: (String, File, UploadProgressListener) -> Video,
    private val listener: UploadServiceListener
) : Callable<Video> {
    companion object {
        private const val TAG = "UploadTask"
    }

    override fun call(): Video {
        return try {
            listener.onUploadStarted(videoIdOrToken)
            val video =
                onUploadTask(videoIdOrToken, file) { bytesWritten, totalBytes, _, _ ->
                    val progress = (bytesWritten.toFloat() / totalBytes * 100).toInt()
                    listener.onUploadProgress(videoIdOrToken, progress)
                }
            listener.onUploadComplete(video)
            video
        } catch (e: Exception) {
            if (Throwables.getRootCause(e) is InterruptedIOException) {
                Log.i(TAG, "Interrupt by user")
                listener.onUploadCancelled(videoIdOrToken)
            } else {
                listener.onUploadError(videoIdOrToken, e)
            }
            throw e
        }
    }
}