package com.project.tysamurai.projectmusic20.Activities;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.tysamurai.projectmusic20.Fragments.MusicList;
import com.project.tysamurai.projectmusic20.Fragments.RecommendedMusic;
import com.project.tysamurai.projectmusic20.Services.MusicService;
import com.project.tysamurai.projectmusic20.R;
import com.project.tysamurai.projectmusic20.POJO.Songs;
import com.project.tysamurai.projectmusic20.Utls;
import com.project.tysamurai.projectmusic20.Helpers.VPAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;

public class PagerMainActivity extends AppCompatActivity {

    public Intent playIntent;
    public MusicService musicService;
    public ViewPager vPager;
    public TabLayout tabLayout;
    public MusicList musicFrag;
    public LinearLayout musicArea;
    public TextView songname,artist,totalDuration,currentPos;
    public SlidingUpPanelLayout slidingPaneLayout;
    public ImageView play,playNext,playPrev,btn_hide;
    public RecommendedMusic musicRecom;
    public SeekBar seekBar2;
    ArrayList<Songs> allSongList;
    boolean musicBound=false;
    SeekTask task;
    Utls utls;
    boolean seekBarTouched;

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setSongList(allSongList);
            musicBound = true;
            Log.d("TAG","service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent=new Intent(this,MusicService.class);
            bindService(playIntent,musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            Toast.makeText(PagerMainActivity.this, "onStart", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingPaneLayout=(SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        play=(ImageView) findViewById(R.id.play_pause);
        playNext=(ImageView) findViewById(R.id.play_next);
        playPrev=(ImageView) findViewById(R.id.play_prev);
        btn_hide=(ImageView) findViewById(R.id.btn_hide);
        vPager=(ViewPager) findViewById(R.id.viewpager);
        tabLayout=(TabLayout) findViewById(R.id.tablayout);
        musicArea=(LinearLayout) findViewById(R.id.musicarea);
        seekBar2=(SeekBar) findViewById(R.id.seekBar2);
        songname=(TextView) findViewById(R.id.name);
        artist=(TextView) findViewById(R.id.artist);
        totalDuration=(TextView) findViewById(R.id.totalDur);
        currentPos=(TextView) findViewById(R.id.currentPos);

        utls=new Utls();
        task=new SeekTask();

        allSongList=new ArrayList<>();
        getSongList();
        task.execute();

        vPager.setOffscreenPageLimit(2);
        setViewPager(vPager);
        tabLayout.setupWithViewPager(vPager);

        //----------------------------------------------------------------------------------LISTNERS

        btn_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService!=null && musicService.isPng()){
                    musicService.pausePlayer();
                    btn_hide.setImageDrawable(null);
                    btn_hide.setImageResource(R.mipmap.play);
                    play.setImageDrawable(null);
                    play.setImageResource(R.mipmap.play);
                }
                else if(musicService!=null && !musicService.isPng()){
                    musicService.go();
                    btn_hide.setImageDrawable(null);
                    btn_hide.setImageResource(R.mipmap.pause);
                    play.setImageDrawable(null);
                    play.setImageResource(R.mipmap.pause);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService!=null && musicService.isPng()){
                    musicService.pausePlayer();
                    btn_hide.setImageDrawable(null);
                    btn_hide.setImageResource(R.mipmap.play);
                    play.setImageDrawable(null);
                    play.setImageResource(R.mipmap.play);


                }
                else if(musicService!=null && !musicService.isPng()){
                    musicService.go();
                    btn_hide.setImageDrawable(null);
                    btn_hide.setImageResource(R.mipmap.pause);
                    play.setImageDrawable(null);
                    play.setImageResource(R.mipmap.pause);
                }
            }
        });

        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar2.setProgress(0);
                musicService.playNext();
            }
        });

        playPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar2.setProgress(0);
                musicService.playPrev();
            }
        });

        slidingPaneLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.d("TAG","onPanelSlide");
                btn_hide.setVisibility(View.GONE);
            }

            @Override
            public void onPanelCollapsed(View panel) {
               // Log.d("TAG","onPanelCollapsed");
                btn_hide.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelExpanded(View panel) {
                //Log.d("TAG","onPanelExpanded");
                btn_hide.setVisibility(View.GONE);
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.d("TAG","onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.d("TAG","onPanelHidden");
            }
        });

        seekBar2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekBarTouched=true;
                return false;
            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Log.d("TAG"," "+ String.valueOf(progress));
                //seekBarTouched=true;
                if(seekBarTouched) {
                    Float x = ((float) progress) / 400;
                    Integer y = (int) (x * musicService.getDur());
                    musicService.seek(y);

                    seekBarTouched=false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(musicService, "grabbed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(musicService, "left", Toast.LENGTH_SHORT).show();
            }
        });


    }// end of onCreate

    void setViewPager(ViewPager viewPager){

        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        musicFrag=new MusicList();
        musicRecom=new RecommendedMusic();

        adapter.addFrag(musicFrag,"All Songs");
        adapter.addFrag(musicRecom,"Recommendation");
        viewPager.setAdapter(adapter);
    }

    public void getSongList(){
        ContentResolver musicResolver=getContentResolver();
        Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor=musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                allSongList.add(new Songs(thisId,thisTitle,thisArtist));
            }
            while (musicCursor.moveToNext());
            Collections.sort(allSongList);
        }
    }

    public ArrayList getMusicArray(){
        return allSongList;
    }


    public class SeekTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //Toast.makeText(musicService, "yo", Toast.LENGTH_SHORT).show();
            if(musicService.isPng()) {
                Double x = ((double)musicService.getPosn()) / musicService.getDur();
                seekBar2.setProgress((int)(x*400));
                //seekBar2.setProgress(musicService.getPosn());
                songname.setText(allSongList.get(musicService.getSongIndex()).getTitle());
                if(totalDuration!=null)
                totalDuration.setText(utls.timeFormatter(musicService.getDur()));
                if(currentPos!=null)
                currentPos.setText(utls.timeFormatter(musicService.getPosn()));
                artist.setText(allSongList.get(musicService.getSongIndex()).getArtist());
                seekBarTouched=false;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(true) {
                //loopOneSec();
                try {
                    Thread.sleep(200);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                publishProgress();
            }
        }

    }

}