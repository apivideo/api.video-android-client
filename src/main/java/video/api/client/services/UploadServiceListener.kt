package video.api.client.services

import video.api.client.api.models.Video

interface UploadServiceListener {
    fun onUploadStarted(id: String)
    fun onUploadProgress(id: String, progress: Int)
    fun onUploadError(id: String, e: Exception)
    fun onUploadCancelled(id: String)
    fun onUploadComplete(video: Video)
    fun onLastUpload()
}