package com.textonphoto.texttophoto.quotecreator.pro.model;

import android.content.Context;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.textonphoto.texttophoto.quotecreator.pro.customsticker.EditSticker;
import com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview.Sticker;

import java.io.Serializable;

public class OverlayModel extends EditSticker implements Serializable {
    private String nameOverlay;
    private String nameFolder;
    private int opacity;
    private boolean flipX;
    private boolean flipY;
    private boolean isSelected;
    private Matrix matrix;

    public OverlayModel(OverlayModel overlayModel) {
        this.nameOverlay = overlayModel.getNameOverlay();
        this.nameFolder = overlayModel.getNameFolder();
        this.opacity = overlayModel.getOpacity();
        this.flipX = overlayModel.isFlipX();
        this.flipY = overlayModel.isFlipY();
        this.isSelected = overlayModel.isSelected();
        this.matrix = overlayModel.getMatrix();
    }

    public OverlayModel(String nameOverlay, String nameFolder, int opacity, boolean flipX,
                        boolean flipY, boolean isSelected, Matrix matrix) {
        this.nameOverlay = nameOverlay;
        this.nameFolder = nameFolder;
        this.opacity = opacity;
        this.flipX = flipX;
        this.flipY = flipY;
        this.isSelected = isSelected;
        this.matrix = matrix;
    }

    public String getNameOverlay() {
        return nameOverlay;
    }

    public void setNameOverlay(String nameEmoji) {
        this.nameOverlay = nameEmoji;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
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
