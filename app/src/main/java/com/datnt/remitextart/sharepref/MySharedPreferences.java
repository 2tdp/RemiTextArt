package com.datnt.remitextart.sharepref;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private static final String MY_SHARED_PREFERENCE = "MY_SHARED_PREFERENCE";
    private Context context;

    public MySharedPreferences(Context context) {
        this.context = context;
    }

    public void putBooleanValue(String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void putStringwithKey(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringwithKey(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, value);
    }

    public void putIntWithKey(String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntWithKey(String key, int defaultInt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultInt);
    }
}
