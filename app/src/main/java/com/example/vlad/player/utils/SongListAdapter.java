package com.example.vlad.player.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.player.R;
import com.example.vlad.player.models.Song;
import com.example.vlad.player.services.AlbumArtService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongListAdapter extends ArrayAdapter<Song> {
    private LayoutInflater inflater;
    private Context context;

    private class ViewHolder {
        ImageView imageAlbumImage;
        TextView textSongTitle;
        TextView textSongArtist;
    }

    public SongListAdapter(Context context, List<Song> objects) {
        super(context, R.layout.activity_playlist, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;

        if(row == null) {

            row = inflater.inflate(R.layout.partial_song, parent, false);
            holder = new ViewHolder();
            holder.imageAlbumImage = (ImageView)row.findViewById(R.id.album_image);
            holder.textSongTitle = (TextView)row.findViewById(R.id.song_title);
            holder.textSongArtist = (TextView)row.findViewById(R.id.song_artist);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        Song song = this.getItem(position);

        if (song != null) {
            String searchQuery = song.Artist + " " + song.Title;
            String url = AlbumArtService.getInstance().getAlbumArtUrl(searchQuery);

            Picasso.with(context)
                .load(url)
                .error(R.drawable.album_art_default)
                .into(holder.imageAlbumImage);
            holder.textSongTitle.setText(song.Title);
            holder.textSongArtist.setText(song.Artist);
        }

        return row;
    }
}
