package sylvain121.github.com.streammydesktopandroid.video.h264;

import android.util.Log;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sylvain121 on 11/01/16.
 */
public class NALBuffer {


    private static NALBuffer instance;

    private NALBuffer() {

    }

    public static NALBuffer getInstance() {
        if( instance == null) {
            instance = new NALBuffer();
        }

        return instance;
    }

    LinkedBlockingQueue<NAL> buffer = new LinkedBlockingQueue<NAL>();


    public void addNALtoDecodeQueue(NAL nal) {

        try {
            this.buffer.put(nal);
            bytesToHex(nal.getByteArray(), 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public NAL getNext() throws InterruptedException {
        synchronized (buffer) {
            return this.buffer.take();
        }
    }
    private void bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        String result = "";
        for (int i=0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
            result +=" ";
            result +=sb.toString();
        }
        Log.d(this.getClass().getName(), result);
    }
}
