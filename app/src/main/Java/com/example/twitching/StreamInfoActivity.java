package com.example.twitching;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.twitching.Utils.TwitchApiUtils;

import java.util.List;

public class StreamInfoActivity extends AppCompatActivity  {
    private TextView mStreamNameTV;
    private TextView mStreamerTV;
    private TextView mViewCountTV;
    private TextView mStreamStartTV;

    private ImageView mThumbnailIV;

    private String streamerL;

    private TwitchApiUtils.Stream mStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_info);

        mStreamNameTV = findViewById(R.id.tv_title);
        mStreamerTV = findViewById(R.id.tv_user);
        mViewCountTV = findViewById(R.id.tv_views);
        mStreamStartTV = findViewById(R.id.tv_start);

        mThumbnailIV = findViewById(R.id.iv_stream_thumbnail);

        Button watchButton = findViewById(R.id.btn_watch);
        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "https://twitch.tv/" + streamerL;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TwitchApiUtils.EXTRA_STREAM_ITEM)){
            mStream = (TwitchApiUtils.Stream)intent.getSerializableExtra(TwitchApiUtils.EXTRA_STREAM_ITEM);
            fillInLayout(mStream);
        }
    }

    private void fillInLayout(TwitchApiUtils.Stream stream){
        String title = getString(R.string.stream_title, stream.title);
        String streamer = getString(R.string.stream_streamer, stream.user_name);
        String watchers = getString(R.string.stream_views, stream.viewer_count);
        String start = getString(R.string.stream_start, stream.started_at);

        String thumb = stream.thumbnail_url;

        streamerL = stream.user_name;

        Glide.with(this).load(thumb).into(mThumbnailIV);
        mStreamNameTV.setText(title);
        mStreamerTV.setText(streamer);
        mViewCountTV.setText(watchers);
        mStreamStartTV.setText(start);
    }


}
