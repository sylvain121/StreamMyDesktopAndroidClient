package sylvain121.github.com.streammydesktopandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sylvain121.github.com.streammydesktopandroid.net.VideoStream;
import sylvain121.github.com.streammydesktopandroid.video.h264.H264Decoder;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceView sv = new SurfaceView(this);
        sv.getHolder().addCallback(this);
        setContentView(sv);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        VideoStream vs = new VideoStream();
        vs.connect("192.168.1.2");
        new H264Decoder(1920, 1080, holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
