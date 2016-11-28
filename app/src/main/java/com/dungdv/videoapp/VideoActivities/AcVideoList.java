package com.dungdv.videoapp.VideoActivities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class AcVideoList extends Activity {

    List<EnVideoData> videoList;
    private ListView listView;
    private DraggableView draggableView;
    private VideoListAdapter adapter;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_video_list);

        initView();
        populateData();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.list_view);
        videoView = (VideoView) findViewById(R.id.videoView);
        draggableView = (DraggableView) findViewById(R.id.draggable_view);
        draggableView.setVisibility(View.GONE);
        draggableView.setClickToMaximizeEnabled(true);
        draggableView.setClickToMinimizeEnabled(false);

        draggableView.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                if(draggableView.getVisibility() == View.GONE)
                    draggableView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMinimized() {

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
                draggableView.maximize();
            }
        });

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

        videoData = new EnVideoData();
        videoData.setVideoDescription("test 2");
        videoData.setVideoName("Ho Chi Minh");
        videoData.setVideoUrl("http://nvc42.ddns.net:81/viewer/main.html");
        videoData.setVideoThumb(GlobalParams.THUMBS_VIDEO_SAMPLE);


        videoList.add(videoData);
        Toast.makeText(this, "populateData: " + videoList.size(), Toast.LENGTH_SHORT).show();
        adapter = new VideoListAdapter(videoList, this);
        listView.setAdapter(adapter);
    }


}
