package com.example.gdg_opensource_codelab_sample_1;

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

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
	private GetListItemsTask mGetListTask;
	private GetListsTask mGetListsTask;

	private YouTubeChannelClient(String apiKey, String channelID) {
		mApiKey = apiKey;
		mChannelID = channelID;

		mYouTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				new HttpRequestInitializer() {
					public void initialize(HttpRequest request) throws IOException {
					}
				})
				.setApplicationName("youtube-channel-list-client")
				.build();
	}

	public static YouTubeChannelClient newYouTubeChannelClient(String apiKey, String channelID) {
		return new YouTubeChannelClient(apiKey, channelID);
	}

	public void getPlayList(final Callbacks callbacks) {
		cancelAsyncTaskIfNeeded(mGetListsTask);

		mGetListsTask = new GetListsTask(mYouTube, mApiKey, mChannelID, callbacks);
		mGetListsTask.execute();
	}

	public void getPlaylistItem(final String playListId, Callbacks callbacks) {
		cancelAsyncTaskIfNeeded(mGetListTask);

		mGetListTask = new GetListItemsTask(mYouTube, mApiKey, playListId, callbacks);
		mGetListTask.execute();
	}

	private void cancelAsyncTaskIfNeeded(AsyncTask asyncTask) {
		if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
			asyncTask.cancel(true);
		}
	}

	private static class GetListsTask extends AsyncTask<Void, Void, List<Playlist>> {
		private final String mChannelId;
		private final YouTube mClient;
		private final WeakReference<Callbacks> mCallbacksWeakReference;
		private final String mApiKey;

		public GetListsTask(YouTube client, String apiKey, String channelId,
				Callbacks callbacks) {
			mClient = client;
			mApiKey = apiKey;
			mChannelId = channelId;
			mCallbacksWeakReference = new WeakReference<Callbacks>(callbacks);
		}

		@Override
		protected List<Playlist> doInBackground(Void... params) {
			PlaylistListResponse searchListResponse = null;
			try {
				YouTube.Playlists.List request = mClient.playlists().list("snippet");
				request.setKey(mApiKey);
				request.setChannelId(mChannelId);
				request.setMaxResults(50L);
				searchListResponse = request.execute();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return searchListResponse == null ? null : searchListResponse.getItems();
		}

		@Override
		protected void onPostExecute(List<Playlist> playlists) {
			if (mCallbacksWeakReference.get() != null) {
				mCallbacksWeakReference.get().onLoadPlaylist(playlists);
			}
		}

	}

	private static class GetListItemsTask extends AsyncTask<Void, Void, List<PlaylistItem>> {
		private final String mPlayListId;
		private final String mApiKey;
		private final YouTube mClient;
		private final WeakReference<Callbacks> mCallbacksWeakReference;

		public GetListItemsTask(YouTube client, String apiKey, String playlistId,
				Callbacks callbacks) {
			mPlayListId = playlistId;
			mApiKey = apiKey;
			mClient = client;
			mCallbacksWeakReference = new WeakReference<Callbacks>(callbacks);
		}

		@Override
		protected List<PlaylistItem> doInBackground(Void... params) {
			PlaylistItemListResponse playlistItemListResponse = null;
			try {
				YouTube.PlaylistItems.List request
						= mClient.playlistItems().list("snippet, contentDetails");
				request.setPlaylistId(mPlayListId);
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
			if (mCallbacksWeakReference.get() != null) {
				mCallbacksWeakReference.get().onLoadPlaylistItem(mPlayListId, playlist);
			}
		}

	}

	public interface Callbacks {
		public void onLoadPlaylist(List<Playlist> playlist);

		public void onLoadPlaylistItem(String playlistId, List<PlaylistItem> playlistItem);
	}
}