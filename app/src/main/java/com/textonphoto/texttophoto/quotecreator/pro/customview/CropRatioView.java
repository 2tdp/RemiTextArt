package com.textonphoto.texttophoto.quotecreator.pro.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.edmodo.cropper.CropImageView;

public class CropRatioView extends CropImageView {

    private Bitmap bmRoot;
    private int size = -1;

    public CropRatioView(Context context) {
        super(context);
    }

    public CropRatioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSize(int pos) {
        this.size = pos;
        switch (pos) {
            case 0:
                setAspectRatio(bmRoot.getWidth(), bmRoot.getHeight());
                break;
            case 1:
                setAspectRatio(1, 1);
                break;
            case 2:
                setAspectRatio(9, 16);
                break;
            case 3:
                setAspectRatio(4, 5);
                break;
            case 4:
                setAspectRatio(16, 9);
                break;
        }
        setFixedAspectRatio(true);
        setGuidelines(2);

        invalidate();
    }

    public void setData(Bitmap bitmap) {
        if (bitmap != null) {
            this.bmRoot = bitmap;
            setImageBitmap(bitmap);
        }
    }

    public int getSize() {
        return size;
    }

    public Bitmap getBitmap() {
        return bmRoot;
    }
}
