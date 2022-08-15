package com.datnt.remitextart.data.blend;

import android.graphics.Bitmap;
import com.datnt.remitextart.model.BlendModel;

import org.wysaid.nativePort.CGENativeLibrary;

import java.util.ArrayList;

public class BlendImage {

    public static ArrayList<BlendModel> getDataBlend(Bitmap bitmap) {
        ArrayList<BlendModel> lstBlend = new ArrayList<>();

        String type = "";
        Bitmap bm;
        for (String s : BLEND_NAMES) {
            bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, "@blend lut " + s, 0.5f);
            if (bm != null) {

                if (s.equals("")) type = "None";
                if (s.equals("blend_darken.png")) type = "Darken";
                if (s.equals("blend_multiply.png")) type = "Multiply";
                if (s.equals("blend_lighten.png")) type = "Lighten";
                if (s.equals("blend_screen.png")) type = "Screen";
                if (s.equals("blend_overlay.png")) type = "Overlay";
                if (s.equals("blend_soft_light.png")) type = "Soft Light";
                if (s.equals("blend_hard_light.png")) type = "Hard Light";
                if (s.equals("blend_difference.png")) type = "Difference";
                if (s.equals("blend_exclusion.png")) type = "Exclusion";
                if (s.equals("blend_color_doge.png")) type = "Color Doge";

                lstBlend.add(new BlendModel(bm, type, s, false));
            }
        }
        return lstBlend;
    }

    public static final String[] BLEND_NAMES = {
      "",
      "blend_darken.png",
      "blend_multiply.png",
      "blend_lighten.png",
      "blend_screen.png",
      "blend_overlay.png",
      "blend_soft_light.png",
      "blend_hard_light.png",
      "blend_difference.png",
      "blend_exclusion.png",
      "blend_color_doge.png",
    };
}
