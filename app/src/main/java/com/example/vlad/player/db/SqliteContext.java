package com.example.vlad.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;

import java.util.ArrayList;
import java.util.List;


public class SqliteContext extends SQLiteOpenHelper implements IDbContext  {
    private static final String DATABASE_NAME = "playlist4.db";
    private static final int SCHEMA = 1;
    private static final String PLAYLISTS_TABLE = "playlists";
    private static final String SONGS_TABLE = "songs";
    private static final String PLAYLISTS_SONGS_TABLE = "playlists_songs";

    private static final String PLAYLIST_ID = "id";
    private static final String PLAYLIST_NAME = "name";
    private static final String SONG_ID = "id";
    private static final String SONG_TITLE = "title";
    private static final String SONG_ARTIST = "artist";
    private static final String SONG_URL = "url";

    private static final String PS_SONG_ID = "song_id";
    private static final String PS_PLAYLIST_ID = "playlist_id";

    private SQLiteDatabase db;


    public SqliteContext(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("CREATE TABLE " + PLAYLISTS_TABLE + " (" +
                PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAYLIST_NAME + " NVARCHAR(50) " +
                ");");

        db.execSQL("CREATE TABLE " + SONGS_TABLE + " (" +
                SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SONG_ARTIST + " NVARCHAR(50), " +
                SONG_TITLE + " NVARCHAR(50), " +
                SONG_URL + " NVARCHAR(100), " +
                PS_PLAYLIST_ID + " INTEGER, " +
                "FOREIGN KEY (" + PS_PLAYLIST_ID + ") REFERENCES " + PLAYLISTS_TABLE + "(" + PLAYLIST_ID + ") ON DELETE CASCADE " +
                ");");

        // Seed
        db.execSQL("INSERT INTO "+ PLAYLISTS_TABLE +
                "(" + PLAYLIST_NAME + ")" +
                " VALUES ('For night');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ PLAYLISTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ SONGS_TABLE);
        onCreate(db);
    }

    @Override
    public List<Song> getSongs() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + SONGS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                    cursor.getInt(cursor.getColumnIndex(SONG_ID)),
                    cursor.getString(cursor.getColumnIndex(SONG_TITLE)),
                    cursor.getString(cursor.getColumnIndex(SONG_ARTIST)),
                    cursor.getString(cursor.getColumnIndex(SONG_URL))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public List<Playlist> getPlaylists() {
        String selectQuery = "SELECT  * FROM " + PLAYLISTS_TABLE;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Playlist> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Playlist(
                        cursor.getInt(cursor.getColumnIndex(PLAYLIST_ID)),
                        cursor.getString(cursor.getColumnIndex(PLAYLIST_NAME))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + SONGS_TABLE + " WHERE " + PS_PLAYLIST_ID + " = " + playlistId + " ;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                        cursor.getInt(cursor.getColumnIndex(SONG_ID)),
                        cursor.getString(cursor.getColumnIndex(SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(SONG_ARTIST)),
                        cursor.getString(cursor.getColumnIndex(SONG_URL))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public void addSong(Song song, int playlistId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO "+ SONGS_TABLE +
                "(" + SONG_TITLE + ", " + SONG_ARTIST + ", " + SONG_URL + ", " + PS_PLAYLIST_ID + ")" +
                " VALUES ('" + song.Title + "', '" + song.Artist + "', '" + song.Url + "', '" + playlistId + "');");
    }


    @Override
    public void addPlaylist(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + PLAYLISTS_TABLE + "( " + PLAYLIST_NAME + " )" + " VALUES ('" + playlist.Name + "');");
    }

    @Override
    public void deleteSongById() {

    }

    public Song getSongById(int  id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + SONGS_TABLE + " WHERE id = " + id + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                        cursor.getInt(cursor.getColumnIndex(SONG_ID)),
                        cursor.getString(cursor.getColumnIndex(SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(SONG_ARTIST)),
                        cursor.getString(cursor.getColumnIndex(SONG_URL))
                ));
            } while (cursor.moveToNext());
        }

        return data.get(0);
    }

    public ArrayList<Song> getSongsByIds(ArrayList<Integer>  arrayListIds) {
        SQLiteDatabase  db = this.getReadableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer id : arrayListIds) {
            stringBuilder.append(id + " ,");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        String selectQuery = "SELECT * FROM " + SONGS_TABLE + " WHERE id IN ( " + stringBuilder.toString() + " );";
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                        cursor.getInt(cursor.getColumnIndex(SONG_ID)),
                        cursor.getString(cursor.getColumnIndex(SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(SONG_ARTIST)),
                        cursor.getString(cursor.getColumnIndex(SONG_URL))
                ));
            } while (cursor.moveToNext());
        }

        return data;
    }
}
