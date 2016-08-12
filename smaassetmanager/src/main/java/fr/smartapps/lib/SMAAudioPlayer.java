package fr.smartapps.lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;


/**
 * Created by vchann on 12/08/2016.
 */
public class SMAAudioPlayer {

    protected Context context;
    protected MediaPlayer mediaPlayer;
    protected int currentPosition;
    protected int maxPosition;
    protected Handler handler = new Handler();
    protected Runnable runnable;
    protected SMAAudioPlayerListener audioPlayerListener;
    public String url;

    private static String TAG = "AudioPlayerManager";

    /*
    Constructor
    */
    public SMAAudioPlayer(Context context, SMAAssetManager assetManager, String url, final SMAAudioPlayerListener audioPlayerListener) {
        this.context = context;
        this.audioPlayerListener = audioPlayerListener;
        this.url = url;

        // reset media player
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        AssetFileDescriptor assetFileDescriptor = assetManager.getAssetFileDescriptor(url);
        if(assetFileDescriptor != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // set runnable callbacks
        ((Activity)context).runOnUiThread(runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    maxPosition = mediaPlayer.getDuration();
                    if(audioPlayerListener != null) {
                        audioPlayerListener.onSongProgress(currentPosition, maxPosition);
                    }
                }
                handler.postDelayed(runnable, 500);
            }
        });

        // set on finish listener
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioPlayerListener.onSongFinish(currentPosition);
            }
        });

        // pause runnable
        handler.removeCallbacks(runnable);
    }

    public void start() {
        if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            if(runnable != null) {
                runnable.run();
            }
        }
    }

    public void pause() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if(handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void stop() {
        pause();
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void release() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null)
            return mediaPlayer.isPlaying();
        return false;
    }

    public void seekTo(int time) {
        if(mediaPlayer != null)
            mediaPlayer.seekTo(time);
    }

    public int getCurrentTimeMilli() {
        if(mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        return 0;
    }

    public int getMaxTimeMilli() {
        if(mediaPlayer != null)
            return mediaPlayer.getDuration();
        return 100;
    }

}
