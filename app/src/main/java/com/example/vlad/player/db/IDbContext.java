package com.example.vlad.player.db;

import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;

import java.util.List;

public interface IDbContext {
    List<Song> getSongs();

    List<Playlist> getPlaylists();

    List<Song> getSongsInPlaylist(int playlistId);

    void addSong(Song song, int playlistId);

    void addPlaylist(Playlist playlist);

    void deleteSongById();
}
