package com.datnt.remitextart.customsticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
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
import com.datnt.remitextart.model.ColorModel;
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

    private EmojiModel emojiModel;
    private ImageModel imageModel;
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

    private void initDecor() {
        if (this.drawable == null)
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text);

        setDataDecor(this.decorModel);
    }

    private void setDataDecor(DecorModel decor) {
        if (paintDecor == null) paintDecor = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (pathDecor == null) pathDecor = new Path();

        if (!decor.getLstPathData().isEmpty()) {
            pathDecor.reset();
            for (String path : decor.getLstPathData()) {
                pathDecor.addPath(PathParser.createPathFromPathData(path));
            }
            scalePathDecor();
        }

        if (decor.getColorModel() != null) setColor(decor.getColorModel());
    }

    private void scalePathDecor() {
        if (rectFDecor == null) rectFDecor = new RectF();
        this.pathDecor.computeBounds(rectFDecor, true);

        float maxRect = Math.max(rectFDecor.width(), rectFDecor.height());
        float scale = (getWidth() - 2f * distance) / maxRect;
        Matrix matrix = new Matrix();
        matrix.preScale(scale, scale);
        pathDecor.transform(matrix);

        pathDecor.computeBounds(rectFDecor, true);

        createDrawable();
    }

    private void createDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        if (typeSticker.equals(Utils.DECOR)) {
            drawable.setSize((int) rectFDecor.width(), (int) rectFDecor.height());
            drawable.setColor(Color.TRANSPARENT);
            drawable.setBounds(distance, distance, (int) rectFDecor.right - distance, (int) rectFDecor.bottom - distance);
        }
        setDrawable(drawable);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (isShadowImage && (this.typeSticker.equals(Utils.IMAGE) || this.typeSticker.equals(Utils.DECOR))) {
            if (isShadowCrop || this.typeSticker.equals(Utils.DECOR)) {
                canvas.save();
                canvas.concat(getMatrix());
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

            canvas.translate((int) realBounds.left, (int) realBounds.top);
            canvas.drawPath(pathDecor, paintDecor);
            canvas.restore();
        }

        canvas.save();
        canvas.concat(getMatrix());

        if (!typeSticker.equals(Utils.DECOR) && !isShadowImage)
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

    public void setColor(ColorModel color) {
        this.decorModel.setColorModel(color);
        if (typeSticker.equals(Utils.DECOR)) {
            if (color.getColorStart() == color.getColorEnd()) {
                paintDecor.setShader(null);
                paintDecor.setColor(color.getColorStart());
            } else if (color.getDirec() == 4) {
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

            paintDecor.setShader(shader);
        }
    }

    private int[] setDirection(int direc) {
        float w = getWidth();
        float h = getHeight();
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
        return new int[]{};
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

        float maxScreen = Math.max(realBounds.width(), realBounds.height());
        float max = Math.max(rectFShadow.width(), rectFShadow.height());
        float scale = maxScreen / max;

        Matrix matrix = new Matrix();
        matrix.preTranslate(realBounds.left, realBounds.top);
        matrix.preScale(scale, scale);
        shadowPath.transform(matrix);

        shadowPath.computeBounds(rectFShadow, true);
    }

    public void setShadow(ShadowModel shadow) {
        if (this.shadowPaint == null) this.shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setShadowImage(true);
        if (typeSticker.equals(Utils.IMAGE))
            isShadowCrop = !imageModel.getPathShape().equals("");

        if (shadow != null) {
            if (shadow.getColorBlur() == 0 && shadow.getBlur() == 0f
                    && shadow.getXPos() == 0f && shadow.getYPos() == 0f) {
                this.shadowPaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        Color.TRANSPARENT);
                this.shadowPaint.setColor(Color.TRANSPARENT);
            } else if (shadow.getColorBlur() != 0) {
                this.shadowPaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        shadow.getColorBlur());
            } else if (shadow.getColorBlur() == 0 && (shadow.getBlur() != 0f
                    || shadow.getXPos() != 0f || shadow.getYPos() != 0f)) {
                this.shadowPaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        Color.BLACK);
            }
        }
    }

    public DecorModel getDecorModel() {
        return decorModel;
    }

    public void setDecorModel(DecorModel decorModel) {
        this.decorModel = decorModel;
        initDecor();
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
