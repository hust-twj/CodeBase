
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class MediaManager {

    private static MediaPlayer mPlayer;

    private static boolean isPause;

    private static String tag;

    private static AudioManager sAudioManager;

    public static void playSound(String filePath, OnPreparedListener preparedListener, OnCompletionListener completionListener) {
        playSound(AudioManager.STREAM_VOICE_CALL, filePath, preparedListener, completionListener);
    }

    public static void playSound(int type, String filePath, OnPreparedListener preparedListener, OnCompletionListener completionListener) {
        if (TextUtils.isEmpty(filePath)) {
            Log.d(TAG, "file path is empty");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Log.d(TAG, "voice file not exist");
            return;
        }
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            //保险起见，设置报错监听
            mPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayer.reset();
                    return false;
                }
            });
        } else {
            mPlayer.reset();//就恢复
        }

        try {
            sAudioManager = (AudioManager) App.getContext().getSystemService(Context.AUDIO_SERVICE);
            mPlayer.setAudioStreamType(type);
            mPlayer.setOnPreparedListener(preparedListener);
            mPlayer.setOnCompletionListener(completionListener);
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
            tag = filePath;
        } catch (Exception e) {
            if (completionListener != null) {
                completionListener.onCompletion(mPlayer);
            }
            e.printStackTrace();
        }
    }

    public static String getTag() {
        return tag;
    }

    public static boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }

    public static void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause = true;
        }
    }

    public static void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public static void resume() {
        if (mPlayer != null && isPause) {
            mPlayer.start();
            isPause = false;
        }
    }

    public static void release() {
        if (mPlayer != null) {
            tag = null;
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * 切换到外放
     */
    public static void changeToSpeaker() {
        if (sAudioManager == null) {
            return;
        }
        sAudioManager.setMode(AudioManager.MODE_NORMAL);
        sAudioManager.setSpeakerphoneOn(true);
    }

    /**
     * 切换到耳机模式
     */
    public static void changeToHeadset() {
        if (sAudioManager == null) {
            return;
        }
        sAudioManager.setSpeakerphoneOn(false);
    }

    /**
     * 切换到听筒
     */
    public static void changeToReceiver() {
        if (sAudioManager == null) {
            return;
        }
        sAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        sAudioManager.setSpeakerphoneOn(false);
    }


}
