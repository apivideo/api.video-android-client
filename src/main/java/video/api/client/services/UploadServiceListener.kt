package video.api.client.services

import video.api.client.api.models.Video

interface UploadServiceListener {
    fun onUploadStarted(videoIdOrToken: String)
    fun onUploadProgress(videoIdOrToken: String, progress: Int)
    fun onUploadError(videoIdOrToken: String, e: Exception)
    fun onUploadCancelled(videoIdOrToken: String)
    fun onUploadComplete(video: Video)
    fun onLastUpload()
}