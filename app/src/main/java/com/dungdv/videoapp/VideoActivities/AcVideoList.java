package com.dungdv.videoapp.VideoActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dungdv.videoapp.Adapters.VideoListAdapter;
import com.dungdv.videoapp.Entities.EnVideoData;
import com.dungdv.videoapp.Entities.EnYoutubeInformationData;
import com.dungdv.videoapp.Helper.YoutubeHelper;
import com.dungdv.videoapp.R;
import com.dungdv.videoapp.Utilities.GlobalParams;
import com.dungdv.videoapp.Utilities.Logger;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggableView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AcVideoList extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1234;
    List<EnVideoData> videoList;
    private ListView listView;
    private DraggableView draggableView;
    private VideoListAdapter adapter;
    private boolean isFirstTimeClick;
    private LinearLayout ll;
    private YouTubePlayerView videoView;
    private YouTubePlayer youtubePlayer;
    private TextView tvTitle, tvProvider, tvAuthor;
    String VIDEO_ID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_video_list);

        isFirstTimeClick = true;

        initView();
        populateData();
    }

    int currentState;
    private void initView(){
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

        draggableView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                listView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        draggableView.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {

                draggableView.bringToFront();
                listView.setVisibility(View.GONE);
                videoView.bringToFront();
                if(youtubePlayer != null){

//                    if(youtubePlayer.isPlaying() || isPlaying == true){
//                        Logger.error("is playing");
//                        youtubePlayer.play();
//                    }else {
//                        Logger.error("loadVideo");
                        youtubePlayer.loadVideo(VIDEO_ID);
//                    }
//                    if(currentState != 0){
//                        youtubePlayer.seekToMillis(currentState);
//                    }

                }
            }

            @Override
            public void onMinimized() {
                listView.setVisibility(View.VISIBLE);
                draggableView.bringToFront();
                videoView.bringToFront();
                videoView.bringChildToFront(draggableView);
                if(youtubePlayer != null){
//                    currentState = youtubePlayer.getCurrentTimeMillis();
                    Logger.error("pause video in state: " + currentState);
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

                EnVideoData videoData = (EnVideoData) parent.getAdapter().getItem(position);
                VIDEO_ID = videoData.getVideoUrl();
                draggableView.setVisibility(View.VISIBLE);
                if(isFirstTimeClick){
                    isFirstTimeClick = false;
                }

                YoutubeHelper.getTitleQuietly(AcVideoList.this, VIDEO_ID, tvTitle, tvAuthor, tvProvider);

                draggableView.maximize();
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
            youtubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
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


    EnVideoData videoData;
    private void populateData(){
        videoList = new ArrayList<>();

        videoData = new EnVideoData();
        videoData.setVideoDescription("Chưa cập nhập địa chỉ");
        videoData.setVideoName("Đường Nguyễn Văn Cừ");
        videoData.setVideoUrl("RskPgmc2ngg");
        videoData.setVideoThumb(GlobalParams.THUMBS_VIDEO_SAMPLE);
        videoList.add(videoData);

        videoData = new EnVideoData();
        videoData.setVideoDescription("Chưa cập nhập địa chỉ");
        videoData.setVideoName("Đường Võ Thị Sáu");
        videoData.setVideoUrl("8EkJnJyVPtA");
        videoData.setVideoThumb(GlobalParams.THUMBS_VIDEO_SAMPLE);

        videoList.add(videoData);
        adapter = new VideoListAdapter(videoList, this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RECOVERY_DIALOG_REQUEST){
            videoView.initialize(GlobalParams.YOUTUBE_API_KEY, this);
        }
    }
}
