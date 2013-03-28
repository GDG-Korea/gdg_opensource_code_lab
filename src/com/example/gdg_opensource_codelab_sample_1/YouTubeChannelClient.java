package com.example.gdg_opensource_codelab_sample_1;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.io.IOException;
import java.util.List;

public class YouTubeChannelClient {
    /**
     * Global instance of the HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private final String mApiKey;
    private final String mChannelID;
    private final YouTube mYouTube;
    Callbacks mChannelCallbacks;

    private YouTubeChannelClient(String apiKey, String channelID, Callbacks callbacks) {
        mApiKey = apiKey;
        mChannelID = channelID;
        mChannelCallbacks = callbacks;

        mYouTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                })
                .setApplicationName("youtube-channel-list-client")
                .build();
    }

    public static YouTubeChannelClient getYouTubeChannelClient(String apiKey, String channelID,
            Callbacks callbacks) {
        return new YouTubeChannelClient(apiKey, channelID, callbacks);
    }

    public void getPlayList() {
        new AsyncTask<Void, Void, List<Playlist>>() {

            @Override
            protected List<Playlist> doInBackground(Void... params) {
                PlaylistListResponse searchListResponse = null;
                try {
                    YouTube.Playlists.List request = mYouTube.playlists().list("snippet");
                    request.setKey(mApiKey);
                    request.setChannelId(mChannelID);
                    request.setMaxResults(50L);
                    searchListResponse = request.execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return searchListResponse == null ? null : searchListResponse.getItems();
            }

            @Override
            protected void onPostExecute(List<Playlist> playlists) {
                mChannelCallbacks.onLoadPlaylist(playlists);
            }

        }.execute();

    }

    public void getPlaylistItem(final String playListId) {
        new AsyncTask<Void, Void, List<PlaylistItem>>() {

            @Override
            protected List<PlaylistItem> doInBackground(Void... params) {
                PlaylistItemListResponse playlistItemListResponse = null;
                try {
                    YouTube.PlaylistItems.List request = mYouTube.playlistItems()
                            .list("snippet, contentDetails");
                    request.setPlaylistId(playListId);
                    request.setKey(mApiKey);
                    request.setMaxResults(50L);

                    playlistItemListResponse = request.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return playlistItemListResponse == null ? null
                        : playlistItemListResponse.getItems();
            }

            @Override
            protected void onPostExecute(List<PlaylistItem> playlist) {
                mChannelCallbacks.onLoadPlaylistItem(playlist);
            }

        }.execute();
    }

    public interface Callbacks {
        public void onLoadPlaylist(List<Playlist> playlist);
        public void onLoadPlaylistItem(List<PlaylistItem> playlistItem);
    }
}