package video.api.client.api.work

import androidx.work.Data
import video.api.client.api.JSON
import video.api.client.api.models.Video
import video.api.client.api.work.workers.AbstractUploadWorker
import java.io.File

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
 * Extension functions for [Data] to retrieve the file.
 *
 * @return The [File]
 */
fun Data.toFile(): File {
    val filePath = this.getString(AbstractUploadWorker.FILE_PATH_KEY)
    return if (filePath != null) {
        File(filePath)
    } else {
        throw IllegalStateException("File path is null")
    }
}

/**
 * Extension functions for [Data] to retrieve the progress.
 *
 * @return The progress
 */
fun Data.toProgress(): Int = this.getInt(AbstractUploadWorker.PROGRESS_KEY, 0)