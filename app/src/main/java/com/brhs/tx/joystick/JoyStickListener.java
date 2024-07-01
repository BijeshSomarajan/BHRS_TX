package com.brhs.tx.joystick;

/**
 * Joy stick listener interface
 */
public interface JoyStickListener {
    public void OnMoved(float pan, float tilt);
    public void OnReleased();
    public void OnReturnedToCenter();
}
