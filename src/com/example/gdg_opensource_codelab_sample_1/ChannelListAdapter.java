package com.example.gdg_opensource_codelab_sample_1;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChannelListAdapter extends ArrayAdapter<Playlist> {

    public ChannelListAdapter(Context context, List<Playlist> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.playlist_item, null);
        }

        Playlist res = getItem(position);

        TextView tv1 = (TextView) convertView.findViewById(R.id.video_title);

        tv1.setText(res.getSnippet().getTitle());

        return convertView;
    }


}//end of class
