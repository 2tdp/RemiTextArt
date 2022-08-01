package com.datnt.remitextart.data.text;

import android.content.Context;
import android.util.Log;

import com.datnt.remitextart.model.text.QuoteModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class DataQuotes {

    public static ArrayList<QuoteModel> getListTitleQuote(Context context) {
        ArrayList<QuoteModel> lstQuotes = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("quotes/");
            for (String s : f) {
                lstQuotes.add(new QuoteModel(getListQuotes(context, s), s, !s.equals("quotes_love.json")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstQuotes;
    }

    public static ArrayList<String> getListQuotes(Context context, String nameQuotes) {
        String quote = "";
        try {
            InputStream stream = context.getAssets().open("quotes/" + nameQuotes);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            quote = new String(buffer);
        } catch (IOException ignored) {
        }
        if (!quote.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(quote, type);
        }
        return new ArrayList<>();
    }

    public static ArrayList<String> getListQuotesWithName(Context context, String nameQuotes) {
        String quote = "";
        try {
            InputStream stream = context.getAssets().open("quotes/quotes_" + nameQuotes + ".json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            quote = new String(buffer);
        } catch (IOException ignored) {
        }
        if (!quote.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(quote, type);
        }
        return new ArrayList<>();
    }
}
