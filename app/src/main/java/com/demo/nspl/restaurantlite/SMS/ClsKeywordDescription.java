package com.demo.nspl.restaurantlite.SMS;

public class ClsKeywordDescription {
    String keyword = "", Description = "";

    public ClsKeywordDescription(String keyword, String Description) {
        this.keyword = keyword;
        this.Description = Description;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
