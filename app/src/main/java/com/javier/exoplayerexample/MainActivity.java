package com.javier.exoplayerexample;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    private static final String VIDEO_URI = "http://techslides.com/demos/sample-videos/small.mp4";

    private SimpleExoPlayer player;
    private SimpleExoPlayerView simpleExoPlayerView;
    private static final DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.main_exoplayer);

        createPlayer();
        attachPlayerView();
        preparePlayer();
    }

    public void createPlayer() {
        TrackSelection.Factory videoTrackSelectionFactory
                = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        player = ExoPlayerFactory.newSimpleInstance(this,
                new DefaultTrackSelector(videoTrackSelectionFactory), new DefaultLoadControl());
    }

    public void attachPlayerView() {
        simpleExoPlayerView.setPlayer(player);
    }

    public void preparePlayer() {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(VIDEO_URI),
                dataSourceFactory, new DefaultExtractorsFactory(), new Handler(), null);
        player.prepare(videoSource);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(this,
                getString(R.string.app_name)), bandwidthMeter);
    }

}
