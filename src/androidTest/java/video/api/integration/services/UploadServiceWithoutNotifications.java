package video.api.integration.services;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import video.api.client.api.models.Video;
import video.api.client.services.UploadService;

public class UploadServiceWithoutNotifications extends UploadService {
    @Nullable
    @Override
    public Notification onUploadStartedNotification(@NonNull String videoIdOrToken) {
        return null;
    }

    @Nullable
    @Override
    public Notification onUploadSuccessNotification(@NonNull Video video) {
        return null;
    }

    @Nullable
    @Override
    public Notification onUploadProgressNotification(@NonNull String videoIdOrToken, int progress) {
        return null;
    }

    @Nullable
    @Override
    public Notification onUploadErrorNotification(@NonNull String videoIdOrToken, @NonNull Exception e) {
        return null;
    }

    @Nullable
    @Override
    public Notification onUploadCancelledNotification(@NonNull String videoIdOrToken) {
        return null;
    }

    @Nullable
    @Override
    public Notification onLastUploadNotification() {
        return null;
    }
}
