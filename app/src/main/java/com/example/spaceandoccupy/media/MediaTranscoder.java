package com.example.spaceandoccupy.media;

/**
 * Mock backend transcoding pipeline: compresses agent media into a mobile-friendly
 * playback URL so listings can stream on slower connections.
 */
public final class MediaTranscoder {

    private MediaTranscoder() {}

    public static String transcodeToMobile(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.trim().isEmpty()) {
            return sourceUrl;
        }
        String trimmed = sourceUrl.trim();
        if (trimmed.contains("fmt=mobile_720p")) {
            return trimmed;
        }
        return trimmed + (trimmed.contains("?") ? "&" : "?") + "fmt=mobile_720p";
    }

    public static String formatLabel() {
        return "H.264 720p mobile";
    }
}
