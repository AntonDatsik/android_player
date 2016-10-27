package com.example.anton.player.models;

public class Song {
    public Song(int id, String title, String artist, String url) {
        this(title, artist, url);
        Id = id;
    }

    public Song(String title, String artist, String url) {
        Title = title;
        Artist = artist;
        Url = url;
    }

    public int Id;
    public String Title;
    public String Artist;
    public String Url;
}
