package com.datnt.remitextart.model.picture;

import java.io.Serializable;

public class PicModel implements Serializable {

    private String id;
    private String bucket;
    private String uri;
    private boolean isCheck;

    public PicModel(String id, String bucket, String uri, boolean isCheck) {
        this.id = id;
        this.bucket = bucket;
        this.uri = uri;
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public String getBucket() {
        return bucket;
    }

    public String getUri() {
        return uri;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
