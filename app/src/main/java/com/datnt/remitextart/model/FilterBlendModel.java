package com.datnt.remitextart.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class FilterBlendModel implements Serializable {
    private Bitmap bitmap;
    private String nameFilter;
    private String parameterFilter;
    private boolean isCheck;

    public FilterBlendModel(Bitmap bitmap, String nameFilter, String parameterFilter, boolean isCheck) {
        this.bitmap = bitmap;
        this.nameFilter = nameFilter;
        this.parameterFilter = parameterFilter;
        this.isCheck = isCheck;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public String getParameterFilter() {
        return parameterFilter;
    }

    public void setParameterFilter(String parameterFilter) {
        this.parameterFilter = parameterFilter;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
