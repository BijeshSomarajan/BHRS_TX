package com.brhs.tx.domain;

import com.brhs.tx.network.NetworkQuality;

public class NetworkInfo {
    public static final int RSSI_MIN_VALUE = -100;
    public static boolean networkConnected = false;
    public static boolean wasNetworkConnected = false;
    public static NetworkQuality networkQuality = NetworkQuality.NA;
    public static int networkRssi = RSSI_MIN_VALUE;
    public static int prevNetworkRssi = RSSI_MIN_VALUE;
    public static String networkSsid ="";
    public static int ipAddress;
    private NetworkInfo(){
        //Masking instantiation
    }
}
