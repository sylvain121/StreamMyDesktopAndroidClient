package sylvain121.github.com.streammydesktopandroid.video.h264;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by user on 25/01/17.
 */

public class NAL {


    private static byte[] inputBuffer = new byte[1000000];
    private static int index = 0;
    private final byte[] data;


    public static NAL parseInputBuffer(byte[] buffer) {
        System.arraycopy(buffer, 0, inputBuffer, index, buffer.length);
        int nextNalPos = getNextNalHeaderPosition();
        if(nextNalPos < 0) {
            byte[] newNal = Arrays.copyOfRange(inputBuffer, 0, nextNalPos);
            inputBuffer = Arrays.copyOfRange(inputBuffer, nextNalPos, inputBuffer.length);
            return new NAL(newNal);
        }
        return null;
    }

    private static int getNextNalHeaderPosition() {
        for(int i = 3; i < inputBuffer.length; i++) {
            if(NAL.isAnnexBstartSequence(i) || NAL.isAnnexBFrameStart(i)) {
                return i;
            }
        }
        return -1;
    }



    private static boolean isAnnexBstartSequence(int i) {
        return inputBuffer[i] == 0x00
                && inputBuffer[i+1] == 0x00
                && inputBuffer[i+2] == 0x00
                && inputBuffer[i+3] == 0x01;
    }

    private static boolean isAnnexBFrameStart(int i) {
        return inputBuffer[i] == 0x00
                && inputBuffer[i+2] == 0x00
                && inputBuffer[i+3] == 0x01;
    }

    public NAL(byte[] newNal) {
        this.data = newNal;
    }


    public boolean isSps() {
        return this.data[4] == 0x67;
    }

    public boolean isPps() {
        return this.data[4] == 0x68;

    }

    public byte[] getByteArray() {
        return data;
    }
}
