package com.dungdv.videoapp.SpashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.dungdv.videoapp.R;
import com.dungdv.videoapp.VideoActivities.AcVideoList;

/**
 * Created by Microsoft on 11/28/16.
 */

public class SprashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sprash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SprashScreen.this, AcVideoList.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
