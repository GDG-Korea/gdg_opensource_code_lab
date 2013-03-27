
package com.example.gdg_opensource_codelab_sample_1;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import net.simonvt.menudrawer.MenuDrawer;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends SherlockFragmentActivity
        implements YouTubeChannelClient.Callbacks {
    public static final String YOUTUBE_API_KEY = "AIzaSyCwvRaeu1TqEG0ONmCcssVNeSTeHJx-eqY";
    public static final String ANDROID_DEVELOPER_CHANNEL_ID = "UCVHFbqXqoYvEWM1Ddxl0QDg";
    private static final String TAG = "MainActivity";
    private MenuDrawer mDrawer;
    private YouTubeChannelClient mYouTubeClient;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.hello_opensoruce_code_lab);

        mDrawer = MenuDrawer.attach(this);
        mDrawer.setContentView(R.layout.activity_main);
        mDrawer.setMenuView(R.layout.activity_menu);

        mYouTubeClient = YouTubeChannelClient
                .getYouTubeChannelClient(YOUTUBE_API_KEY, ANDROID_DEVELOPER_CHANNEL_ID, this);

        mListView = (ListView) findViewById(android.R.id.list);

        mYouTubeClient.getPlayList();
    }

    @Override
    public void onLoadPlaylist(List<Playlist> playlist) {
        for (Playlist item : playlist) {
            Log.d(TAG, "channedId = " + item.getId());
        }

        if (playlist.size() > 0) {
            mYouTubeClient.getPlaylistItem(playlist.get(playlist.size() - 1).getId());
        }
    }

    @Override
    public void onLoadPlaylistItem(List<PlaylistItem> playlistItem) {
        for (PlaylistItem item : playlistItem) {
            Log.d(TAG, "title = " + item.getSnippet().getTitle());
            Log.d(TAG, "desc = " + item.getSnippet().getDescription());
            Log.d(TAG, "thumbnails = " + item.getSnippet().getThumbnails().getDefault().getUrl());

            Log.d(TAG, "videoId = " + item.getContentDetails().getVideoId());
        }

        mListView.setAdapter(new VideoResourceAdapter(this, playlistItem));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.abs__home:
                //fall through
            case android.R.id.home:
                mDrawer.toggleMenu(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}//end of class
