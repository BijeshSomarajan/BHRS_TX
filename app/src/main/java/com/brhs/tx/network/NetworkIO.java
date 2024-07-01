package com.brhs.tx.network;

import android.content.Context;
import android.os.Handler;

import com.brhs.tx.MainActivity;
import com.brhs.tx.MainController;
import com.brhs.tx.R;
import com.brhs.tx.domain.FcOutput;
import com.brhs.tx.fcIO.FcIOHandler;
import com.brhs.tx.notification.NotificationHandler;
import com.google.firebase.annotations.concurrent.Background;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkIO {
    private static Context appicationContext;
    private static MainActivity mainActivity;
    private static InetAddress FC_INET_ADDRESS;
    private static int FC_PORT = 0;
    private static boolean initialized = false;

    public static void initialize(Context appContext, MainActivity activity) throws UnknownHostException {
        appicationContext = appContext;
        mainActivity = activity;
        FC_PORT = Integer.parseInt(mainActivity.getResources().getString(R.string.fc_port));
        FC_INET_ADDRESS = InetAddress.getByName(mainActivity.getResources().getString(R.string.fc_ip));
        if (!initialized) {
            receiveData();
            initialized = true;
        }
        NotificationHandler.logMessage("Network I/O Initialized..\n", false);
    }

    private static void receiveData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket udpSocket = null;
                try {
                    udpSocket = new DatagramSocket(FC_PORT);
                    DatagramPacket udpPacket = new DatagramPacket(FcOutput.fcOutputData, FcOutput.fcOutputData.length);
                    NotificationHandler.logMessage("FC Data receiver started..", false);
                    while (true) {
                        udpSocket.receive(udpPacket);
                        try {
                            FcIOHandler.unMarshallFCOutput(udpPacket.getLength());
                            MainController.manageFcDataReceive();
                        } catch (Exception e) {
                            NotificationHandler.logMessage("Error in receiveData >> " + e.getMessage(), false);
                        }
                    }
                } catch (IOException e) {
                    NotificationHandler.logMessage("Error in receiveData >> " + e.getMessage(), false);
                } finally {
                    if (udpSocket != null) {
                        udpSocket.close();
                    }
                }
            }
        });
        thread.start();
    }

     public static void sendData() {
        byte[] fcInputBytes = FcIOHandler.marshallFCInput();
        if (fcInputBytes != null) {
            try {
                DatagramSocket udpSocket = new DatagramSocket();
                DatagramPacket udpOutPacket = new DatagramPacket(fcInputBytes, fcInputBytes.length, FC_INET_ADDRESS, FC_PORT);
                udpSocket.send(udpOutPacket);
            } catch (Exception exp) {
                NotificationHandler.logMessage("Error in sendData >> " + exp, false);
            }
        }
    }


}