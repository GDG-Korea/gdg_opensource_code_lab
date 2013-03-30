
package com.example.gdg_opensource_codelab_sample_1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;

import net.simonvt.menudrawer.MenuDrawer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SherlockFragmentActivity
        implements YouTubeChannelClient.Callbacks {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    public static final String YOUTUBE_API_KEY = "AIzaSyCwvRaeu1TqEG0ONmCcssVNeSTeHJx-eqY";
    public static final String ANDROID_DEVELOPER_CHANNEL_ID = "UCVHFbqXqoYvEWM1Ddxl0QDg";

    private MenuDrawer mDrawer;
    private YouTubeChannelClient mYouTubeClient;
    private ListView mListView;
    private ListView mListMenu;
    private ChannelListAdapter mChannelListAdapter;
    private VideoResourceAdapter mPlayListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.hello_opensoruce_code_lab);

        mDrawer = MenuDrawer.attach(this);
        mDrawer.setContentView(R.layout.activity_main);
        mDrawer.setMenuView(R.layout.activity_menu);
        mDrawer.setMenuSize(getResources().getDimensionPixelSize(R.dimen.menu_width));

        mYouTubeClient = YouTubeChannelClient
                .newYouTubeChannelClient(YOUTUBE_API_KEY, ANDROID_DEVELOPER_CHANNEL_ID);

        mListView = (ListView) findViewById(android.R.id.list);
        mPlayListAdapter = new VideoResourceAdapter(this,
                new ArrayList<PlaylistItem>());

        mListView.setAdapter(mPlayListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                PlaylistItem item = (PlaylistItem) mListView.getItemAtPosition(position);
                String videoId = item.getSnippet().getResourceId().getVideoId();

                Intent intent = new Intent(MainActivity.this, YouTubePlayerActivity.class);
                intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        mChannelListAdapter = new ChannelListAdapter(this, new ArrayList<Playlist>());

        mListMenu = (ListView) findViewById(R.id.list_menu);

        mListMenu.setAdapter(mChannelListAdapter);
        mListMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Playlist item = mChannelListAdapter.getItem(position);
                mYouTubeClient.getPlaylistItem(item.getId(), MainActivity.this);
                mDrawer.closeMenu();
            }
        });
        
        
        if (isNetworkAvailable()) {
        	mYouTubeClient.getPlayList(this);
        } else {
        	Toast.makeText(MainActivity.this, "네트웍상태 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
        
    }

    @Override
    public void onLoadPlaylist(List<Playlist> playlist) {
        mChannelListAdapter.clear();
        for (Playlist item : playlist) {
            mChannelListAdapter.add(item);
        }
        mChannelListAdapter.notifyDataSetChanged();

        if (playlist.size() > 0) {
            mYouTubeClient.getPlaylistItem(playlist.get(0).getId(), this);
        }
    }

    @Override
    public void onLoadPlaylistItem(String playlistId, List<PlaylistItem> playlistItems) {
        mPlayListAdapter.clear();
        for (PlaylistItem item : playlistItems) {
            mPlayListAdapter.add(item);
        }
        mPlayListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.abs__home:
                // fall through
            case android.R.id.home:
                mDrawer.toggleMenu(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * 네트웍 상태 체크
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}// end of class
