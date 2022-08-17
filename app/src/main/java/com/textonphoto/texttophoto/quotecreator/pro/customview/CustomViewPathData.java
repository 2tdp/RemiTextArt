package com.textonphoto.texttophoto.quotecreator.pro.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.textonphoto.texttophoto.quotecreator.pro.utils.UtilsBitmap;

import java.util.List;

public class CustomViewPathData extends View {

    private Path path;
    private Paint paint;
    private RectF rectF;

    public CustomViewPathData(Context context) {
        super(context);
        init();
    }

    public CustomViewPathData(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomViewPathData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.computeBounds(rectF, true);
        int y = (int) (getWidth() * 0.5f * rectF.height() / rectF.width());
        UtilsBitmap.drawIconWithPath(canvas, path, paint, getWidth() / 2f, getWidth() / 4, (getHeight() - y) / 2);
    }

    public void setDataPath(List<String> lstPath, boolean isDecor, boolean isTemp) {
        path.reset();
        for (String pathData : lstPath) {
            path.addPath(PathParser.createPathFromPathData(pathData));
        }
        if (isDecor) {
            paint.setColor(Color.GRAY);
        } else if (isTemp) {
            paint.setColor(Color.WHITE);
        }

        invalidate();
    }
}
