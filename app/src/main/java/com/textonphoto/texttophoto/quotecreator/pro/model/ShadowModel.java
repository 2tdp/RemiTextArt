package com.textonphoto.texttophoto.quotecreator.pro.model;

import java.io.Serializable;

public class ShadowModel implements Serializable {

    private float xPos;
    private float yPos;
    private float blur;
    private int colorBlur;

    public ShadowModel(ShadowModel shadowModel) {
        this.xPos = shadowModel.getXPos();
        this.yPos = shadowModel.getYPos();
        this.blur = shadowModel.getBlur();
        this.colorBlur = shadowModel.getColorBlur();
    }

    public ShadowModel(float xPos, float yPos, float blur, int colorBlur) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.blur = blur;
        this.colorBlur = colorBlur;
    }

    public float getXPos() {
        return xPos;
    }

    public void setXPos(float xPos) {
        this.xPos = xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public int getColorBlur() {
        return colorBlur;
    }

    public void setColorBlur(int colorBlur) {
        this.colorBlur = colorBlur;
    }
}
