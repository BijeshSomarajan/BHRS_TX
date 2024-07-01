package com.brhs.tx.indicator;

import android.content.Context;
import android.widget.TextView;

import com.brhs.tx.MainActivity;
import com.brhs.tx.R;
import com.brhs.tx.domain.NetworkInfo;
import com.brhs.tx.network.NetworkQuality;
import com.brhs.tx.notification.NotificationHandler;

public class IndicatorHandler {
    private static Context appicationContext;
    private static MainActivity mainActivity;
    private static TextView connectStatusIndicatorView;
    private static TextView fcStatusIndicatorView;


    public static void initialize(Context appContext, MainActivity activity) {
        appicationContext = appContext;
        mainActivity = activity;

        connectStatusIndicatorView = mainActivity.findViewById(R.id.connectStatusIndicatorView);
        fcStatusIndicatorView = mainActivity.findViewById(R.id.fcStatusIndicatorView);

        connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorInactive));
        fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorInactive));

        NotificationHandler.logMessage("IndicatorHandler Initialized..", false);
    }

    public static void updateFcStatusIndicator(IndicatorState indicatorState) {
        if (IndicatorState.ERROR == indicatorState) {
            fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorError));
            fcStatusIndicatorView.setText("Crash!");
        }else if (IndicatorState.OK == indicatorState) {
            fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorOk));
            fcStatusIndicatorView.setText("Fly!");
        }else if (IndicatorState.PROCESSING == indicatorState) {
            fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorProcessing));
            fcStatusIndicatorView.setText("Stabilizing");
        }  else if (IndicatorState.READY == indicatorState) {
            fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorReady));
            fcStatusIndicatorView.setText("Start..");
        }else if (IndicatorState.ACTIVE == indicatorState) {
            fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorActive));
            fcStatusIndicatorView.setText("Active");
        } else {
            fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorInactive));
            fcStatusIndicatorView.setText("--");
        }
    }

    public static void inActivateAllIndicators(){
        fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorInactive));
        connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorInactive));
    }

    public static void inActivateFcIndicators(){
        fcStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorInactive));
    }

    public static void updateConnectStatusIndicator(NetworkQuality networkQuality, int rssi) {
        String rssiStr =   NetworkInfo.RSSI_MIN_VALUE != rssi ? "" + rssi : "--";
        if (NetworkQuality.NO_SIGNAL == networkQuality) {
            connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorNoSignal));
            rssiStr+="\nNoSignal";
        } else if (NetworkQuality.VERY_WEAK == networkQuality) {
            connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorVeryWeakSignal));
            rssiStr+="\nV-Weak";
        } else if (NetworkQuality.WEAK == networkQuality) {
            connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorWeakSignal));
            rssiStr+="\nWeak";
        } else if (NetworkQuality.GOOD == networkQuality) {
            connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorGoodSignal));
            rssiStr+="\nGood";
        } else if (NetworkQuality.VERY_GOOD == networkQuality) {
            connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorVeryGoodSignal));
            rssiStr+="\nV-Good";
        } else if (NetworkQuality.EXCELLENT == networkQuality) {
            connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorExcellentSignal));
            rssiStr+="\nExcellent";
        } else {
            connectStatusIndicatorView.setBackgroundColor(appicationContext.getColor(R.color.indicatorInactive));
        }
        connectStatusIndicatorView.setText(rssiStr);
    }

}
