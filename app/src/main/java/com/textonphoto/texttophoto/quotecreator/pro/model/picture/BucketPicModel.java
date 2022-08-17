package com.textonphoto.texttophoto.quotecreator.pro.model.picture;

import java.io.Serializable;
import java.util.ArrayList;

public class BucketPicModel implements Serializable {
    private ArrayList<PicModel> lstPic;
    private String bucket;

    public BucketPicModel(ArrayList<PicModel> lstPic, String bucket) {
        this.lstPic = lstPic;
        this.bucket = bucket;
    }

    public ArrayList<PicModel> getLstPic() {
        return lstPic;
    }

    public void setLstPic(ArrayList<PicModel> lstPic) {
        this.lstPic = lstPic;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
