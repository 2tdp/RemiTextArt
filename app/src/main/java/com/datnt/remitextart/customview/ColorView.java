package com.datnt.remitextart.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.utils.UtilsAdjust;

public class ColorView extends View {

    private Path path;
    private Paint paint;
    private ColorModel color;
    private int size;
    private float w, h, width, height;

    public ColorView(Context context) {
        super(context);
        init();
    }

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (paint == null) paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (path == null) path = new Path();

        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0 || getHeight() == 0) return;

        w = getWidth();
        h = getHeight();

        if (color != null) {
            setSizeColor(size);
            resetColor();
            canvas.drawPath(path, paint);
        }
    }

    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidate();
    }

    public float getW() {
        return width;
    }

    public float getH() {
        return height;
    }

    public void setData(ColorModel color) {
        this.color = color;
        invalidate();
    }

    public void setSize(int size) {
        this.size = size;
        invalidate();
    }

    public int getSize() {
        return size;
    }

    private void setSizeColor(int pos) {
        path.reset();
        switch (pos) {
            case 1:
                setPath(1f);
                break;
            case 2:
                setPath(9 / 16f);
                break;
            case 3:
                setPath(4 / 5f);
                break;
            case 4:
                setPath(16 / 9f);
                break;
            default:
                path.addRect(0, 0, w, h, Path.Direction.CW);
                width = w;
                height = h;
                break;
        }
        invalidate();
    }

    private void setPath(float scale) {
        if (w / h >= scale) {
            path.addRect((w * (1 - scale)) / 2, 0, (w * (1 + scale)) / 2, h, Path.Direction.CW);
            width = scale * w;
            height = h;
        } else if (w / h < scale) {
            path.addRect(0, (h - w / scale) / 2, w, (h + w / scale) / 2, Path.Direction.CW);
            width = w;
            height = w / scale;
        }
    }

    private void resetColor() {
        if (color != null)
            if (color.getColorStart() == color.getColorEnd()) {
                paint.setColor(color.getColorStart());
            } else {
                if (color.getDirec() == 4) {
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

                Shader shader = new LinearGradient(setDirection(color.getDirec())[0],
                        setDirection(color.getDirec())[1],
                        setDirection(color.getDirec())[2],
                        setDirection(color.getDirec())[3],
                        new int[]{Color.parseColor(UtilsAdjust.toRGBString(color.getColorStart())), Color.parseColor(UtilsAdjust.toRGBString(color.getColorEnd()))},
                        new float[]{0, 1}, Shader.TileMode.MIRROR);

                paint.setShader(shader);
            }
    }

    private int[] setDirection(int direc) {
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
        return new int[]{0, 0, 0, 0};
    }
}
