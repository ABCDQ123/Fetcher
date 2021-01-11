package com.abcq.fetcher.manager;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class CursorOption {

    //URI
    protected static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    //SELECTION
    protected static final String SELECTION_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND "
                    + MediaStore.MediaColumns.SIZE
                    + ">0";

    protected static final String SELECTION_ALL =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    //SELECTION ARGS
    protected static final String[] SELECTION_ALL_ARGS = {//SELECTION ARGS ALL
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    protected static final String[] SELECTION_IMAGE_ARGS = { //SELECTION ARGS IMAGE
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
    };
    protected static final String[] SELECTION_VIDEO_ARGS = { //SELECTION ARGS IMAGE
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };

    //PROJECTION
    protected static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            "duration"};

    //ORDER_BY
    protected static final String ORDER_BY = "datetaken DESC";
}
