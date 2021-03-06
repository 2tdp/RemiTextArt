package com.datnt.remitextart.model;

import android.content.Context;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.datnt.remitextart.customsticker.DrawableStickerCustom;
import com.datnt.remitextart.customsticker.EditSticker;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.background.BackgroundModel;
import com.datnt.remitextart.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class TemplateModel extends EditSticker implements Serializable {
    private String text;
    private String background = "";
    private ArrayList<String> lstPathDataText;
    private ColorModel colorModel;
    private ShadowModel shadowModel;
    private int opacity;
    private boolean flipX;
    private boolean flipY;
    private float[] matrix;

    public TemplateModel() {
    }

    public TemplateModel(TemplateModel templateModel) {
        this.text = templateModel.getText();
        this.background = templateModel.getBackground();
        this.lstPathDataText = templateModel.getLstPathDataText();
        this.colorModel = templateModel.getColorModel();
        this.shadowModel = templateModel.getShadowModel();
        this.opacity = templateModel.getOpacity();
        this.flipX = templateModel.isFlipX();
        this.flipY = templateModel.isFlipY();
        this.matrix = templateModel.getMatrix();
    }

    public TemplateModel(String text, String background, ArrayList<String> lstPathDataText,
                         ColorModel colorModel, ShadowModel shadowModel, int opacity, boolean flipX, boolean flipY, float[] matrix) {
        this.text = text;
        this.background = background;
        this.lstPathDataText = lstPathDataText;
        this.colorModel = colorModel;
        this.shadowModel = shadowModel;
        this.opacity = opacity;
        this.flipX = flipX;
        this.flipY = flipY;
        this.matrix = matrix;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public ArrayList<String> getLstPathDataText() {
        return lstPathDataText;
    }

    public void setLstPathDataText(ArrayList<String> lstPathDataText) {
        this.lstPathDataText = lstPathDataText;
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

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
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

    public float[] getMatrix() {
        return matrix;
    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    @Override
    public Sticker duplicate(Context context, @NonNull int id) {
        ShadowModel shadow = null;
        if (shadowModel != null)
            shadow = new ShadowModel(shadowModel);

        TemplateModel templateModel = new TemplateModel(text, background, lstPathDataText, colorModel, shadow,
                opacity, flipX, flipY, matrix);

        return new DrawableStickerCustom(context, templateModel, id, Utils.TEMPLATE);
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            drawableSticker.setShadowPathShape(lstPathDataText);
            drawableSticker.setShadow(shadowModel);
            return sticker;
        }
        return null;
    }

    @Override
    public Sticker opacity(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            drawableSticker.setAlpha(opacity);
            return sticker;
        }
        return null;
    }

    @Override
    public Sticker flip(Context context, @NonNull Sticker sticker) {
        return null;
    }
}
