package com.brhs.tx.joystick;

import android.content.Context;
import android.widget.TextView;

import com.brhs.tx.MainActivity;
import com.brhs.tx.R;
import com.brhs.tx.domain.UserInput;
import com.brhs.tx.notification.NotificationHandler;
import com.brhs.tx.utils.MathUtils;

/**
 * Joy stick handler
 */
public class JoyStickHandler {
    public static int MIN_OUTPUT_MOVEMENT_RANGE = 1000;
    public static int MAX_OUTPUT_MOVEMENT_RANGE = 2000;
    public static int JOYSTICK_MIDDLE_VALUE = (MIN_OUTPUT_MOVEMENT_RANGE + MAX_OUTPUT_MOVEMENT_RANGE) / 2;
    public static int THROTTLE_STICK_DEAD_BAND = 25;
    public static int YAW_STICK_DEAD_BAND = 60;
    public static int PITCH_ROLL_STICK_DEAD_BAND = 60;
    private static TextView throttleValueView;
    private static TextView yawValueView;
    private static TextView rollValueView;
    private static TextView pitchValueView;
    private static JoyStickView throttleYawJoystickView;
    private static JoyStickView pitchRollJoystickView;
    private static Context appicationContext;
    private static MainActivity mainActivity;

    public static void initialize(Context appContext, MainActivity activity) {
        appicationContext = appContext;
        mainActivity = activity;

        yawValueView = (TextView) mainActivity.findViewById(R.id.yawValueView);
        throttleValueView = (TextView) mainActivity.findViewById(R.id.throttleValueView);
        rollValueView = (TextView) mainActivity.findViewById(R.id.rollValueView);
        pitchValueView = (TextView) mainActivity.findViewById(R.id.pitchValueView);

        throttleYawJoystickView = (JoyStickView) mainActivity.findViewById(R.id.throttleYawJoystickView);
        pitchRollJoystickView = (JoyStickView) mainActivity.findViewById(R.id.pitchRollJoystickView);
        pitchRollJoystickView.setLeft(false);

        throttleYawJoystickView.setOnJoystickMovedListener(new ThrottleAndYawJoyStickListener());
        pitchRollJoystickView.setOnJoystickMovedListener(new PitchRollYawJoyStickListener());

        yawValueView.setText("" + JOYSTICK_MIDDLE_VALUE);
        throttleValueView.setText("" + JOYSTICK_MIDDLE_VALUE);
        rollValueView.setText("" + JOYSTICK_MIDDLE_VALUE);
        pitchValueView.setText("" + JOYSTICK_MIDDLE_VALUE);

        UserInput.yaw = JOYSTICK_MIDDLE_VALUE;
        UserInput.throttle = JOYSTICK_MIDDLE_VALUE;
        UserInput.roll = JOYSTICK_MIDDLE_VALUE;
        UserInput.pitch = JOYSTICK_MIDDLE_VALUE;

        NotificationHandler.logMessage("JoyStick Handler Initialized..", false);
    }

    public static class ThrottleAndYawJoyStickListener implements JoyStickListener {
        @Override
        public void OnMoved(float pan, float tilt) {
            int yaw = MathUtils.mapToRangeInt(pan, -JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, MIN_OUTPUT_MOVEMENT_RANGE, MAX_OUTPUT_MOVEMENT_RANGE);
            int throttle = MathUtils.mapToRangeInt(tilt, -JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, MIN_OUTPUT_MOVEMENT_RANGE, MAX_OUTPUT_MOVEMENT_RANGE);
            UserInput.throttle = MathUtils.applyDeadBandInt(JOYSTICK_MIDDLE_VALUE, throttle, THROTTLE_STICK_DEAD_BAND);
            UserInput.yaw = MathUtils.applyDeadBandInt(JOYSTICK_MIDDLE_VALUE, yaw, YAW_STICK_DEAD_BAND);
            throttleValueView.setText("" + UserInput.throttle);
            yawValueView.setText("" + UserInput.yaw);
        }

        @Override
        public void OnReleased() {
        }

        @Override
        public void OnReturnedToCenter() {
        }
    }

    public static class PitchRollYawJoyStickListener implements JoyStickListener {
        @Override
        public void OnMoved(float pan, float tilt) {
            int roll = MathUtils.mapToRangeInt(pan, -JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, MIN_OUTPUT_MOVEMENT_RANGE, MAX_OUTPUT_MOVEMENT_RANGE);
            int pitch = MathUtils.mapToRangeInt(tilt, -JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, JoyStickConstants.JOYSTICK_MOVEMENT_RESOLUTION, MIN_OUTPUT_MOVEMENT_RANGE, MAX_OUTPUT_MOVEMENT_RANGE);
            UserInput.roll = MathUtils.applyDeadBandInt(JOYSTICK_MIDDLE_VALUE, roll, PITCH_ROLL_STICK_DEAD_BAND);
            UserInput.pitch = MathUtils.applyDeadBandInt(JOYSTICK_MIDDLE_VALUE, pitch, PITCH_ROLL_STICK_DEAD_BAND);
            rollValueView.setText("" + UserInput.roll);
            pitchValueView.setText("" + UserInput.pitch);
        }

        @Override
        public void OnReleased() {
        }

        @Override
        public void OnReturnedToCenter() {
        }
    }

}
