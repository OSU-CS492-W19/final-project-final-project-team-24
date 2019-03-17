package com.example.twitching.Utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;

import static android.content.ContentValues.TAG;

public class TwitchApiUtils {

    private static final String ClientId = "j6hqib6mxgjmd04c9hexytji00i052";
    private static final String ClientSecret = "vj33sanos0i74v37z1oh7dwo27aw3x";

    public static class Game implements Serializable {
        public String name;
        public String id;
    }
    public static class TopGames implements Serializable {
        public Game[] data;
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

    public static String getStreams(String gameId) {
        String url = "https://api.twitch.tv/helix/streams?"+gameId;
        try {
            return NetworkUtils.doHTTPGet(url,ClientId);
        } catch (IOException e) {
            Log.d(TAG, "getTopGames: failed");
            e.printStackTrace();
            return "Error, exception encountered";
        }
    }

    public static Game[] parseSearchResults(String json) {
        Gson gson = new Gson();
        TopGames results = gson.fromJson(json, TopGames.class);
        if (results != null && results.data != null) {
            return results.data;
        } else {
            return null;
        }
    }
}