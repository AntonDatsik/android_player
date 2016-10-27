package com.example.anton.player;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.anton.player.db.SqliteContext;
import com.example.anton.player.models.Song;
import com.example.anton.player.utils.OpenFileDialog;
import com.example.anton.player.utils.SongListAdapter;

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
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private final int CM_DELETE_ID = 1;

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

        registerForContextMenu(this.getListView());

        dlgSongInfo = new SongInfoFragment();

        btn = (Button)findViewById(R.id.buttonAddSong);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileDialog openFileDialog = new OpenFileDialog(self);
                openFileDialog.setOpenDialogListener(self);
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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить запись");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            this.sqlLiteContext.deleteSongById(this.songs.get(acmi.position).Id);

            this.songs.remove(acmi.position);

            resetListAdapter();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void OnSelectedFile(String fileName) {
        Uri uri = Uri.parse(fileName);
        List<String> segments = uri.getPathSegments();

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(fileName);

        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        if (title == null) {
            title = segments.get(segments.size() - 1);
        }
        title = title.replace('\'', ' ');

        if (artist != null) {
            artist = artist.replace('\'', ' ');
        }

        Song song = new Song(title, artist, fileName);
        sqlLiteContext.addSong(song, this.playListId);
        this.songs = this.sqlLiteContext.getSongsInPlaylist(this.playListId);
        resetListAdapter();
    }

    private void resetListAdapter() {
        setListAdapter(new SongListAdapter(this, songs));
        resetSongsIds();
    }

    private void resetSongsIds() {
        this.songsIds.clear();
        for(Song s : this.songs) {
            this.songsIds.add(s.Id);
        }
    }
}
