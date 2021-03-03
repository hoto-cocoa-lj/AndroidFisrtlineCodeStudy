package com.slq.r1.pojo;

import java.io.Serializable;

public class Fruit implements Serializable {
    int imgId;
    String name;

    public Fruit(int imgId, String name) {
        this.imgId = imgId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
