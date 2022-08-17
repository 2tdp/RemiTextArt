package com.textonphoto.texttophoto.quotecreator.pro.model;

import com.textonphoto.texttophoto.quotecreator.pro.model.background.BackgroundModel;
import com.textonphoto.texttophoto.quotecreator.pro.model.image.ImageModel;
import com.textonphoto.texttophoto.quotecreator.pro.model.text.TextModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    private String uriThumb;
    private BackgroundModel backgroundModel;

    private ArrayList<TextModel> lstTextModel = new ArrayList<>();
    private ArrayList<EmojiModel> lstEmojiModel = new ArrayList<>();
    private ArrayList<ImageModel> lstImageModel = new ArrayList<>();
    private ArrayList<DecorModel> lstDecorModel = new ArrayList<>();
    private ArrayList<TemplateModel> lstTempModel = new ArrayList<>();

    public Project() {
    }

    public Project(Project project) {
        this.uriThumb = project.getUriThumb();
        this.backgroundModel = project.getBackgroundModel();
        this.lstTextModel = project.getLstTextModel();
        this.lstEmojiModel = project.getLstEmojiModel();
        this.lstImageModel = project.getLstImageModel();
        this.lstDecorModel = project.getLstDecorModel();
        this.lstTempModel = project.getLstTempModel();
    }

    public Project(String uriThumb, BackgroundModel backgroundModel, ArrayList<TextModel> lstTextModel,
                   ArrayList<EmojiModel> lstEmojiModel, ArrayList<ImageModel> lstImageModel,
                   ArrayList<DecorModel> lstDecorModel, ArrayList<TemplateModel> lstTempModel) {
        this.uriThumb = uriThumb;
        this.backgroundModel = backgroundModel;
        this.lstTextModel = lstTextModel;
        this.lstEmojiModel = lstEmojiModel;
        this.lstImageModel = lstImageModel;
        this.lstDecorModel = lstDecorModel;
        this.lstTempModel = lstTempModel;
    }

    public String getUriThumb() {
        return uriThumb;
    }

    public void setUriThumb(String uriThumb) {
        this.uriThumb = uriThumb;
    }

    public BackgroundModel getBackgroundModel() {
        return backgroundModel;
    }

    public void setBackgroundModel(BackgroundModel backgroundModel) {
        this.backgroundModel = backgroundModel;
    }

    public ArrayList<TextModel> getLstTextModel() {
        return lstTextModel;
    }

    public void setLstTextModel(ArrayList<TextModel> lstTextModel) {
        this.lstTextModel = lstTextModel;
    }

    public ArrayList<EmojiModel> getLstEmojiModel() {
        return lstEmojiModel;
    }

    public void setLstEmojiModel(ArrayList<EmojiModel> lstEmojiModel) {
        this.lstEmojiModel = lstEmojiModel;
    }

    public ArrayList<ImageModel> getLstImageModel() {
        return lstImageModel;
    }

    public void setLstImageModel(ArrayList<ImageModel> lstImageModel) {
        this.lstImageModel = lstImageModel;
    }

    public ArrayList<DecorModel> getLstDecorModel() {
        return lstDecorModel;
    }

    public void setLstDecorModel(ArrayList<DecorModel> lstDecorModel) {
        this.lstDecorModel = lstDecorModel;
    }

    public ArrayList<TemplateModel> getLstTempModel() {
        return lstTempModel;
    }

    public void setLstTempModel(ArrayList<TemplateModel> lstTempModel) {
        this.lstTempModel = lstTempModel;
    }
}
