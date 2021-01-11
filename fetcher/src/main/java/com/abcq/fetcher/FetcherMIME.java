package com.abcq.fetcher;

public enum FetcherMIME {

    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    BMP("image/x-ms-bmp"),
    WEBP("image/webp"),

    MP4("video/mp4"),
    FLV("video/flv"),
    AVI("video/avi"),
    MPEG("video/mpeg"),
    THREEGPP("video/3gpp"),
    THREEGPP2("video/3gpp2"),
    MKV("video/x-matroska"),
    WEBM("video/webm"),
    TS("video/mp2ts"),
    QUICKTIME("video/quicktime");
    private final String mime;

    FetcherMIME(String mime) {
        this.mime = mime;
    }

    public String mime() {
        return mime;
    }

    public static boolean isJPG(String mime) {
        if (mime == null) return false;
        return mime.equals(JPEG.mime);
    }

    public static boolean isPNG(String mime) {
        if (mime == null) return false;
        return mime.equals(PNG.mime);
    }

    public static boolean isGIF(String mime) {
        if (mime == null) return false;
        return mime.equals(GIF.mime);
    }

    public static boolean isMP4(String mime) {
        if (mime == null) return false;
        return mime.equals(MP4.mime);
    }

    public static boolean isFLV(String mime) {
        if (mime == null) return false;
        return mime.equals(FLV.mime);
    }

    public static boolean isAVI(String mime) {
        if (mime == null) return false;
        return mime.equals(AVI.mime);
    }
}
