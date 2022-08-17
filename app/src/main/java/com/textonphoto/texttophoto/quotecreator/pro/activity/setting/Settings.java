package com.textonphoto.texttophoto.quotecreator.pro.activity.setting;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.activity.base.BaseActivity;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

public class Settings extends BaseActivity {

    private ImageView ivBack, ivVip;
    private RelativeLayout rlRate, rlHelp, rlPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {
        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        rlRate.setOnClickListener(v -> Utils.rateApp(this));
        rlHelp.setOnClickListener(v -> Utils.sendFeedback(this));
        rlPolicy.setOnClickListener(v -> Utils.privacyApp(this));
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivVip = findViewById(R.id.ivVip);
        rlRate = findViewById(R.id.rlRate);
        rlHelp = findViewById(R.id.rlHelp);
        rlPolicy = findViewById(R.id.rlPolicy);
    }
}