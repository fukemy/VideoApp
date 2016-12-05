package com.dungdv.videoapp.VideoActivities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dungdv.videoapp.Adapters.VideoListAdapter;
import com.dungdv.videoapp.Helper.EnVideoItem;
import com.dungdv.videoapp.Helper.YoutubeConnecter;
import com.dungdv.videoapp.Interface.iYoutubeQuery;
import com.dungdv.videoapp.R;
import com.dungdv.videoapp.Utilities.GlobalParams;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggableView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

public class AcVideoList extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1234;
    private ListView listView;
    private DraggableView draggableView;
    private VideoListAdapter adapter;
    private LinearLayout ll;
    private YouTubePlayerView videoView;
    private YouTubePlayer youtubePlayer;
    private TextView tvTitle, tvProvider, tvAuthor;
    private SwipeRefreshLayout swipeLayout;
    boolean isShowingFullscreen;
    String VIDEO_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_video_list);

        initView();
        populateData();
    }

    int currentState;
    private void initView(){
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        listView = (ListView) findViewById(R.id.list_view);
        ll = (LinearLayout) findViewById(R.id.ll);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvProvider = (TextView) findViewById(R.id.tvProvider);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        videoView = (YouTubePlayerView) findViewById(R.id.videoView);

        videoView.initialize(GlobalParams.YOUTUBE_API_KEY, this);

        draggableView = (DraggableView) findViewById(R.id.draggable_view);
        draggableView.setVisibility(View.GONE);
        draggableView.setClickToMaximizeEnabled(true);
        draggableView.setClickToMinimizeEnabled(false);

        draggableView.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                draggableView.bringToFront();
                isShowingFullscreen = false;
                if(!youtubePlayer.isPlaying()){
                    youtubePlayer.play();
                }
            }

            @Override
            public void onMinimized() {
                draggableView.setAlpha(1.0f);
                draggableView.bringToFront();
                isShowingFullscreen = false;
                if(youtubePlayer.isPlaying()){
                    youtubePlayer.pause();
                }
            }

            @Override
            public void onClosedToLeft() {

            }

            @Override
            public void onClosedToRight() {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EnVideoItem videoData = (EnVideoItem) parent.getAdapter().getItem(position);
                VIDEO_ID = videoData.getId();
                youtubePlayer.loadVideo(VIDEO_ID);
                draggableView.setVisibility(View.VISIBLE);
                draggableView.maximize();

                tvTitle.setText(videoData.getTitle());
                tvAuthor.setText(videoData.getDescription());
                tvProvider.setText("THEHEGEO");

            }
        });

        swipeLayout.setColorScheme(new int[]{android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light});

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        populateData();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("YouTube Error (%1$s)",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            draggableView.bringToFront();
            youtubePlayer = player;
            youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youtubePlayer.setShowFullscreenButton(true);
            youtubePlayer.setFullscreen(false);

            youtubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean b) {
                        isShowingFullscreen = b;
                }
            });
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Display display = AcVideoList.this.getWindowManager()
                            .getDefaultDisplay();

            Point size = new Point();
            display.getSize(size);
            draggableView.setTopViewHeight(size.y);

        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            Resources r = getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics()
            );
            draggableView.setTopViewHeight(px);
        }
    }

    private void populateData(){
        new YoutubeConnecter(this).searchVideoByChannel(GlobalParams.THEHEGEO_CHANNEL_ID, new iYoutubeQuery() {
            @Override
            public void onSuccess(List<EnVideoItem> videoList) {
                if(swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);
                if(videoList.size() > 0) {
                    adapter = new VideoListAdapter(videoList, AcVideoList.this);
                    listView.setAdapter(adapter);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (draggableView.isMaximized()) {
            if (isShowingFullscreen == true){
                isShowingFullscreen = false;
                youtubePlayer.setFullscreen(false);
            }else {
                draggableView.minimize();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RECOVERY_DIALOG_REQUEST){
            videoView.initialize(GlobalParams.YOUTUBE_API_KEY, this);
        }
    }
}
