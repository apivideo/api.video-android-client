package video.api.client.api.work.stores

import video.api.client.api.ApiClient
import video.api.client.api.clients.VideosApi
import video.api.client.api.models.Environment

/**
 * A store for [VideosApi]s.
 */
object VideosApiStore {
    private const val DEFAULT_SDK_NAME = "android-workmanager"
    private const val DEFAULT_SDK_VERSION = "1.0.0"

    private var videosApi: VideosApi? = null

    /**
     * Initialize the store with a custom [VideosApi].
     *
     * @param videosApi The [VideosApi] to use.
     */
    @JvmStatic
    fun initialize(videosApi: VideosApi) {
        if (!videosApi.apiClient.hasSdkName()) {
            videosApi.apiClient.setSdkName(DEFAULT_SDK_NAME, DEFAULT_SDK_VERSION)
        }
        VideosApiStore.videosApi = videosApi
    }

    /**
     * Initialize the store.
     *
     * @param apiKey The API key if you want to upload with video id
     * @param environment The targeted environment
     * @param timeout The API timeout in milliseconds
     * @param appName The application name for stats
     * @param appVersion The application version for stats
     * @param sdkName The SDK name for stats (internal usage only)
     * @param sdkVersion The SDK version for stats (internal usage only)
     */
    @JvmStatic
    fun initialize(
        apiKey: String? = null,
        environment: Environment = Environment.PRODUCTION,
        timeout: Int = 30_000,
        appName: String? = null,
        appVersion: String? = null,
        sdkName: String? = null,
        sdkVersion: String? = null
    ) {
        val apiClient = (apiKey?.let {
            ApiClient(it, environment.basePath)
        } ?: ApiClient(environment.basePath)).apply {
            if (appName != null && appVersion != null) {
                setApplicationName(appName, appVersion)
            }
            if (sdkName != null && sdkVersion != null) {
                setSdkName(sdkName, sdkVersion)
            }
            readTimeout = timeout
            writeTimeout = timeout
        }
        initialize(VideosApi(apiClient))
    }

    /**
     * Get the [VideosApi] instance.
     *
     * @return The [VideosApi] instance.
     */
    @JvmStatic
    fun getInstance(): VideosApi {
        return videosApi ?: throw IllegalStateException("VideosApi not initialized")
    }
}