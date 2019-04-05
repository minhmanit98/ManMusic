package com.man.manmusic.Model;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.Serializable;

public class ItemSong implements Serializable {
    private String dataPath, title, displayName, album, artist;
    private int duration;
    long id;

    public ItemSong(long id, String dataPath, String title, String displayName, String album, String artist, int duration) {
        this.dataPath = dataPath;
        this.title = title;
        this.displayName = displayName;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.id=id;

    }

    public long getId(){return id;}
    public String getDataPath() {
        return dataPath;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public byte[] getAlbum_Art(Context context) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, getUri());
        return mediaMetadataRetriever.getEmbeddedPicture();
    }

    public Uri getUri() {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }



}
