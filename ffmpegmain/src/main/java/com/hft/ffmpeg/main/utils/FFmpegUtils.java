//package com.hft.ffmpeg.main.utils;
//
//import android.media.AudioFormat;
//import android.media.AudioManager;
//import android.media.AudioTrack;
//import android.support.annotation.IntDef;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
//public final class FFmpegUtils {
//
//    private static final String TAG = "FFmpegUtils";
//
//    static {
//        System.loadLibrary("ffcommand");
//    }
//
//    private FFmpegUtils() {}
//
//    private native static int handle(String[] commands);
//
//    private native static String handleProbe(String[] commands);
//
//    private native static void cancelTaskJni(int cancel);
//
//    private native static int fastStart(String inputFile, String outputFile);
//
//    private static final int STATE_INIT = 0;
//
//    private static final int STATE_RUNNING = 1;
//
//    private static final int STATE_FINISH = 2;
//
//    private static final int STATE_ERROR = 3;
//
//    private static OnHandleListener mProgressListener;
//
//    public static void setOnHandleListener(OnHandleListener listener) {
//        mProgressListener = listener;
//    }
//
//    public static void cmd(String[] commands) {
//        if (mProgressListener != null) {
//            mProgressListener.onBegin();
//        }
//        String msg = "";
//        int result = -1;
//        try {
//            result = handle(commands);
//        } catch (Throwable e) {
//            msg = e.getMessage();
//            e.printStackTrace();
//        }
//        if (mProgressListener != null) {
//            mProgressListener.onEnd(result, msg);
//        }
//    }
//
//    @Documented
//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef({STATE_INIT, STATE_RUNNING, STATE_FINISH, STATE_ERROR})
//    public @interface FFmpegState {}
//
//    public static void onProgressCallback(int position, int duration, @FFmpegState int state) {
//        LogUtil.INSTANCE.e(TAG, "onProgress position=" + position
//                + "--duration=" + duration + "--state=" + state);
//        if (position > duration && duration > 0) {
//            return;
//        }
//        if (mProgressListener != null) {
//            if (position > 0 && duration > 0) {
//                int progress = position * 100 / duration;
//                if (progress < 100 || state == STATE_FINISH || state == STATE_ERROR) {
//                    mProgressListener.onProgress(progress, duration);
//                }
//            } else {
//                mProgressListener.onProgress(position, duration);
//            }
//        }
//    }
//
//    public static void onMsgCallback(String msg) {
//        if (msg != null && !msg.isEmpty()) {
//            LogUtil.INSTANCE.e(TAG, "from native msg=" + msg);
//
//            // silence detect callback
//            if (msg.startsWith("silence") && mProgressListener != null) {
//                mProgressListener.onMsg(msg);
//            }
//        }
//    }
//
//    //video
//    public native int setup2(String filePath, Object surface);
//
//    public native int play2();
//
//    public native void again2(int position);
//
//    public native void playAudio2(boolean playAudio);
//
//    public native void releaseVideo();
//
//    //using AudioTrack to play
//    public native void play(String audioPath, String filterDesc);
//
//    public native void again(String filterDesc);
//
//    public native void releaseAudio();
//
//    //using OpenSL ES to play
//    public native void playAudio(String audioPath);
//
//    public native void stop();
//
//    //mp3
//    public native static void lameInitDefault();
//
//    public native static void lameInit(int inSamplerate, int outChannel,
//                                       int outSamplerate, int outBitrate, float scaleInput, int mode, int vbrMode,
//                                       int quality, int vbrQuality, int abrMeanBitrate, int lowpassFreq, int highpassFreq, String id3tagTitle,
//                                       String id3tagArtist, String id3tagAlbum, String id3tagYear,
//                                       String id3tagComment);
//
//    public native static int lameEncode(short[] buffer_l, short[] buffer_r,
//                                        int samples, byte[] mp3buf);
//
//    public native static int encodeBufferInterleaved(short[] pcm, int samples,
//                                                     byte[] mp3buf);
//
//    public native static int lameFlush(byte[] mp3buf);
//
//    public native static void lameClose();
//
//    /**
//     * JNI interface: select file and push to rtmp server
//     *
//     * @param filePath liveUrl
//     * @param liveUrl  the url of rtmp server
//     * @return the result of pushing stream
//     */
//    public native int pushStream(String filePath, String liveUrl);
//
//    /**
//     * Create an AudioTrack instance for JNI calling
//     *
//     * @param sampleRate sampleRate
//     * @param channels   channel layout
//     * @return AudioTrack
//     */
//
//    private AudioTrack mAudioTrack;
//
//    public AudioTrack createAudioTrack(int sampleRate, int channels) {
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//        int channelConfig;
//        if (channels == 1) {
//            channelConfig = AudioFormat.CHANNEL_OUT_MONO;
//        } else if (channels == 2) {
//            channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
//        } else {
//            channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
//        }
//
//        int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
//
//        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat,
//                bufferSizeInBytes, AudioTrack.MODE_STREAM);
//        return mAudioTrack;
//    }
//
//    public void releaseAudioTrack() {
//        if (mAudioTrack != null) {
//            mAudioTrack.release();
//            mAudioTrack = null;
//        }
//    }
//
//    public interface OnHandleListener {
//        void onBegin();
//        void onMsg(String msg);
//        void onProgress(int progress, int duration);
//        void onEnd(int resultCode, String resultMsg);
//    }
//
//    public static native int setFontConfigEnvironmentVariable(final String variableName, final String variableValue);
//}
