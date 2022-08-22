package video.api.client.api.services

import video.api.client.api.models.Video

interface UploadServiceListener {
    /**
     * Call when a new file is going to be upload
     *
     * @param id The unique upload id
     */
    fun onUploadStarted(id: String)

    /**
     * Call to update upload progress on a file
     *
     * @param id The unique upload id
     */
    fun onUploadProgress(id: String, progress: Int)

    /**
     * Call when the upload failed
     *
     * @param id The unique upload id
     * @param e The reason why upload failed
     */
    fun onUploadError(id: String, e: Exception)

    /**
     * Call when the upload has been cancelled
     *
     * @param id The unique upload id
     */
    fun onUploadCancelled(id: String)

    /**
     * Call when the file was successfully uploaded
     *
     * @param video The uploaded video
     */
    fun onUploadComplete(video: Video)

    /**
     * Call when there are no more file to upload
     */
    fun onLastUpload()
}