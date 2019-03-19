package com.example.twitching.Utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TwitchApiUtils {

    private static final String ClientId = "j6hqib6mxgjmd04c9hexytji00i052";
    private static final String ClientSecret = "vj33sanos0i74v37z1oh7dwo27aw3x";

//  Top games api call classes
    public static class Game implements Serializable {
        public String name;
        public String id;
    }
    public static class TopGames implements Serializable {
        public Game[] data;
    }

//  Streams api call classes
    public static class Stream implements Serializable {
        public String title;
    }
    public static class Streams implements Serializable {
        public Stream[] data;
    }

    public static String getTopGames() {
        try {
            //   gets top 100 streamed games from twitch game list
            return NetworkUtils.doHTTPGet("https://api.twitch.tv/helix/games/top?100",ClientId);
        } catch (IOException e) {
            Log.d(TAG, "getTopGames: failed");
            e.printStackTrace();
            return "Error, exception encountered";
        }
    }

    public static String getStreams(String gameId, String lang, String community) {
        // TODO: Create URL based on SharedPreferences
        String baseUrl = "https://api.twitch.tv/helix/streams?"+gameId;

        String url = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("language", lang)
                .appendQueryParameter("community_id", community)
                .build().toString();

        try {
            return NetworkUtils.doHTTPGet(url,ClientId);
        } catch (IOException e) {
            Log.d(TAG, "getTopGames: failed");
            e.printStackTrace();
            return "Error, exception encountered";
        }
    }

    public static ArrayList<Game> parseSearchResults(String json) {
        // TODO: I think there is an error where the TwitchAPI doesn't
        // TODO: return a NULL string. It returns an error message or something.
        Gson gson = new Gson();
        TopGames results = gson.fromJson(json, TopGames.class);
        if (results != null && results.data != null) {
            ArrayList<Game> top_games = new ArrayList<>();
            for(Game game : results.data){
                top_games.add(game);
            }
            return top_games;
        } else {
            return null;
        }
    }
}
