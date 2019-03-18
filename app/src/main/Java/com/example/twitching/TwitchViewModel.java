package com.example.twitching;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.twitching.Utils.TwitchApiUtils;

import java.util.List;

import static android.content.ContentValues.TAG;


public class TwitchViewModel extends AndroidViewModel {
    private MutableLiveData<List<TwitchApiUtils.Game>> games;
    private MainActivityInterface mainActivityInterface;

    public TwitchViewModel(@NonNull Application application) {
        super(application);
    }

    public void setup(MainActivityInterface m){
        mainActivityInterface = m;
        doGetTopGames();
    }

    public interface MainActivityInterface {
        void onPreExecute();
        void onError();
        void onSuccess(List<TwitchApiUtils.Game> games);
    }



    public List<TwitchApiUtils.Game> doGetTopGames() {
        if(games == null){
            games = new MutableLiveData<List<TwitchApiUtils.Game>>();
            new TopGamesTask().execute("");
            Log.d(TAG, "Performing Async Task");
        }else{
            Log.d(TAG, "Just returning stuff. no async task");
        }
        return games.getValue();
    }


    // THIS IS STREAM GET HTTP TASK
/*    class StreamsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... gameId) {
            Log.d(TAG, "GameID ----- " + gameId[0]);

            String results = null;
            results = TwitchApiUtils.getStreams(gameId[0]);

            Log.d(TAG, "RESULT ----- " + results);
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
//                Log.d(TAG, "RESULT ----- " + s);
            } else {

            }
        }
    }*/


    class TopGamesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainActivityInterface.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            String results = null;
            results = TwitchApiUtils.getTopGames();
            Log.d(TAG, "RESULT ----- " + results);
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Log.d(TAG, "RESULT ----- " + s);
                games.setValue(TwitchApiUtils.parseSearchResults(s));
                mainActivityInterface.onSuccess(games.getValue());
            } else {
                mainActivityInterface.onError();
            }
        }
    }
}
