package com.example.anton.player.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class PlayMusicService extends IntentService {

    private static final String FILE_PATH_PARAM = "filePath";

    private static final String PLAY_MUSIC_ACTION = "playMusic";
    private static final String STOP_MUSIC_ACTION = "stopMusic";

    private static MediaPlayer mediaPlayer;
    private static String currentFilePath = "";

    public PlayMusicService() {
        super("PlayMusicService");
    }

    public static void startPlayingMusic(Context context, String filePath) {
        Intent intent = new Intent(context, PlayMusicService.class);
        intent.setAction(PLAY_MUSIC_ACTION);
        intent.putExtra(FILE_PATH_PARAM, filePath);
        context.startService(intent);
    }

    public static void pausePlayingMusic(Context context) {
        Intent intent = new Intent(context, PlayMusicService.class);
        intent.setAction(STOP_MUSIC_ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (PLAY_MUSIC_ACTION.equals(action)) {
                final String filePath = intent.getStringExtra(FILE_PATH_PARAM);
                handleActionPlay(filePath);
            } else if (STOP_MUSIC_ACTION.equals(action)) {
                handleActionStop();
            }
        }
    }

    private void handleActionPlay(String filePath) {
        if (!currentFilePath.equals(filePath) || mediaPlayer == null) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(filePath));
            currentFilePath = filePath;
            mediaPlayer.start();
            return;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            return;
        }

        if (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() > 1) {
            int length = mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
            return;
        }
    }

    private void handleActionStop() {
        mediaPlayer.pause();
    }
}
