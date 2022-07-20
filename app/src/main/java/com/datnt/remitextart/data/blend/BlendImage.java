package com.datnt.remitextart.data.blend;

import android.graphics.Bitmap;

import com.datnt.remitextart.customsticker.imgpro.actions.Blend;
import com.datnt.remitextart.model.BlendModel;

import java.util.ArrayList;

public class BlendImage {

    public static ArrayList<BlendModel> getDataBlend(Bitmap bitmap) {
        ArrayList<BlendModel> lstBlend = new ArrayList<>();

        String type = "";
        Bitmap bm = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (Blend.Mode mode : MODES_BLEND) {
            if (mode != Blend.Mode.NORMAL)
                new Blend(0.5f, mode).adjustBitmap(bm);

            if (Blend.Mode.NORMAL.equals(mode)) type = "None";
            if (Blend.Mode.DARKEN.equals(mode)) type = "Darken";
            if (Blend.Mode.MULTIPLY.equals(mode)) type = "Multiply";
            if (Blend.Mode.LIGHTEN.equals(mode)) type = "Lighten";
            if (Blend.Mode.SCREEN.equals(mode)) type = "Screen";
            if (Blend.Mode.OVERLAY.equals(mode)) type = "Overlay";
            if (Blend.Mode.SOFT_LIGHT.equals(mode)) type = "Soft Light";
            if (Blend.Mode.HARD_LIGHT.equals(mode)) type = "Hard Light";
            if (Blend.Mode.DIFFERENCE.equals(mode)) type = "Difference";
            if (Blend.Mode.EXCLUSION.equals(mode)) type = "Exclusion";
            if (Blend.Mode.COLOR_DOGE.equals(mode)) type = "Color Doge";

            lstBlend.add(new BlendModel(bm, type, mode, false));
        }
        return lstBlend;
    }

    public static final Blend.Mode[] MODES_BLEND = {
            Blend.Mode.NORMAL,
            Blend.Mode.DARKEN,
            Blend.Mode.MULTIPLY,
            Blend.Mode.LIGHTEN,
            Blend.Mode.SCREEN,
            Blend.Mode.OVERLAY,
            Blend.Mode.SOFT_LIGHT,
            Blend.Mode.HARD_LIGHT,
            Blend.Mode.DIFFERENCE,
            Blend.Mode.EXCLUSION,
            Blend.Mode.COLOR_DOGE,
    };
}
