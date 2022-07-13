package com.datnt.remitextart.customview.cropimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import com.edmodo.cropper.CropImageView;

public class CropImageCustomView extends CropImageView {

    private CropOverlayCustomView cropOverlay;

    public CropImageCustomView(Context context) {
        super(context);
        init(context);
    }

    public CropImageCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        cropOverlay = new CropOverlayCustomView(context);
    }

    public void setPath(String pathShape) {
        if (!pathShape.equals(""))
            cropOverlay.setPathShape(pathShape);
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) setImageBitmap(bitmap);

        setFixedAspectRatio(true);
        setGuidelines(2);

        invalidate();
    }
}
