package com.man.manmusic.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import com.man.manmusic.R;

public class BackgroundSoundService extends Service
        implements
        MediaPlayer.OnErrorListener,

        // Interface used by the visual representation of the player controls.
        MediaController.MediaPlayerControl,

        // implemented to possibly upgrade the media player interface.
        MediaPlayer.OnBufferingUpdateListener

{
    // Connect service who to call onBind
    private final IBinder mBinder = new ServiceBinder();

    // Instance of media player
    MediaPlayer mPlayer;

    // Saves the data buffer of the media player (used by MediaController).
    private int mBuffer = 0;

    public BackgroundSoundService() { }

    // Called by the interface ServiceConnected when calling the service
    public class ServiceBinder extends Binder {
        public BackgroundSoundService getService()
        {
            return BackgroundSoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent context)
    {
        return mBinder;
    }

    @Override
    public void onCreate () {
        super.onCreate();

        create();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        if(mPlayer != null)
        {
            try{
                mPlayer.stop();
                mPlayer.release();
            }finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();

        if(mPlayer != null)
        {
            try{
                mPlayer.stop();
                mPlayer.release();
            }finally {
                mPlayer = null;
            }
        }
        return false;
    }

    public void create()
    {
        mPlayer = MediaPlayer.create(this, R.raw.music1);
        mPlayer.setOnErrorListener(this);

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return mBuffer;
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if(mPlayer != null && isPlaying())
        {
            mPlayer.pause();
        }
    }

    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);
    }

    public void resume()
    {
        if(isPlaying() == false)
        {
            seekTo(getCurrentPosition());
            start();
        }
    }

    @Override
    public void start() {
        if (mPlayer == null) create();

        Thread th = new Thread(new Runnable() {
            @Override
            public void run()
            {
                mPlayer.start();
            }
        });
        th.start();
    }

    public void stop() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mBuffer = percent;

    }
}