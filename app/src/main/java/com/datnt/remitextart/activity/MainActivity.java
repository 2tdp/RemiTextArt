package com.datnt.remitextart.activity;

import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.activity.project.CreateProjectActivity;
import com.datnt.remitextart.activity.project.ProjectsActivity;
import com.datnt.remitextart.activity.template.TemplateActivity;
import com.datnt.remitextart.fragment.vip.VipOneFragment;
import com.datnt.remitextart.utils.Utils;

import org.wysaid.common.Common;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {

    private ImageView ivVip;
    private TextView tvCrePro, tvTemp, tvPro;

    private final CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //Notice: the 'name' passed in is just what you write in the rule, e.g: 1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {

            Log.d("2tdp", "Loading file: " + name);
            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open("filter/" + name);
            } catch (IOException e) {
//                Log.d("2tdp", "Loading file: can't load file");
                Log.d("2tdp", "Loading file: " + name);
                return BitmapFactory.decodeFile(name);
            }

            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            Log.i(Common.LOG_TAG, "Loading bitmap over, you can choose to recycle or cache");

            //The bitmap is which you returned at 'loadImage'.
            //You can call recycle when this function is called, or just keep it for further usage.
            bmp.recycle();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        CGENativeLibrary.setLoadImageCallback(mLoadImageCallback, null);
    }

    private void init() {
        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        tvCrePro.setOnClickListener(v -> {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_click_create, null);
            ImageView ivUseTemp = view.findViewById(R.id.ivUseTemp);
            ImageView ivCreate = view.findViewById(R.id.ivCreate);
            TextView tvCancel = view.findViewById(R.id.tvCancel);

            AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
            dialog.setView(view);
            dialog.setCancelable(false);
            dialog.show();

            tvCancel.setOnClickListener(v1 -> dialog.cancel());
            ivUseTemp.setOnClickListener(v2 -> {
                Utils.setIntent(this, TemplateActivity.class.getName());
                dialog.cancel();
            });

            ivCreate.setOnClickListener(v3 -> {
                Utils.setIntent(this, CreateProjectActivity.class.getName());
                dialog.cancel();
            });
        });

        tvPro.setOnClickListener(v -> {
            Utils.setIntent(this, ProjectsActivity.class.getName());
        });

        tvTemp.setOnClickListener(v -> {
            Utils.setIntent(this, TemplateActivity.class.getName());
        });

        ivVip.setOnClickListener(v -> clickVip());
    }

    private void clickVip() {
        VipOneFragment vipOneFragment = VipOneFragment.newInstance();
        Utils.replaceFragment(getSupportFragmentManager(), vipOneFragment, false, true);
    }

    private void setUpLayout() {
        ivVip = findViewById(R.id.ivVip);
        tvCrePro = findViewById(R.id.tvCreate);
        tvTemp = findViewById(R.id.tvTemp);
        tvPro = findViewById(R.id.tvPro);
    }
}