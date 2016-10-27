package com.example.anton.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.anton.player.db.SqliteContext;
import com.example.anton.player.models.Playlist;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainActivity activity;
    List<Playlist> playlists;
    ListView lvPlaylists;
    SqliteContext sqliteContext;
    private CreatePlayListFragment dlgAddPlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.sqliteContext = new SqliteContext(getApplicationContext());

        playlists = sqliteContext.getPlaylists();
        lvPlaylists = (ListView)findViewById(R.id.playlists);

        ArrayAdapter<Playlist> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, playlists);

        lvPlaylists.setAdapter(adapter);
        lvPlaylists.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(activity, PlaylistActivity.class);
                intent.putExtra("playlistId", playlists.get(position).Id);
                startActivity(intent);
            }
        });

        dlgAddPlaylist = new CreatePlayListFragment();
        dlgAddPlaylist.setListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_add_playlist:
                dlgAddPlaylist.show(getFragmentManager(), "dlgAddPlaylist");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updatePlayLists() {
        playlists = sqliteContext.getPlaylists();
        ArrayAdapter<Playlist> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, playlists);
        lvPlaylists.setAdapter(adapter);
    }
}
