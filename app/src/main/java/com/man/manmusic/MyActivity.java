package com.man.manmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.man.manmusic.Adapter.SongListAdapter;
import com.man.manmusic.Sensor.ShakeDetector;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MyActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, ShakeDetector.OnShakeListener{
    private ListView lvSong;
    public static MediaManager mediaManager;

    private static SongListAdapter mSongListAdapter;
    private Handler handler = new Handler();
    private boolean run = false;

    //playActivity
    private static final String PLAYING = "STATE_PLAY";
    private static final String PAUSE = "STATE_PAUSE";

    private static final String LOOPING = "STATE_LOOPING";
    private static final String NOLOOP = "STATE_NOLOOP";

    private static final String SHUFFLING = "STATE_SHUFFLING";
    private static final String NOSHUFFLE = "STATE_NOSHUFFLE";

    private ImageView btStartPause, btShuffle, btPrevious, btNext, btRepeat;
    private TextView tvSongName, tvSongArtist, tvIndex, tvTimeSong;
    private SeekBar seekBarPlay;
    private static int beginSong;
    private static String state;
    private static String looping;
    private static String shuffling;
    private static boolean IS_RUNNING = true;
    private ImageView songImage;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main3);

        mediaManager= new MediaManager(this);
        initView();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(this);
        initSongDisplay();

    }

    private void initView() {


        lvSong = (ListView) findViewById(R.id.lv_SongList);


        mediaManager.getAllAudioSongs();

        mSongListAdapter = new SongListAdapter(this, mediaManager.getArrSongs());
        lvSong.setAdapter(mSongListAdapter);
        lvSong.setOnItemClickListener(this);

//        doStart();

        state = PAUSE;
        looping = NOLOOP;
        shuffling = NOSHUFFLE;
        btStartPause = (ImageView) findViewById(R.id.bt_StartPause);
//        btShuffle = (ImageView) findViewById(R.id.bt_Shuffle);
//        btPrevious = (ImageView) findViewById(R.id.bt_Previous);
//        btNext = (ImageView) findViewById(R.id.bt_Next);
        btRepeat = (ImageView) findViewById(R.id.bt_Repeat);
        btStartPause.setOnClickListener(this);
//        btShuffle.setOnClickListener(this);
//        btPrevious.setOnClickListener(this);
//        btNext.setOnClickListener(this);
//        btRepeat.setOnClickListener(this);

        tvSongName = (TextView) findViewById(R.id.tv_SongName2);
        songImage = (ImageView) findViewById(R.id.songImage);
        tvSongArtist = (TextView) findViewById(R.id.tv_SongArtist2);
//        tvIndex = (TextView) findViewById(R.id.tv_Index);
//        tvTimeSong = (TextView) findViewById(R.id.tv_SongTime2);

        seekBarPlay = (SeekBar) findViewById(R.id.seekBar);
        songImage.setOnClickListener(this);
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
        initSongDisplay();

    }

    private void initSongDisplay() {
        tvSongArtist.setText(mediaManager.getCurrentSongName());
        tvSongName.setText(mediaManager.getCurrentArtist());
        seekBarPlay.setMax(mediaManager.getMaxDuration());
        songImage.setImageBitmap(mediaManager.getImage());
//        tvIndex.setText(mediaManager.getCurrentIndex() + "/" + mediaManager.getArrSongs().size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_StartPause:
                mediaManager.play();
                if (state == PLAYING) {
                    mediaManager.pause();
                    state = PAUSE;
                    btStartPause.setImageResource(R.drawable.play);
                } else if (state == PAUSE) {
                    mediaManager.play();
                    state = PLAYING;
                    btStartPause.setImageResource(R.drawable.pause);
                }
                break;
            case R.id.songImage:
                Intent intent2 = new Intent(getApplicationContext(),PlayActivity.class);
            //    intent2.putExtra("mediaManager",mediaManager);
                startActivity(intent2);
                break;
//            case R.id.bt_Previous:
//                mediaManager.previous();
//                break;
//            case R.id.bt_Next:
//                mediaManager.next();
//                break;
//            case R.id.bt_Shuffle:
//                if (shuffling == NOSHUFFLE) {
//                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
//                    mediaManager.shuffle(true);
//                    shuffling = SHUFFLING;
//                    btShuffle.setImageResource(R.drawable.shuffle);
//                } else if (shuffling == SHUFFLING) {
//                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
//                    mediaManager.shuffle(false);
//                    shuffling = NOSHUFFLE;
//                    btShuffle.setImageResource(R.drawable.shuffledisabled);
//                }
//                break;
//            case R.id.bt_Repeat:
//                if (looping == NOLOOP) {
//                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
//                    mediaManager.repeatSong(true);
//                    looping = LOOPING;
//                    btRepeat.setImageResource(R.drawable.repeat);
//                } else if (looping == LOOPING) {
//                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
//                    mediaManager.repeatSong(false);
//                    looping = NOLOOP;
//                    btRepeat.setImageResource(R.drawable.repeatoff);
//                }
//                break;
        }
    }

    private void seekTo(int progress) {
        mediaManager.seekTo(progress);
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
            tvSongArtist.setText(mediaManager.getCurrentSongName());
            tvSongName.setText(mediaManager.getCurrentArtist());
            songImage.setImageBitmap(mediaManager.getImage());
//            tvTimeSong.setText(convertToDate(values[0]));
//            tvIndex.setText(mediaManager.getCurrentIndex() + "/" + mediaManager.getArrSongs().size());
            if (state == PLAYING) {
                btStartPause.setImageResource(R.drawable.pause);
            }
        }
    };

    public void repeatSong() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_Backup:
                SharedPreferences sharedPref = this.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                int songIndex = sharedPref.getInt("Song", 0);
                int songTime = sharedPref.getInt("Length", 0);
                if (state == PLAYING) {
                    mediaManager.stop();
                    mediaManager.play(songIndex);
                    mediaManager.seekTo(songTime);
                } else if (state == PAUSE) {
                    mediaManager.stop();
                    mediaManager.play(songIndex);
                    mediaManager.seekTo(songTime);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ItemSong itemSong = mSongListAdapter.getItem(position);
//
//        File file = new File(itemSong.getDataPath());
//        boolean deleted = file.delete();
//        Toast.makeText(this, deleted + "", Toast.LENGTH_LONG).show();
//        setBeginSong(position);
//        toPlaySongActivity();
        if (IS_RUNNING == true) {
            state = PLAYING;
            mediaManager.stop();
        }
        mediaManager.play(position);
        initSongDisplay();

    }

    private void toPlaySongActivity() {
        Intent myIntent = new Intent(this, PlayActivity.class);
        startActivity(myIntent);
    }

    private void setBeginSong(int newPosition) {

        SharedPreferences sharedPref = this.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("CurrentSong", newPosition);
        editor.commit();
    }

    private void doStart() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateListView();
                        }
                    });
                }


            }
        });
        run = true;
        thread.start();
    }

    private void updateListView() {
//        mSongListAdapter.addSong();
        mSongListAdapter.notifyDataSetChanged();
    }


    private String convertToDate(Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(value));
    }

    @Override
    protected void onDestroy() {
        IS_RUNNING = false;
//        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putInt("Song", mediaManager.getCurrentIndex() - 1);
//        editor.putInt("Length", mediaManager.getCurrentDuration());
//        editor.commit();
//        Log.i("Giang", mediaManager.getCurrentIndex() + "");
//        Log.i("Giang", mediaManager.getCurrentDuration() + "");
        super.onDestroy();
//        Log.i("Giangd", mediaManager.getCurrentIndex() + "");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static void createTable() {
        mSongListAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("Song", mediaManager.getCurrentIndex() - 1);
        editor.putInt("Length", mediaManager.getCurrentDuration());
        editor.commit();
        Log.i("Giang", mediaManager.getCurrentIndex() + "");
        Log.i("Giang", mediaManager.getCurrentDuration() + "");
        super.onStop();
    }

    @Override
    public void onShake(int count) {
        Toast.makeText(getApplicationContext(),"Lac lac",Toast.LENGTH_LONG).show();
        mediaManager.next();
        initSongDisplay();
    }

    @Override
    public void onResume() {
        super.onResume();
        initSongDisplay();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}
