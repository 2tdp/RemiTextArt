package com.textonphoto.texttophoto.quotecreator.pro.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.textonphoto.texttophoto.quotecreator.pro.model.TemplateModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataTemplate {

    public static ArrayList<TemplateModel> getTemplate(Context context, String name) {
        ArrayList<TemplateModel> lstTemp = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("template/template" + name);
            for (String s : f) {
                String background = "template_background" + s.split("template")[1];
                String text = "json_temp_text" + s.split("template")[1].replace(".webp", ".json");
                lstTemp.add(new TemplateModel(s, background, getPathDataTemp(context, text),
                        null, null, 255, false, false, false, true, null));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstTemp;
    }

    public static ArrayList<String> getPathDataTemp(Context context, String nameDecor) {
        String tContents = "";
        try {
            InputStream stream = context.getAssets().open("template/template_text/" + nameDecor);
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
