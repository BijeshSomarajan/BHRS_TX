package com.brhs.tx.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.WifiInfo;

import com.brhs.tx.MainActivity;
import com.brhs.tx.MainController;
import com.brhs.tx.R;
import com.brhs.tx.domain.NetworkInfo;
import com.brhs.tx.notification.NotificationHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkHandler {
    private static Context appicationContext;
    private static MainActivity mainActivity;
    private static ConnectivityManager connectivityManager;
    private static android.net.wifi.WifiManager wifiManager;
    private static String WIFI_NAME;
    private static ScheduledExecutorService netWorkUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    private static boolean initialized = false;

    public static void initialize(Context appContext, MainActivity activity) {
        appicationContext = appContext;
        mainActivity = activity;
        connectivityManager = (ConnectivityManager) mainActivity.getSystemService(ConnectivityManager.class);
        wifiManager = (android.net.wifi.WifiManager) mainActivity.getSystemService(Context.WIFI_SERVICE);

        WIFI_NAME = mainActivity.getResources().getString(R.string.wifi_name);

        mainActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PackageManager.PERMISSION_GRANTED);

        wifiManager.startScan();
        updateNetworkStatus();
        MainController.manageNetworkStateChange();

        if (!initialized) {
            netWorkUpdateExecutor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    updateNetworkStatus();
                    MainController.manageNetworkStateChange();
                }
            }, 1, 1L, TimeUnit.SECONDS);
            initialized = true;
        }
        NotificationHandler.logMessage("Network Handler Initialized..", false);
    }

    private static void setDefaultNetworkInfo() {
        NetworkInfo.networkConnected = false;
        NetworkInfo.networkRssi = NetworkInfo.RSSI_MIN_VALUE;
        NetworkInfo.networkQuality = NetworkQuality.NA;
        NetworkInfo.ipAddress = 0;
        NetworkInfo.networkSsid = "";
    }

    /**
     * Updates the network info
     */
    public static void updateNetworkStatus() {
        Network network = connectivityManager.getActiveNetwork();
        if (network != null) {
            android.net.NetworkInfo ntkInfo = connectivityManager.getNetworkInfo(network);
            if (ntkInfo != null && ntkInfo.getType() == ConnectivityManager.TYPE_WIFI && ntkInfo.isConnectedOrConnecting()) {
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && WIFI_NAME.equalsIgnoreCase(connectionInfo.getSSID())) {
                    NetworkInfo.networkConnected = true;
                    NetworkInfo.networkRssi = connectionInfo.getRssi();
                    NetworkInfo.ipAddress = connectionInfo.getIpAddress();
                    if (NetworkInfo.networkRssi >= -55) {
                        NetworkInfo.networkQuality = NetworkQuality.EXCELLENT;
                    } else if (NetworkInfo.networkRssi >= -65) {
                        NetworkInfo.networkQuality = NetworkQuality.VERY_GOOD;
                    } else if (NetworkInfo.networkRssi >= -75) {
                        NetworkInfo.networkQuality = NetworkQuality.GOOD;
                    } else if (NetworkInfo.networkRssi >= -85) {
                        NetworkInfo.networkQuality = NetworkQuality.WEAK;
                    } else if (NetworkInfo.networkRssi >= -95) {
                        NetworkInfo.networkQuality = NetworkQuality.WEAK;
                    } else {
                        NetworkInfo.networkQuality = NetworkQuality.NO_SIGNAL;
                    }
                } else {
                    setDefaultNetworkInfo();
                }
            } else {
                setDefaultNetworkInfo();
            }
        } else {
            setDefaultNetworkInfo();
        }
    }

}
