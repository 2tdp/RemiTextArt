package com.datnt.remitextart.model.image;

import android.content.Context;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.datnt.remitextart.customsticker.DrawableStickerCustom;
import com.datnt.remitextart.customsticker.EditSticker;
import com.datnt.remitextart.customsticker.TextStickerCustom;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.ShadowModel;
import com.datnt.remitextart.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageModel extends EditSticker implements Serializable {

    private String idImage;
    private String uri;
    private String uriRoot;
    private String pathShape;
    private int quantity;
    private int posFilter;
    private ShadowModel shadowModel;
    private int opacity;
    private int posBlend;
    private boolean isLock;
    private boolean isLook;
    private float[] matrix;

    public ImageModel(ImageModel imageModel) {
        this.idImage = imageModel.getIdImage();
        this.uri = imageModel.getUri();
        this.uriRoot = imageModel.getUriRoot();
        this.pathShape = imageModel.getPathShape();
        this.quantity = imageModel.getQuantity();
        this.posFilter = imageModel.getPosFilter();
        this.shadowModel = imageModel.getShadowModel();
        this.opacity = imageModel.getOpacity();
        this.posBlend = imageModel.getPosBlend();
        this.isLock = imageModel.isLock();
        this.isLook = imageModel.isLook();
        this.matrix = imageModel.getMatrix();
    }

    public ImageModel(String idImage, String uri, String uriRoot, String pathShape, int quantity,
                      int posFilter, ShadowModel shadowModel, int opacity, int posBlend, boolean isLock,
                      boolean isLook, float[] matrix) {
        this.idImage = idImage;
        this.uri = uri;
        this.uriRoot = uriRoot;
        this.pathShape = pathShape;
        this.quantity = quantity;
        this.posFilter = posFilter;
        this.shadowModel = shadowModel;
        this.opacity = opacity;
        this.posBlend = posBlend;
        this.isLock = isLock;
        this.isLook = isLook;
        this.matrix = matrix;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUriRoot() {
        return uriRoot;
    }

    public void setUriRoot(String uriRoot) {
        this.uriRoot = uriRoot;
    }

    public String getPathShape() {
        return pathShape;
    }

    public void setPathShape(String pathShape) {
        this.pathShape = pathShape;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPosFilter() {
        return posFilter;
    }

    public void setPosFilter(int posFilter) {
        this.posFilter = posFilter;
    }

    public ShadowModel getShadowModel() {
        return shadowModel;
    }

    public void setShadowModel(ShadowModel shadowModel) {
        this.shadowModel = shadowModel;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getPosBlend() {
        return posBlend;
    }

    public void setPosBlend(int posBlend) {
        this.posBlend = posBlend;
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
        ShadowModel shadow = null;
        if (shadowModel != null)
            shadow = new ShadowModel(shadowModel);
        ImageModel imageModel = new ImageModel(idImage, uri, uriRoot, pathShape, quantity, posFilter,
                shadow, opacity, posBlend, isLock, isLook, matrix);

        return new DrawableStickerCustom(context, imageModel, id, Utils.IMAGE);
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
        ArrayList<String> lstPath = new ArrayList<>();
        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            lstPath.add(drawableSticker.getImageModel().getPathShape());
            drawableSticker.setShadowPathShape(lstPath);
            drawableSticker.setShadow(shadowModel);
            return sticker;
        }
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
