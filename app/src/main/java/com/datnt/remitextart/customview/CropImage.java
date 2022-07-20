package com.datnt.remitextart.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.datnt.remitextart.data.FilterImage;
import com.datnt.remitextart.model.image.ImageModel;
import com.datnt.remitextart.utils.Utils;

import org.wysaid.nativePort.CGENativeLibrary;

public class CropImage extends View implements MatrixGestureDetector.OnMatrixChangeListener {

    private Path path;
    private Paint paintLine, paintBitmap;
    private RectF rectF;
    private Bitmap bitmap;
    private final Matrix matrix = new Matrix();
    private final Matrix maskMatrix = new Matrix();
    private MatrixGestureDetector detector;
    private final RectF clip = new RectF();

    private int y = 1;
    private float scale = 1;

    public CropImage(Context context) {
        super(context);
        init();
    }

    public CropImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(Color.parseColor("#E9E9E9"));
        paintLine.setStrokeWidth(5);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        paintLine.setStrokeJoin(Paint.Join.ROUND);

        rectF = new RectF();

        detector = new MatrixGestureDetector(maskMatrix, this);

        paintBitmap = new Paint(Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(getWidth() / 3f, 0, getWidth() / 3f, getHeight(), paintLine);
        canvas.drawLine(2 * getWidth() / 3f, 0, 2 * getWidth() / 3f, getHeight(), paintLine);
        canvas.drawLine(0, 0, getWidth(), 0, paintLine);
        canvas.drawLine(0, getHeight() / 3f, getWidth(), getHeight() / 3f, paintLine);
        canvas.drawLine(0, 2 * getHeight() / 3f, getWidth(), 2 * getHeight() / 3f, paintLine);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paintLine);

        if (rectF != null) {
            canvas.translate(getWidth() / 5f, (getHeight() - y) / 4f);
            canvas.scale(scale, scale);
            canvas.clipPath(path);
        }

        if (bitmap != null) {
            canvas.scale(1 / scale, 1 / scale);
            canvas.drawBitmap(bitmap, maskMatrix, paintBitmap);
        }
    }

    public String getBitmapCreate(Context context, String nameFolderImage) {

        Bitmap bm = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);

        if (rectF != null) {
            canvas.translate(getWidth() / 5f, (getHeight() - y) / 4f);
            canvas.scale(scale, scale);
            canvas.clipPath(path);
        }

        if (bitmap != null) {
            canvas.scale(1 / scale, 1 / scale);
            canvas.drawBitmap(bitmap, maskMatrix, paintBitmap);
        }

        return Utils.saveBitmapToApp(context, Utils.trim(bm), nameFolderImage, Utils.IMAGE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        if (bitmap != null) {
            RectF src = new RectF(0, 0, bitmap.getWidth() * 2, bitmap.getHeight());
            RectF dst = new RectF(0, 0, w, h);
            matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            matrix.mapRect(dst, src);

            maskMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            setupClip();
        }
    }

    public void setBitmap(ImageModel imageModel) {

        this.bitmap = BitmapFactory.decodeFile(imageModel.getUriRoot());

        if (imageModel.getPosFilter() != 0)
            this.bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap,
                    FilterImage.EFFECT_CONFIGS[imageModel.getPosFilter()], 0.8f);

        paintBitmap.setAlpha(imageModel.getOpacity());
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (bitmap != null)
            detector.onTouchEvent(event);
        return true;
    }

    private void setupClip() {
        clip.set(0, 0, getWidth(), getHeight());
        maskMatrix.mapRect(clip);
    }

    @Override
    public void onChange(Matrix matrix) {
        setupClip();
        invalidate();
    }

    public void setPath(String o) {
        path.reset();
        path.addPath(PathParser.createPathFromPathData(o));

        path.computeBounds(rectF, true);
        scale = 3 * getWidth() / (rectF.width() * 5f);
        y = (int) (getWidth() * 0.5f * rectF.height() / rectF.width());

        invalidate();
    }
}
