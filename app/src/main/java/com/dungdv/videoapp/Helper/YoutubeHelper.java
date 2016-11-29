package com.dungdv.videoapp.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.TextView;

import com.dungdv.videoapp.Entities.EnYoutubeInformationData;
import com.dungdv.videoapp.R;
import com.dungdv.videoapp.Utilities.GlobalParams;
import com.dungdv.videoapp.Utilities.Logger;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Microsoft on 11/29/16.
 */

public class YoutubeHelper {
    public static void getTitleQuietly(Context mContext, String VIDEO_ID, TextView title, TextView author, TextView provider) {
       YoutubeHelper helper = new YoutubeHelper();
        helper.getYoutubeInformation(mContext, VIDEO_ID, title, author, provider);
    }

    private void getYoutubeInformation(Context mContext, String VIDEO_ID, TextView title, TextView author, TextView provider){
        new GetYoutubeInformation(mContext, VIDEO_ID, title, author, provider).execute();
    }

    private class GetYoutubeInformation extends AsyncTask<Void, Void, EnYoutubeInformationData>{
        String VIDEO_ID;
        TextView title, author, provider;
        Context mContext;

        public GetYoutubeInformation(Context mContext,String VIDEO_ID, TextView title, TextView author, TextView provider){
            this.VIDEO_ID = VIDEO_ID;
            this.author = author;
            this.provider = provider;
            this.title = title;
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            Logger.error("start get information of video : " + VIDEO_ID);
        }

        @Override
        protected EnYoutubeInformationData doInBackground(Void... voids) {
            EnYoutubeInformationData en;
            try {
                en = new EnYoutubeInformationData();
                if (VIDEO_ID != null) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    URL embededURL = new URL("http://www.youtube.com/oembed?url=youtube.com/watch?v=" +
                            VIDEO_ID + "&format=json"
                    );
                    Logger.error("detail: " + IOUtils.toString(embededURL));
                    en.setAuthor(new JSONObject(IOUtils.toString(embededURL)).getString(GlobalParams.YOUTUBE_AUTHOR));
                    en.setProvider(new JSONObject(IOUtils.toString(embededURL)).getString(GlobalParams.YOUTUBE_PROVIDER));
                    en.setTitle(new JSONObject(IOUtils.toString(embededURL)).getString(GlobalParams.YOUTUBE_TITLE));
                    return en;
                }else{
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Logger.error("Problem while get video information : " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(EnYoutubeInformationData data) {
            if(data != null){
                title.setText(data.getTitle());
                provider.setText(data.getProvider());
                author.setText(data.getAuthor());
            }else{
                title.setText(mContext.getResources().getString(R.string.youtube_error_get_video_information));
                provider.setText("");
                author.setText("");
            }
        }
    }


}
