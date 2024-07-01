package com.brhs.tx.fcIO;

import com.brhs.tx.domain.FcInput;
import com.brhs.tx.domain.FcOutput;
import com.brhs.tx.utils.MathUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FcIOHandler {
    /*
       On/Off, Land, RTH, Throtte, pitch,  roll  , yaw
          [0], [1] , [2], [3][4] , [5][6], [7][8], [9][10]
    */
    public static byte[] marshallFCInput() {
        ByteBuffer bb = ByteBuffer.allocate(20);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(FcInput.txOn);
        bb.put(FcInput.land);
        bb.put(FcInput.rth);

        bb.put((byte) (FcInput.throttle));
        bb.put((byte) (FcInput.throttle >> 8));

        bb.put((byte) (FcInput.pitch));
        bb.put((byte) (FcInput.pitch >> 8));

        bb.put((byte) (FcInput.roll));
        bb.put((byte) (FcInput.roll >> 8));

        bb.put((byte) (FcInput.yaw));
        bb.put((byte) (FcInput.yaw >> 8));

        return bb.array();
    }

    /**
     * canStart, canStabilize, canFly, hasCrashed, pitch        , roll           , yaw               , alt              , loopFrequency
     * [0]     , [1]         , [2]   , [3]       , [4][5][6][7] , [8][9][10][11] , [12][13][14][15]  ,[16][17][18][19]  , [20][21][22][23]
     */
    public static void unMarshallFCOutput(int length) {
        ByteBuffer bb = ByteBuffer.wrap(FcOutput.fcOutputData, 0, length);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        FcOutput.canStart = (bb.get() > 0);
        FcOutput.canArm = (bb.get() > 0) ;
        FcOutput.canStabilize = (bb.get() > 0) ;
        FcOutput.canFly = (bb.get() > 0);
        FcOutput.hasCrashed = (bb.get() > 0);

        FcOutput.pitch = (0xFF & bb.get());
        FcOutput.pitch |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.pitch |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.pitch |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);

        FcOutput.roll = (0xFF & bb.get());
        FcOutput.roll |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.roll |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.roll |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);

        FcOutput.yaw = (0xFF & bb.get());
        FcOutput.yaw |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.yaw |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.yaw |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);

        FcOutput.alt = (0xFF & bb.get());
        FcOutput.alt |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.alt |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.alt |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);

        FcOutput.vVelocity = (0xFF & bb.get());
        FcOutput.vVelocity |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.vVelocity |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.vVelocity |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);

        FcOutput.imuFrequency = (0xFF & bb.get());
        FcOutput.imuFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.imuFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.imuFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);

        FcOutput.gyroFrequency = (0xFF & bb.get());
        FcOutput.gyroFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.gyroFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.gyroFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);

        FcOutput.accFrequency = (0xFF & bb.get());
        FcOutput.accFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 8);
        FcOutput.accFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 16);
        FcOutput.accFrequency |= MathUtils.shiftBitsLeft32((0xFF & bb.get()), 24);
    }
}
