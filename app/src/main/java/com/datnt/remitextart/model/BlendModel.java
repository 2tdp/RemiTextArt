package com.datnt.remitextart.model;

import android.graphics.Bitmap;

import com.datnt.remitextart.customsticker.imgpro.actions.Blend;

import java.io.Serializable;

public class BlendModel implements Serializable {
    private Bitmap bitmap;
    private String nameBlend;
    private String parameter;
    private boolean isCheck;

    public BlendModel(Bitmap bitmap, String nameBlend, String parameter, boolean isCheck) {
        this.bitmap = bitmap;
        this.nameBlend = nameBlend;
        this.parameter = parameter;
        this.isCheck = isCheck;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getNameBlend() {
        return nameBlend;
    }

    public void setNameBlend(String nameBlend) {
        this.nameBlend = nameBlend;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
