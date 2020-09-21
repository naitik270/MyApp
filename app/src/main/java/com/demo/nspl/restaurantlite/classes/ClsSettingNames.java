package com.demo.nspl.restaurantlite.classes;

public class ClsSettingNames {
    private int Images;
    private String SettingName;


    private String Description;

    public ClsSettingNames() {

    }

    public ClsSettingNames(int images, String settingName, String Description) {
        this.Images = images;
        this.SettingName = settingName;
        this.Description = Description;
    }

    public ClsSettingNames(int images, String settingName) {
        this.Images = images;
        this.SettingName = settingName;
    }

    public int getImages() {
        return Images;
    }

    public void setImages(int images) {
        Images = images;
    }

    public String getSettingName() {
        return SettingName;
    }

    public void setSettingName(String settingName) {
        SettingName = settingName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
