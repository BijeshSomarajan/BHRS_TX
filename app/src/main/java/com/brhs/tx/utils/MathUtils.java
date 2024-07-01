package com.brhs.tx.utils;

/**
 * Mathematical utilities
 */
public class MathUtils {
    /**
     * Maps the input value to given range
     *
     * @param inValue
     * @param minInRange
     * @param maxInRange
     * @param minOutRange
     * @param maxOutRange
     * @return
     */
    public static int mapToRangeInt(float inValue, float minInRange, float maxInRange, float minOutRange, float maxOutRange) {
        float inputRatio = (inValue - minInRange) / (maxInRange - minInRange);
        float outValue = minOutRange + (maxOutRange - minOutRange) * inputRatio;
        return Math.round(outValue);
    }

    /**
     * Left Shifts the 32bit int to nBits
     *
     * @param value
     * @param nBits
     * @return
     */
    public static int shiftBitsLeft32(int value, int nBits) {
        if (value < 0) {
            return (-((-value) << nBits));
        } else {
            return (value << nBits);
        }

    }

    /**
     * Applies deadband
     *
     * @param value
     * @param boundary
     * @return
     */
    public static int applyDeadBandInt(int neutralValue, int value, int boundary) {
        int delta = value - neutralValue;
        if (Math.abs(delta) >= boundary) {
            if (delta < 0.0f) {
                return value + boundary;
            } else {
                return value - boundary;
            }
        } else {
            return neutralValue;
        }
    }

}
