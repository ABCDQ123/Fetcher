package com.abcq.fetcher;

public class FetcherKey {

    public final static int MEDIA_IMAGE = 0010;
    public final static int MEDIA_VIDEO = 0013;
    public final static int MEDIA_ALL = 0014;

    public static String mime() {
        return "mime";
    }

    public static String path() {
        return "path";
    }

    public static String name() {
        return "name";
    }

    public static String duration() {
        return "duration";
    }
}
