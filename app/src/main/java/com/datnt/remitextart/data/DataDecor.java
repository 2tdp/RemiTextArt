package com.datnt.remitextart.data;

import android.content.Context;

import com.datnt.remitextart.model.DecorModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataDecor {

    public static ArrayList<DecorModel> getTitleDecor(Context context, String name) {
        ArrayList<DecorModel> lstDecor = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("decor/decor_" + name);
            for (String s : f)
                lstDecor.add(new DecorModel(s, "decor_" + name, getPathDataDecor(context, s),
                        null, null, 255, false, false, false, null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstDecor;
    }

    public static ArrayList<DecorModel> getDataDecor(Context context, String name) {
        ArrayList<DecorModel> lstDecor = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("decor/json_" + name);
            for (String s : f)
                lstDecor.add(new DecorModel(s, "json" + name, getPathDataDecor(context, s),
                        null, null, 255, false, false, false, null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstDecor;
    }

    public static ArrayList<String> getPathDataDecor(Context context, String nameDecor) {
        String tContents = "", nameFolder = "";
        if (nameDecor.contains("box")) nameFolder = "json_box/";
        if (nameDecor.contains("draw")) nameFolder = "json_draw/";
        if (nameDecor.contains("frame")) nameFolder = "json_frame/";
        if (nameDecor.contains("shape")) nameFolder = "json_shape/";
        try {
            InputStream stream = context.getAssets().open("decor/" + nameFolder + nameDecor.replace(".png", ".json"));
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException ignored) {
        }
        if (!tContents.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(tContents, type);
        }
        return new ArrayList<>();
    }
}
