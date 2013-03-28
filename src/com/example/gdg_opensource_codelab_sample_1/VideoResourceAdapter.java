
package com.example.gdg_opensource_codelab_sample_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;

import java.util.List;

public class VideoResourceAdapter extends ArrayAdapter<PlaylistItem> {

    private ImageTagFactory mTagFactory = ImageTagFactory.newInstance();

    public VideoResourceAdapter(Context context, List<PlaylistItem> objects) {
        super(context, 0, objects);
        mTagFactory.setHeight(100);
        mTagFactory.setWidth(100);
        mTagFactory.setDefaultImageResId(R.drawable.ic_launcher);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_resource_item,
                    null);
        }

        PlaylistItem res = getItem(position);

        ImageView iv = (ImageView) convertView.findViewById(R.id.video_thumbnail);
        TextView tv1 = (TextView) convertView.findViewById(R.id.video_title);

        tv1.setText(res.getSnippet().getTitle());

        String url = getThumbnailUrl(res.getSnippet());
        ImageTag tag = mTagFactory.build(url, getContext());
        iv.setTag(tag);
        SampleApplication.getImageManager().getLoader().load(iv);
        return convertView;
    }

    private String getThumbnailUrl(PlaylistItemSnippet snippet) {
        ThumbnailDetails thumbs = snippet.getThumbnails();
        Thumbnail thumb = thumbs.getDefault();
        if (thumb == null)
            return null;

        return thumb.getUrl();
    }

}// end of class
