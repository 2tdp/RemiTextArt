package com.datnt.remitextart.model.background;

import com.datnt.remitextart.model.ColorModel;

import java.io.Serializable;

public class BackgroundModel implements Serializable {

    private String uriCache;
    private String uriRoot;
    private ColorModel colorModel;
    private int sizeViewColor;
    private AdjustModel adjustModel;
    private int positionFilterBackground = 0;
    private float opacity = 1f;

    public BackgroundModel() {
    }

    public BackgroundModel(String uriCache, String uriRoot, ColorModel colorModel, int sizeViewColor,
                           AdjustModel adjustModel, int positionFilterBackground, float opacity) {
        this.uriCache = uriCache;
        this.uriRoot = uriRoot;
        this.colorModel = colorModel;
        this.adjustModel = adjustModel;
        this.sizeViewColor = sizeViewColor;
        this.positionFilterBackground = positionFilterBackground;
        this.opacity = opacity;
    }

    public String getUriCache() {
        return uriCache;
    }

    public void setUriCache(String uriCache) {
        this.uriCache = uriCache;
    }

    public String getUriRoot() {
        return uriRoot;
    }

    public void setUriRoot(String uriRoot) {
        this.uriRoot = uriRoot;
    }

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    public AdjustModel getAdjustModel() {
        return adjustModel;
    }

    public void setAdjustModel(AdjustModel adjustModel) {
        this.adjustModel = adjustModel;
    }

    public int getSizeViewColor() {
        return sizeViewColor;
    }

    public void setSizeViewColor(int sizeViewColor) {
        this.sizeViewColor = sizeViewColor;
    }

    public int getPositionFilterBackground() {
        return positionFilterBackground;
    }

    public void setPositionFilterBackground(int positionFilterBackground) {
        this.positionFilterBackground = positionFilterBackground;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}
