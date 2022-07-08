package com.datnt.remitextart.customview.stickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;

import java.util.ArrayList;

/**
 * @author wupanjie
 */
public class DrawableSticker extends Sticker {

    private final Path path;
    private final Paint paint, paintShadow;
    private Drawable drawable;
    private ArrayList<String> lstPathData;
    private int id, colorShadow = 0;
    private float dx = 0f, dy = 0f, radiusBlur = 0f;
    private boolean isImage, isOverlay, isDecor, isTemplate, isShadow;
    private RectF realBounds;

    public DrawableSticker(Context context, Drawable drawable, ArrayList<String> lstPathData, int id, boolean isImage, boolean isOverlay, boolean isDecor, boolean isTemplate) {
        this.drawable = drawable;
        if (drawable == null) {
            this.drawable = Utils.getDrawableTransparent(context);
        }
        this.lstPathData = lstPathData;
        this.id = id;
        this.isImage = isImage;
        this.isOverlay = isOverlay;
        this.isDecor = isDecor;
        this.isTemplate = isTemplate;

        path = new Path();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShadow.setStrokeJoin(Paint.Join.ROUND);
        paintShadow.setStrokeCap(Paint.Cap.ROUND);

        if (isDecor) paint.setColor(Color.GRAY);
        else if (isTemplate) paint.setColor(Color.WHITE);

        if (!lstPathData.isEmpty()) {
            for (String pathData : lstPathData) {
                path.addPath(PathParser.createPathFromPathData(pathData));
            }
        }

        realBounds = new RectF(0, 0, getWidth(), getHeight());
    }

    @NonNull
    public Drawable getDrawable() {
        return drawable;
    }

    public DrawableSticker setDrawable(@Nullable Drawable drawable) {
        this.drawable = drawable;
        if (drawable != null)
            realBounds = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        if (isShadow)
            UtilsAdjust.drawIconWithPath(canvas, path, paintShadow, realBounds.width(), 0, 0);

        UtilsAdjust.drawIconWithPath(canvas, path, paint, realBounds.width(), 0, 0);

        drawable.setBounds((int) realBounds.left, (int) realBounds.top, (int) realBounds.right, (int) realBounds.bottom);
        drawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    @Override
    public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        if (isDecor || isTemplate)
            paint.setAlpha(alpha);
        else
            drawable.setAlpha(alpha);
        return this;
    }

    public void setPathData(ArrayList<String> lstPath) {
        this.lstPathData = lstPath;
        path.reset();
        for (String pathData : lstPath) {
            path.addPath(PathParser.createPathFromPathData(pathData));
        }

        path.computeBounds(realBounds, true);
    }

    public ArrayList<String> getListPathData() {
        return lstPathData;
    }

    public void setShadow(float radiusBlur, float dx, float dy, int color, boolean isDecorOrTemp) {
        isShadow = true;
        this.colorShadow = color;
        this.dx = dx;
        this.dy = dy;
        this.radiusBlur = radiusBlur;

        if (!isDecorOrTemp)
            path.addRect(0, 0, realBounds.width(), realBounds.height(), Path.Direction.CW);

        if (color == 0) paintShadow.setShadowLayer(radiusBlur, dx, dy, Color.BLACK);
        else
            paintShadow.setShadowLayer(radiusBlur, dx, dy, Color.parseColor(UtilsAdjust.toRGBString(color)));
    }

    public void setColor(ColorModel color) {
        paint.setColor(color.getColorStart());
    }

    public int getColorShadow() {
        return colorShadow;
    }

    public float getRadiusBlur() {
        return radiusBlur;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public int getAlpha() {
        if (isDecor || isTemplate) return paint.getAlpha();
        else return drawable.getAlpha();
    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        isTemplate = template;
    }

    public boolean isDecor() {
        return isDecor;
    }

    public void setDecor(boolean decor) {
        isDecor = decor;
    }

    public boolean isOverlay() {
        return isOverlay;
    }

    public void setOverlay(boolean overlay) {
        isOverlay = overlay;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }
}
