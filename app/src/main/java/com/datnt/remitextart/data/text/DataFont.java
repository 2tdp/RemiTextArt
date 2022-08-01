package com.datnt.remitextart.data.text;

import android.content.Context;

import com.datnt.remitextart.model.text.FontModel;
import com.datnt.remitextart.model.text.TypeFontModel;

import java.io.IOException;
import java.util.ArrayList;

public class DataFont {

    public static ArrayList<FontModel> getDataFont(Context context) {
        ArrayList<FontModel> lstFont = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("fonts");
            for (String s : f) {
                lstFont.add(new FontModel(s, getDataTypeFont(context, s), false, false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstFont;
    }

    public static ArrayList<TypeFontModel> getDataTypeFont(Context context, String nameFont) {
        ArrayList<TypeFontModel> lstStyle = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("fonts/" + nameFont);
            for (String s : f) {
                String[] arr = s.split("_");
                StringBuilder style = new StringBuilder();
                for (int i = 1; i < arr.length; i++) {
                    if (i == arr.length - 1) {
                        style.append(" ").append(arr[i].replace(".ttf", " "));
                    } else style.append(" ").append(arr[i]);

                    style = new StringBuilder(style.toString().substring(0, 2).toUpperCase() + style.toString().substring(2).toLowerCase());
                }
                lstStyle.add(new TypeFontModel(style.toString().trim(), nameFont, false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstStyle;
    }
}
