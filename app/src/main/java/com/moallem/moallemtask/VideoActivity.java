package com.moallem.moallemtask;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoActivity extends AppCompatActivity implements  View.OnClickListener, Player.EventListener {
    private SimpleExoPlayer mExoPlayer;
    ImageButton speakerControllerButton;
    ImageButton fullScreenControllerButton;
    ImageButton togglePlayPauseControllerButton;
    boolean flagController = true;
    int mute = 0, fullScreen = 0;
    SimpleExoPlayer player;
    PlayerView playerView;
    float currentVolume;
    PlayerControlView controls;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);
        progressBar = findViewById(R.id.progressBar);
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
        player = ExoPlayerFactory.newSimpleInstance(this);
        playerView = (PlayerView) findViewById(R.id.playerView);
        togglePlayPauseControllerButton = findViewById(R.id.togglePlayPauseControllerButton);
        controls = findViewById(R.id.controls);
        speakerControllerButton = controls.findViewById(R.id.speakerControllerButton);
        fullScreenControllerButton = controls.findViewById(R.id.fullScreenControllerButton);
        controls.setPlayer(player);
        playerView.setPlayer(player);
        playerView.requestFocus();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Exoplayer"));
// This is the MediaSource representing the media to be played.
        MediaSource videoSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
// Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        speakerControllerButton.setOnClickListener(this);
        fullScreenControllerButton.setOnClickListener(this);
        togglePlayPauseControllerButton.setOnClickListener(this);
        playerView.setOnClickListener(this);
        player.addListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
            layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(layoutParams);
            fullScreen = 1;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
            layoutParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
            playerView.setLayoutParams(layoutParams);
            fullScreen = 0;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.togglePlayPauseControllerButton) {
            if (player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady()) {
                player.setPlayWhenReady(false);
                player.getPlaybackState();
                togglePlayPauseControllerButton.setImageResource(R.drawable.ic_video_play);

            } else {
                player.setPlayWhenReady(true);
                player.getPlaybackState();
                togglePlayPauseControllerButton.setImageResource(R.drawable.ic_video_pause);

            }
        } else if (v.getId() == R.id.speakerControllerButton) {
            if (mute == 0) {
                speakerControllerButton.setImageResource(R.drawable.ic_mute);
                currentVolume = player.getVolume();
                player.setVolume(0f);
                mute = 1;
            } else {
                speakerControllerButton.setImageResource(R.drawable.ic_speaker);
                player.setVolume(currentVolume);
                mute = 0;
            }
        } else if (v.getId() == R.id.fullScreenControllerButton) {
            if (fullScreen == 0) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(layoutParams);
                fullScreen = 1;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                layoutParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                playerView.setLayoutParams(layoutParams);
                fullScreen = 0;
            }
        } else{
            if(controls.isVisible()) {
                controls.setVisibility(View.GONE);
                togglePlayPauseControllerButton.setVisibility(View.GONE);

            }else {
                controls.setVisibility(View.VISIBLE);
                togglePlayPauseControllerButton.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY ) {
            progressBar.setVisibility(View.INVISIBLE);
            togglePlayPauseControllerButton.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            togglePlayPauseControllerButton.setVisibility(View.INVISIBLE);
        }
        if (playWhenReady) {
            togglePlayPauseControllerButton.setImageResource(R.drawable.ic_video_pause);
        } else {
            togglePlayPauseControllerButton.setImageResource(R.drawable.ic_video_play);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

}