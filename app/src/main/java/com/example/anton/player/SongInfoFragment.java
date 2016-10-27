package com.example.anton.player;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anton.player.db.SqliteContext;
import com.example.anton.player.models.Song;
import com.example.anton.player.services.AlbumArtService;
import com.example.anton.player.services.PlayMusicService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class SongInfoFragment extends DialogFragment implements View.OnClickListener {
    private ImageView imageAlbumArt;
    private Button btnPrevSong;
    private Button btnNextSong;
    private Button btnControl;
    private TextView textTitle;
    private TextView textArtist;

    private ButtonControlStates buttonControlStates;

    private ArrayList<Integer> songIds;
    private SqliteContext sqliteContext;

    private int currentPlayingId = -1;

    private Song currentSong;
    private int songIndex;
    private ArrayList<Song> songs;

    private static final String TAG = "myLogs";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_info, container, false);

        imageAlbumArt = (ImageView)view.findViewById(R.id.album_full_image);
        btnPrevSong = (Button)view.findViewById(R.id.btn_prev_song);
        btnNextSong = (Button)view.findViewById(R.id.btn_next_song);
        btnControl = (Button)view.findViewById(R.id.btn_control);
        textTitle = (TextView)view.findViewById(R.id.song_info_title);
        textArtist = (TextView)view.findViewById(R.id.song_info_artist);

        btnNextSong.setOnClickListener(this);
        btnPrevSong.setOnClickListener(this);
        btnControl.setOnClickListener(this);

        this.songIds = getArguments().getIntegerArrayList("songIds");
        this.songIndex = getArguments().getInt("songIndex");

        sqliteContext = new SqliteContext(getActivity().getApplicationContext());
        this.songs = sqliteContext.getSongsByIds(this.songIds);
        this.currentSong = this.songs.get(this.songIndex);
        renderView();
        Log.d(TAG, "onCreate");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Click on fragment");
        int id = v.getId();
        switch (id) {
            case R.id.btn_prev_song:
                this.songIndex -= 1;
                if (this.songIndex < 0) {
                    this.songIndex += this.songIds.size();
                }
                this.currentSong = this.songs.get(this.songIndex);
                renderView();
                break;
            case R.id.btn_next_song:
                this.songIndex += 1;
                if (this.songIndex >= this.songIds.size()) {
                    this.songIndex -= this.songIds.size();
                }
                this.currentSong = this.songs.get(this.songIndex);
                renderView();
                break;
            case R.id.btn_control:
                if (currentPlayingId == -1) {
                    currentPlayingId = this.currentSong.Id;
                }
                String currText = (String)btnControl.getText();
                if (currText.equals(buttonControlStates.PLAY.getText())) {
                    PlayMusicService.startPlayingMusic(getActivity(), this.currentSong.Url);
                    btnControl.setText(buttonControlStates.STOP.getText());
                    currentPlayingId = this.currentSong.Id;
                } else {
                    PlayMusicService.pausePlayingMusic(getActivity());
                    btnControl.setText(buttonControlStates.PLAY.getText());
                }
                break;
        }
    }

    private void renderView() {
        Context context = getActivity().getApplicationContext();

        String searchQuery = this.currentSong.Artist + " " + this.currentSong.Title;
        String url = AlbumArtService.getInstance().getAlbumArtUrl(searchQuery);
        Picasso.with(context)
                .load(url)
                .error(R.drawable.album_art_default)
                .into(imageAlbumArt);
        textTitle.setText(String.format(" Title: %s", this.currentSong.Title));
        textArtist.setText(String.format(" Artist: %s", this.currentSong.Artist));
        String currText = currentPlayingId == this.currentSong.Id ? "Pause" : "Play";
        btnControl.setText(currText);
    }
}
