package com.example.anton.player.services;


public class AlbumArtService {
    private AlbumArtService() {}

    private static AlbumArtService instance;

    public static AlbumArtService getInstance() {
        if (instance == null) {
            instance = new AlbumArtService();
        }

        return instance;
    }

    public String getAlbumArtUrl(String Query) {
        return "http://mcreplication.com/assets/artwork_templates/22/cache/disc-face.png_400_400_75_landscape.png";
    }
}
