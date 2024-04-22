package video.api.client.api.upload;

import video.api.client.api.ApiException;
import video.api.client.api.ApiResponse;
import video.api.client.api.models.Video;

import java.io.File;

public interface IProgressiveUploadSession {
    String getVideoId();

    String getToken();

    Video uploadPart(File part) throws ApiException;

    Video uploadLastPart(File part) throws ApiException;

    Video uploadPart(File part, UploadPartProgressListener uploadProgressListener) throws ApiException;

    Video uploadLastPart(File part, UploadPartProgressListener uploadProgressListener) throws ApiException;

    Video uploadPart(File part, boolean isLastPart, UploadPartProgressListener uploadProgressListener)
            throws ApiException;

    Video uploadPart(File part, Integer partId, UploadPartProgressListener uploadProgressListener) throws ApiException;

    Video uploadLastPart(File part, Integer partId, UploadPartProgressListener uploadProgressListener)
            throws ApiException;

    Video uploadPart(File part, Integer partId, boolean isLastPart, UploadPartProgressListener uploadProgressListener)
            throws ApiException;

    ApiResponse<Video> uploadPartWithHttpInfo(File part, Integer partId, boolean isLastPart,
            UploadPartProgressListener uploadProgressListener) throws ApiException;
}