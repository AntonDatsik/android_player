package com.example.vlad.player.models;

public class Playlist {
    public Playlist(String name) {
        this.Name = name;
    }

    public Playlist(int id, String name) {
        this(name);
        Id = id;
    }

    public int Id;
    public String Name;

    @Override
    public String toString() {
        return Name;
    }
}
