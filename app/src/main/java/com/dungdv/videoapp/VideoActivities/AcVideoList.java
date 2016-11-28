package com.dungdv.videoapp.VideoActivities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        draggableView.setClickToMinimizeEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EnVideoData videoData = (EnVideoData) parent.getAdapter().getItem(position);
                draggableView.maximize();
            }
        });
    }

    private void populateData(){

        Toast.makeText(this, "populateData: " + videoList.size(), Toast.LENGTH_SHORT).show();
        videoList = new ArrayList<>();
        EnVideoData videoData = new EnVideoData();
        videoData.setVideoDescription("test");
        videoData.setVideoName("test");
        videoData.setVideoUrl("http://nvc42.ddns.net:81/viewer/main.html");
        videoData.setVideoThumb(GlobalParams.THUMBS_VIDEO_SAMPLE);

        adapter = new VideoListAdapter(videoList, this);
        listView.setAdapter(adapter);
    }


}
