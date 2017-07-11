package com.project.tysamurai.projectmusic20;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.util.ArrayList;

/**
 * Created by tanay on 30/5/17.
 */

public class MusicService extends Service implements MediaPlayer.OnErrorListener
        ,MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener {

    MediaPlayer player;
    Integer songPosition;
    ArrayList<Songs> songList=new ArrayList<>();
    private final IBinder mBinder=new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition=0;
        initMusicPlayer();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        playNext();
    }


//---------------------------------------------------------------------------------Service control methods

    void playSong(){
        player.reset();
        Songs currentSong=songList.get(songPosition);

        Uri songUri= ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSong.getId());

        try {
            player.setDataSource(getApplicationContext(), songUri);
        }
        catch (Exception e){
            Log.d("TAG","error in setting data source");
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getSongIndex(){
        return songPosition;
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        songPosition--;
        if(songPosition<0) songPosition=songList.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        songPosition++;
        if(songPosition>=songList.size()) songPosition=0;
        playSong();
    }

//---------------------------------------------------------------------------------custom stuff


    public void setSongList(ArrayList<Songs> songList) {
        this.songList = songList;
    }

    public void setSongPosition(Integer songPosition) {
        this.songPosition = songPosition;
    }

    private void initMusicPlayer(){
        player=new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
    }

    public class MusicBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }
}
