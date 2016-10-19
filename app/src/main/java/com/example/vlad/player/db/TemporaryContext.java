package com.example.vlad.player.db;

import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;

import java.util.List;

public class TemporaryContext implements IDbContext {
    @Override
    public List<Song> getSongs() {
        return null;
    }

    @Override
    public List<Playlist> getPlaylists() {
        return null;
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        return null;
    }

    @Override
    public void addSong(Song song, int playlistId) {

    }

    @Override
    public void addPlaylist(Playlist playlist) {

    }

    @Override
    public void deleteSongById() {

    }
}
