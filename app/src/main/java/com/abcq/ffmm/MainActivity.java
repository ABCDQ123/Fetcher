package com.abcq.ffmm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.abcq.fetcher.Fetcher;
import com.abcq.fetcher.FetcherKey;
import com.abcq.fetcher.FetcherMIME;
import com.abcq.fetcher.FetcherResponse;
import com.abcq.ffmm.permission.PermissionManager;
import com.abcq.ffmm.permission.PermissionsResponse;
import com.bumptech.glide.Glide;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetcherResponse {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.item_image, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ImageView imageView = holder.itemView.findViewById(R.id.iv);
                Glide.with(getBaseContext()).load("" + mList.get(holder.getAdapterPosition())).into(imageView);
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }
        });
        //=====================================
        PermissionManager.request(this, new PermissionsResponse() {
            @Override
            public void response(boolean done) {
                if (!done) return;
                Fetcher.putFilter(FetcherMIME.GIF);
                Fetcher.start(MainActivity.this, getBaseContext(), FetcherKey.MEDIA_ALL, MainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //===========================
        PermissionManager.clear(this);
        Fetcher.destroy();
    }

    //===============================
    @Override
    public void response(final List<JSONObject> response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (JSONObject object : response) {
                    String mime = (String) object.opt(FetcherKey.mime());
                    String name = (String) object.opt(FetcherKey.name());
                    String path = (String) object.opt(FetcherKey.path());
                    long duration = (long) object.opt(FetcherKey.duration());
                    Log.e("MediaLoaderManager", " " + mime + "  " + name + "  " + path + "  " + duration);
                    mList.add("" + path);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
