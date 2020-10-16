package com.fm.modules.app.commons.conectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class Conectividad {
    public static boolean isNetActive(@NonNull FragmentActivity activity) {
        boolean c = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                c = true;
            }
        } catch (Exception e) {
            System.out.println("error isNetActive: " + e);
            c = false;
        }
        return c;
    }
}
