[![badge](https://img.shields.io/twitter/follow/api_video?style=social)](https://twitter.com/intent/follow?screen_name=api_video) &nbsp; [![badge](https://img.shields.io/github/stars/apivideo/api.video-android-client?style=social)](https://github.com/apivideo/api.video-android-client) &nbsp; [![badge](https://img.shields.io/discourse/topics?server=https%3A%2F%2Fcommunity.api.video)](https://community.api.video)
![](https://github.com/apivideo/.github/blob/main/assets/apivideo_banner.png)
<h1 align="center">api.video Android </h1>

[api.video](https://api.video) is the video infrastructure for product builders. Lightning fast video APIs for integrating, scaling, and managing on-demand & low latency live streaming features in your app.

# Table of contents

- [Project description](#project-description)
- [Getting started](#getting-started)
  - [Requirements](#requirements)
  - [Installation](#installation)
    - [Maven users](#maven-users)
    - [Gradle users](#gradle-users)
    - [Others](#others)
  - [Code sample](#code-sample)
  - [Upload options](#upload-options)
  - [Permissions](#permissions)
- [Documentation](#documentation)
  - [API Endpoints](#api-endpoints)
    - [CaptionsApi](#captionsapi)
    - [ChaptersApi](#chaptersapi)
    - [LiveStreamsApi](#livestreamsapi)
    - [PlayerThemesApi](#playerthemesapi)
    - [RawStatisticsApi](#rawstatisticsapi)
    - [UploadTokensApi](#uploadtokensapi)
    - [VideosApi](#videosapi)
    - [WatermarksApi](#watermarksapi)
    - [WebhooksApi](#webhooksapi)
  - [Models](#models)
  - [Authorization](#documentation-for-authorization)
    - [API key](#api-key)
    - [Public endpoints](#public-endpoints)
  - [Recommendation](#recommendation)
- [Have you gotten use from this API client?](#have-you-gotten-use-from-this-api-client-)
- [Contribution](#contribution)

# Project description

api.video's Android  streamlines the coding process. Chunking files is handled for you, as is pagination and refreshing your tokens.

# Getting started

## Requirements

Building the API client library requires:
1. Java 1.8+
2. Maven/Gradle

## Installation

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>video.api</groupId>
  <artifactId>android-api-client</artifactId>
  <version>1.3.2</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
implementation "video.api:android-api-client:1.3.2"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/android-api-client-1.3.2.jar`
* `target/lib/*.jar`

## Code sample

Please follow the [installation](#installation) instruction and execute the following Kotlin code:

```kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import video.api.client.ApiVideoClient
import video.api.client.api.ApiException
import video.api.client.api.models.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val apiVideoClient = ApiVideoClient("YOUR_API_KEY")
        // if you rather like to use the sandbox environment:
        // val apiVideoClient = ApiVideoClient("YOU_SANDBOX_API_KEY", Environment.SANDBOX)

        val myVideoFile = File("my-video.mp4")

        /**
         * Notice: you must not call API from the UI/main thread. Dispatch with Thread, Executors or Kotlin coroutines.
         */
        executor.execute {
            try {
                var video = apiVideoClient.videos().create(VideoCreationPayload().title("my video"))
                video = apiVideoClient.videos().upload(video.videoId, myVideoFile)
                Log.i("Example", "$video")
            } catch (e: ApiException) {
                Log.e("Example", "Exception when calling VideoApi")
                e.message?.let {
                    Log.e("Example", "Reason: ${it}")
                }
            }
        }
    }
}

```

### Example

Examples that demonstrate how to use the API is provided in folder `examples/`.

## Upload options

To upload a video, you have 3 differents options:
* WorkManager: preferred option: Upload with Android WorkManager API. It supports progress notifications. Directly use, WorkManager extensions. See [example](examples/workmanager) for more details.
* UploadService: Upload with an Android Service. It supports progress notifications. You have to extend the `UploadService` and register it in your `AndroidManifest.xml`. See [example](examples/service) for more details.
* Direct call with `ApiClient`: Do not call API from the main thread, otherwise you will get a android.os.NetworkOnMainThreadException. Dispatch API calls with Thread, Executors or Kotlin coroutine to avoid this.

## Permissions

You have to add the following permissions in your `AndroidManifest.xml`:

```xml
    <uses-permission android:name="android.permission.INTERNET" />
<!-- Application requires android.permission.READ_EXTERNAL_STORAGE to upload videos` -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
``` 

Your application also has to dynamically request the `android.permission.READ_EXTERNAL_STORAGE` permission to upload videos.

# Documentation

## API Endpoints

All URIs are relative to *https://ws.api.video*


### CaptionsApi


#### Retrieve an instance of CaptionsApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val captions = client.captions()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**upload**](docs/CaptionsApi.md#upload) | **POST** /videos/{videoId}/captions/{language} | Upload a caption
[**get**](docs/CaptionsApi.md#get) | **GET** /videos/{videoId}/captions/{language} | Retrieve a caption
[**update**](docs/CaptionsApi.md#update) | **PATCH** /videos/{videoId}/captions/{language} | Update a caption
[**delete**](docs/CaptionsApi.md#delete) | **DELETE** /videos/{videoId}/captions/{language} | Delete a caption
[**list**](docs/CaptionsApi.md#list) | **GET** /videos/{videoId}/captions | List video captions


### ChaptersApi


#### Retrieve an instance of ChaptersApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val chapters = client.chapters()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**upload**](docs/ChaptersApi.md#upload) | **POST** /videos/{videoId}/chapters/{language} | Upload a chapter
[**get**](docs/ChaptersApi.md#get) | **GET** /videos/{videoId}/chapters/{language} | Retrieve a chapter
[**delete**](docs/ChaptersApi.md#delete) | **DELETE** /videos/{videoId}/chapters/{language} | Delete a chapter
[**list**](docs/ChaptersApi.md#list) | **GET** /videos/{videoId}/chapters | List video chapters


### LiveStreamsApi


#### Retrieve an instance of LiveStreamsApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val liveStreams = client.liveStreams()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](docs/LiveStreamsApi.md#create) | **POST** /live-streams | Create live stream
[**get**](docs/LiveStreamsApi.md#get) | **GET** /live-streams/{liveStreamId} | Retrieve live stream
[**update**](docs/LiveStreamsApi.md#update) | **PATCH** /live-streams/{liveStreamId} | Update a live stream
[**delete**](docs/LiveStreamsApi.md#delete) | **DELETE** /live-streams/{liveStreamId} | Delete a live stream
[**list**](docs/LiveStreamsApi.md#list) | **GET** /live-streams | List all live streams
[**uploadThumbnail**](docs/LiveStreamsApi.md#uploadThumbnail) | **POST** /live-streams/{liveStreamId}/thumbnail | Upload a thumbnail
[**deleteThumbnail**](docs/LiveStreamsApi.md#deleteThumbnail) | **DELETE** /live-streams/{liveStreamId}/thumbnail | Delete a thumbnail


### PlayerThemesApi


#### Retrieve an instance of PlayerThemesApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val playerThemes = client.playerThemes()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](docs/PlayerThemesApi.md#create) | **POST** /players | Create a player
[**get**](docs/PlayerThemesApi.md#get) | **GET** /players/{playerId} | Retrieve a player
[**update**](docs/PlayerThemesApi.md#update) | **PATCH** /players/{playerId} | Update a player
[**delete**](docs/PlayerThemesApi.md#delete) | **DELETE** /players/{playerId} | Delete a player
[**list**](docs/PlayerThemesApi.md#list) | **GET** /players | List all player themes
[**uploadLogo**](docs/PlayerThemesApi.md#uploadLogo) | **POST** /players/{playerId}/logo | Upload a logo
[**deleteLogo**](docs/PlayerThemesApi.md#deleteLogo) | **DELETE** /players/{playerId}/logo | Delete logo


### RawStatisticsApi


#### Retrieve an instance of RawStatisticsApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val rawStatistics = client.rawStatistics()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**listLiveStreamSessions**](docs/RawStatisticsApi.md#listLiveStreamSessions) | **GET** /analytics/live-streams/{liveStreamId} | List live stream player sessions
[**listSessionEvents**](docs/RawStatisticsApi.md#listSessionEvents) | **GET** /analytics/sessions/{sessionId}/events | List player session events
[**listVideoSessions**](docs/RawStatisticsApi.md#listVideoSessions) | **GET** /analytics/videos/{videoId} | List video player sessions


### UploadTokensApi


#### Retrieve an instance of UploadTokensApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val uploadTokens = client.uploadTokens()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**createToken**](docs/UploadTokensApi.md#createToken) | **POST** /upload-tokens | Generate an upload token
[**getToken**](docs/UploadTokensApi.md#getToken) | **GET** /upload-tokens/{uploadToken} | Retrieve upload token
[**deleteToken**](docs/UploadTokensApi.md#deleteToken) | **DELETE** /upload-tokens/{uploadToken} | Delete an upload token
[**list**](docs/UploadTokensApi.md#list) | **GET** /upload-tokens | List all active upload tokens.


### VideosApi


#### Retrieve an instance of VideosApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val videos = client.videos()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](docs/VideosApi.md#create) | **POST** /videos | Create a video
[**upload**](docs/VideosApi.md#upload) | **POST** /videos/{videoId}/source | Upload a video
[**uploadWithUploadToken**](docs/VideosApi.md#uploadWithUploadToken) | **POST** /upload | Upload with an upload token
[**get**](docs/VideosApi.md#get) | **GET** /videos/{videoId} | Retrieve a video
[**update**](docs/VideosApi.md#update) | **PATCH** /videos/{videoId} | Update a video
[**delete**](docs/VideosApi.md#delete) | **DELETE** /videos/{videoId} | Delete a video
[**list**](docs/VideosApi.md#list) | **GET** /videos | List all videos
[**uploadThumbnail**](docs/VideosApi.md#uploadThumbnail) | **POST** /videos/{videoId}/thumbnail | Upload a thumbnail
[**pickThumbnail**](docs/VideosApi.md#pickThumbnail) | **PATCH** /videos/{videoId}/thumbnail | Pick a thumbnail
[**getStatus**](docs/VideosApi.md#getStatus) | **GET** /videos/{videoId}/status | Retrieve video status


### WatermarksApi


#### Retrieve an instance of WatermarksApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val watermarks = client.watermarks()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**upload**](docs/WatermarksApi.md#upload) | **POST** /watermarks | Upload a watermark
[**delete**](docs/WatermarksApi.md#delete) | **DELETE** /watermarks/{watermarkId} | Delete a watermark
[**list**](docs/WatermarksApi.md#list) | **GET** /watermarks | List all watermarks


### WebhooksApi


#### Retrieve an instance of WebhooksApi:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
val webhooks = client.webhooks()
```



#### Endpoints

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](docs/WebhooksApi.md#create) | **POST** /webhooks | Create Webhook
[**get**](docs/WebhooksApi.md#get) | **GET** /webhooks/{webhookId} | Retrieve Webhook details
[**delete**](docs/WebhooksApi.md#delete) | **DELETE** /webhooks/{webhookId} | Delete a Webhook
[**list**](docs/WebhooksApi.md#list) | **GET** /webhooks | List all webhooks



## Documentation for Models

 - [AccessToken](docs/AccessToken.md)
 - [AuthenticatePayload](docs/AuthenticatePayload.md)
 - [BadRequest](docs/BadRequest.md)
 - [BytesRange](docs/BytesRange.md)
 - [Caption](docs/Caption.md)
 - [CaptionsListResponse](docs/CaptionsListResponse.md)
 - [CaptionsUpdatePayload](docs/CaptionsUpdatePayload.md)
 - [Chapter](docs/Chapter.md)
 - [ChaptersListResponse](docs/ChaptersListResponse.md)
 - [Link](docs/Link.md)
 - [LiveStream](docs/LiveStream.md)
 - [LiveStreamAssets](docs/LiveStreamAssets.md)
 - [LiveStreamCreationPayload](docs/LiveStreamCreationPayload.md)
 - [LiveStreamListResponse](docs/LiveStreamListResponse.md)
 - [LiveStreamSession](docs/LiveStreamSession.md)
 - [LiveStreamSessionClient](docs/LiveStreamSessionClient.md)
 - [LiveStreamSessionDevice](docs/LiveStreamSessionDevice.md)
 - [LiveStreamSessionLocation](docs/LiveStreamSessionLocation.md)
 - [LiveStreamSessionReferrer](docs/LiveStreamSessionReferrer.md)
 - [LiveStreamSessionSession](docs/LiveStreamSessionSession.md)
 - [LiveStreamUpdatePayload](docs/LiveStreamUpdatePayload.md)
 - [Metadata](docs/Metadata.md)
 - [NotFound](docs/NotFound.md)
 - [Pagination](docs/Pagination.md)
 - [PaginationLink](docs/PaginationLink.md)
 - [PlayerSessionEvent](docs/PlayerSessionEvent.md)
 - [PlayerTheme](docs/PlayerTheme.md)
 - [PlayerThemeAssets](docs/PlayerThemeAssets.md)
 - [PlayerThemeCreationPayload](docs/PlayerThemeCreationPayload.md)
 - [PlayerThemeUpdatePayload](docs/PlayerThemeUpdatePayload.md)
 - [PlayerThemesListResponse](docs/PlayerThemesListResponse.md)
 - [Quality](docs/Quality.md)
 - [RawStatisticsListLiveStreamAnalyticsResponse](docs/RawStatisticsListLiveStreamAnalyticsResponse.md)
 - [RawStatisticsListPlayerSessionEventsResponse](docs/RawStatisticsListPlayerSessionEventsResponse.md)
 - [RawStatisticsListSessionsResponse](docs/RawStatisticsListSessionsResponse.md)
 - [RefreshTokenPayload](docs/RefreshTokenPayload.md)
 - [TokenCreationPayload](docs/TokenCreationPayload.md)
 - [TokenListResponse](docs/TokenListResponse.md)
 - [UploadToken](docs/UploadToken.md)
 - [Video](docs/Video.md)
 - [VideoAssets](docs/VideoAssets.md)
 - [VideoClip](docs/VideoClip.md)
 - [VideoCreationPayload](docs/VideoCreationPayload.md)
 - [VideoSession](docs/VideoSession.md)
 - [VideoSessionClient](docs/VideoSessionClient.md)
 - [VideoSessionDevice](docs/VideoSessionDevice.md)
 - [VideoSessionLocation](docs/VideoSessionLocation.md)
 - [VideoSessionOs](docs/VideoSessionOs.md)
 - [VideoSessionReferrer](docs/VideoSessionReferrer.md)
 - [VideoSessionSession](docs/VideoSessionSession.md)
 - [VideoSource](docs/VideoSource.md)
 - [VideoSourceLiveStream](docs/VideoSourceLiveStream.md)
 - [VideoSourceLiveStreamLink](docs/VideoSourceLiveStreamLink.md)
 - [VideoStatus](docs/VideoStatus.md)
 - [VideoStatusEncoding](docs/VideoStatusEncoding.md)
 - [VideoStatusEncodingMetadata](docs/VideoStatusEncodingMetadata.md)
 - [VideoStatusIngest](docs/VideoStatusIngest.md)
 - [VideoStatusIngestReceivedParts](docs/VideoStatusIngestReceivedParts.md)
 - [VideoThumbnailPickPayload](docs/VideoThumbnailPickPayload.md)
 - [VideoUpdatePayload](docs/VideoUpdatePayload.md)
 - [VideoWatermark](docs/VideoWatermark.md)
 - [VideosListResponse](docs/VideosListResponse.md)
 - [Watermark](docs/Watermark.md)
 - [WatermarksListResponse](docs/WatermarksListResponse.md)
 - [Webhook](docs/Webhook.md)
 - [WebhooksCreationPayload](docs/WebhooksCreationPayload.md)
 - [WebhooksListResponse](docs/WebhooksListResponse.md)


## Documentation for Authorization

### API key

Most endpoints required to be authenticated using the API key mechanism described in our [documentation](https://docs.api.video/reference#authentication).
The access token generation mechanism is automatically handled by the client. All you have to do is provide an API key when instantiating the `ApiVideoClient`:
```kotlin
val client = ApiVideoClient("YOUR_API_KEY")
```

### Public endpoints

Some endpoints don't require authentication. These one can be called with a client instantiated without API key:
```kotlin
val client = ApiVideoClient()
```

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.
For direct call with `ApiClient`: Do not call API from the main thread, otherwise you will get a `android.os.NetworkOnMainThreadException`. Dispatch API calls with Thread, Executors or Kotlin coroutine to avoid this. Alternatively, APIs come with an asynchronous counterpart (`createAsync` for `create`) except for the upload endpoint.

## Have you gotten use from this API client?

Please take a moment to leave a star on the client ⭐

This helps other users to find the clients and also helps us understand which clients are most popular. Thank you!

# Contribution

Since this API client is generated from an OpenAPI description, we cannot accept pull requests made directly to the repository. If you want to contribute, you can open a pull request on the repository of our [client generator](https://github.com/apivideo/api-client-generator). Otherwise, you can also simply open an issue detailing your need on this repository.