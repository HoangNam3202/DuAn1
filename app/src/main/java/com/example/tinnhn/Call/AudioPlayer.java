package com.example.tinnhn.Call;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.example.tinnhn.R;

import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer {

    static final String LOG_TAG = AudioPlayer.class.getSimpleName();

    private Context mContext;

    private MediaPlayer mPlayer,mPlayer2,mPlayer3,mPlayer4;

    private AudioTrack mProgressTone;

    private final static int SAMPLE_RATE = 16000;

    public AudioPlayer(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void playRingtone() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        // Honour silent mode
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_RING);

                try {
                    mPlayer.setDataSource(mContext,
                            Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.phone_loud1));
                    mPlayer.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not setup media player for ringtone");
                    mPlayer = null;
                    return;
                }
                mPlayer.setLooping(true);
                mPlayer.start();
                break;
        }
    }
    public void playjointone() {

        AudioManager audioManager1 = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager1.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                mPlayer2 = new MediaPlayer();
                mPlayer2.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                try {
                    mPlayer2.setDataSource(mContext,
                            Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.in));
                    mPlayer2.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not setup media player for ringtone");
                    mPlayer2 = null;
                    return;
                }
                mPlayer2.start();
                break;
        }
    }
    public void playmestone() {

        AudioManager audioManager1 = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager1.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                mPlayer4 = new MediaPlayer();
                mPlayer4.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                try {
                    mPlayer4.setDataSource(mContext,
                            Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.mestone));
                    mPlayer4.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not setup media player for ringtone");
                    mPlayer4 = null;
                    return;
                }
                mPlayer4.start();
                break;
        }
    }
    public void stopmess(){
        if (mPlayer4 != null) {
            mPlayer4.stop();
            mPlayer4.release();
            mPlayer4= null;
        }
    }
    public void stopin(){
        if (mPlayer2 != null) {
            mPlayer2.stop();
            mPlayer2.release();
            mPlayer2= null;
        }
    }
    public void stopout(){
        if (mPlayer3 != null) {
            mPlayer3.stop();
            mPlayer3.release();
            mPlayer3= null;
        }
    }
    public void playouttone() {

        AudioManager audioManager2 = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager2.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                mPlayer3 = new MediaPlayer();
                mPlayer3.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                try {
                    mPlayer3.setDataSource(mContext,
                            Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.out));
                    mPlayer3.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not setup media player for ringtone");
                    mPlayer3 = null;
                    return;
                }
                mPlayer3.start();
                break;
        }
    }
    public void stopRingtone() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void playProgressTone() {
        stopProgressTone();
        try {
            mProgressTone = createProgressTone(mContext);
            mProgressTone.play();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not play progress tone", e);
        }
    }

    public void stopProgressTone() {
        if (mProgressTone != null) {
            mProgressTone.stop();
            mProgressTone.release();
            mProgressTone = null;
        }
    }

    private static AudioTrack createProgressTone(Context context) throws IOException {
        AssetFileDescriptor fd = context.getResources().openRawResourceFd(R.raw.ringtone);
        int length = (int) fd.getLength();

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, length, AudioTrack.MODE_STATIC);

        byte[] data = new byte[length];
        readFileToBytes(fd, data);

        audioTrack.write(data, 0, data.length);
        audioTrack.setLoopPoints(0, data.length / 2, 30);

        return audioTrack;
    }

    private static void readFileToBytes(AssetFileDescriptor fd, byte[] data) throws IOException {
        FileInputStream inputStream = fd.createInputStream();

        int bytesRead = 0;
        while (bytesRead < data.length) {
            int res = inputStream.read(data, bytesRead, (data.length - bytesRead));
            if (res == -1) {
                break;
            }
            bytesRead += res;
        }
    }
}