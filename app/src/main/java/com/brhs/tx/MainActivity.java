package com.brhs.tx;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.brhs.tx.fcStatus.FcStatusHelper;
import com.brhs.tx.indicator.IndicatorHandler;
import com.brhs.tx.joystick.JoyStickHandler;
import com.brhs.tx.network.NetworkHandler;
import com.brhs.tx.network.NetworkIO;
import com.brhs.tx.notification.NotificationHandler;
import com.brhs.tx.switche.SwitchHandler;

/**
 * Main App utility
 */
public class MainActivity extends AppCompatActivity {
    private Context appicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        appicationContext = getApplicationContext();
        try {
            NotificationHandler.initialize(appicationContext, this);
            IndicatorHandler.initialize(appicationContext, this);
            JoyStickHandler.initialize(appicationContext, this);
            SwitchHandler.initialize(appicationContext, this);
            NetworkHandler.initialize(appicationContext, this);
            NetworkIO.initialize(appicationContext, this);
            FcStatusHelper.initialize(appicationContext, this);
            MainController.initialize(appicationContext, this);
        } catch (Exception exp) {
            NotificationHandler.logMessage("Error in start up \n"+exp.getMessage(),true);
        }

    }
/*
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
*/
}