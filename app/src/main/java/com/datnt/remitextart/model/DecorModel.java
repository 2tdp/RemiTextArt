package com.datnt.remitextart.model;


import android.content.Context;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.datnt.remitextart.customsticker.DrawableStickerCustom;
import com.datnt.remitextart.customsticker.EditSticker;
import com.datnt.remitextart.customsticker.TextStickerCustom;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.image.ImageModel;
import com.datnt.remitextart.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class DecorModel extends EditSticker implements Serializable {
    private String nameDecor;
    private String nameFolder;
    private ArrayList<String> lstPathData;
    private ColorModel colorModel;
    private ShadowModel shadowModel;
    private int opacity;
    private boolean flipX;
    private boolean flipY;
    private boolean isSelected;
    private Matrix matrix;

    public DecorModel(DecorModel decorModel) {
        this.nameDecor = decorModel.getNameDecor();
        this.nameFolder = decorModel.getNameFolder();
        this.lstPathData = decorModel.getLstPathData();
        this.colorModel = decorModel.getColorModel();
        this.shadowModel = decorModel.getShadowModel();
        this.opacity = decorModel.getOpacity();
        this.flipX = decorModel.isFlipX();
        this.flipY = decorModel.isFlipY();
        this.isSelected = decorModel.isSelected();
        this.matrix = decorModel.getMatrix();
    }

    public DecorModel(String nameDecor, String nameFolder, ArrayList<String> lstPathData,
                      ColorModel colorModel, ShadowModel shadowModel, int opacity, boolean flipX,
                      boolean flipY, boolean isSelected, Matrix matrix) {
        this.nameDecor = nameDecor;
        this.nameFolder = nameFolder;
        this.lstPathData = lstPathData;
        this.colorModel = colorModel;
        this.shadowModel = shadowModel;
        this.opacity = opacity;
        this.flipX = flipX;
        this.flipY = flipY;
        this.isSelected = isSelected;
        this.matrix = matrix;
    }

    public String getNameDecor() {
        return nameDecor;
    }

    public void setNameDecor(String nameDecor) {
        this.nameDecor = nameDecor;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public ArrayList<String> getLstPathData() {
        return lstPathData;
    }

    public void setLstPathData(ArrayList<String> lstPathData) {
        this.lstPathData = lstPathData;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public Sticker duplicate(Context context, @NonNull int id) {
        ShadowModel shadow = null;
        if (shadowModel != null)
            shadow = new ShadowModel(shadowModel);

        DecorModel decorModel = new DecorModel(nameDecor, nameFolder, lstPathData, colorModel, shadow,
                opacity, flipX, flipY, isSelected, matrix);

        return new DrawableStickerCustom(context, decorModel, id, Utils.DECOR);
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            for (String path : lstPathData) {
                drawableSticker.setShadowPathShape(path);
            }
            drawableSticker.setShadow(shadowModel);
            return sticker;
        }
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
