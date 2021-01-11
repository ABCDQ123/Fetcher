package com.abcq.fetcher.manager;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.abcq.fetcher.FetcherKey;
import com.abcq.fetcher.FetcherMIME;
import com.abcq.fetcher.FetcherResponse;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class CursorLoader<T extends LifecycleOwner & ViewModelStoreOwner> extends CursorOption implements LoaderManager.LoaderCallbacks<Cursor> {

    private int mMediaType;
    private List<FetcherMIME> mFilters;//MIME.XXX

    private WeakReference<T> mOwner;
    private WeakReference<Context> mContext;
    private FetcherResponse mFetcherResponse;
    private List<JSONObject> mList;

    private ExecutorService mQueen = Executors.newFixedThreadPool(1);
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void init(T owner, Context context) {
        mOwner = new WeakReference<>(owner);
        mContext = new WeakReference<>(context);
        mList = new ArrayList<>();
    }

    public void putFilter(FetcherMIME fetcherMime) {
        if (mFilters == null) mFilters = new ArrayList<>();
        mFilters.add(fetcherMime);
    }

    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void load(int media_T, FetcherResponse mediaCall) {
        if (mOwner.get() == null || mContext.get() == null) return;
        mMediaType = media_T;
        mFetcherResponse = mediaCall;
        LoaderManager.getInstance(mOwner.get()).initLoader(0, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (mOwner.get() == null || mContext.get() == null) return null;
        Context context = mContext.get();
        if (mMediaType == FetcherKey.MEDIA_IMAGE) {
            return new androidx.loader.content.CursorLoader(context, QUERY_URI, null, SELECTION_SINGLE_MEDIA_TYPE, SELECTION_IMAGE_ARGS, ORDER_BY);
        } else if (mMediaType == FetcherKey.MEDIA_VIDEO) {
            return new androidx.loader.content.CursorLoader(context, QUERY_URI, null, SELECTION_SINGLE_MEDIA_TYPE, SELECTION_VIDEO_ARGS, ORDER_BY);
        } else if (mMediaType == FetcherKey.MEDIA_ALL) {
            return new androidx.loader.content.CursorLoader(context, QUERY_URI, null, SELECTION_ALL, SELECTION_ALL_ARGS, ORDER_BY);
        }
        return new androidx.loader.content.CursorLoader(context, QUERY_URI, null, SELECTION_ALL, SELECTION_ALL_ARGS, ORDER_BY);
        //uri表 projection 查询的列名 selection 指定where的约束条件 selectionArgs 为where中的占位符提供具体的值 orderBy 排序方式
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, final Cursor data) {
        if (loader == null || data == null) return;
        mQueen.execute(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                data.moveToFirst();
                reset(data);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mOwner == null || mContext == null) return;
                        mFetcherResponse.response(mList);
                        LoaderManager.getInstance(mOwner.get()).destroyLoader(0);
                    }
                });
            }
        });
    }

    private void reset(final Cursor data) {
        try {
            do {
                String mime = data.getString(data.getColumnIndexOrThrow("mime_type"));
                boolean doNext = false;
                for (FetcherMIME f : mFilters) {
                    if (f.mime().equals(mime)) {
                        doNext = true;
                        break;
                    }
                }
                if (doNext) continue;
                else if (FetcherMIME.isJPG(mime) || FetcherMIME.isPNG(mime) || FetcherMIME.isGIF(mime)) {
                    String path = data.getString(data.getColumnIndexOrThrow("_data"));
                    String name = data.getString(data.getColumnIndexOrThrow("_display_name"));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(FetcherKey.mime(), mime);
                    jsonObject.put(FetcherKey.name(), name);
                    jsonObject.put(FetcherKey.path(), path);
                    jsonObject.put(FetcherKey.duration(), 0l);
                    mList.add(jsonObject);
                } else if (FetcherMIME.isMP4(mime) || FetcherMIME.isFLV(mime) || FetcherMIME.isAVI(mime)) {
                    String path = data.getString(data.getColumnIndexOrThrow("_data"));
                    String name = data.getString(data.getColumnIndexOrThrow("_display_name"));
                    long duration = data.getLong(data.getColumnIndexOrThrow("duration"));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(FetcherKey.mime(), mime);
                    jsonObject.put(FetcherKey.name(), name);
                    jsonObject.put(FetcherKey.path(), path);
                    jsonObject.put(FetcherKey.duration(), duration);
                    mList.add(jsonObject);
                } else continue;
            } while (data.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (data.moveToNext()) reset(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
