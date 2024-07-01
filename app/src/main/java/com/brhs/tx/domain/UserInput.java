package com.brhs.tx.domain;

public class UserInput {
    public static boolean connectOn = false;
    public static boolean txOn = false;
    public static boolean landOn = false;
    public static boolean rthOn = false;
    public static int throttle = 0;
    public static int yaw = 0;
    public static int pitch = 0;
    public static int roll = 0;
    private UserInput() {
        //Masking instantiation
    }
}
