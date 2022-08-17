package com.textonphoto.texttophoto.quotecreator.pro.model;

import java.io.Serializable;

public class OverlayModel implements Serializable {
    private String nameOverlay;
    private String nameFolder;
    private int opacity;
    private boolean flipX;
    private boolean flipY;
    private boolean isSelected;

    public OverlayModel(OverlayModel overlayModel) {
        this.nameOverlay = overlayModel.getNameOverlay();
        this.nameFolder = overlayModel.getNameFolder();
        this.opacity = overlayModel.getOpacity();
        this.flipX = overlayModel.isFlipX();
        this.flipY = overlayModel.isFlipY();
        this.isSelected = overlayModel.isSelected();
    }

    public OverlayModel(String nameOverlay, String nameFolder, int opacity, boolean flipX,
                        boolean flipY, boolean isSelected) {
        this.nameOverlay = nameOverlay;
        this.nameFolder = nameFolder;
        this.opacity = opacity;
        this.flipX = flipX;
        this.flipY = flipY;
        this.isSelected = isSelected;
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
}
