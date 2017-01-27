package sylvain121.github.com.streammydesktopandroid.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import sylvain121.github.com.streammydesktopandroid.video.h264.NAL;
import sylvain121.github.com.streammydesktopandroid.video.h264.NALBuffer;


/**
 * Created by user on 25/01/17.
 */

public class VideoStream {

    private int videoPort = 8001;
    private Socket socket;
    private InputStream inputStream;

    public void connect(String ipAddress) {
        try {
            InetAddress serverAddress = InetAddress.getByName(ipAddress);
            socket = new Socket(serverAddress, videoPort);
            inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int read;
            while((read = inputStream.read(buffer)) != -1) {
                NAL nal = NAL.parseInputBuffer(buffer);
                NALBuffer.getInstance().addNALtoDecodeQueue(nal);
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
