package com.textonphoto.texttophoto.quotecreator.pro.activity.base;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarTransparent(this);
    }

    protected void setIntent(String nameActivity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getPackageName(), nameActivity));
        startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
    }
}
