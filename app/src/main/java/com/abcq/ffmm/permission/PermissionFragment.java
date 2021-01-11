package com.abcq.ffmm.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionFragment extends Fragment {

    private final String mPermissions[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private PermissionsResponse mResponse;

    public void setListener(PermissionsResponse response) {
        mResponse = response;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mResponse == null) return;
        if (ContextCompat.checkSelfPermission(context, mPermissions[0]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(mPermissions, 1);
        } else mResponse.response(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mResponse == null) return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mResponse.response(true);
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            mResponse.response(false);
        }
    }
}
