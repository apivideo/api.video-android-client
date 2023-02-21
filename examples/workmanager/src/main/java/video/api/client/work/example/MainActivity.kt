package video.api.client.work.example

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import video.api.client.ApiVideoClient
import video.api.client.api.ApiCallback
import video.api.client.api.ApiException
import video.api.client.api.JSON
import video.api.client.api.models.Environment
import video.api.client.api.models.Video
import video.api.client.api.models.VideoCreationPayload
import video.api.client.api.work.workers.AbstractUploadWorker.Companion.PROGRESS_KEY
import video.api.client.api.work.workers.AbstractUploadWorker
import video.api.client.api.work.stores.VideosApiStore
import video.api.client.api.work.toProgress
import video.api.client.api.work.toVideo
import video.api.client.api.work.upload
import video.api.client.work.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val storePermissionManager = ReadStorePermissionManager(this,
        onGranted = { launchFilePickerIntent() },
        onShowPermissionRationale = { permission, onRequiredPermissionLastTime ->
            // Explain why we need permissions
            showDialog(
                title = "Permission needed",
                message = "Explain why you need to grant $permission permission to stream",
                positiveButtonText = R.string.accept,
                onPositiveButtonClick = { onRequiredPermissionLastTime() },
                negativeButtonText = R.string.denied
            )
        },
        onDenied = {
            showDialog(
                "Permission denied", "You need to grant permission to stream"
            )
        })
    private val environment: Environment
        get() = if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(
                getString(R.string.environment_key), true
            )
        ) {
            Environment.SANDBOX
        } else {
            Environment.PRODUCTION
        }
    private val apiKey: String?
        get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString(getString(R.string.api_key_key), null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /**
         * Provides the configuration for the API endpoints.
         */
        VideosApiStore.initialize(apiKey, environment)

        supportFragmentManager.beginTransaction().replace(R.id.settingsLayout, SettingsFragment())
            .commit()

        binding.pickFiles.setOnClickListener {
            Log.i(getString(R.string.app_name), "Requesting permission")
            storePermissionManager.requestPermission()
        }

        binding.cancel.setOnClickListener {
            WorkManager.getInstance(applicationContext).cancelAllWork()
        }
    }

    private fun launchFilePickerIntent() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "video/mp4"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        filesPickerResult.launch(intent)
    }

    private var filesPickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                binding.progressBar.visibility = View.VISIBLE

                result.data?.let { data ->
                    // Multiple files
                    data.clipData?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            clipData.getItemAt(i).uri?.let { uri ->
                                val path = this@MainActivity.getFilePath(uri)!!
                                createVideo(
                                    path
                                ) { video ->
                                    upload(video.videoId, path)
                                }
                            }
                        }
                    } ?: run {
                        // Single file
                        data.data?.let { uri ->
                            val path = this@MainActivity.getFilePath(uri)!!
                            createVideo(
                                path
                            ) { video ->
                                upload(video.videoId, path)
                            }
                        }
                    }
                } ?: Log.e(TAG, "No data received")
            }
        }

    private fun upload(videoId: String, path: String) {
        val workManager = WorkManager.getInstance(applicationContext)
        val operationWithRequest = workManager.upload(videoId, path)

        onUploadStarted(operationWithRequest.request.id.toString())

        val workInfoliveData = workManager.getWorkInfoByIdLiveData(operationWithRequest.request.id)
        runOnUiThread {
            workInfoliveData.observe(this) { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.RUNNING -> {
                            onUploadProgress(
                                operationWithRequest.request.id.toString(),
                                workInfo.progress.toProgress()
                            )
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            onUploadComplete(
                                operationWithRequest.request.id.toString(),
                                workInfo.outputData.toVideo()
                            )
                        }
                        WorkInfo.State.FAILED -> {
                            onUploadError(
                                operationWithRequest.request.id.toString(),
                                workInfo.outputData.getString(AbstractUploadWorker.ERROR_KEY) ?: "Unknown error"
                            )
                        }
                        WorkInfo.State.CANCELLED -> {
                            onUploadCancelled(operationWithRequest.request.id.toString())
                        }
                        else -> {
                            // Do nothing
                        }
                    }
                }
            }
        }
    }

    /**
     * Create a video id from path
     */
    private fun createVideo(path: String, onVideoCreated: (Video) -> Unit) {
        val videoName = path.split("/").last()
        ApiVideoClient(apiKey, environment).videos()
            .createAsync(VideoCreationPayload().apply { title = videoName },
                object : ApiCallback<Video> {
                    override fun onFailure(
                        e: ApiException?,
                        statusCode: Int,
                        responseHeaders: MutableMap<String, MutableList<String>>?
                    ) {
                        runOnUiThread {
                            this@MainActivity.showDialog(
                                getString(R.string.error),
                                getString(R.string.failed_to_create_video, e?.localizedMessage)
                            )
                        }
                    }

                    override fun onSuccess(
                        result: Video?,
                        statusCode: Int,
                        responseHeaders: MutableMap<String, MutableList<String>>?
                    ) {
                        onVideoCreated(result!!)
                    }

                    override fun onUploadProgress(
                        bytesWritten: Long, contentLength: Long, done: Boolean
                    ) {
                        // Nothing to do
                    }

                    override fun onDownloadProgress(
                        bytesRead: Long, contentLength: Long, done: Boolean
                    ) {
                        // Nothing to do
                    }

                })
    }

    private fun onUploadStarted(id: String) {
        Log.i(TAG, "Started to upload: $id")

        runOnUiThread {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar.progress = 0
            binding.cancel.visibility = View.VISIBLE
        }
    }

    private fun onUploadComplete(id: String, video: Video) {
        Log.i(
            TAG, "File has been successfully upload $id: ${video.videoId}"
        )

        runOnUiThread {
            showDialog(
                getString(R.string.success), getString(R.string.file_uploaded)
            )
        }
    }

    private fun onUploadProgress(id: String, progress: Int) {
        binding.progressBar.progress = progress
    }

    private fun onUploadError(id: String, error: String) {
        Log.i(
            TAG, "Failed to send $id: $error"
        )

        runOnUiThread {
            showDialog(
                getString(R.string.error), getString(R.string.upload_failed, error)
            )
        }
    }

    private fun onUploadCancelled(id: String) {
        // Nothing
    }

    private fun onLastUpload() {
        Log.i(TAG, "Last upload")

        runOnUiThread {
            binding.progressBar.visibility = View.GONE
            binding.cancel.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "UploadActivity"
    }
}