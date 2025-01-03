package com.textonphoto.texttophoto.quotecreator.pro.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.textonphoto.texttophoto.quotecreator.pro.customsticker.DrawableStickerCustom;
import com.textonphoto.texttophoto.quotecreator.pro.customsticker.EditSticker;
import com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview.Sticker;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

import java.io.Serializable;

public class EmojiModel extends EditSticker implements Serializable {

    private String nameEmoji;
    private String folder;
    private int opacity;
    private boolean flipX;
    private boolean flipY;
    private boolean isSelected;
    private boolean isLock;
    private boolean isLook;
    private float[] matrix;

    public EmojiModel(EmojiModel emojiModel) {
        this.nameEmoji = emojiModel.getNameEmoji();
        this.folder = emojiModel.getFolder();
        this.opacity = emojiModel.getOpacity();
        this.flipX = emojiModel.isFlipX();
        this.flipY = emojiModel.isFlipY();
        this.isSelected = emojiModel.isSelected;
        this.isLock = emojiModel.isLock;
        this.isLook = emojiModel.isLook;
        this.matrix = emojiModel.getMatrix();
    }

    public EmojiModel(String nameEmoji, String folder, int opacity, boolean flipX, boolean flipY,
                      boolean isSelected, boolean isLock, boolean isLook, float[] matrix) {
        this.nameEmoji = nameEmoji;
        this.folder = folder;
        this.opacity = opacity;
        this.flipX = flipX;
        this.flipY = flipY;
        this.isSelected = isSelected;
        this.isLock = isLock;
        this.isLook = isLook;
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
    public Sticker duplicate(Context context, @NonNull int id) {
        EmojiModel emojiModel = new EmojiModel(nameEmoji, folder, opacity, flipX, flipY, isSelected, isLock, isLook, matrix);

        return new DrawableStickerCustom(context, emojiModel, id, Utils.EMOJI);
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
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
