package com.textonphoto.texttophoto.quotecreator.pro.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import com.textonphoto.texttophoto.quotecreator.pro.customview.ColorView;
import com.textonphoto.texttophoto.quotecreator.pro.model.ColorModel;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UtilsBitmap {

    public static void drawIconWithPath(Canvas canvas, Path path, Paint paint, float size, int x, int y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        float scale = size / rectF.width();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        canvas.drawPath(path, paint);
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

    public static Bitmap trim(Bitmap source) {
        int firstX = 0, firstY = 0;
        int lastX = source.getWidth();
        int lastY = source.getHeight();
        int[] pixels = new int[source.getWidth() * source.getHeight()];
        source.getPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
        loop:
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    firstX = x;
                    break loop;
                }
            }
        }
        loop:
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = firstX; x < source.getWidth(); x++) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    firstY = y;
                    break loop;
                }
            }
        }
        loop:
        for (int x = source.getWidth() - 1; x >= firstX; x--) {
            for (int y = source.getHeight() - 1; y >= firstY; y--) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    lastX = x;
                    break loop;
                }
            }
        }
        loop:
        for (int y = source.getHeight() - 1; y >= firstY; y--) {
            for (int x = source.getWidth() - 1; x >= firstX; x--) {
                if (pixels[x + (y * source.getWidth())] != Color.TRANSPARENT) {
                    lastY = y;
                    break loop;
                }
            }
        }
        return Bitmap.createBitmap(source, firstX, firstY, lastX - firstX, lastY - firstY);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri selectedFileUri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String saveBitmapToApp(Context context, Bitmap bitmap, String nameFolder, String nameFile) {
        String directory = Utils.getStore(context) + "/" + nameFolder + "/";
        File myPath = new File(directory, nameFile + ".png");

        try {
            FileOutputStream fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return myPath.getPath();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
        return "";
    }

    public static Bitmap getBitmapFromAsset(Context context, String nameFolder, String name, boolean isEmoji, boolean isDecor) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            if (isEmoji) istr = assetManager.open("emoji/" + nameFolder + "/" + name);
            else if (isDecor) istr = assetManager.open("decor/" + nameFolder + "/" + name);
            else istr = assetManager.open(nameFolder + "/" + name);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap loadBitmapFromView(Context context, View view, boolean isColor) {
        Bitmap b = null;
        if (!isColor) {
            b = Bitmap.createBitmap(view.getLayoutParams().width, view.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

            Canvas c = new Canvas(b);

            c.rotate(view.getRotation(), (float) view.getWidth() / 2, (float) view.getHeight() / 2);
            view.draw(c);
            Log.d("2tdp", "view: w" + view.getLayoutParams().width + ".... h: " + view.getLayoutParams().height);
        } else {
            if (view instanceof ColorView) {
                ColorView color = new ColorView(context);
                ColorView v = (ColorView) view;
                b = Bitmap.createBitmap((int) v.getW(), (int) v.getH(), Bitmap.Config.ARGB_8888);
                color.setData(v.getData());
                color.setSize(0);
                color.layout(v.getLeft(), v.getTop(), (int) v.getW(), (int) v.getH());
                Log.d("2tdp", "color: w: " + color.getW() + ".... h: " + color.getH());
                Log.d("2tdp", "color: w: " + v.getW() + ".... h: " + v.getH());

                Canvas c = new Canvas(b);

//                c.rotate(color.getRotation(), (float) color.getWidth() / 2, (float) color.getHeight() / 2);
                color.draw(c);
            }
        }
        return b;
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);
        RectF rectF = new RectF(0, 0, bmp1.getWidth(), bmp1.getHeight());
        canvas.drawBitmap(bmp1, null, rectF, new Paint());
        canvas.drawBitmap(bmp2, null, rectF, new Paint());
        return bmOverlay;
    }

    public static Bitmap modifyOrientation(Context context, Bitmap bitmap, Uri uri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        ExifInterface ei = new ExifInterface(is);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                ivFilter.setRotation(180);
//                break;
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                return flip(bitmap, false, true);
            default:
                return bitmap;
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap setOpacityBitmap(Bitmap bitmap, int opacity) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint alphaPaint = new Paint();
        alphaPaint.setAlpha(opacity);
        canvas.drawBitmap(bitmap, 0, 0, alphaPaint);

        return newBitmap;
    }
}
