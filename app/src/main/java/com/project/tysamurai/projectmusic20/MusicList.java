package com.project.tysamurai.projectmusic20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static com.project.tysamurai.projectmusic20.R.id.element;

/**
 * Created by tanay on 28/4/17.
 */

public class MusicList extends Fragment {

    RecyclerView allSongsRV;
    PagerMainActivity context;
    ArrayList<Songs> allSongList;
    SongRVAdaptor adaptor;


//------------------------------------------------------------------------- oncreate

    public MusicList() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.music_list,container,false);

        allSongsRV=(RecyclerView) rootView.findViewById(R.id.musicRecyclerView);
        allSongsRV.setLayoutManager(new LinearLayoutManager(context));
        context=(PagerMainActivity) getActivity();


        createArray();
        adaptor=new SongRVAdaptor();
        allSongsRV.setLayoutManager(new LinearLayoutManager(context));
        allSongsRV.setAdapter(adaptor);
        //Toast.makeText(context, "list created", Toast.LENGTH_SHORT).show();


        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx



//---------------------------------------------------------------------Recycler stuff
    class SongRVAdaptor extends RecyclerView.Adapter<SongHolder>{
        //Integer ind=-1;
        @Override
        public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater li=context.getLayoutInflater();
            View itemView=li.inflate(R.layout.songelement,null,false);

            return new SongHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SongHolder holder, final int position) {

//            MediaMetadataRetriever mmr=new MediaMetadataRetriever();
//            Log.d("TAG", mmr.extractMetadata(allSongList.get(position).getId()));
            holder.title.setText(allSongList.get(position).getTitle());
            holder.artist.setText(allSongList.get(position).getArtist());

            holder.element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, allSongList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    if(context.musicService!=null) {
                        context.musicService.setSongPosition(position);
                        context.musicService.playSong();

                        if(context.musicService.isPng()){
                            context.musicService.seek(0);
                        }
                        context.btn_hide.setImageDrawable(null);
                        context.btn_hide.setImageResource(R.mipmap.pause);
                        context.play.setImageDrawable(null);
                        context.play.setImageResource(R.mipmap.pause);
                        context.songname.setText(allSongList.get(position).getTitle());
                        context.artist.setText(allSongList.get(position).getArtist());
                        context.seekBar2.setProgress(0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return allSongList.size();
        }
    }
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

//--------------------------------------------------------------------custom functions
    public void createArray(){
        allSongList=context.getMusicArray();
    }

//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
}
