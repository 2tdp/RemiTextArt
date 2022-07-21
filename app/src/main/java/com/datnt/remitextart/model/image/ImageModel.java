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

public class ImageModel extends EditSticker implements Serializable {

    private String uri;
    private String uriRoot;
    private String pathShape;
    private int posFilter;
    private ShadowModel shadowModel;
    private int opacity;
    private int posBlend;
    private Matrix matrix;

    public ImageModel(ImageModel imageModel) {
        this.uri = imageModel.getUri();
        this.uriRoot = imageModel.getUriRoot();
        this.pathShape = imageModel.getPathShape();
        this.posFilter = imageModel.getPosFilter();
        this.shadowModel = imageModel.getShadowModel();
        this.opacity = imageModel.getOpacity();
        this.posBlend = imageModel.getPosBlend();
        this.matrix = imageModel.getMatrix();
    }

    public ImageModel(String uri, String uriRoot, String pathShape, int posFilter, ShadowModel shadowModel, int opacity, int posBlend, Matrix matrix) {
        this.uri = uri;
        this.uriRoot = uriRoot;
        this.pathShape = pathShape;
        this.posFilter = posFilter;
        this.shadowModel = shadowModel;
        this.opacity = opacity;
        this.posBlend = posBlend;
        this.matrix = matrix;
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

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public Sticker duplicate(Context context, @NonNull int id) {
        ShadowModel shadow = null;
        if (shadowModel != null)
            shadow = new ShadowModel(shadowModel);
        ImageModel imageModel = new ImageModel(uri, uriRoot, pathShape, posFilter, shadow, opacity,
                posBlend, matrix);

        return new DrawableStickerCustom(context, imageModel, id, Utils.IMAGE);
    }

    @Override
    public Sticker shadow(Context context, @NonNull Sticker sticker) {
        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            drawableSticker.setShadowPathShape(drawableSticker.getImageModel().getPathShape());
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
