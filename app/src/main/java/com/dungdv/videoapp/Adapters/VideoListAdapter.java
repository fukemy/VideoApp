package com.dungdv.videoapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dungdv.videoapp.Entities.EnVideoData;
import com.dungdv.videoapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Microsoft on 11/28/16.
 */

public class VideoListAdapter extends BaseAdapter {

    List<EnVideoData> videoList;
    private Context mContext;

    public VideoListAdapter(List<EnVideoData> videoList, Context mContext){
        this.videoList = videoList;
        this.mContext = mContext;
    }

    private static class ViewHolder {
        private TextView videoName;
        private ImageView thumb;

        public ViewHolder(View v) {
            videoName = (TextView) v.findViewById(R.id.videoName);
            thumb = (ImageView) v.findViewById(R.id.videoThumb);
        }
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.video_thumb_listview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.videoName.setText(getItem(position).getVideoName());
        Picasso.with(mContext).load(getItem(position).getVideoThumb()).into(holder.thumb);

        return convertView;
    }

    @Override
    public EnVideoData getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
