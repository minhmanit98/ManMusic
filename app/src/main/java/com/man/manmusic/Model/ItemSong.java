package com.man.manmusic.Model;

import android.graphics.Bitmap;

public class ItemSong {
    private String dataPath, title, displayName, album, artist;
    private int duration;
    private Bitmap songImage;
    public ItemSong(String dataPath, String title, String displayName, String album, String artist, int duration,Bitmap songImage) {
        this.dataPath = dataPath;
        this.title = title;
        this.displayName = displayName;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.songImage=songImage;
    }

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

    public Bitmap getSongImage(){return songImage;}
}
