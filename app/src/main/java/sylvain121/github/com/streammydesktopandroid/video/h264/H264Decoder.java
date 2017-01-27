package sylvain121.github.com.streammydesktopandroid.video.h264;

import android.view.Surface;
;

/**
 * Created by sylvain121 on 29/12/15.
 */
public class H264Decoder {

    private final int width;
    private final int height;
    private final Surface rendererSurface;
    private final MediaCodecH264DecoderThread thread;

    public H264Decoder(int width, int height, Surface rendererSurface) {
        this.width = width;
        this.height = height;
        this.rendererSurface = rendererSurface;
        this.thread = new MediaCodecH264DecoderThread(this.width, this.height, this.rendererSurface);
        this.thread.init();
        this.thread.start();

    }

    public static H264Decoder create(int width, int height, Surface renderSurface) {
        return new H264Decoder(width, height, renderSurface);
    }


    public void stopDecode() {
        //TODO
    }
}
