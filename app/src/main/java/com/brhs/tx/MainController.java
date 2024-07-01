package com.brhs.tx;

import android.content.Context;
import android.os.Handler;

import com.brhs.tx.domain.FcInput;
import com.brhs.tx.domain.FcOutput;
import com.brhs.tx.domain.NetworkInfo;
import com.brhs.tx.domain.UserInput;
import com.brhs.tx.fcStatus.FcStatusHelper;
import com.brhs.tx.indicator.IndicatorHandler;
import com.brhs.tx.indicator.IndicatorState;
import com.brhs.tx.network.NetworkHandler;
import com.brhs.tx.network.NetworkIO;
import com.brhs.tx.network.NetworkQuality;
import com.brhs.tx.notification.NotificationHandler;
import com.brhs.tx.switche.SwitchHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Note : Disable DHCP and set the DNS IP as 0.0.0.0
 * so that Internet access is not constantly checked.
 */
public class MainController {
    private static Context appicationContext;
    private static MainActivity mainActivity;
    private static String networkConnectionOkMsg;
    private static String networkConnectionErrorMsg;
    private static ScheduledExecutorService fcDataRelayExecutor = Executors.newSingleThreadScheduledExecutor();
    private static boolean initialized = false;
    private static final Handler asyncTaskHandler = new Handler();
    public static void initialize(Context appContext, MainActivity activity) {
        appicationContext = appContext;
        mainActivity = activity;
        networkConnectionOkMsg = mainActivity.getResources().getString(R.string.connection_ok);
        networkConnectionErrorMsg = mainActivity.getResources().getString(R.string.connection_error);
        if (!initialized) {
            fcDataRelayExecutor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    sendDataToFc();
                }
            }, 2, 20, TimeUnit.MILLISECONDS);
            initialized = true;
        }
        NotificationHandler.logMessage("Main Controller Initialized..", false);
    }
    public static void manageFcDataReceive() {
        if(UserInput.connectOn && UserInput.txOn) {
            FcStatusHelper.updateFcStatus();
        }else{
            FcStatusHelper.resetFcStatus();
        }
    }
    public static void sendDataToFc() {
        if (NetworkInfo.networkConnected) {
            collectUserInputs();
            NetworkIO.sendData();
        }
    }
    private static void collectUserInputs() {
        FcInput.throttle = (short) UserInput.throttle;
        FcInput.pitch = (short) UserInput.pitch;
        FcInput.roll = (short) UserInput.roll;
        FcInput.yaw = (short) UserInput.yaw;
        FcInput.txOn = UserInput.txOn ? (byte) 1 : (byte) 0;
        FcInput.land = UserInput.landOn ? (byte) 1 : (byte) 0;
        FcInput.rth = 0;
    }
    public static void manageFcStatusChange() {
        if (FcOutput.hasCrashed) {
            IndicatorHandler.updateFcStatusIndicator(IndicatorState.ERROR);
        } else if (FcOutput.canFly) {
            IndicatorHandler.updateFcStatusIndicator(IndicatorState.OK);
        } else if (FcOutput.canStabilize) {
            IndicatorHandler.updateFcStatusIndicator(IndicatorState.PROCESSING);
        } else if (FcOutput.canArm) {
            IndicatorHandler.updateFcStatusIndicator(IndicatorState.READY);
        }else if (FcOutput.canStart) {
            IndicatorHandler.updateFcStatusIndicator(IndicatorState.ACTIVE);
        } else {
            IndicatorHandler.updateFcStatusIndicator(IndicatorState.INACTIVE);
        }
    }
    public static void manageFcOnOffSwitchStateChange() {
        if (UserInput.txOn && UserInput.connectOn) {
            NetworkHandler.updateNetworkStatus();
            if (UserInput.connectOn && NetworkInfo.networkConnected) {
                SwitchHandler.enableAllFcSubSwitches();
                IndicatorHandler.updateFcStatusIndicator(IndicatorState.ACTIVE);
            } else {
                SwitchHandler.disableAllFcSubSwitches();
                IndicatorHandler.updateFcStatusIndicator(IndicatorState.INACTIVE);
            }
        } else {
            SwitchHandler.disableAllFcSubSwitches();
            IndicatorHandler.updateFcStatusIndicator(IndicatorState.INACTIVE);
        }
    }
    public static void manageConnectSwitchStateChange() {
        if (UserInput.connectOn) {
            NetworkHandler.updateNetworkStatus();
            if (NetworkInfo.networkConnected) {
                IndicatorHandler.updateConnectStatusIndicator(NetworkInfo.networkQuality, NetworkInfo.networkRssi);
                SwitchHandler.enableFcOnOffSwitch();
                manageNetworkStateChange();
            } else {
                SwitchHandler.turnOffAllSwitches();
                SwitchHandler.disableAllFcSwitches();
                IndicatorHandler.inActivateAllIndicators();
                IndicatorHandler.updateConnectStatusIndicator(NetworkQuality.NA, NetworkInfo.RSSI_MIN_VALUE);
                NotificationHandler.notify("Network Connection Failed!!", networkConnectionErrorMsg);
            }
        } else {
            SwitchHandler.turnOffAllSwitches();
            SwitchHandler.disableAllFcSwitches();
            IndicatorHandler.inActivateAllIndicators();
            IndicatorHandler.updateConnectStatusIndicator(NetworkQuality.NA, NetworkInfo.RSSI_MIN_VALUE);
        }
    }
    public static void manageNetworkStateChange() {
        if (UserInput.connectOn) {
            if (NetworkInfo.networkConnected) {
                if (!NetworkInfo.wasNetworkConnected) {
                    NotificationHandler.logMessage(networkConnectionOkMsg, false);
                    IndicatorHandler.updateConnectStatusIndicator(NetworkInfo.networkQuality, NetworkInfo.networkRssi);
                }
                NetworkInfo.wasNetworkConnected = true;
                if (NetworkInfo.prevNetworkRssi != NetworkInfo.networkRssi) {
                    IndicatorHandler.updateConnectStatusIndicator(NetworkInfo.networkQuality, NetworkInfo.networkRssi);
                    NetworkInfo.prevNetworkRssi = NetworkInfo.networkRssi;
                }
            } else {
                if (NetworkInfo.wasNetworkConnected) {
                    NotificationHandler.logMessage(networkConnectionErrorMsg, false);
                    IndicatorHandler.updateConnectStatusIndicator(NetworkQuality.NO_SIGNAL, NetworkInfo.RSSI_MIN_VALUE);
                }
                NetworkInfo.wasNetworkConnected = false;
            }
        } else {
            NetworkInfo.wasNetworkConnected = false;
            NetworkInfo.prevNetworkRssi = NetworkInfo.RSSI_MIN_VALUE;
        }
    }
    public static void manageAppClose() {
        if (UserInput.txOn && NetworkInfo.networkConnected) {
            SwitchHandler.turnOffAllSwitches();
            asyncTaskHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainActivity.finish();
                }
            }, 500);
        } else {
            mainActivity.finish();
        }
    }
}
