package sylvain121.github.com.streammydesktopandroid.video.h264;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;


class MediaCodecH264DecoderThread extends Thread {

    private MediaCodec decoder;
    private Surface surface;
    private long startMs;
    private int width;
    private int height;
    private MediaFormat format;
    private ByteBuffer[] decodeInputBuffers;


    public MediaCodecH264DecoderThread(int width, int height, Surface rendererSurface) {


        this.surface = rendererSurface;
        this.width = width;
        this.height = height;

    }

    public void init() {

        try {
            decoder = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, this.width, this.height);
            decoder.configure(format, surface, null, 0);
            decoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            decoder.start();
            decodeInputBuffers = decoder.getInputBuffers();
            System.out.println("decode-----" + decoder.getCodecInfo().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {


        ByteBuffer decodeDataBuffer = null;

        try {
            decodeDataBuffer = ByteBuffer.wrap(NALBuffer.getInstance().getNext().getByteArray());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int inputBufferIndex = decoder.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            ByteBuffer buffer = decodeInputBuffers[inputBufferIndex];
            buffer.clear();
            buffer.put(decodeDataBuffer.array()); // FIXME
            decoder.queueInputBuffer(inputBufferIndex, 0, decodeDataBuffer.array().length, 0, 0);
            decodeDataBuffer.clear();
            decodeDataBuffer = null;
        }

        int outputBufferIndex = decoder.dequeueOutputBuffer(info, 1000);
        System.out.println("outputBufferIndex: " + outputBufferIndex);

        do {

            if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                //no output available yet
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                //encodeOutputBuffers = mDecodeMediaCodec.getOutputBuffers();
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat formats = decoder.getOutputFormat();
                //mediaformat changed
            } else if (outputBufferIndex < 0) {
                //unexpected result from encoder.dequeueOutputBuffer
            } else {
                decoder.releaseOutputBuffer(outputBufferIndex,
                        true);

                outputBufferIndex = decoder.dequeueOutputBuffer(info,
                        0);
                System.out.println("inner outputBufferIndex: "
                        + outputBufferIndex);
            }
        } while (outputBufferIndex > 0);
    }
}