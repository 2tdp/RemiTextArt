package com.textonphoto.texttophoto.quotecreator.pro.model.background;

import com.textonphoto.texttophoto.quotecreator.pro.model.ColorModel;
import com.textonphoto.texttophoto.quotecreator.pro.model.OverlayModel;

import java.io.Serializable;

public class BackgroundModel implements Serializable {

    private String uriCache;
    private String uriOverlay = "";
    private String uriRoot;
    private String uriOverlayRoot = "";
    private ColorModel colorModel;
    private int sizeViewColor;
    private AdjustModel adjustModel;
    private OverlayModel overlayModel;
    private int positionFilterBackground = 0;
    private int opacity = 100;

    public BackgroundModel() {
    }

    public BackgroundModel(String uriCache, String uriOverlay, String uriRoot, String uriOverlayRoot, ColorModel colorModel,
                           int sizeViewColor, AdjustModel adjustModel, OverlayModel overlayModel,
                           int positionFilterBackground, int opacity) {
        this.uriCache = uriCache;
        this.uriOverlay = uriOverlay;
        this.uriRoot = uriRoot;
        this.uriOverlayRoot = uriOverlayRoot;
        this.colorModel = colorModel;
        this.adjustModel = adjustModel;
        this.overlayModel = overlayModel;
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

    public String getUriOverlayRoot() {
        return uriOverlayRoot;
    }

    public void setUriOverlayRoot(String uriOverlayRoot) {
        this.uriOverlayRoot = uriOverlayRoot;
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

    public OverlayModel getOverlayModel() {
        return overlayModel;
    }

    public void setOverlayModel(OverlayModel overlayModel) {
        this.overlayModel = overlayModel;
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
