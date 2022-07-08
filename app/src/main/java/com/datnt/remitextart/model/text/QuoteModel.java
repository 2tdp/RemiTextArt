package com.datnt.remitextart.model.text;

import java.io.Serializable;

public class QuoteModel implements Serializable {
    private String content;
    private String typeQuote;

    public QuoteModel(String content, String typeQuote) {
        this.content = content;
        this.typeQuote = typeQuote;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeQuote() {
        return typeQuote;
    }

    public void setTypeQuote(String typeQuote) {
        this.typeQuote = typeQuote;
    }
}
