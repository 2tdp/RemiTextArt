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

    private ImageView ivBeat1, ivBeat2, ivBeat3;

    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        ivBeat1 = findViewById(R.id.ivBeat1);
        ivBeat2 = findViewById(R.id.ivBeat2);
        ivBeat3 = findViewById(R.id.ivBeat3);

        size = getResources().getDisplayMetrics().widthPixels / 2;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Utils.setIntent(SplashActivity.this, TipsActivity.class.getName());
                finish();
            }
        }, 2500);

        setUpSplashBeat();
    }

    private void setUpSplashBeat() {

        ivBeat1.getLayoutParams().width = size;
        size = size + size / 4;
        ivBeat2.getLayoutParams().width = size;
        size = size + size / 4;
        ivBeat3.getLayoutParams().width = size;

        ivBeat1.getLayoutParams().height = size;
        size = size + size / 4;
        ivBeat2.getLayoutParams().height = size;
        size = size + size / 4;
        ivBeat3.getLayoutParams().height = size;

        runAnimation();
    }

    private void runAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_out);
        ivBeat2.setAnimation(animation);
        ivBeat3.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivBeat1.clearAnimation();
                ivBeat2.clearAnimation();
                ivBeat3.clearAnimation();

                setUpSplashBeat();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}