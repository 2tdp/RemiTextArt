package com.datnt.remitextart.customsticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathParser;

import com.datnt.remitextart.R;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.data.FilterBlendImage;
import com.datnt.remitextart.model.EmojiModel;
import com.datnt.remitextart.model.image.ImageModel;
import com.datnt.remitextart.model.ShadowModel;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;

import org.wysaid.nativePort.CGENativeLibrary;

public class DrawableStickerCustom extends Sticker {

    private Context context;
    private Drawable drawable;
    private Bitmap bitmap;
    private RectF realBounds, rectFShadow;
    private Path shadowImagePath;
    private Paint shadowImagePaint;
    private boolean isShadowImage, isShadowCrop;
    private int id;
    private final String typeSticker;

    private float scale = 1f;

    private EmojiModel emojiModel;
    private ImageModel imageModel;

    public DrawableStickerCustom(Context context, Object o, int id, String type) {
        this.context = context;
        this.typeSticker = type;
        this.id = id;
        switch (type) {
            case Utils.EMOJI:
                this.emojiModel = (EmojiModel) o;
                initEmoji();
                break;
            case Utils.IMAGE:
                this.imageModel = (ImageModel) o;
                initImage();
                break;
        }
    }

    private void initEmoji() {
        this.drawable = new BitmapDrawable(context.getResources(),
                Utils.getBitmapFromAsset(context, emojiModel.getFolder(), emojiModel.getNameEmoji(),
                        true, false));

        realBounds = new RectF(10, 10, getWidth() - 10, getHeight() - 10);
    }

    private void initImage() {
        if (this.drawable == null)
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text);

        setDataImage(this.imageModel);
        this.drawable = new BitmapDrawable(context.getResources(), bitmap);

        realBounds = new RectF(10, 10, getWidth() - 10, getHeight() - 10);
    }

    public void setDataImage(ImageModel imageModel) {
        bitmap = BitmapFactory.decodeFile(imageModel.getUri());

        if (imageModel.getPosFilter() != 0) setFilterImage(imageModel.getPosFilter());

        setAlpha(imageModel.getOpacity());

        if (!imageModel.getPathShape().equals(""))
            setShadowPathShapeImage(imageModel.getPathShape());

        if (imageModel.getShadowModel() != null) setShadowImage(imageModel.getShadowModel());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        if (isShadowImage && this.typeSticker.equals(Utils.IMAGE)) {
            if (isShadowCrop) {
                canvas.save();
                canvas.concat(getMatrix());
                canvas.translate(2 * realBounds.left, 2 * realBounds.top);
                canvas.scale(scale, scale);
                canvas.drawPath(shadowImagePath, shadowImagePaint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.concat(getMatrix());
                canvas.drawRect(realBounds, shadowImagePaint);
                canvas.restore();
            }
        }

        canvas.save();
        canvas.concat(getMatrix());

        drawable.setBounds((int) realBounds.left, (int) realBounds.top, (int) realBounds.right, (int) realBounds.bottom);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public DrawableStickerCustom setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        realBounds = new RectF(10, 10, drawable.getIntrinsicWidth() - 10, drawable.getIntrinsicHeight() - 10);
        return this;
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @NonNull
    @Override
    public DrawableStickerCustom setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
        if (shadowImagePaint != null)
            shadowImagePaint.setAlpha(alpha);
        return this;
    }

    public void setFilterImage(int positionFilter) {
        bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, FilterBlendImage.EFFECT_CONFIGS[positionFilter], 0.8f);
    }

    public void setShadowPathShapeImage(String pathShape) {
        if (rectFShadow == null) rectFShadow = new RectF();
        if (this.shadowImagePath == null) this.shadowImagePath = new Path();
        else shadowImagePath.reset();
        if (this.shadowImagePaint == null) this.shadowImagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.shadowImagePath.addPath(PathParser.createPathFromPathData(pathShape));

        shadowImagePath.computeBounds(rectFShadow, true);
        scale = (drawable.getIntrinsicWidth() - realBounds.left) / rectFShadow.width();
    }

    public void setShadowImage(ShadowModel shadow) {
        setShadowImage(true);
        isShadowCrop = !imageModel.getPathShape().equals("");

        if (shadow != null) {
            if (shadow.getColorBlur() == 0f && shadow.getBlur() == 0f
                    && shadow.getXPos() == 0f && shadow.getYPos() == 0f) {
                this.shadowImagePaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(), Color.TRANSPARENT);
            }
            if (shadow.getColorBlur() != 0f) {
                this.shadowImagePaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        shadow.getColorBlur());
            } else
                this.shadowImagePaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(), Color.BLACK);
        }
    }

    public void replaceImage() {
        initImage();
    }

    public boolean isShadowImage() {
        return isShadowImage;
    }

    public void setShadowImage(boolean shadowImage) {
        this.isShadowImage = shadowImage;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public EmojiModel getEmojiModel() {
        return emojiModel;
    }

    public void setEmojiModel(EmojiModel emojiModel) {
        this.emojiModel = emojiModel;
        initEmoji();
    }

    public String getTypeSticker() {
        return typeSticker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
