package com.example.twitching.Utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class NetworkUtils {


    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHTTPGet(String url,String clientID) throws IOException {
        Request request = new Request.Builder()
                .addHeader("Client-ID",clientID)
                .url(url)
                .build();

        Log.d(TAG, "doHTTPGet: "+request.headers());
        Response response = mHTTPClient.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
//            Log.d(TAG, "doHTTPGet: Request failed");
            response.close();
        }
    }

}
