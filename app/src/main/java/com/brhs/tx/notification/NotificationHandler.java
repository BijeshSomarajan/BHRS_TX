package com.brhs.tx.notification;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.brhs.tx.MainActivity;

public class NotificationHandler {
    private static Context appicationContext;
    private static MainActivity mainActivity;
    private static TextView logConsoleTextView;
    private static ScrollView logConsoleScrollView;
    private static ImageButton clearLogConsoleButton;
    private static final Handler asyncLogHandler = new Handler();

    public static void initialize(Context appContext, MainActivity activity) {
        appicationContext = appContext;
        mainActivity = activity;
        /*
        logConsoleTextView = mainActivity.findViewById(R.id.logConsoleView);
        clearLogConsoleButton = mainActivity.findViewById(R.id.clearLogConsoleButton);
        logConsoleScrollView = mainActivity.findViewById(R.id.logConsoleScrollView);
        clearLogConsoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logConsoleTextView.setText("");
            }
        });
        */
        NotificationHandler.logMessage("NotificationHandler Initialized..", false);
    }

    public static void notify(String title, String msg) {
        asyncLogHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle(title);
                builder.setMessage(msg);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }, 1);
    }

    public static void logMessage(String msg, boolean clear) {
        /*
        asyncLogHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (clear) {
                    logConsoleTextView.setText(msg+"\n");
                } else {
                    logConsoleTextView.append(msg+"\n");
                    logConsoleScrollView.fullScroll(View.FOCUS_DOWN);
                }
            }
        }, 1);
        */
    }

}
