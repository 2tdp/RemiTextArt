package com.datnt.remitextart.model;

import com.datnt.remitextart.customview.stickerview.Sticker;

import java.io.Serializable;

public class LayerModel implements Serializable {

    private Sticker sticker;
    private boolean isLook;
    private boolean isLock;
    private boolean isSelected;

    public LayerModel(Sticker sticker, boolean isLook, boolean isLock, boolean isSelected) {
        this.sticker = sticker;
        this.isLook = isLook;
        this.isLock = isLock;
        this.isSelected = isSelected;
    }

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    public boolean isLook() {
        return isLook;
    }

    public void setLook(boolean look) {
        isLook = look;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
