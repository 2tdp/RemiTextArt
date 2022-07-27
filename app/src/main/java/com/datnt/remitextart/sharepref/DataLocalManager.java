package com.datnt.remitextart.sharepref;

import android.content.Context;

import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.Project;
import com.datnt.remitextart.model.TemplateModel;
import com.datnt.remitextart.model.picture.BucketPicModel;
import com.datnt.remitextart.model.text.FontModel;
import com.datnt.remitextart.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataLocalManager {

    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance() {

        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setFirstInstall(String key, boolean isFirst) {
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(key, isFirst);
    }

    public static boolean getFirstInstall(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(key);
    }

    public static void setCheck(String key, boolean volumeOn) {
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(key, volumeOn);
    }

    public static boolean getCheck(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(key);
    }

    public static void setOption(String option, String key) {
        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, option);
    }

    public static String getOption(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
    }

    public static void setInt(int count, String key) {
        DataLocalManager.getInstance().mySharedPreferences.putIntWithKey(key, count);
    }

    public static int getInt(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getIntWithKey(key, -1);
    }

    public static void setListFont(ArrayList<FontModel> lstFont, String key) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstFont).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<FontModel> getListFont(String key) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        ArrayList<FontModel> lstFont = new ArrayList<>();

        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                lstFont.add(gson.fromJson(jsonObject.toString(), FontModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstFont;
    }

    public static void setColor(ColorModel color, String key) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        String json = "";
        if (color != null) {
            jsonObject = gson.toJsonTree(color).getAsJsonObject();
            json = jsonObject.toString();
        }

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ColorModel getColor(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        ColorModel color = null;

        Gson gson = new Gson();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            color = gson.fromJson(jsonObject.toString(), ColorModel.class);
        }

        return color;
    }

    public static void setTemp(TemplateModel temp, String key) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        String json = "";
        if (temp != null) {
            jsonObject = gson.toJsonTree(temp).getAsJsonObject();
            json = jsonObject.toString();
        }

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static TemplateModel getTemp(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        TemplateModel temp = null;

        Gson gson = new Gson();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            temp = gson.fromJson(jsonObject.toString(), TemplateModel.class);
        }

        return temp;
    }

    public static void setProject(Project project, String key) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        String json = "";
        if (project != null) {
            jsonObject = gson.toJsonTree(project).getAsJsonObject();
            json = jsonObject.toString();
        }

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static Project getProject(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        Project project = null;

        Gson gson = new Gson();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) project = gson.fromJson(jsonObject.toString(), Project.class);

        return project;
    }

    public static void setListProject(Context context, ArrayList<Project> lstProject, String name) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstProject).getAsJsonArray();
        String json = jsonArray.toString();

        Utils.writeToFileText(context, json, name);
//        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<Project> getListProject(Context context, String name) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        ArrayList<Project> lstProject = new ArrayList<>();

//        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        String strJson = Utils.readFromFile(context, name);
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                lstProject.add(gson.fromJson(jsonObject.toString(), Project.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstProject;
    }

    public static void setListBucket(ArrayList<BucketPicModel> lstBucket, String key) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstBucket).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<BucketPicModel> getListBucket(String key) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        ArrayList<BucketPicModel> lstBucket = new ArrayList<>();

        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                lstBucket.add(gson.fromJson(jsonObject.toString(), BucketPicModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstBucket;
    }
}
