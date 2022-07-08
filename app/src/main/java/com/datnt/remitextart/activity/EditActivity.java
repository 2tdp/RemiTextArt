package com.datnt.remitextart.activity;

import android.os.Bundle;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;

public class EditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
    }

    private void init(){
        setUpLayout();
    }

    private void setUpLayout() {

    }
}