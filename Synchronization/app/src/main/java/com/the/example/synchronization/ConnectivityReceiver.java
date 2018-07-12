package com.the.example.synchronization;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ConnectivityReceiver extends BroadcastReceiver {
    public static final String NETWORK_AVAILABLE_ACTION = "com.the.example.synchronization.Synchronization";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";
    Context mContext;

    @Override
    public void onReceive(Context context, Intent arg1) {
        mContext = context;
        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE,  isConnectedToInternet(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);



    }
    private boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            Log.e(Synchronization.class.getName(), e.getMessage());
            return false;
        }
    }




}
