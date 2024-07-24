package com.bismillah.pronountest.model;

public class SoalChTwo {
    private String id;
    private String soalText;
    private String imageUrl;
    private String key;

    public SoalChTwo() {}

    public SoalChTwo(String id, String soalText, String imageUrl) {
        this.id = id;
        this.soalText = soalText;
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoalText() {
        return soalText;
    }

    public void setSoalText(String soalText) {
        this.soalText = soalText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
