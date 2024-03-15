package com.example.fitzone;

public class PackagesItemList {
    private String name;
    private String description;
    private String price;
    private String duration;

    public PackagesItemList() {
        // Empty constructor needed for Firestore
    }

    public PackagesItemList(String name, String price, String duration, String description) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    // Getters and setters for the properties
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
