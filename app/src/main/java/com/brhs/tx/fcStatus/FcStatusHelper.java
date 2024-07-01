package com.brhs.tx.fcStatus;

import android.content.Context;
import android.widget.TextView;

import com.brhs.tx.MainActivity;
import com.brhs.tx.MainController;
import com.brhs.tx.R;
import com.brhs.tx.domain.FcOutput;
import com.brhs.tx.notification.NotificationHandler;

public class FcStatusHelper {
    private static Context appicationContext;
    private static MainActivity mainActivity;

    private static TextView fcStatusPitchView;
    private static TextView fcStatusRollView;
    private static TextView fcStatusYawView;
    private static TextView fcStatusAltView;
    private static TextView fcStatusVVelView;
    private static TextView fcStatusIMUFreqView;
    private static TextView fcStatusGyroFreqView;
    private static TextView fcStatusAccFreqView;

    public static void initialize(Context appContext, MainActivity activity) {
        appicationContext = appContext;
        mainActivity = activity;
        fcStatusPitchView = (TextView) mainActivity.findViewById(R.id.fcStatusPitchView);
        fcStatusRollView = (TextView) mainActivity.findViewById(R.id.fcStatusRollView);
        fcStatusYawView = (TextView) mainActivity.findViewById(R.id.fcStatusYawView);
        fcStatusAltView = (TextView) mainActivity.findViewById(R.id.fcStatusAltView);
        fcStatusVVelView =(TextView) mainActivity.findViewById(R.id.fcStatusVVelocityView);
        fcStatusIMUFreqView = (TextView) mainActivity.findViewById(R.id.fcStatusIMUFreqView);
        fcStatusGyroFreqView = (TextView) mainActivity.findViewById(R.id.fcStatusGyroFreqView);
        fcStatusAccFreqView = (TextView) mainActivity.findViewById(R.id.fcStatusAccFreqView);

        resetFcStatus();

        NotificationHandler.logMessage("Status Helper Initialized..", false);
    }

    public static void updateFcStatus() {
        fcStatusPitchView.setText("" + (float) FcOutput.pitch / 100.0f);
        fcStatusRollView.setText("" + (float) FcOutput.roll / 100.0f);
        fcStatusYawView.setText("" + (float) FcOutput.yaw / 100.0f);
        fcStatusAltView.setText("" + String.format("%.02f", (float) FcOutput.alt / 1000.0f));
        fcStatusVVelView.setText("" + String.format("%.02f", (float) FcOutput.vVelocity / 1000.0f));
        fcStatusIMUFreqView.setText("" + String.format("%.02f", (float) FcOutput.imuFrequency / 1000.0f));
        fcStatusGyroFreqView.setText("" + String.format("%.02f", (float) FcOutput.gyroFrequency / 1000.0f));
        fcStatusAccFreqView.setText("" + String.format("%.02f", (float) FcOutput.accFrequency / 1000.0f));
        MainController.manageFcStatusChange();
    }

    public static void resetFcStatus() {
        fcStatusPitchView.setText("--");
        fcStatusRollView.setText("--");
        fcStatusYawView.setText("--");
        fcStatusAltView.setText("--");
        fcStatusVVelView.setText("--");
        fcStatusIMUFreqView.setText("--");
        fcStatusGyroFreqView.setText("--");
        fcStatusAccFreqView.setText("--");
    }
}
