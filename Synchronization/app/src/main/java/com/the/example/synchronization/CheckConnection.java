package com.the.example.synchronization;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by what on 12/26/2017.
 */

 class CheckConnection {


     static boolean checkConnection(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

             Network[] activeNetworks = cm.getAllNetworks();
             for (Network n: activeNetworks) {
                 NetworkInfo nInfo = cm.getNetworkInfo(n);
                 if(nInfo.isConnected())
                     return true;
             }

         } else {
             NetworkInfo[] info = cm.getAllNetworkInfo();
             if (info != null)
                 for (NetworkInfo anInfo : info)
                     if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                         return true;
                     }
         }

         return false;

     }
}
