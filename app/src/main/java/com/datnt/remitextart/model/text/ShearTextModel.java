package com.datnt.remitextart.model.text;

import java.io.Serializable;

public class ShearTextModel implements Serializable {

    private float shearX;
    private float shearY;
    private float stretch;

    public ShearTextModel(float shearX, float shearY, float stretch) {
        this.shearX = shearX;
        this.shearY = shearY;
        this.stretch = stretch;
    }

    public float getShearX() {
        return shearX;
    }

    public void setShearX(float shearX) {
        this.shearX = shearX;
    }

    public float getShearY() {
        return shearY;
    }

    public void setShearY(float shearY) {
        this.shearY = shearY;
    }

    public float getStretch() {
        return stretch;
    }

    public void setStretch(float s) {
        stretch = s;
    }
}
