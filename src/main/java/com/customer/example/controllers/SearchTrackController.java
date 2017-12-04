package com.customer.example.controllers;

import java.io.File;

import com.customer.example.util.Settings;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Matthew Fair on 11/18/2017.
 */

public class SearchTrackController {

    private static String resultUri, artistName;
    private SpotifyService apiService;
    private Settings savedSettings;
    private File file;



    public SearchTrackController(Settings s, File newFile){
        savedSettings = s;
        SpotifyApi api  = new SpotifyApi();
        api.setAccessToken(savedSettings.getAccessToken());
        apiService = api.getService();
        file = newFile;
    }

    public void SearchTrack(String trackName, final String newArtistName) {
        apiService.searchTracks(trackName, new Callback<TracksPager>(){
            @Override
            public void success(TracksPager tracksPager, Response response){
                String artist;

                for(int i = 0; i < tracksPager.tracks.items.size(); i++ ) {
                    System.out.println("SEARCH: " + i + " " + tracksPager.tracks.items.get(i).name);

                    artist = tracksPager.tracks.items.get(i).artists.get(0).name.toLowerCase().trim();
                    System.out.println("SEARCH: " + i + " " + artist);
                    System.out.println("SEARCH - INPUT ARTIST: " + newArtistName.toLowerCase());
                    if(artist.contains(newArtistName.toLowerCase())) {
                        savedSettings.setTrackName(tracksPager.tracks.items.get(i).name);
                        savedSettings.setTrackArtist(tracksPager.tracks.items.get(i).artists.get(0).name);
                        savedSettings.setUriResult(tracksPager.tracks.items.get(i).uri);
                        System.out.println("SEARCH: "+tracksPager.tracks.items.get(i).uri);
                        savedSettings.saveSettings(savedSettings, file);
                        break;
                    } else {
                        System.out.println("NO MATCH FOUND");
                    }
                    if(newArtistName.toLowerCase().equals("various artists")){
                        savedSettings.setTrackName(tracksPager.tracks.items.get(0).name);
                        savedSettings.setTrackArtist(tracksPager.tracks.items.get(0).artists.get(0).name);
                        savedSettings.setUriResult(tracksPager.tracks.items.get(0).uri);
                        savedSettings.saveSettings(savedSettings, file);
                        break;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error){

            }
        });
    }

    public void SearchTrack(String trackName) {

        apiService.searchTracks(trackName, new Callback<TracksPager>(){
            @Override
            public void success(TracksPager tracksPager, Response response){
                savedSettings.setTrackName(tracksPager.tracks.items.get(0).name);
                savedSettings.setTrackArtist(tracksPager.tracks.items.get(0).artists.get(0).name);
                savedSettings.setUriResult(tracksPager.tracks.items.get(0).uri);
                savedSettings.saveSettings(savedSettings, file);
            }

            @Override
            public void failure(RetrofitError error){

            }
        });
    }

    public String getResultUri(){
        return resultUri;
    }





}
