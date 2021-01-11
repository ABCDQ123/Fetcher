package com.abcq.fetcher;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import com.abcq.fetcher.manager.CursorLoader;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class Fetcher {

    private volatile static CursorLoader mCursorLoader;

    private static CursorLoader instance() {
        if (mCursorLoader == null) {
            synchronized (Fetcher.class) {
                if (mCursorLoader == null) {
                    mCursorLoader = new CursorLoader();
                }
            }
        }
        return mCursorLoader;
    }

    public static void putFilter(FetcherMIME filter) {
        instance().putFilter(filter);
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void start(T owner, Context context, int media_T, FetcherResponse response) {
        instance().init(owner, context);
        instance().load(media_T, response);
    }

    public static void destroy() {
        instance().destroy();
    }

}
