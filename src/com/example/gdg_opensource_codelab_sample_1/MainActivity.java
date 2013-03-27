
package com.example.gdg_opensource_codelab_sample_1;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import net.simonvt.menudrawer.MenuDrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends SherlockFragmentActivity
        implements YouTubeChannelClient.Callbacks {
    public static final String YOUTUBE_API_KEY = "AIzaSyCwvRaeu1TqEG0ONmCcssVNeSTeHJx-eqY";
    public static final String ANDROID_DEVELOPER_CHANNEL_ID = "UCVHFbqXqoYvEWM1Ddxl0QDg";
    private static final String TAG = "MainActivity";
    private MenuDrawer mDrawer;
    private YouTubeChannelClient mYouTubeClient;
    private ListView mListView;
    private ListView mListMenu;
    private ChannelListAdapter mChannelListAdapter;

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

        mChannelListAdapter = new ChannelListAdapter(this, new ArrayList<Playlist>());

        mListMenu = (ListView) findViewById(R.id.list_menu);

        mListMenu.setAdapter(mChannelListAdapter);
        mListMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Playlist item = mChannelListAdapter.getItem(position);
                mYouTubeClient.getPlaylistItem(item.getId());
                mDrawer.closeMenu();
            }
        });

        mYouTubeClient.getPlayList();
    }

    @Override
    public void onLoadPlaylist(List<Playlist> playlist) {

        mChannelListAdapter.clear();
        for(Playlist item : playlist) {
            mChannelListAdapter.add(item);
        }
        mChannelListAdapter.notifyDataSetChanged();

        if (playlist.size() > 0) {
            mYouTubeClient.getPlaylistItem(playlist.get(0).getId());
        }
    }

    @Override
    public void onLoadPlaylistItem(List<PlaylistItem> playlistItem) {
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
