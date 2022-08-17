package com.textonphoto.texttophoto.quotecreator.pro.model.background;

import java.io.Serializable;

public class AdjustModel implements Serializable {

    private float brightness;
    private float contrast;
    private float exposure;
    private float highlight;
    private float shadows;
    private float blacks;
    private float whites;
    private float saturation;
    private float hue;
    private float warmth;
    private float vibrance;
    private float vignette;

    public AdjustModel() {
    }

    public AdjustModel(float brightness, float contrast, float exposure, float highlight,
                       float shadows, float blacks, float whites, float saturation, float hue,
                       float warmth, float vibrance, float vignette) {
        this.brightness = brightness;
        this.contrast = contrast;
        this.exposure = exposure;
        this.highlight = highlight;
        this.shadows = shadows;
        this.blacks = blacks;
        this.whites = whites;
        this.saturation = saturation;
        this.hue = hue;
        this.warmth = warmth;
        this.vibrance = vibrance;
        this.vignette = vignette;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public float getExposure() {
        return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
    }

    public float getHighlight() {
        return highlight;
    }

    public void setHighlight(float highlight) {
        this.highlight = highlight;
    }

    public float getShadows() {
        return shadows;
    }

    public void setShadows(float shadows) {
        this.shadows = shadows;
    }

    public float getBlacks() {
        return blacks;
    }

    public void setBlacks(float blacks) {
        this.blacks = blacks;
    }

    public float getWhites() {
        return whites;
    }

    public void setWhites(float whites) {
        this.whites = whites;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public float getWarmth() {
        return warmth;
    }

    public void setWarmth(float warmth) {
        this.warmth = warmth;
    }

    public float getVibrance() {
        return vibrance;
    }

    public void setVibrance(float vibrance) {
        this.vibrance = vibrance;
    }

    public float getVignette() {
        return vignette;
    }

    public void setVignette(float vignette) {
        this.vignette = vignette;
    }
}
