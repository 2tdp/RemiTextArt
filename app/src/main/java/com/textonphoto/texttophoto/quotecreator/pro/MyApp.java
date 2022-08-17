package com.textonphoto.texttophoto.quotecreator.pro;

import android.app.Application;

import com.textonphoto.texttophoto.quotecreator.pro.sharepref.DataLocalManager;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        DataLocalManager.init(getApplicationContext());

        super.onCreate();
    }
}
