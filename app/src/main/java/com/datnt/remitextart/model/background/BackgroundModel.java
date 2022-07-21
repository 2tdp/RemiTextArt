package com.datnt.remitextart.model.background;

import com.datnt.remitextart.model.ColorModel;

import java.io.Serializable;

public class BackgroundModel implements Serializable {

    private String uriCache;
    private String uriOverlay = "";
    private String uriRoot;
    private ColorModel colorModel;
    private int sizeViewColor;
    private AdjustModel adjustModel;
    private int positionFilterBackground = 0;
    private int opacity = 100;

    public BackgroundModel() {
    }

    public BackgroundModel(String uriCache, String uriOverlay, String uriRoot, ColorModel colorModel,
                           int sizeViewColor, AdjustModel adjustModel, int positionFilterBackground, int opacity) {
        this.uriCache = uriCache;
        this.uriOverlay = uriOverlay;
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

    public String getUriOverlay() {
        return uriOverlay;
    }

    public void setUriOverlay(String uriOverlay) {
        this.uriOverlay = uriOverlay;
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

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }
}
