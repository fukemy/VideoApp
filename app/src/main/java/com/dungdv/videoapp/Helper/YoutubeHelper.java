package com.dungdv.videoapp.Helper;

import android.os.StrictMode;

import com.dungdv.videoapp.Entities.EnYoutubeInformationData;
import com.dungdv.videoapp.Utilities.GlobalParams;
import com.dungdv.videoapp.Utilities.Logger;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Microsoft on 11/29/16.
 */

public class YoutubeHelper {
    public static EnYoutubeInformationData getTitleQuietly(String VIDEO_ID) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        EnYoutubeInformationData en;
        try {
            en = new EnYoutubeInformationData();
            if (VIDEO_ID != null) {
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
            return null;
        }
    }
}
