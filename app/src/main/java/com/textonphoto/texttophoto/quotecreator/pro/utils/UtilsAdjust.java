package com.textonphoto.texttophoto.quotecreator.pro.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;

import com.textonphoto.texttophoto.quotecreator.pro.model.ColorModel;
import com.textonphoto.texttophoto.quotecreator.pro.model.background.AdjustModel;

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

    public static Bitmap adjust(Bitmap bitmap, AdjustModel adjust) {
        if (adjust.getBrightness() != 0f) return adjustBrightness(bitmap, adjust);
        else if (adjust.getContrast() != 0f) return adjustContrast(bitmap, adjust);
        else if (adjust.getExposure() != 0f) return adjustExposure(bitmap, adjust);
        else if (adjust.getHighlight() != 0f) return adjustHighLight(bitmap, adjust);
        else if (adjust.getShadows() != 0f) return adjustShadow(bitmap, adjust);
        else if (adjust.getBlacks() != 0f) return adjustBlacks(bitmap, adjust);
        else if (adjust.getWhites() != 0f) return adjustWhites(bitmap, adjust);
        else if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustBrightness(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustBrightness: " + adjust.getBrightness());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust brightness 0.5", adjust.getBrightness() / 100f);

        if (adjust.getContrast() != 0f) return adjustContrast(bitmap, adjust);
        else if (adjust.getExposure() != 0f) return adjustExposure(bitmap, adjust);
        else if (adjust.getHighlight() != 0f) return adjustHighLight(bitmap, adjust);
        else if (adjust.getShadows() != 0f) return adjustShadow(bitmap, adjust);
        else if (adjust.getBlacks() != 0f) return adjustBlacks(bitmap, adjust);
        else if (adjust.getWhites() != 0f) return adjustWhites(bitmap, adjust);
        else if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustContrast(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustContrast: " + adjust.getContrast());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust contrast 2", adjust.getContrast() / 100f);

        if (adjust.getExposure() != 0f) return adjustExposure(bitmap, adjust);
        else if (adjust.getHighlight() != 0f) return adjustHighLight(bitmap, adjust);
        else if (adjust.getShadows() != 0f) return adjustShadow(bitmap, adjust);
        else if (adjust.getBlacks() != 0f) return adjustBlacks(bitmap, adjust);
        else if (adjust.getWhites() != 0f) return adjustWhites(bitmap, adjust);
        else if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustExposure(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustExposure: " + adjust.getExposure());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust exposure 0.62", adjust.getExposure() / 100f);

        if (adjust.getHighlight() != 0f) return adjustHighLight(bitmap, adjust);
        else if (adjust.getShadows() != 0f) return adjustShadow(bitmap, adjust);
        else if (adjust.getBlacks() != 0f) return adjustBlacks(bitmap, adjust);
        else if (adjust.getWhites() != 0f) return adjustWhites(bitmap, adjust);
        else if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustHighLight(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustHighLight: " + adjust.getHighlight());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust shadowhighlight 0 200", adjust.getHighlight() / 100);

        if (adjust.getShadows() != 0f) return adjustShadow(bitmap, adjust);
        else if (adjust.getBlacks() != 0f) return adjustBlacks(bitmap, adjust);
        else if (adjust.getWhites() != 0f) return adjustWhites(bitmap, adjust);
        else if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustShadow(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustShadow: " + adjust.getShadows());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust shadowhighlight -99 0", adjust.getShadows() / 100);

        if (adjust.getBlacks() != 0f) return adjustBlacks(bitmap, adjust);
        else if (adjust.getWhites() != 0f) return adjustWhites(bitmap, adjust);
        else if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustBlacks(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustBlacks: " + adjust.getBlacks());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@selcolor black(-50, -50, -50, -50)", -adjust.getBlacks() / 100f);

        if (adjust.getWhites() != 0f) return adjustWhites(bitmap, adjust);
        else if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustWhites(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustWhites: " + adjust.getWhites());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@selcolor white(62, 62, 62, 62)", -adjust.getWhites() / 100f);

        if (adjust.getSaturation() != 0f) return adjustSaturation(bitmap, adjust);
        else if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustSaturation(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustSaturation: " + adjust.getSaturation());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust saturation 2", adjust.getSaturation() / 100f);

        if (adjust.getHue() != 0f) return adjustHue(bitmap, adjust);
        else if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustHue(Bitmap bitmap, AdjustModel adjust) {
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
        Log.d(TAG, "adjustHue: " + adjust.getHue());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust hsl -0.66 0.34 0.15", adjust.getHue() / 100f);

        if (adjust.getWarmth() != 0f) return adjustWarmth(bitmap, adjust);
        else if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustWarmth(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustWarmth: " + adjust.getWarmth());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust colorbalance 0.34 0.2 -0.49", adjust.getWarmth() / 100f);

        if (adjust.getVibrance() != 0f) return adjustVibrance(bitmap, adjust);
        else if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustVibrance(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustVibrance: " + adjust.getVibrance());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@adjust saturation 0 @adjust level 0 0.83921 0.8772", -adjust.getVibrance() / 100f);

        if (adjust.getVignette() != 0f) return adjustVignette(bitmap, adjust);
        else return bitmap;
    }

    public static Bitmap adjustVignette(Bitmap bitmap, AdjustModel adjust) {
        Log.d(TAG, "adjustVignette: " + adjust.getVignette());
        return CGENativeLibrary.filterImage_MultipleEffects(bitmap, "@vignette 0.1 0.9", -adjust.getVignette() / 100f);
    }

    protected static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
}
