package com.project.tysamurai.projectmusic20;

import android.support.annotation.NonNull;

/**
 * Created by tanay on 8/4/17.
 */

public class Songs implements Comparable{
    private long id;
    private String title;
    private String artist;
    //private String genre;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Songs(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        Songs temp=(Songs) o;
        if (temp.getTitle().compareTo(this.getTitle())<0)
            return  1;
        else
            return -1;
    }
}
