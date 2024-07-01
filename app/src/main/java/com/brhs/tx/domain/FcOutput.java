package com.brhs.tx.domain;

public class FcOutput {
    public static final int MAX_FC_OUTPUT_LENGTH = 128;
    public static final byte[] fcOutputData = new byte[FcOutput.MAX_FC_OUTPUT_LENGTH];
    public static boolean canStart;
    public static boolean canArm;
    public static boolean canStabilize;
    public static boolean canFly;
    public static boolean hasCrashed;
    public static int pitch;
    public static int roll;
    public static int yaw;
    public static int alt;
    public static int vVelocity;
    public static int imuFrequency;
    public static int gyroFrequency;
    public static int accFrequency;
    private FcOutput() {
        //Masking instantiation
    }
}
