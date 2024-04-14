package com.example.fitzone;

public class AchievementsListItem {
    private String name, description,imageUrl,cerid;

    public AchievementsListItem( String name,String imageUrl,String description,String cerid) {
        this.name =name;
        this.imageUrl =imageUrl;
        this.description =description;
        this.cerid =cerid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCerid() {
        return cerid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCerid(String cerid) {
        this.cerid = cerid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
