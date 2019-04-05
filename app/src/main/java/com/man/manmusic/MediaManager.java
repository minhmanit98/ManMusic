package com.man.manmusic;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import com.man.manmusic.Model.ItemSong;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class MediaManager implements MediaPlayer.OnCompletionListener, Serializable {
    private Context mContext;
    private ArrayList<ItemSong> arrSongs = new ArrayList<>();

    /**
     * MediaPlayer to play audio
     */
    private MediaPlayer mPlayer = new MediaPlayer();
    private int currentIndex = 0;
    private static boolean shuffle;
    private Random random = new Random();

    public static final int STATE_IDE = 0;
    public static final int STATE_PLAY = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_STOP = 3;


    private int statePlayer = STATE_IDE;

    public MediaManager(Context mContext) {
        this.mContext = mContext;
        shuffle = false;
        mPlayer.setOnCompletionListener(this);
    }

    public void getAllAudioSongs() {
        String cols[] = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST};

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                cols, null, null, null, null);
        if (cursor == null)
            return;
        int indexId = cursor.getColumnIndex(cols[0]);
        int indexData = cursor.getColumnIndex(cols[1]);
        int indexTitle = cursor.getColumnIndex(cols[2]);
        int indexDisplayName = cursor.getColumnIndex(cols[3]);
        int indexDuration = cursor.getColumnIndex(cols[4]);
        int indexAlbum = cursor.getColumnIndex(cols[5]);
        int indexArtist = cursor.getColumnIndex(cols[6]);
        long id;
        String dataPath, title, displayName, album, artist;
        int duration;
        cursor.moveToFirst();

        arrSongs.clear();
        while (!cursor.isAfterLast()) {
            id=cursor.getLong(indexId);
            dataPath = cursor.getString(indexData);
            title = cursor.getString(indexTitle);
            displayName = cursor.getString(indexDisplayName);
            album = cursor.getString(indexAlbum);
            artist = cursor.getString(indexArtist);
            duration = cursor.getInt(indexDuration);
            arrSongs.add(new ItemSong(id,dataPath, title, displayName, album, artist, duration));
            cursor.moveToNext();
        }
        cursor.close();
    }

    /**
     * Get array songs
     */
    public ArrayList<ItemSong> getArrSongs() {
        return arrSongs;
    }

    public void play(int number) {
        if (statePlayer == STATE_IDE || statePlayer == STATE_STOP) {
            try {
                currentIndex = number;
                mPlayer.setDataSource(arrSongs.get(currentIndex).getDataPath());
                mPlayer.prepare();
                mPlayer.start();
                statePlayer = STATE_PLAY;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (statePlayer == STATE_PAUSE) {
            mPlayer.start();
            statePlayer = STATE_PLAY;
        }
    }

    public void play() {
        if (statePlayer == STATE_IDE || statePlayer == STATE_STOP) {
            try {

                mPlayer.setDataSource(arrSongs.get(currentIndex).getDataPath());
                mPlayer.prepare();
                mPlayer.start();
                statePlayer = STATE_PLAY;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (statePlayer == STATE_PAUSE) {
            mPlayer.start();
            statePlayer = STATE_PLAY;
        }
    }

    public void pause() {
        if (mPlayer.isPlaying() && statePlayer == STATE_PLAY) {
            mPlayer.pause();
            statePlayer = STATE_PAUSE;
        }
    }

    public void stop() {
        if (mPlayer.isPlaying())
            mPlayer.stop();
        mPlayer.reset();
        statePlayer = STATE_STOP;
    }

    public void next() {
        if (currentIndex == arrSongs.size() - 1)
            currentIndex = 0;
        else
            currentIndex++;
        mPlayer.seekTo(0);
        stop();

        play(currentIndex);

    }

    public void previous() {
        if (currentIndex == 0)
            currentIndex = arrSongs.size() - 1;
        else
            currentIndex--;
        mPlayer.seekTo(0);
        stop();

        play(currentIndex);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (shuffle == true) {
            stop();
            Log.i("Giang", shuffle + "");
            currentIndex = random.nextInt(arrSongs.size());
            Log.i("Giang", currentIndex + "");
            play(currentIndex);
        } else if (shuffle == false) {
            Log.i("Son", shuffle + "");
            next();
        }

    }

    public Bitmap getImageFromMp3File(String path) {

        // load data file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);

        // Getting picture
        byte[] artByteArray = metaRetriever.getEmbeddedPicture();
        if (artByteArray != null) {
            Bitmap artBitmap = BitmapFactory.decodeByteArray(artByteArray, 0,
                    artByteArray.length);

            // release memory
            metaRetriever.release();

            return artBitmap;
        }
        return null;

    }
    public Bitmap getImage() {
        return getImageFromMp3File(arrSongs.get(currentIndex).getDataPath());
    }
    public String getCurrentSongName() {
        return arrSongs.get(currentIndex).getArtist();
    }

    public String getCurrentArtist() {
        return arrSongs.get(currentIndex).getTitle();
    }

    public String getCurrentTotalDuration() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(arrSongs.get(currentIndex).getDuration()));
    }

    public int getCurrentDuration() {
        if (mPlayer.isPlaying())
            return mPlayer.getCurrentPosition();
        return -1;
    }

    public int getMaxDuration() {
        return arrSongs.get(currentIndex).getDuration();
    }

    public void seekTo(int progress) {
        if (mPlayer.isPlaying()) {
            mPlayer.seekTo(progress);
        }
    }

    public int getCurrentIndex() {
        return currentIndex + 1;
    }

    public void repeatSong(boolean repeat) {
        mPlayer.setLooping(repeat);
    }

    public void shuffle(boolean newShuffle) {
        shuffle = newShuffle;
    }

}
