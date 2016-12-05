package com.dungdv.videoapp.Helper;

import android.content.Context;
import android.os.StrictMode;

import com.dungdv.videoapp.R;
import com.dungdv.videoapp.Utilities.GlobalParams;
import com.dungdv.videoapp.Utilities.Logger;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Microsoft on 12/5/16.
 */

public class YoutubeConnecter {

    private YouTube youtube;
    private YouTube.Search.List query;

    public YoutubeConnecter(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {
                hr.setConnectTimeout(15000);
            }
        }).setApplicationName(context.getResources().getString(R.string.app_name)).build();

        try{
            query = youtube.search().list("id,snippet");
            query.setKey(GlobalParams.YOUTUBE_API_KEY);
            query.setType("channel");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        }catch(IOException e){
            Logger.error("Could not initialize: "+e);
        }
    }

    public List<EnVideoItem> search(String keywords){
        query.setQ(keywords);
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<EnVideoItem> items = new ArrayList<>();
            for(SearchResult result:results){
                EnVideoItem item = new EnVideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);

                Logger.error("found video: " + item.toString());
            }
            return items;
        }catch(Exception e){
            Logger.error("Could not search: "+e);
            return null;
        }
    }
}
