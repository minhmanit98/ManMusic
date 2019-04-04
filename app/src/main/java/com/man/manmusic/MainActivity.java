package com.man.manmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.man.manmusic.Animations.MusicCoverView;
import com.man.manmusic.Sensor.ShakeDetector;
import com.man.manmusic.Services.BackgroundSoundService;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private MusicCoverView mCoverView;
    private BackgroundSoundService mServ;
    private boolean mIsBound = false;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent music = new Intent(this, BackgroundSoundService.class);
        startService(music);

        doBindService();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                mServ.start();
                if (mCoverView.isRunning()) {
                    mCoverView.stop();
                    mServ.pause();

                } else {
                    mCoverView.morph();
                }
            }


        });

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCoverView.morph();
                mServ.start();
                if (mCoverView.isRunning()) {
                    mCoverView.stop();
                    mServ.pause();

                } else {
                    mCoverView.morph();

                }
            }
        });
    }
    // interface connection with the service activity
    public void onServiceConnected(ComponentName name, IBinder binder)
    {
        mServ = (BackgroundSoundService) ((BackgroundSoundService.ServiceBinder) binder).getService();
    }

    public void onServiceDisconnected(ComponentName name)
    {
        mServ = null;
    }

    // local methods used in connection/disconnection activity with service.

    public void doBindService()
    {
        // activity connects to the service.
        Intent intent = new Intent(this, BackgroundSoundService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    public void doUnbindService()
    {
        // disconnects the service activity.
        if(mIsBound)
        {
            unbindService(this);
            mIsBound = false;
        }
    }
    // when closing the current activity, the service will automatically shut down(disconnected).
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        doUnbindService();
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
}
