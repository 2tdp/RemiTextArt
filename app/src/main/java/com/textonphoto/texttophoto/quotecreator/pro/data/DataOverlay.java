package com.textonphoto.texttophoto.quotecreator.pro.data;

import android.content.Context;

import com.textonphoto.texttophoto.quotecreator.pro.model.OverlayModel;

import java.io.IOException;
import java.util.ArrayList;

public class DataOverlay {

    public static ArrayList<OverlayModel> getOverlay(Context context, String name) {
        ArrayList<OverlayModel> lstOverlay = new ArrayList<>();
        try {
            String[] f = context.getAssets().list(name);
            for (String s : f) lstOverlay.add(new OverlayModel(s, name, 255, false, false, false));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstOverlay;
    }
}
