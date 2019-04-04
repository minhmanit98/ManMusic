package com.man.manmusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jgabrielfreitas.core.BlurImageView;
import com.man.manmusic.Animations.MusicCoverView;
import com.man.manmusic.Sensor.ShakeDetector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener,ShakeDetector.OnShakeListener {
    private static final String PLAYING = "STATE_PLAY";
    private static final String PAUSE = "STATE_PAUSE";
    private ImageView  btStartPause, btShuffle, btPrevious, btNext, btRepeat;
    private TextView tvSongName, tvSongArtist, tvIndex, tvTimeSong;
    private ImageView songImage;
    private BlurImageView blurImageView;
    private SeekBar seekBarPlay;
    private MediaManager mediaManager;
    private ImageView menuback,menusearch;
    private static int beginSong;
    private static String state;
    private static boolean IS_RUNNING = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private MusicCoverView mCoverView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playsong2);

        menuback=(ImageView) findViewById(R.id.menuback);
        menusearch=(ImageView) findViewById(R.id.menusearch);
        menuback.setOnClickListener(this);
        menusearch.setOnClickListener(this);


        mCoverView = (MusicCoverView) findViewById(R.id.cover);
        mCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                if (MusicCoverView.SHAPE_CIRCLE == coverView.getShape()) {
                    coverView.start();
                }
            }

            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                coverView.morph();
            }
        });



        initView();



    }

    private void initView() {
        state = PLAYING;


        SharedPreferences sharedPref = this.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        beginSong = sharedPref.getInt("CurrentSong", 0);
        Toast.makeText(this, beginSong + "", Toast.LENGTH_LONG).show();


        btStartPause = (ImageView) findViewById(R.id.bt_StartPause);
        btShuffle = (ImageView) findViewById(R.id.bt_Shuffle);
        btPrevious = (ImageView) findViewById(R.id.bt_Previous);
        btNext = (ImageView) findViewById(R.id.bt_Next);
        btRepeat = (ImageView) findViewById(R.id.bt_Repeat);
        songImage = (ImageView) findViewById(R.id.songImage);
        blurImageView=(BlurImageView) findViewById(R.id.album_art_blurred) ;



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(this);



        btStartPause.setOnClickListener(this);
        btShuffle.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btRepeat.setOnClickListener(this);

        tvSongName = (TextView) findViewById(R.id.tv_SongName2);
        tvSongArtist = (TextView) findViewById(R.id.tv_SongArtist2);
       // tvIndex = (TextView) findViewById(R.id.tv_Index);
        tvTimeSong = (TextView) findViewById(R.id.tv_SongTime2);

        seekBarPlay = (SeekBar) findViewById(R.id.seekBar);

        mediaManager = new MediaManager(this);
        mediaManager.getAllAudioSongs();


        IS_RUNNING = true;
        startSeekBar.execute();

        seekBarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
            }
        });

        mediaManager.play(beginSong);
        if(!mCoverView.isRunning()){
            mCoverView.morph();
        }
        initSongDisplay();
    }

    private void seekTo(int progress) {
        mediaManager.seekTo(progress);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_StartPause:
                if (state == PLAYING ) {
                    mediaManager.pause();

                    state = PAUSE;
                    btStartPause.setImageResource(R.drawable.play);
                    mCoverView.stop();
                } else if (state == PAUSE) {
                    mediaManager.play();
                    state = PLAYING;

                    btStartPause.setImageResource(R.drawable.pause);
                    mCoverView.morph();
                }
                break;
            case R.id.bt_Previous:
                mediaManager.previous();
                initSongDisplay();
                break;
            case R.id.bt_Next:
                mediaManager.next();
                initSongDisplay();
                break;
            case R.id.bt_Shuffle:
                break;
            case R.id.bt_Repeat:
                break;
            case R.id.menuback:
                onBackPressed();
                break;
            case R.id.menusearch:
                Intent intent = new Intent(getApplicationContext(),MyActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initSongDisplay() {
        Bitmap artWork = mediaManager.getImage();
        if (artWork == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("this podcast have no image");
            builder.setNeutralButton("OK", null);
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            // set output
            songImage.setImageBitmap(artWork);
            mCoverView.setImageBitmap(artWork);
            blurImageView.setImageBitmap(artWork);
            blurImageView.setBlur(2);

        }
        tvSongName.setText(mediaManager.getCurrentArtist());
        tvSongArtist.setText(mediaManager.getCurrentSongName());
        seekBarPlay.setMax(mediaManager.getMaxDuration());
      //  tvIndex.setText(mediaManager.getCurrentIndex() + "/" + mediaManager.getArrSongs().size());
    }

    private AsyncTask<Void, Integer, Void> startSeekBar = new AsyncTask<Void, Integer, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
            while (IS_RUNNING) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int currentDuration = mediaManager.getCurrentDuration();
                if (currentDuration > 0) {
                    publishProgress(currentDuration);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            seekBarPlay.setProgress(values[0]);
            tvTimeSong.setText(convertToDate(values[0]));
          //  tvIndex.setText(mediaManager.getCurrentIndex() + "/" + mediaManager.getArrSongs().size());
        }
    };

    private String convertToDate(Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(value));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IS_RUNNING = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onShake(int count) {
        Toast.makeText(getApplicationContext(),"Lac lac",Toast.LENGTH_LONG).show();

        if(!mCoverView.isRunning()){
            mCoverView.morph();
        }
        if (state == PLAYING ) {
          mediaManager.next();
            initSongDisplay();
            btStartPause.setImageResource(R.drawable.pause);
        } else if (state == PAUSE) {


            mediaManager.next();
            initSongDisplay();
            mCoverView.stop();
            btStartPause.setImageResource(R.drawable.play);
        }
    }





}
