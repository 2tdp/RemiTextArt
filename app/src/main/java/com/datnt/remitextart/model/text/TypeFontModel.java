package com.datnt.remitextart.model.text;

import java.io.Serializable;

public class TypeFontModel implements Serializable {

    private String name;
    private String font;
    private boolean isSelected;

    public TypeFontModel(String name, String font, boolean isSelected) {
        this.name = name;
        this.font = font;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
