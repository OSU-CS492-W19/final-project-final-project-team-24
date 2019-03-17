package com.example.twitching;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.twitching.Utils.NetworkUtils;
import com.example.twitching.Utils.TwitchApiUtils;

public class MainActivity extends AppCompatActivity implements TopGamesAdapter.OnTopGameItemClickListener {
    private static final String TAG = "main";

    private RecyclerView mTopGameListRV;
    private TextView mLoadingErrorTV;
    private ProgressBar mLoadingPB;
    private TopGamesAdapter mTopGameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopGameListRV = findViewById(R.id.rv_forecast_list);


        mLoadingErrorTV = findViewById(R.id.tv_loading_error);
        mLoadingPB = findViewById(R.id.pb_loading);

        mTopGameListRV.setLayoutManager(new LinearLayoutManager(this));
        mTopGameListRV.setHasFixedSize(true);

        mTopGameAdapter = new TopGamesAdapter(this);
        mTopGameListRV.setAdapter(mTopGameAdapter);

        doGitHubSearch();
    }

    private void doGitHubSearch() {
        new TopGamesTask().execute("");
    }

    @Override
    public void onTopGameItemClick(TwitchApiUtils.Game detailedTopGame) {
        new StreamsTask().execute(detailedTopGame.id);
    }

    class StreamsTask extends AsyncTask<String, Void, String> {

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
                Log.d(TAG, "RESULT ----- " + s);
//                TwitchApiUtils.Game[] games = TwitchApiUtils.parseSearchResults(s);
//                mTopGameAdapter.updateSearchResults(games);
            } else {
//                mLoadingErrorTV.setVisibility(View.VISIBLE);
//                mTopGameListRV.setVisibility(View.INVISIBLE);
            }
//            mLoadingPB.setVisibility(View.INVISIBLE);
        }
    }

    
    class TopGamesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPB.setVisibility(View.VISIBLE);
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
                mLoadingErrorTV.setVisibility(View.INVISIBLE);
                mTopGameListRV.setVisibility(View.VISIBLE);
                Log.d(TAG, "RESULT ----- " + s);
                TwitchApiUtils.Game[] games = TwitchApiUtils.parseSearchResults(s);
                mTopGameAdapter.updateSearchResults(games);
            } else {
                mLoadingErrorTV.setVisibility(View.VISIBLE);
                mTopGameListRV.setVisibility(View.INVISIBLE);
            }
            mLoadingPB.setVisibility(View.INVISIBLE);
        }
    }
}
