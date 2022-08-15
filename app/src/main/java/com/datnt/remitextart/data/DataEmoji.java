package com.datnt.remitextart.data;

import android.content.Context;

import com.datnt.remitextart.model.EmojiModel;

import java.io.IOException;
import java.util.ArrayList;

public class DataEmoji {

    public static ArrayList<EmojiModel> getTitleEmoji(Context context, String name) {
        ArrayList<EmojiModel> lstEmoji = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("emoji/emoji_" + name);
            for (String s : f) lstEmoji.add(new EmojiModel(s, "emoji_" + name, 255, false, false, false, false, true, null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstEmoji;
    }
}
