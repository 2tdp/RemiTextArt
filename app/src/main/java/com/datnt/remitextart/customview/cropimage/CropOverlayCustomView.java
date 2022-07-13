package com.datnt.remitextart.customview.cropimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.core.graphics.PathParser;

import com.edmodo.cropper.cropwindow.CropOverlayView;
import com.edmodo.cropper.util.PaintUtil;

public class CropOverlayCustomView extends CropOverlayView {

    private Path pathShape;
    private Paint paintShape;

    public CropOverlayCustomView(Context context) {
        super(context);
        init(context);
    }

    public CropOverlayCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        pathShape = new Path();
        paintShape = PaintUtil.newBorderPaint(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(pathShape, paintShape);
    }

    public void setPathShape(String pathShape){
        this.pathShape.reset();
        this.pathShape.addPath(PathParser.createPathFromPathData(pathShape));

        invalidate();
    }
}
