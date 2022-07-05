package video.api.integration;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.clients.VideosApi;
import video.api.client.api.models.Environment;
import video.api.client.api.models.Metadata;
import video.api.client.api.models.Page;
import video.api.client.api.models.TokenCreationPayload;
import video.api.client.api.models.UploadToken;
import video.api.client.api.models.Video;
import video.api.client.api.models.VideoCreationPayload;
import video.api.client.api.models.VideoStatus;
import video.api.client.api.models.VideoThumbnailPickPayload;
import video.api.client.api.models.VideoUpdatePayload;
import video.api.integration.utils.Utils;

@DisplayName("Integration tests of api.videos() methods")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "INTEGRATION_TESTS_API_KEY", matches = ".+")
public class VideosTest {

    final ApiVideoClient apiClient;

    public VideosTest() throws IOException {
        this.apiClient = new ApiVideoClient(Utils.getApiKey(), Environment.SANDBOX);
        this.apiClient.setApplicationName("client-integration-tests", "0");
    }

    @Nested
    @DisplayName("upload by chunk")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class UploadByChunk {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos()
                    .create(new VideoCreationPayload().title("[Android-SDK-tests] upload with chunk")._public(false));
        }

        @Test
        public void uploadVideo() throws ApiException, IOException {
            File mp4File = Utils.getFileFromAsset("10m.mp4");

            long fileSize = mp4File.length();
            int chunkSize = 1024 * 1024 * 5;

            apiClient.getHttpClient().setUploadChunkSize(chunkSize);

            AtomicLong totalUploadedAtomic = new AtomicLong(0);
            AtomicLong totalBytesAtomic = new AtomicLong(0);
            AtomicLong chunkCountAtomic = new AtomicLong(0);
            HashSet<Integer> seenChunkNums = new HashSet<>();

            apiClient.videos().upload(testVideo.getVideoId(), mp4File,
                    (bytesWritten, totalBytes, chunkCount, chunkNum) -> {
                        totalUploadedAtomic.set(bytesWritten);
                        totalBytesAtomic.set(totalBytes);
                        chunkCountAtomic.set(chunkCount);
                        seenChunkNums.add(chunkNum);
                    });

            assertThat(totalBytesAtomic.get()).isEqualTo(fileSize);
            assertThat(totalUploadedAtomic.get()).isEqualTo(fileSize);
            assertThat(chunkCountAtomic.get())
                    .isEqualTo(new Double(Math.ceil((float) fileSize / chunkSize)).longValue());
            assertThat(seenChunkNums).containsExactly(1, 2, 3);

            System.out.println(testVideo);
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("progressive upload")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ProgressiveUpload {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos()
                    .create(new VideoCreationPayload().title("[Android-SDK-tests] progressive upload")._public(false));
        }

        @Test
        public void uploadVideo() throws ApiException, IOException {
            File part1 = Utils.getFileFromAsset("10m.mp4.part.a");
            File part2 = Utils.getFileFromAsset("10m.mp4.part.b");
            File part3 = Utils.getFileFromAsset("10m.mp4.part.c");

            VideosApi.UploadProgressiveSession uploadProgressiveSession = apiClient.videos().createUploadProgressiveSession(this.testVideo.getVideoId());

            uploadProgressiveSession.uploadPart(part1);
            uploadProgressiveSession.uploadPart(part2);
            uploadProgressiveSession.uploadLastPart(part3);
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("progressive upload with upload token")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ProgressiveUploadWithUploadToken {
        private UploadToken uploadToken;
        private Video result;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.uploadToken = apiClient.uploadTokens().createToken(new TokenCreationPayload());
        }

        @Test
        public void uploadVideo() throws ApiException, IOException {
            File part1 = Utils.getFileFromAsset("10m.mp4.part.a");
            File part2 = Utils.getFileFromAsset("10m.mp4.part.b");
            File part3 = Utils.getFileFromAsset("10m.mp4.part.c");

            VideosApi.UploadWithUploadTokenProgressiveSession uploadProgressiveSession = apiClient.videos()
                    .createUploadWithUploadTokenProgressiveSession(this.uploadToken.getToken());

            uploadProgressiveSession.uploadPart(part1);
            uploadProgressiveSession.uploadPart(part2);
            this.result = uploadProgressiveSession.uploadLastPart(part3);
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(result.getVideoId());
        }
    }

    @Nested
    @DisplayName("upload without chunk")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Upload {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos()
                    .create(new VideoCreationPayload().title("[Android-SDK-tests] upload without chunk"));
        }

        @Test
        public void uploadVideo() throws ApiException, IOException {
            File mp4File = Utils.getFileFromAsset("558k.mp4");

            long fileSize = mp4File.length();
            long chunkSize = apiClient.getHttpClient().getUploadChunkSize();

            AtomicLong totalUploadedAtomic = new AtomicLong(0);
            AtomicLong totalBytesAtomic = new AtomicLong(0);
            AtomicLong chunkCountAtomic = new AtomicLong(0);
            HashSet<Integer> seenChunkNums = new HashSet<>();

            apiClient.videos().upload(testVideo.getVideoId(), mp4File,
                    (bytesWritten, totalBytes, chunkCount, chunkNum) -> {
                        totalUploadedAtomic.set(bytesWritten);
                        totalBytesAtomic.set(totalBytes);
                        chunkCountAtomic.set(chunkCount);
                        seenChunkNums.add(chunkNum);
                    });

            assertThat(totalBytesAtomic.get()).isEqualTo(fileSize);
            assertThat(totalUploadedAtomic.get()).isEqualTo(fileSize);
            assertThat(chunkCountAtomic.get()).isEqualTo(1);
            assertThat(seenChunkNums).containsExactly(1);
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("update")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Update {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos()
                    .create(new VideoCreationPayload().title("[Android-SDK-tests] video updates"));
        }

        @Test
        public void addMetadata() throws ApiException {

            Video updated = apiClient.videos().update(testVideo.getVideoId(),
                    new VideoUpdatePayload().addMetadataItem(new Metadata("firstKey", "firstValue"))
                            .addMetadataItem(new Metadata("secondKey", "secondValue")));

            assertThat(updated.getMetadata()).containsExactlyElementsIn(Arrays.asList(new Metadata("firstKey", "firstValue"),
                    new Metadata("secondKey", "secondValue")));

            Video updated2 = apiClient.videos().update(testVideo.getVideoId(), new VideoUpdatePayload()
                    .metadata(updated.addMetadataItem(new Metadata("thirdKey", "thirdValue")).getMetadata()));

            assertThat(updated2.getMetadata()).containsExactlyElementsIn(Arrays.asList(new Metadata("firstKey", "firstValue"),
                    new Metadata("secondKey", "secondValue"), new Metadata("thirdKey", "thirdValue")));

            Video updated3 = apiClient.videos().update(testVideo.getVideoId(),
                    new VideoUpdatePayload().metadata(Collections.emptyList()));

            assertThat(updated3.getMetadata()).isEmpty();
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("get")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Get {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos().create(new VideoCreationPayload().title("[Android-SDK-tests] get"));
        }

        @Test
        public void get() throws ApiException {
            Video video = apiClient.videos().get(testVideo.getVideoId());

            assertThat(video.getVideoId()).isEqualTo(testVideo.getVideoId());
            assertThat(video.getTitle()).isEqualTo(testVideo.getTitle());
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("list with metadata")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ListWithMetadata {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos()
                    .create(new VideoCreationPayload()
                            .metadata(Collections.singletonList(new Metadata("key1", "value1")))
                            .title("[Android-SDK-tests] list metadatas"));
        }

        @Test
        public void listMetadataNotFound() throws ApiException {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("key1", "valueNotFound");
            Page<Video> page = apiClient.videos().list().metadata(metadata).execute();

            assertThat(page.getItemsTotal()).isEqualTo(0);
        }

        @Test
        public void listMetadataFound() throws ApiException {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("key1", "value1");
            Page<Video> page = apiClient.videos().list().metadata(metadata).execute();

            assertThat(page.getItemsTotal()).isGreaterThan(0);
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("list with tags")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ListWithTags {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos().create(
                    new VideoCreationPayload().tags(Arrays.asList("tag1", "tag2")).title("[Android-SDK-tests] list tags"));
        }

        @Test
        public void listTagNotFound() throws ApiException {
            Page<Video> page = apiClient.videos().list().tags(Collections.singletonList("valueNotFound")).execute();

            assertThat(page.getItemsTotal()).isEqualTo(0);
        }

        @Test
        public void listTagFound() throws ApiException {
            Page<Video> page = apiClient.videos().list().tags(Arrays.asList("tag1", "tag2")).execute();

            assertThat(page.getItemsTotal()).isGreaterThan(0);
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("thumbnail")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Thumbnail {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos().create(new VideoCreationPayload().title("[Android-SDK-tests] thumbnail"));
        }

        @Test
        public void uploadThumbnail() throws ApiException, IOException {
            File jpgFile = Utils.getFileFromAsset("cat.jpg");

            Video video = apiClient.videos().uploadThumbnail(testVideo.getVideoId(), jpgFile);

            assertThat(video.getAssets()).isNotNull();
            assertThat(video.getAssets().getThumbnail()).isNotNull();
        }

        @Test
        public void pickThumbnail() throws ApiException {
            Video video = apiClient.videos().pickThumbnail(testVideo.getVideoId(),
                    new VideoThumbnailPickPayload().timecode("00:00:02"));

            assertThat(video.getAssets()).isNotNull();
            assertThat(video.getAssets().getThumbnail()).isNotNull();
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }

    @Nested
    @DisplayName("video status")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class VideoStatusTest {
        private Video testVideo;

        @BeforeAll
        public void createVideo() throws ApiException {
            this.testVideo = apiClient.videos()
                    .create(new VideoCreationPayload().title("[Android-SDK-tests] videoStatus"));
        }

        @Test
        public void getVideoStatus() throws ApiException {
            VideoStatus videoStatus = apiClient.videos().getStatus(testVideo.getVideoId());

            assertThat(videoStatus.getIngest()).isNull();
            assertThat(videoStatus.getEncoding()).isNotNull();
            assertThat(videoStatus.getEncoding().getPlayable()).isFalse();
        }

        @AfterAll
        public void deleteVideo() throws ApiException {
            apiClient.videos().delete(testVideo.getVideoId());
        }
    }
}
