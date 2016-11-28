package com.dungdv.videoapp.VideoActivities;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dungdv.videoapp.Adapters.VideoListAdapter;
import com.dungdv.videoapp.Entities.EnVideoData;
import com.dungdv.videoapp.R;
import com.dungdv.videoapp.Utilities.GlobalParams;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggableView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class AcVideoList extends YouTubeBaseActivity {

    List<EnVideoData> videoList;
    private ListView listView;
    private DraggableView draggableView;
    private VideoListAdapter adapter;
    private boolean isFirstTimeClick;
    private ListView lv;
    private YouTubePlayerView videoView;
    private YouTubePlayer youtubePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_video_list);

        isFirstTimeClick = true;

        initView();
        populateData();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.list_view);
        lv = (ListView) findViewById(R.id.ll);
        videoView = (YouTubePlayerView) findViewById(R.id.videoView);
        videoView.initialize(GlobalParams.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    draggableView.bringToFront();
                    youtubePlayer = youTubePlayer;
//                    youTubePlayer.loadVideo("15on8DquWgA");
//                    youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        draggableView = (DraggableView) findViewById(R.id.draggable_view);
        draggableView.setVisibility(View.GONE);
        draggableView.setClickToMaximizeEnabled(true);
        draggableView.setClickToMinimizeEnabled(false);

        draggableView.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                if(youtubePlayer != null){
                    youtubePlayer.loadVideo("15on8DquWgA");
                }
                draggableView.bringToFront();
                listView.setVisibility(View.GONE);
                videoView.bringToFront();
            }

            @Override
            public void onMinimized() {
                if(youtubePlayer != null){
                    youtubePlayer.release();
                }
                draggableView.bringToFront();
                listView.setVisibility(View.VISIBLE);
                videoView.bringToFront();
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
                Toast.makeText(AcVideoList.this, "click in pos: " + position, Toast.LENGTH_SHORT).show();

                draggableView.setVisibility(View.VISIBLE);
                if(isFirstTimeClick){
                    isFirstTimeClick = false;
                }
                draggableView.maximize();
            }
        });
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

            Toast.makeText(AcVideoList.this, "landscape", Toast.LENGTH_SHORT)
                    .show();
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(AcVideoList.this, "portraite", Toast.LENGTH_SHORT)
                    .show();

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
        videoData.setVideoDescription("test 1");
        videoData.setVideoName("Nguyen Van Cu");
        videoData.setVideoUrl("http://nvc42.ddns.net:81/viewer/main.html");
        videoData.setVideoThumb(GlobalParams.THUMBS_VIDEO_SAMPLE);

        videoList.add(videoData);
        adapter = new VideoListAdapter(videoList, this);
        listView.setAdapter(adapter);
    }


}
