package com.example.vlad.player;

/**
 * Created by Anton on 18.10.2016.
 */

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.player.db.SqliteContext;
import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;
import com.example.vlad.player.services.AlbumArtService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CreatePlayListFragment extends DialogFragment implements View.OnClickListener {
    private Button btnAddPlayList;
    private EditText editPlaylistName;
    private TextView textTitle;
    private SqliteContext sqliteContext;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_playlist, container, false);
        btnAddPlayList = (Button)view.findViewById(R.id.button_add_playlist);
        editPlaylistName = (EditText)view.findViewById(R.id.editPlaylistName);

        btnAddPlayList.setOnClickListener(this);
        sqliteContext = new SqliteContext(getActivity().getApplicationContext());
        renderView();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_add_playlist:
                addPlayList();
                this.activity.updatePlayLists();
                this.dismiss();
                break;
        }
    }

    private void addPlayList() {
        String playlistName = this.editPlaylistName.getText().toString();
        this.sqliteContext.addPlaylist(new Playlist(playlistName));
    }

    private void renderView() {
        Context context = getActivity().getApplicationContext();
    }

    public void setListener(MainActivity activity) {
        this.activity = activity;
    }
}
