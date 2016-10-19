package com.example.vlad.player;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.vlad.player.db.SqliteContext;
import com.example.vlad.player.models.Song;
import com.example.vlad.player.utils.OpenFileDialog;
import com.example.vlad.player.utils.SongListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends ListActivity implements OpenFileDialog.OpenDialogListener {
    private DialogFragment dlgSongInfo;
    private int playListId;
    private Button btn;
    private final PlaylistActivity self = this;
    private SqliteContext sqlLiteContext;
    private List<Song> songs;
    private ArrayList<Integer> songsIds = new ArrayList<>();
    private OpenFileDialog openFileDialog;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.playListId = extras.getInt("playlistId");
        }
        this.sqlLiteContext = new SqliteContext(getApplicationContext());
        this.songs = this.sqlLiteContext.getSongsInPlaylist(this.playListId);

        resetListAdapter();
        resetSongsIds();

        dlgSongInfo = new SongInfoFragment();

        this.openFileDialog = new OpenFileDialog(this);
        this.openFileDialog.setOpenDialogListener(this);

        btn = (Button)findViewById(R.id.buttonAddSong);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileDialog.show();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Bundle fragmentData = new Bundle();

        fragmentData.putIntegerArrayList("songIds", this.songsIds);
        fragmentData.putInt("songIndex", position);

        dlgSongInfo.setArguments(fragmentData);
        dlgSongInfo.show(getFragmentManager(), "dlgSongInfo");
    }

    @Override
    public void OnSelectedFile(String fileName) {
        Uri uri = Uri.parse(fileName);
        List<String> segments = uri.getPathSegments();
//        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(fileName));
//        MediaPlayer.TrackInfo[] trackInfos = mediaPlayer.get;
//        for (MediaPlayer.TrackInfo trackInfo : trackInfos) {
//            trackInfo.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO
//        }
        Song song = new Song(segments.get(segments.size() - 1), "Gena", fileName);
        sqlLiteContext.addSong(song, this.playListId);
        this.songs = this.sqlLiteContext.getSongsInPlaylist(this.playListId);
        resetListAdapter();
        resetSongsIds();
    }

    private void resetListAdapter() {
        setListAdapter(new SongListAdapter(this, songs));
    }

    private void resetSongsIds() {
        this.songsIds.clear();
        for(Song s : this.songs) {
            this.songsIds.add(s.Id);
        }
    }
}
