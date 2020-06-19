package com.pavan.moviebuff.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {

    ConnectionChangeCallback connectionChangeCallback;
    Context con;

    public NetworkChangeReceiver(Context context) {
        this.con = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if (connectionChangeCallback != null) {
            connectionChangeCallback.onConnectionChange(isConnected);
        }

    }

    public void setConnectionChangeCallback(ConnectionChangeCallback
                                                    connectionChangeCallback) {
        this.connectionChangeCallback = connectionChangeCallback;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public interface ConnectionChangeCallback {
        void onConnectionChange(boolean isConnected);
    }


}