package com.example.twitching;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.twitching.Utils.TwitchApiUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StreamsActivity extends AppCompatActivity implements StreamAdapter.OnStreamClickListener, SharedPreferences.OnSharedPreferenceChangeListener,  NavigationView.OnNavigationItemSelectedListener, StreamViewModel.StreamActvityInterface {
    private DrawerLayout mDrawerLayout;

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mTopGameListRV;
    private TextView mLoadingErrorTV;
    private ProgressBar mLoadingPB;
    private StreamAdapter mTopGameAdapter;

    private String mGame;
    private String mLanguage;
    private String mCommunity;

    private StreamViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mTopGameListRV = findViewById(R.id.rv_forecast_items);


        mLoadingErrorTV = findViewById(R.id.tv_loading_error);
        mLoadingPB = findViewById(R.id.pb_loading);

        mTopGameListRV.setLayoutManager(new LinearLayoutManager(this));
        mTopGameListRV.setHasFixedSize(true);

        mTopGameAdapter = new StreamAdapter(this);
        mTopGameListRV.setAdapter(mTopGameAdapter);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mLanguage = prefs.getString(
                getString(R.string.pref_language_key),
                ""
        );

        mCommunity = prefs.getString(
                getString(R.string.pref_community_key),
                ""
        );

        // Get the viewmodel, set it up using MainActivityInterface (used for making loading
        // and error messages invisible/visible). Then, get the top games!
        mViewModel = ViewModelProviders.of(this).get(StreamViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Stuff")){
            mGame = (String)intent.getSerializableExtra("Stuff");
            mViewModel.setup( this, mGame, mLanguage, mCommunity);
            mViewModel.doGetTopStreams(mGame, mLanguage, mCommunity);
        }
    }

    // On Resume, update the
    @Override
    public void onResume(){
        super.onResume();
        mTopGameAdapter.updateAdapter(mViewModel.doGetTopStreams(mGame, mLanguage, mCommunity));
    }

    @Override
    public void onPreExecute(){
        mLoadingPB.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(List<TwitchApiUtils.Stream> games){
        mLoadingErrorTV.setVisibility(View.INVISIBLE);
        mTopGameListRV.setVisibility(View.VISIBLE);
        mLoadingPB.setVisibility(View.INVISIBLE);
        games = shuffle(games);
        mTopGameAdapter.updateAdapter(games);
    }

    @Override
    public void onError(){
        mLoadingErrorTV.setVisibility(View.VISIBLE);
        mTopGameListRV.setVisibility(View.INVISIBLE);
        mLoadingPB.setVisibility(View.INVISIBLE);
    }

    public List<TwitchApiUtils.Stream> shuffle(List<TwitchApiUtils.Stream> strem){
        Collections.shuffle(strem);
        return strem;
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
        System.out.println("preferences changed");
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


    @Override
    public void onStreamClick(TwitchApiUtils.Stream streamDetail){
        Intent intent = new Intent(this, StreamInfoActivity.class);
        intent.putExtra(TwitchApiUtils.EXTRA_STREAM_ITEM,streamDetail);
        startActivity(intent);
    }
}