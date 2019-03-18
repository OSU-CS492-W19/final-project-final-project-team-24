package com.example.twitching;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.twitching.Utils.TwitchApiUtils;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements TopGamesAdapter.OnTopGameItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener,  NavigationView.OnNavigationItemSelectedListener, TwitchViewModel.MainActivityInterface {
    private DrawerLayout mDrawerLayout;

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mTopGameListRV;
    private TextView mLoadingErrorTV;
    private ProgressBar mLoadingPB;
    private TopGamesAdapter mTopGameAdapter;


    private TwitchViewModel mViewModel;

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

        // Get the viewmodel, set it up using MainActivityInterface (used for making loading
        // and error messages invisible/visible). Then, get the top games!
        mViewModel = ViewModelProviders.of(this).get(TwitchViewModel.class);
        mViewModel.setup(this);
        mViewModel.doGetTopGames();

    }

    // On Resume, update the
    @Override
    public void onResume(){
        super.onResume();
        mTopGameAdapter.updateAdapter(mViewModel.doGetTopGames());
    }

    @Override
    public void onPreExecute(){
        mLoadingPB.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(List<TwitchApiUtils.Game> games){
        mLoadingErrorTV.setVisibility(View.INVISIBLE);
        mTopGameListRV.setVisibility(View.VISIBLE);
        mLoadingPB.setVisibility(View.INVISIBLE);
        mTopGameAdapter.updateAdapter(games);
    }

    @Override
    public void onError(){
        mLoadingErrorTV.setVisibility(View.VISIBLE);
        mTopGameListRV.setVisibility(View.INVISIBLE);
        mLoadingPB.setVisibility(View.INVISIBLE);
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
    public void onTopGameItemClick(TwitchApiUtils.Game detailedTopGame) {
        mViewModel.doGetTopStreams(detailedTopGame.id);
    }
}