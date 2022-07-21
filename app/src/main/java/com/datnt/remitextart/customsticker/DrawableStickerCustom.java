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
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathParser;

import com.datnt.remitextart.R;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.data.FilterImage;
import com.datnt.remitextart.model.DecorModel;
import com.datnt.remitextart.model.EmojiModel;
import com.datnt.remitextart.model.OverlayModel;
import com.datnt.remitextart.model.image.ImageModel;
import com.datnt.remitextart.model.ShadowModel;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;

import org.wysaid.nativePort.CGENativeLibrary;

public class DrawableStickerCustom extends Sticker {

    private final int distance = 10;

    private Context context;
    private Drawable drawable;
    private Bitmap bitmap;
    private RectF realBounds, rectFShadow, rectFDecor;
    private Path shadowPath, pathDecor;
    private Paint shadowPaint, paintDecor;
    private final Paint paintBitmap = new Paint(Paint.FILTER_BITMAP_FLAG);
    private boolean isShadowImage, isShadowCrop;
    private int id;
    private final String typeSticker;

    private float scaleX = 1f, scaleY = 1f;

    private EmojiModel emojiModel;
    private ImageModel imageModel;
    private OverlayModel overlayModel;
    private DecorModel decorModel;

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
            case Utils.OVERLAY:
                this.overlayModel = (OverlayModel) o;
                initOverlay();
                break;
            case Utils.DECOR:
                this.decorModel = (DecorModel) o;
                initDecor();
                break;
        }
    }

    private void initEmoji() {
        this.drawable = new BitmapDrawable(context.getResources(),
                Utils.getBitmapFromAsset(context, emojiModel.getFolder(), emojiModel.getNameEmoji(),
                        true, false));

        realBounds = new RectF(distance, distance, getWidth() - distance, getHeight() - distance);
    }

    private void initImage() {
        if (this.drawable == null)
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text);

        setDataImage(this.imageModel);

        realBounds = new RectF(distance, distance, bitmap.getWidth() - distance, bitmap.getHeight() - distance);
    }

    public void setDataImage(ImageModel imageModel) {
        bitmap = BitmapFactory.decodeFile(imageModel.getUri());

        this.drawable = new BitmapDrawable(context.getResources(), bitmap);

        if (imageModel.getPosFilter() != 0) setFilterImage(imageModel.getPosFilter());

        setAlpha(imageModel.getOpacity());

        if (!imageModel.getPathShape().equals("")) setShadowPathShape(imageModel.getPathShape());

        if (imageModel.getShadowModel() != null) setShadow(imageModel.getShadowModel());
    }

    private void initOverlay() {
        this.drawable = new BitmapDrawable(context.getResources(),
                Utils.getBitmapFromAsset(context, overlayModel.getNameFolder(), overlayModel.getNameOverlay(),
                        false, false));

        realBounds = new RectF(distance, distance, getWidth() - distance, getHeight() - distance);
    }

    private void initDecor() {
        if (this.drawable == null)
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text);

        setDataDecor(this.decorModel);
    }

    private void setDataDecor(DecorModel decor) {
        if (paintDecor == null) paintDecor = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (pathDecor == null) pathDecor = new Path();

        if (!decor.getLstPathData().isEmpty())
            for (String path : decor.getLstPathData()) {
                pathDecor.addPath(PathParser.createPathFromPathData(path));
            }
        scalePathDecor();
    }

    private void scalePathDecor() {
        if (rectFDecor == null) rectFDecor = new RectF();
        this.pathDecor.computeBounds(rectFDecor, true);

        scaleX = (getWidth() - 2f * distance) / rectFDecor.width();
        scaleY = (getHeight() - 2f * distance) / rectFDecor.height();
        Log.d("2tdp", "scalePathDecor: " + scaleX + "..." + scaleY);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setSize( (int) (rectFDecor.width() + scaleX), (int) (rectFDecor.height() * scaleY));
        drawable.setColor(Color.TRANSPARENT);
        setDrawable(drawable);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        if (isShadowImage && this.typeSticker.equals(Utils.IMAGE)) {
            if (isShadowCrop) {
                canvas.save();
                canvas.concat(getMatrix());
                canvas.translate(distance, distance);
                canvas.scale(scaleX, scaleY);
                canvas.drawPath(shadowPath, shadowPaint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.concat(getMatrix());
                canvas.drawRect(realBounds, shadowPaint);
                canvas.restore();
            }
        }

        if (this.typeSticker.equals(Utils.IMAGE)) {
            canvas.save();
            canvas.concat(getMatrix());
            canvas.drawBitmap(bitmap, null, realBounds, paintBitmap);
            canvas.restore();
        } else if (this.typeSticker.equals(Utils.DECOR)) {
            canvas.save();
            canvas.concat(getMatrix());

            canvas.scale(scaleX, scaleY);
            canvas.drawPath(pathDecor, paintDecor);
            canvas.restore();
        }

        canvas.save();
        canvas.concat(getMatrix());

        if (!this.typeSticker.equals(Utils.DECOR))
            drawable.setBounds((int) realBounds.left, (int) realBounds.top, (int) realBounds.right, (int) realBounds.bottom);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        if (typeSticker.equals(Utils.IMAGE)) return bitmap.getWidth();
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        if (typeSticker.equals(Utils.IMAGE)) return bitmap.getHeight();
        return drawable.getIntrinsicHeight();
    }

    @Override
    public DrawableStickerCustom setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        realBounds = new RectF(5, 5, drawable.getIntrinsicWidth() - 5, drawable.getIntrinsicHeight() - 5);
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
        if (!typeSticker.equals(Utils.IMAGE))
            drawable.setAlpha(alpha);
        else paintBitmap.setAlpha(alpha);
        if (shadowPaint != null) shadowPaint.setAlpha(alpha);
        return this;
    }

    public void setFilterImage(int positionFilter) {
        bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, FilterImage.EFFECT_CONFIGS[positionFilter], 0.8f);
    }

    public void setShadowPathShape(String pathShape) {
        if (this.rectFShadow == null) rectFShadow = new RectF();
        if (this.shadowPath == null) this.shadowPath = new Path();

        this.shadowPath.reset();
        this.shadowPath.addPath(PathParser.createPathFromPathData(pathShape));

        shadowPath.computeBounds(rectFShadow, true);

        scaleX = (getWidth() - 2f * distance) / rectFShadow.width();
        scaleY = (getHeight() - 2f * distance) / rectFShadow.height();
    }

    public void setShadow(ShadowModel shadow) {
        if (this.shadowPaint == null) this.shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setShadowImage(true);
        isShadowCrop = !imageModel.getPathShape().equals("");

        if (shadow != null) {
            if (shadow.getColorBlur() == 0f && shadow.getBlur() == 0f
                    && shadow.getXPos() == 0f && shadow.getYPos() == 0f) {
                this.shadowPaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        Color.TRANSPARENT);
            }
            if (shadow.getColorBlur() != 0f) {
                this.shadowPaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        shadow.getColorBlur());
            } else
                this.shadowPaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        Color.BLACK);
        }
    }

    public DecorModel getDecorModel() {
        return decorModel;
    }

    public void setDecorModel(DecorModel decorModel) {
        this.decorModel = decorModel;
        initDecor();
    }

    public OverlayModel getOverlayModel() {
        return overlayModel;
    }

    public void setOverlayModel(OverlayModel overlayModel) {
        this.overlayModel = overlayModel;
        initOverlay();
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
