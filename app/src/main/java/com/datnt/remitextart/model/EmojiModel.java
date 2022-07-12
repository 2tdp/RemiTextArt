package com.datnt.remitextart.model;

import android.content.Context;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.datnt.remitextart.custom.EditSticker;
import com.datnt.remitextart.customview.stickerview.Sticker;

import java.io.Serializable;

public class EmojiModel extends EditSticker implements Serializable {

    private String nameEmoji;
    private String folder;
    private int opacity;
    private boolean flipX;
    private boolean flipY;
    private boolean isSelected;
    private Matrix matrix;

    public EmojiModel(EmojiModel emojiModel) {
        this.nameEmoji = emojiModel.getNameEmoji();
        this.folder = emojiModel.getFolder();
        this.opacity = emojiModel.getOpacity();
        this.flipX = emojiModel.isFlipX();
        this.flipY = emojiModel.isFlipY();
        this.isSelected = emojiModel.isSelected;
        this.matrix = emojiModel.getMatrix();
    }

    public EmojiModel(String nameEmoji, String folder, int opacity,
                      boolean flipX, boolean flipY, boolean isSelected, Matrix matrix) {
        this.nameEmoji = nameEmoji;
        this.folder = folder;
        this.opacity = opacity;
        this.flipX = flipX;
        this.flipY = flipY;
        this.isSelected = isSelected;
        this.matrix = matrix;
    }

    public String getNameEmoji() {
        return nameEmoji;
    }

    public void setNameEmoji(String nameEmoji) {
        this.nameEmoji = nameEmoji;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
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
