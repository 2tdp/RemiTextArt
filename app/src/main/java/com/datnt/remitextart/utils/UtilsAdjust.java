package com.datnt.remitextart.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.background.AdjustModel;

import org.wysaid.nativePort.CGENativeLibrary;

public class UtilsAdjust {

    private static final float lumR = 0.3086f; // or  0.2125
    private static final float lumG = 0.6094f;  // or  0.7154
    private static final float lumB = 0.0820f; // or  0.0721
    private static final String TAG = "2tdp";

    public static void setColor(ColorModel color, Paint paint, float w, float h) {
        if (color.getColorStart() == color.getColorEnd()) {
            paint.setShader(null);
            paint.setColor(color.getColorStart());
        } else if (color.getDirec() == 4) {
            int c = color.getColorStart();
            color.setColorStart(color.getColorEnd());
            color.setColorEnd(c);

            color.setDirec(0);
        } else if (color.getDirec() == 5) {
            int c = color.getColorStart();
            color.setColorStart(color.getColorEnd());
            color.setColorEnd(c);

            color.setDirec(2);
        }

        Shader shader = new LinearGradient(setDirection(color.getDirec(), w, h)[0],
                setDirection(color.getDirec(), w, h)[1],
                setDirection(color.getDirec(), w, h)[2],
                setDirection(color.getDirec(), w, h)[3],
                new int[]{Color.parseColor(UtilsBitmap.toRGBString(color.getColorStart())), Color.parseColor(UtilsBitmap.toRGBString(color.getColorEnd()))},
                new float[]{0, 1}, Shader.TileMode.MIRROR);

        paint.setShader(shader);
    }

    public static int[] setDirection(int direc, float w, float h) {
        switch (direc) {
            case 0:
                return new int[]{(int) w / 2, 0, (int) w / 2, (int) h};
            case 1:
                return new int[]{0, 0, (int) w, (int) h};
            case 2:
                return new int[]{0, (int) h / 2, (int) w, (int) h / 2};
            case 3:
                return new int[]{0, (int) h, (int) w, 0};
        }
        return new int[]{};
    }

    public static Bitmap adjustBrightness(Bitmap bmp, float brightness) {
        Log.d(TAG, "adjustBrightness: " + brightness);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust brightness 0.5".replace("0.5", String.valueOf(brightness / 100f)), 1.0f);
    }

    public static Bitmap adjustContrast(Bitmap bmp, float contrast) {
        Log.d(TAG, "adjustContrast: " + contrast);
        if (contrast < -25.0f)
            return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust contrast 2".replace("2", String.valueOf((contrast + 25f) / 25.0f)), 1.0f);
        else if (contrast == 25.0f)
            return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust contrast 2".replace("2", "0"), 1.0f);
        else if (contrast < 0f)
            return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust contrast 2".replace("2", String.valueOf(contrast / -75.0f)), 1.0f);
        else
            return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust contrast 2".replace("2", String.valueOf(contrast / 75.0f)), 1.0f);
    }

    public static Bitmap adjustExposure(Bitmap bmp, float exposure) {
        Log.d(TAG, "adjustExposure: " + exposure);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust exposure 0.62", exposure / 100f);
    }

    public static Bitmap adjustHighLight(Bitmap bmp, float highLight) {
        Log.d(TAG, "adjustHighLight: " + highLight);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust shadowhighlight 0 200", highLight / 100);
    }

    public static Bitmap adjustShadow(Bitmap bmp, float shadow) {
        Log.d(TAG, "adjustShadow: " + shadow);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust shadowhighlight -99 0", shadow / 100);
    }

    public static Bitmap adjustBlacks(Bitmap bmp, float blacks) {
        Log.d(TAG, "adjustBlacks: " + blacks);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@selcolor black(-50, -50, -50, -50)", -blacks / 100f);
    }

    public static Bitmap adjustWhites(Bitmap bmp, float whites) {
        Log.d(TAG, "adjustWhites: " + whites);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@selcolor white(62, 62, 62, 62)", -whites / 100f);
    }

    public static Bitmap adjustSaturation(Bitmap bmp, float saturation) {
        Log.d(TAG, "adjustSaturation: " + saturation);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust saturation 2", saturation / 100f);
    }

    public static Bitmap adjustHue(Bitmap bmp, float hue) {
//        value = cleanValue(value, 360f) / 360f * (float) Math.PI;
//        if (value == 0) {
//            return bmp;
//        }
//        float cosVal = (float) Math.cos(value);
//        float sinVal = (float) Math.sin(value);
//
//        ColorMatrix cm = new ColorMatrix(new float[]
//                {
//                        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
//                        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
//                        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,
//                        0f, 0f, 0f, 1f, 0f});
//
//        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
//
//        Canvas canvas = new Canvas(ret);
//
//        Paint paint = new Paint();
//        paint.setColorFilter(new ColorMatrixColorFilter(cm));
//        canvas.drawBitmap(bmp, 0, 0, paint);
//
//        return ret;
        Log.d(TAG, "adjustHue: " + hue);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust hsl -0.66 0.34 0.15", hue / 100f);
    }

    public static Bitmap adjustWarmth(Bitmap bmp, float warmth) {
        Log.d(TAG, "adjustWarmth: " + warmth);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust colorbalance 0.34 0.2 -0.49", warmth / 100f);
    }

    public static Bitmap adjustVibrance(Bitmap bmp, float vibrance) {
        Log.d(TAG, "adjustVibrance: " + vibrance);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust saturation 0 @adjust level 0 0.83921 0.8772", -vibrance / 100f);
    }

    public static Bitmap adjustVignette(Bitmap bmp, float vignette) {
        Log.d(TAG, "adjustVignette: " + vignette);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@vignette 0.1 0.9", -vignette / 100f);
    }

    protected static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
}
