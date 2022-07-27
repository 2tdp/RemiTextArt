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
import com.datnt.remitextart.model.TemplateModel;
import com.datnt.remitextart.model.image.ImageModel;
import com.datnt.remitextart.model.ShadowModel;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;
import com.datnt.remitextart.utils.UtilsBitmap;

import org.wysaid.nativePort.CGENativeLibrary;

import java.util.ArrayList;

public class DrawableStickerCustom extends Sticker {

    private final int distance = 10;

    private Context context;
    private Drawable drawable;
    private Bitmap bitmap;
    private RectF realBounds, rectFShadow, rectFDecor, rectFTemp;
    private Path shadowPath, pathDecor, pathTemp;
    private Paint shadowPaint, paintDecor, paintTemp;
    private final Paint paintBitmap = new Paint(Paint.FILTER_BITMAP_FLAG);
    private boolean isShadow, isShadowCrop;
    private int id;
    private final String typeSticker;

    private EmojiModel emojiModel;
    private ImageModel imageModel;
    private DecorModel decorModel;
    private TemplateModel templateModel;

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
            case Utils.TEMPLATE:
                this.templateModel = (TemplateModel) o;
                initTemplate();
                break;
        }
    }

    //Emoji
    private void initEmoji() {
        this.drawable = new BitmapDrawable(context.getResources(),
                UtilsBitmap.getBitmapFromAsset(context, emojiModel.getFolder(), emojiModel.getNameEmoji(),
                        true, false));

        setAlpha(this.emojiModel.getOpacity());

        realBounds = new RectF(distance, distance, getWidth() - distance, getHeight() - distance);
    }

    //Image
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

        if (!imageModel.getPathShape().equals("")) {
            ArrayList<String> lstPath = new ArrayList<>();
            lstPath.add(imageModel.getPathShape());
            setShadowPathShape(lstPath);
        }

        if (imageModel.getShadowModel() != null) setShadow(imageModel.getShadowModel());
    }

    //Decor
    private void initDecor() {
        if (this.drawable == null)
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text);

        if (paintDecor == null) paintDecor = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (pathDecor == null) pathDecor = new Path();

        if (!this.decorModel.getLstPathData().isEmpty()) {
            pathDecor.reset();
            for (String path : this.decorModel.getLstPathData()) {
                pathDecor.addPath(PathParser.createPathFromPathData(path));
            }
            scalePath();
        }

        if (this.decorModel.getColorModel() != null) setColor(this.decorModel.getColorModel());

        if (this.decorModel.getShadowModel() != null) {
            setShadowPathShape(this.decorModel.getLstPathData());
            setShadow(this.decorModel.getShadowModel());
        }

        setAlpha(this.decorModel.getOpacity());
    }

    //Template
    private void initTemplate() {
        if (this.drawable == null)
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text);

        if (paintTemp == null) {
            paintTemp = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintTemp.setColor(Color.WHITE);
        }
        if (pathTemp == null) pathTemp = new Path();

        if (!this.templateModel.getLstPathDataText().isEmpty()) {
            pathTemp.reset();
            for (String path : this.templateModel.getLstPathDataText()) {
                pathTemp.addPath(PathParser.createPathFromPathData(path));
            }
            scalePath();
        }

        if (this.templateModel.getColorModel() != null)
            setColor(this.templateModel.getColorModel());

        if (this.templateModel.getShadowModel() != null) {
            setShadowPathShape(this.templateModel.getLstPathDataText());
            setShadow(this.templateModel.getShadowModel());
        }
    }

    private void scalePath() {
        if (this.typeSticker.equals(Utils.DECOR)) {
            if (rectFDecor == null) rectFDecor = new RectF();
            this.pathDecor.computeBounds(rectFDecor, true);

            float maxRect = Math.max(rectFDecor.width(), rectFDecor.height());
            float scale = (getWidth() - 2f * distance) / maxRect;
            Matrix matrix = new Matrix();
            matrix.preScale(scale, scale);
            pathDecor.transform(matrix);

            pathDecor.computeBounds(rectFDecor, true);

            createDrawable();
        } else if (this.typeSticker.equals(Utils.TEMPLATE)) {
            if (rectFTemp == null) rectFTemp = new RectF();
            this.pathTemp.computeBounds(rectFTemp, true);

            float maxRect = Math.max(rectFTemp.width(), rectFTemp.height());
            float scale = (getWidth() - 2f * distance) / maxRect;
            Matrix matrix = new Matrix();
            matrix.preScale(scale, scale);
            pathTemp.transform(matrix);

            pathTemp.computeBounds(rectFTemp, true);

            createDrawable();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Matrix matrix = getMatrix();
        Log.d("2tdp", "draw: 2" + matrix);
        if (isShadow && (this.typeSticker.equals(Utils.IMAGE)
                || this.typeSticker.equals(Utils.DECOR)
                || this.typeSticker.equals(Utils.TEMPLATE))) {
            if (isShadowCrop) {
                canvas.save();
                canvas.concat(matrix);
                canvas.drawPath(shadowPath, shadowPaint);
                canvas.restore();
            } else if (this.typeSticker.equals(Utils.DECOR)) {
                canvas.save();
                canvas.concat(matrix);
                canvas.translate((int) realBounds.left, (int) realBounds.top);
                canvas.drawPath(pathDecor, shadowPaint);
                canvas.restore();
            } else if (this.typeSticker.equals(Utils.TEMPLATE)) {
                canvas.save();
                canvas.concat(matrix);
                canvas.translate((int) realBounds.left, (int) realBounds.top);
                canvas.drawPath(pathTemp, shadowPaint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.concat(matrix);
                canvas.drawRect(realBounds, shadowPaint);
                canvas.restore();
            }

            Log.d("2tdp", "draw: " + shadowPaint.getColor());
        }

        canvas.save();
        canvas.concat(matrix);
        switch (this.typeSticker) {
            case Utils.IMAGE:
                canvas.drawBitmap(bitmap, null, realBounds, paintBitmap);
                break;
            case Utils.DECOR:
                canvas.translate((int) realBounds.left, (int) realBounds.top);
                canvas.drawPath(pathDecor, paintDecor);
                break;
            case Utils.TEMPLATE:
                canvas.translate((int) realBounds.left, (int) realBounds.top);
                canvas.drawPath(pathTemp, paintTemp);
                break;
        }
        canvas.restore();

        canvas.save();
        canvas.concat(matrix);

        if (!typeSticker.equals(Utils.DECOR) && !isShadow)
            drawable.setBounds((int) realBounds.left, (int) realBounds.top, (int) realBounds.right, (int) realBounds.bottom);
        drawable.draw(canvas);
        canvas.restore();
    }

    private Matrix getMatrixSticker() {
        switch (this.typeSticker) {
            case Utils.EMOJI:
                return this.emojiModel.getMatrix();
            case Utils.IMAGE:
                return this.imageModel.getMatrix();
            case Utils.DECOR:
                return this.decorModel.getMatrix();
            case Utils.TEMPLATE:
                return this.templateModel.getMatrix();
            default:
                return getMatrix();
        }
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
        if (typeSticker.equals(Utils.DECOR)) paintDecor.setAlpha(alpha);
        if (typeSticker.equals(Utils.TEMPLATE)) paintTemp.setAlpha(alpha);
        if (shadowPaint != null) shadowPaint.setAlpha(alpha);
        return this;
    }

    public void setColor(ColorModel color) {
        if (this.typeSticker.equals(Utils.DECOR)) {
            this.decorModel.setColorModel(color);
            UtilsAdjust.setColor(color, paintDecor, getWidth(), getHeight());
        }
        if (typeSticker.equals(Utils.TEMPLATE)) {
            this.templateModel.setColorModel(color);
            UtilsAdjust.setColor(color, paintTemp, getWidth(), getHeight());
        }
    }

    public void setFilterImage(int positionFilter) {
        bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, FilterImage.EFFECT_CONFIGS[positionFilter], 0.8f);
    }

    public void setShadowPathShape(ArrayList<String> lstPath) {
        if (this.rectFShadow == null) rectFShadow = new RectF();
        if (this.shadowPath == null) this.shadowPath = new Path();

        this.shadowPath.reset();
        for (String path : lstPath) {
            this.shadowPath.addPath(PathParser.createPathFromPathData(path));
        }

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

        setShadow(true);
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
                this.shadowPaint.setColor(shadow.getColorBlur());
            } else if (shadow.getColorBlur() == 0 && (shadow.getBlur() != 0f
                    || shadow.getXPos() != 0f || shadow.getYPos() != 0f)) {
                this.shadowPaint.setShadowLayer(shadow.getBlur(), shadow.getXPos(), shadow.getYPos(),
                        Color.BLACK);
                this.shadowPaint.setColor(Color.BLACK);
            }
        }
    }

    public TemplateModel getTemplateModel() {
        return templateModel;
    }

    public void replaceTemp() {
        initTemplate();
    }

    public DecorModel getDecorModel() {
        return decorModel;
    }

    public void replaceDecor() {
        initDecor();
    }

    public void replaceImage() {
        initImage();
    }

    public boolean isShadow() {
        return isShadow;
    }

    public void setShadow(boolean isShadow) {
        this.isShadow = isShadow;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public EmojiModel getEmojiModel() {
        return emojiModel;
    }

    public void replaceEmoji() {
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

    private void createDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        if (this.typeSticker.equals(Utils.DECOR)) {
            drawable.setSize((int) rectFDecor.width() + 2 * distance, (int) rectFDecor.height() + 2 * distance);
            drawable.setColor(Color.TRANSPARENT);
            drawable.setBounds(distance, distance, (int) rectFDecor.right - distance, (int) rectFDecor.bottom - distance);
        } else if (this.typeSticker.equals(Utils.TEMPLATE)) {
            drawable.setSize((int) rectFTemp.width() + 2 * distance, (int) rectFTemp.height() + 2 * distance);
            drawable.setColor(Color.TRANSPARENT);
            drawable.setBounds(distance, distance, (int) rectFTemp.right - distance, (int) rectFTemp.bottom - distance);
        }
        setDrawable(drawable);
    }
}
