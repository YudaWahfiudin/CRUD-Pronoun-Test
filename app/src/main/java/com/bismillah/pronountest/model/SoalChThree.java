package com.bismillah.pronountest.model;

public class SoalChThree {
    private String id;
    private String soalText;
    private String imageUrl;
    private String key;

    public SoalChThree() {}

    public SoalChThree(String id, String soalText) {
        this.id = id;
        this.soalText = soalText;
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
}
