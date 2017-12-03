package com.customer.example.controllers;

/**
 * Created by Matthew Fair on 11/22/2017.
 */

import java.io.File;
import java.util.HashMap;

import com.customer.example.util.Settings;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistController {


    private SpotifyService apiService;
    private Settings s;
    private File file;
    private final String PLAYLIST_NAME = "Wabam";

    public PlaylistController(Settings settings, File f){
        s = settings;
        file = f;
        SpotifyApi api  = new SpotifyApi();
        api.setAccessToken(s.getAccessToken());
        apiService = api.getService();
    }

    public void createPlaylist(){
        if(s.getPlaylistID() == null) {
            HashMap<String, Object> playlistParams = new HashMap<String, Object>();
            playlistParams.put("name", PLAYLIST_NAME);
            playlistParams.put("public", false);

            apiService.createPlaylist(s.getUserID(), playlistParams, new Callback<Playlist>() {
                @Override
                public void success(Playlist playlist, Response response) {
                    System.out.println("CREATE: " + playlist.id);
                    s.setPlaylistID(playlist.id);
                    s.saveSettings(s, file);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } else
            System.out.println("ALREADY CREATED: " + s.getPlaylistID());
    }

    public void addToPlaylist(String trackUri){
            if(s.getPlaylistID() != null) {
                HashMap parametersMap = new HashMap();
                parametersMap.put("uris", trackUri);
                apiService.addTracksToPlaylist(s.getUserID(), s.getPlaylistID(), parametersMap, new HashMap<String, Object>(), new Callback<Pager<PlaylistTrack>>() {
                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            } else
                System.out.println("There has not been a playlist created yet.");
    }



}
