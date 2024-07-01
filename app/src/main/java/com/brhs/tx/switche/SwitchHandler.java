package com.brhs.tx.switche;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;

import com.brhs.tx.MainActivity;
import com.brhs.tx.MainController;
import com.brhs.tx.R;
import com.brhs.tx.domain.UserInput;
import com.brhs.tx.notification.NotificationHandler;

/**
 * Handler for switches
 */
public class SwitchHandler {
    private static Context appicationContext;
    private static MainActivity mainActivity;
    private static ToggleButton txOnOffSwitch;
    private static ToggleButton landSwitch;
    private static ToggleButton connectSwitch;
    private static ToggleButton rthSwitch;
    private static ImageButton closeAppButton;
    private static String quitActionPositiveMessage;
    private static String quitActionNegativeMessage;
    private static String quitActionTitle;
    private static String quitActionMessage;

    /**
     * Initializes the switch handler
     *
     * @param appContext
     * @param activity
     */
    public static void initialize(Context appContext, MainActivity activity) {
        appicationContext = appContext;
        mainActivity = activity;

        txOnOffSwitch = (ToggleButton) mainActivity.findViewById(R.id.txOnOffSwitch);
        landSwitch = (ToggleButton) mainActivity.findViewById(R.id.landSwitch);
        connectSwitch = (ToggleButton) mainActivity.findViewById(R.id.connectSwitch);
        rthSwitch = (ToggleButton) mainActivity.findViewById(R.id.rthSwitch);
        closeAppButton = (ImageButton) mainActivity.findViewById(R.id.appQuitButton);

        connectSwitch.setOnCheckedChangeListener(new ConnectSwitchListener());
        txOnOffSwitch.setOnCheckedChangeListener(new TXOnOffSwitchListener());
        landSwitch.setOnCheckedChangeListener(new LandSwitchListener());
        rthSwitch.setOnCheckedChangeListener(new RTHSwitchListener());
        closeAppButton.setOnClickListener(new CloseAppButtonListener());

        quitActionPositiveMessage = mainActivity.getResources().getString(R.string.quitActionPositiveMessage);
        quitActionNegativeMessage = mainActivity.getResources().getString(R.string.quitActionNegativeMessage);
        quitActionTitle = mainActivity.getResources().getString(R.string.quitActionTitle);
        quitActionMessage = mainActivity.getResources().getString(R.string.quitActionMessage);

        UserInput.connectOn = false;
        UserInput.txOn = false;
        UserInput.landOn = false;
        UserInput.rthOn = false;

        NotificationHandler.logMessage("Switch Handler Initialized..", false);
    }

    public static void turnOffAllSwitches() {
        turnOffAllFcSwitches();
        if (connectSwitch.isChecked()) {
            connectSwitch.setChecked(false);
        }
        UserInput.connectOn = false;
    }

    public static void enableFcOnOffSwitch() {
        if (txOnOffSwitch.isChecked()) {
            txOnOffSwitch.setChecked(false);
            UserInput.txOn = false;
        }
        txOnOffSwitch.setEnabled(true);
    }

    public static void turnOffAllFcSwitches() {
        turnOffAllFcSubSwitches();
        if (txOnOffSwitch.isChecked()) {
            txOnOffSwitch.setChecked(false);
            UserInput.txOn = false;
        }
    }

    public static void disableAllFcSwitches() {
        disableAllFcSubSwitches();
        if (txOnOffSwitch.isChecked()) {
            txOnOffSwitch.setChecked(false);
            UserInput.txOn = false;
        }
        txOnOffSwitch.setEnabled(false);
    }

    public static void turnOffAllFcSubSwitches() {
        if (landSwitch.isChecked()) {
            landSwitch.setChecked(false);
        }
        if (rthSwitch.isChecked()) {
            rthSwitch.setChecked(false);
        }
        UserInput.landOn = false;
        UserInput.rthOn = false;
    }

    public static void disableAllFcSubSwitches() {
        turnOffAllFcSubSwitches();
        landSwitch.setEnabled(false);
        rthSwitch.setEnabled(false);
    }

    public static void enableAllFcSubSwitches() {
        turnOffAllFcSubSwitches();
        landSwitch.setEnabled(true);
        rthSwitch.setEnabled(true);
    }

    /**
     * TX On/Off switch listener
     */
    public static class CloseAppButtonListener implements CompoundButton.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle(quitActionTitle);
            builder.setMessage(quitActionMessage);
            builder.setPositiveButton(quitActionPositiveMessage, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainController.manageAppClose();
                }
            });
            builder.setNegativeButton(quitActionNegativeMessage, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * TX On/Off switch listener
     */
    public static class ConnectSwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            UserInput.connectOn = isChecked;
            MainController.manageConnectSwitchStateChange();
        }
    }

    /**
     * TX On/Off switch listener
     */
    public static class TXOnOffSwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            UserInput.txOn = isChecked;
            MainController.manageFcOnOffSwitchStateChange();
        }
    }

    /**
     * Land switch listener
     */
    public static class LandSwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            UserInput.landOn = isChecked;
        }
    }

    /**
     * Land switch listener
     */
    public static class RTHSwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            UserInput.rthOn = isChecked;
        }
    }

}
