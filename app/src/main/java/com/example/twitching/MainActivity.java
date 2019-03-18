package com.example.twitching;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import com.example.twitching.Utils.NetworkUtils;
import com.example.twitching.Utils.TwitchApiUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements TopGamesAdapter.OnTopGameItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener,  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    private static final String TAG = "main";

    private RecyclerView mTopGameListRV;
    private TextView mLoadingErrorTV;
    private ProgressBar mLoadingPB;
    private TopGamesAdapter mTopGameAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mTopGameListRV = findViewById(R.id.rv_forecast_items);


        mLoadingErrorTV = findViewById(R.id.tv_loading_error);
        mLoadingPB = findViewById(R.id.pb_loading);

        mTopGameListRV.setLayoutManager(new LinearLayoutManager(this));
        mTopGameListRV.setHasFixedSize(true);

        mTopGameAdapter = new TopGamesAdapter(this);
        mTopGameListRV.setAdapter(mTopGameAdapter);

        doGitHubSearch();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        System.out.println("here");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
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
//                Log.d(TAG, "RESULT ----- " + s);
            } else {

            }
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
