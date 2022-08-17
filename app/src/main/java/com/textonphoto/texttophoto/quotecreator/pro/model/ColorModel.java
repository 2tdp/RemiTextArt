package com.textonphoto.texttophoto.quotecreator.pro.model;

import java.io.Serializable;

public class ColorModel implements Serializable {

    private int colorStart;
    private int colorEnd;
    private int direc;
    private boolean isCheck;

    public ColorModel() {
    }

    public ColorModel(int colorStart, int colorEnd, int direc, boolean isCheck) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.direc = direc;
        this.isCheck = isCheck;
    }

    public int getColorStart() {
        return colorStart;
    }

    public void setColorStart(int colorStart) {
        this.colorStart = colorStart;
    }

    public int getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(int colorEnd) {
        this.colorEnd = colorEnd;
    }

    public int getDirec() {
        return direc;
    }

    public void setDirec(int direc) {
        this.direc = direc;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
