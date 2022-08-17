package com.textonphoto.texttophoto.quotecreator.pro.activity.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.activity.base.BaseActivity;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Utils.setIntent(SplashActivity.this, TipsActivity.class.getName());
                finish();
            }
        }, 2500);
    }
}