package com.textonphoto.texttophoto.quotecreator.pro.model.text;

import android.content.Context;

import androidx.annotation.NonNull;

import com.textonphoto.texttophoto.quotecreator.pro.customsticker.EditSticker;
import com.textonphoto.texttophoto.quotecreator.pro.customsticker.TextStickerCustom;
import com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview.Sticker;
import com.textonphoto.texttophoto.quotecreator.pro.model.ColorModel;
import com.textonphoto.texttophoto.quotecreator.pro.model.ShadowModel;

import java.io.Serializable;

public class TextModel extends EditSticker implements Serializable {

    private String content;
    private FontModel fontModel;
    private ColorModel colorModel;
    private ShadowModel shadowModel;
    private ShearTextModel shearTextModel;
    private int size;
    private int typeAlign;
    private boolean flipX, flipY;
    private int opacity;
    private boolean isLock;
    private boolean isLook;
    private float[] matrix;

    public TextModel() {
    }

    public TextModel(String content, FontModel fontModel, ColorModel colorModel, ShadowModel shadowModel,
                     ShearTextModel shearTextModel, int size, int typeAlign, boolean flipX, boolean flipY,
                     int opacity, boolean isLock, boolean isLook, float[] matrix) {
        this.content = content;
        this.fontModel = fontModel;
        this.colorModel = colorModel;
        this.shadowModel = shadowModel;
        this.shearTextModel = shearTextModel;
        this.size = size;
        this.typeAlign = typeAlign;
        this.flipX = flipX;
        this.flipY = flipY;
        this.opacity = opacity;
        this.isLock = isLock;
        this.isLook = isLook;
        this.matrix = matrix;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FontModel getFontModel() {
        return fontModel;
    }

    public void setFontModel(FontModel fontModel) {
        this.fontModel = fontModel;
    }

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    public ShadowModel getShadowModel() {
        return shadowModel;
    }

    public void setShadowModel(ShadowModel shadowModel) {
        this.shadowModel = shadowModel;
    }

    public ShearTextModel getShearTextModel() {
        return shearTextModel;
    }

    public void setShearTextModel(ShearTextModel shearTextModel) {
        this.shearTextModel = shearTextModel;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTypeAlign() {
        return typeAlign;
    }

    public void setTypeAlign(int typeAlign) {
        this.typeAlign = typeAlign;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public boolean isLook() {
        return isLook;
    }

    public void setLook(boolean look) {
        isLook = look;
    }

    public float[] getMatrix() {
        return matrix;
    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    @Override
    public Sticker duplicate(Context context, int id) {
        ShadowModel shadow = null;
        if (shadowModel != null)
            shadow = new ShadowModel(shadowModel);

        TextModel textModel = new TextModel(content, fontModel, colorModel, shadow,
                new ShearTextModel(0f, 0f, 0f), size,
                typeAlign, flipX, flipY, opacity, isLock, isLook, matrix);

        return new TextStickerCustom(context, textModel, id);
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            return textSticker.setShadow(shadowModel);
        }
        return null;
    }

    @Override
    public Sticker opacity(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            textSticker.setAlpha(opacity);
            return sticker;
        }
        return null;
    }

    @Override
    public Sticker flip(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            if (flipX) textSticker.setFlippedHorizontally(true);
            if (flipY) textSticker.setFlippedVertically(true);
            return sticker;
        }
        return null;
    }
}
