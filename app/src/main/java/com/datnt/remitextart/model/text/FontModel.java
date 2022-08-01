package com.datnt.remitextart.model.text;

import java.io.Serializable;
import java.util.ArrayList;

public class FontModel implements Serializable {
    private String nameFont;
    private ArrayList<TypeFontModel> lstType;
    private boolean isSelected;
    private boolean isFavorite;

    public FontModel(String nameFont, ArrayList<TypeFontModel> lstType, boolean isSelected, boolean isFavorite) {
        this.nameFont = nameFont;
        this.lstType = lstType;
        this.isSelected = isSelected;
        this.isFavorite = isFavorite;
    }

    public String getNameFont() {
        return nameFont;
    }

    public void setNameFont(String nameFont) {
        this.nameFont = nameFont;
    }

    public ArrayList<TypeFontModel> getLstType() {
        return lstType;
    }

    public void setLstType(ArrayList<TypeFontModel> lstType) {
        this.lstType = lstType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
