package com.datnt.remitextart;

import android.app.Application;

import com.datnt.remitextart.sharepref.DataLocalManager;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        DataLocalManager.init(getApplicationContext());

        super.onCreate();
    }
}
