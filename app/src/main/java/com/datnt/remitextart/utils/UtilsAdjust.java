package com.datnt.remitextart.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import com.datnt.remitextart.model.ColorModel;

import org.wysaid.nativePort.CGENativeLibrary;

public class UtilsAdjust {

    private static final float lumR = 0.3086f; // or  0.2125
    private static final float lumG = 0.6094f;  // or  0.7154
    private static final float lumB = 0.0820f; // or  0.0721
    private static final String TAG = "2tdp";

    public static void drawIconWithPath(Canvas canvas, Path path, Paint paint, float size, int x, int y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        float scale = size / rectF.width();
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    public static Bitmap createFlippedBitmap(Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap changeBitmapColor(Bitmap sourceBitmap, ColorModel color) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color.getColorStart(), 1);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }

    public static String toRGBString(int color) {
        // format: #RRGGBB
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (red.length() == 1)
            red = "0" + red;
        if (green.length() == 1)
            green = "0" + green;
        if (blue.length() == 1)
            blue = "0" + blue;
        return "#" + red + green + blue;
    }

    public static Bitmap adjustBrightness(Bitmap bmp, float brightness) {
        Log.d(TAG, "adjustBrightness: " + brightness);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust brightness 0.5", brightness / 100f);
    }

    public static Bitmap adjustContrast(Bitmap bmp, float contrast) {
        Log.d(TAG, "adjustContrast: " + contrast);
        return CGENativeLibrary.filterImage_MultipleEffects(bmp, "@adjust contrast 2", contrast / 100f);
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

    public static Bitmap adjustHue(Bitmap bmp, float value) {
        value = cleanValue(value, 360f) / 360f * (float) Math.PI;
        if (value == 0) {
            return bmp;
        }
        float cosVal = (float) Math.cos(value);
        float sinVal = (float) Math.sin(value);

        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,
                        0f, 0f, 0f, 1f, 0f});

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
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
