package com.datnt.remitextart.activity.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.utils.Utils;

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