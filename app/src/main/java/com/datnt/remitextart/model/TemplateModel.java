package com.datnt.remitextart.model;

import android.content.Context;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.datnt.remitextart.customsticker.EditSticker;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.background.BackgroundModel;

import java.io.Serializable;
import java.util.ArrayList;

public class TemplateModel extends EditSticker implements Serializable {
    private String text;
    private String background;
    private ArrayList<String> lstPathDataText;
    private ColorModel colorModel;
    private ShadowModel shadowModel;
    private int opacity;
    private boolean flipX;
    private boolean flipY;
    private Matrix matrix;

    public TemplateModel(String text, String background, ArrayList<String> lstPathDataText,
                         ColorModel colorModel, ShadowModel shadowModel, int opacity, boolean flipX, boolean flipY, Matrix matrix) {
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

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public Sticker duplicate(Context context, @NonNull int id) {
        return null;
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
        return null;
    }

    @Override
    public Sticker opacity(Context context, @NonNull Sticker sticker) {
        return null;
    }

    @Override
    public Sticker flip(Context context, @NonNull Sticker sticker) {
        return null;
    }
}
