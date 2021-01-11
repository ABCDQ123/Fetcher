package com.abcq.ffmm.permission;

import androidx.fragment.app.FragmentActivity;

public class PermissionManager {

    private volatile static PermissionFragment mPermission;

    private static PermissionFragment instance() {
        if (mPermission == null) {
            synchronized (PermissionManager.class) {
                if (mPermission == null) {
                    mPermission = new PermissionFragment();
                }
            }
        }
        return mPermission;
    }

    public static void request(FragmentActivity activity, PermissionsResponse response) {
        instance().setListener(response);
        activity.getSupportFragmentManager().beginTransaction().add(instance(), "").commit();
    }

    public static void clear(FragmentActivity activity) {
        activity.getSupportFragmentManager().beginTransaction().remove(instance()).commit();
    }
}
