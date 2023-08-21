# Changelog
All changes to this project will be documented in this file.

## [1.5.0] - 2023-08-21
- Improve cancel of upload workers for the WorkManager API

## [1.4.2] - 2023-08-10
- Fix upload with upload token and video id when video is smaller than chunk size

## [1.4.1] - 2023-08-08
- Fix upload notification resources name

## [1.4.0] - 2023-06-28
- Introducing new live streams restream feature
- Introducing new analytics endpoints

## [1.3.2] - 2023-04-20
- Add upload token and videoId in WorkManager tags

## [1.3.1] - 2023-04-04
- Add custom tag for WorkManager
- Fix tag for progressive upload in WorkManager
- Worker now returns the file in case developer want to delete it after upload.
- Use api.video theme and icon for examples

## [1.3.0] - 2023-02-28
- Introduce WorkManager dedicated API

## [1.2.3] - 2022-11-16
- Fix the UploadService notification level
- In example, add support for Android 13

## [1.2.2] - 2022-09-13
- period parameter is now mandatory in analytics endpoints

## [1.2.1] - 2022-08-30
- Improve the upload Service

## [1.2.0] - 2022-08-22
- Add an upload Service

## [1.1.0] - 2022-07-12
- Add Async APIs

## [1.0.6] - 2022-07-05
- Add SDK origin header

## [1.0.5] - 2022-04-21
- Fix `video.publishedAt` type

## [1.0.4] - 2022-03-21
- Add `ingest.receivedParts` attribute in GET /videos/{video_id}/status

## [1.0.3] - 2022-03-17
- Fix metadata query param in GET /videos

## [1.0.2] - 2022-03-10
- Add Origin identification headers

## [1.0.1] - 2022-01-24
- Add applicationName parameter (to allow user agent extension)

## [1.0.0] - 2022-01-07
- Add watermark endpoints
- Add video clips
- Fix tags[] query parameter format of GET /videos
- Correctly distinguish between "undefined", "defined" and "null" values if the playerId parameter of PATCH /videos/{videoId}

## [0.2.2] - 2022-01-06
- Increase chunked and progressive upload speed
- Update min Sdk version to Android API 24

## [0.2.1] - 2021-12-13
- Add an interface for progressive upload session

## [0.2.0] - 2021-12-06
- Add `name` attribute in player themes

## [0.1.0] - 2021-11-19
- Initial release
