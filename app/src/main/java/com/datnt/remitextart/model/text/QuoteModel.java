package com.datnt.remitextart.model.text;

import java.io.Serializable;
import java.util.ArrayList;

public class QuoteModel implements Serializable {
    private ArrayList<String> lstQuote;
    private String typeQuote;
    private boolean isSelected;

    public QuoteModel(ArrayList<String> lstQuote, String typeQuote, boolean isSelected) {
        this.lstQuote = lstQuote;
        this.typeQuote = typeQuote;
        this.isSelected = isSelected;
    }

    public ArrayList<String> getLstQuote() {
        return lstQuote;
    }

    public void setLstQuote(ArrayList<String> lstQuote) {
        this.lstQuote = lstQuote;
    }

    public String getTypeQuote() {
        return typeQuote;
    }

    public void setTypeQuote(String typeQuote) {
        this.typeQuote = typeQuote;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
