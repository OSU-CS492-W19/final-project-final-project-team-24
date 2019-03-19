package com.example.twitching;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.twitching.Utils.TwitchApiUtils;

import java.util.List;

import static android.content.ContentValues.TAG;

public class StreamViewModel extends AndroidViewModel {
    private MutableLiveData<List<TwitchApiUtils.Game>> games;
    private MutableLiveData<List<TwitchApiUtils.Stream>> streams;

    private StreamActvityInterface streamActvityInterface;

    private String gameId;

    public StreamViewModel(@NonNull Application application) {
        super(application);
    }

    public void setup(StreamActvityInterface m, String game){
        streamActvityInterface = m;
        doGetTopStreams(game);
    }

    public interface StreamActvityInterface {
        void onPreExecute();
        void onError();
        void onSuccess(List<TwitchApiUtils.Stream> streams);
    }


    public List<TwitchApiUtils.Stream> doGetTopStreams(String gameID) {
//      Request has to be performed every time since each gameId is unique
        gameId = gameID;
        if (streams == null){
            streams = new MutableLiveData<List<TwitchApiUtils.Stream>>();
            new StreamsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            Log.d(TAG, "Performing Async Task");
        }
        else{
            Log.d(TAG, "Streeeeeams");
        }
        System.out.println(streams.getValue());
        return streams.getValue();
    }


    // THIS IS STREAM GET HTTP TASK
    class StreamsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            streamActvityInterface.onPreExecute();
        }

        //@SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "GameID 0-0----- " + gameId);

            String results = null;
            results = TwitchApiUtils.getStreams(gameId);

            //streams.setValue(TwitchApiUtils.parseSearchResultsStream(results));

            Log.d(TAG, "RESULT 0-0----- " + streams.getValue());

            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
//              Log.d(TAG, "RESULT ----- " + s);
                streams.setValue(TwitchApiUtils.parseSearchResultsStream(s));
                System.out.println("Post: " + streams.getValue());
                streamActvityInterface.onSuccess(streams.getValue());

            } else {
                streamActvityInterface.onError();
            }
        }
    }

}
