package com.datnt.remitextart.model;

import android.graphics.Matrix;

import java.io.Serializable;
import java.util.ArrayList;

public class TemplateModel implements Serializable {
    private String name;
    private String background;
    private String text;
    private ArrayList<String> lstPathData;
    private ColorModel colorModel;
    private Matrix matrix;

    public TemplateModel(String name, String background, String text, ArrayList<String> lstPathData,
                         ColorModel colorModel, Matrix matrix) {
        this.name = name;
        this.background = background;
        this.text = text;
        this.lstPathData = lstPathData;
        this.colorModel = colorModel;
        this.matrix = matrix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getLstPathData() {
        return lstPathData;
    }

    public void setLstPathData(ArrayList<String> lstPathData) {
        this.lstPathData = lstPathData;
    }

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
