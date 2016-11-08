package com.shah0150algonquinlive.doorsopenottawa.model;

/**
 * Created by adeshshah on 2016-11-08.
 */

public class Building {
    private int buildingId;
    private String name;
    private String address;
    private String image;

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
}
