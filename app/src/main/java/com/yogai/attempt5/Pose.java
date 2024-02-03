package com.yogai.attempt5;

public class Pose {
    private int imageResourceId;
    private String poseName;

    public Pose(int imageResource, String poseName) {
        imageResourceId = imageResource;
        this.poseName = poseName;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getPoseName() {
        return poseName;
    }
}
