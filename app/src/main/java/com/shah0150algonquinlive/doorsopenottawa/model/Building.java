package com.shah0150algonquinlive.doorsopenottawa.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by adeshshah on 2016-11-08.
 */

public class Building {
    private int buildingId;
    private String name;
    private String address;
    private String image;
    private List<String>  openHours;
    public List<String> getOpenHours() {
        return openHours;
    }
    private Bitmap bitmap;
    private int thumbnail;
    public void setOpenHours(List<String> openHours) {
        this.openHours = openHours;
    }


    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address + " Ottawa, Ontario";
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public int getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
