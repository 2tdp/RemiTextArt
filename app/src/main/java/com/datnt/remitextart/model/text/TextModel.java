package com.datnt.remitextart.model.text;

import android.content.Context;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.datnt.remitextart.customsticker.EditSticker;
import com.datnt.remitextart.customsticker.TextStickerCustom;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.ShadowModel;

import java.io.Serializable;

public class TextModel extends EditSticker implements Serializable {

    private String content;
    private QuoteModel quoteModel;
    private FontModel fontModel;
    private ColorModel colorModel;
    private ShadowModel shadowModel;
    private ShearTextModel shearTextModel;
    private int typeAlign;
    private boolean flipX, flipY;
    private int opacity;
    private Matrix matrix;

    public TextModel(String content, QuoteModel quoteModel, FontModel fontModel, ColorModel colorModel,
                     ShadowModel shadowModel, ShearTextModel shearTextModel,
                     int typeAlign, boolean flipX, boolean flipY, int opacity, Matrix matrix) {
        this.content = content;
        this.quoteModel = quoteModel;
        this.fontModel = fontModel;
        this.colorModel = colorModel;
        this.shadowModel = shadowModel;
        this.shearTextModel = shearTextModel;
        this.typeAlign = typeAlign;
        this.flipX = flipX;
        this.flipY = flipY;
        this.opacity = opacity;
        this.matrix = matrix;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuoteModel getQuoteModel() {
        return quoteModel;
    }

    public void setQuoteModel(QuoteModel quoteModel) {
        this.quoteModel = quoteModel;
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

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public Sticker duplicate(Context context, int id) {
        ShadowModel shadow = null;
        if (shadowModel != null)
            shadow = new ShadowModel(shadowModel);

        TextModel textModel = new TextModel(content, quoteModel, fontModel, colorModel, shadow,
                shearTextModel, typeAlign, flipX, flipY, opacity, matrix);

        return new TextStickerCustom(context, textModel, id);
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            textSticker.setShadow(shadowModel);
            return sticker;
        }
        return null;
    }

    @Override
    public Sticker opacity(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            textSticker.setAlpha((int) (opacity * 255 / 100f));
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
