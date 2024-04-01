package com.example.fitzone;

public class WorkoutExercisesListItem {
    private String name;
    private String id,wid;
    private String body;
    private String imageUrl;
    private String w_image,w_name;

    public WorkoutExercisesListItem(String name, String body, String imageUrl, String id,String wid,String w_name,String w_image) {
        this.name = name;
        this.body = body;
        this.imageUrl = imageUrl;
        this.id = id;
        this.wid = wid;
        this.w_image = w_image;
        this.w_name = w_name;
    }

    public String getW_image() {
        return w_image;
    }

    public String getW_name() {
        return w_name;
    }

    // Getters and setters for the properties
    public String getName() {
        return name;
    }

    public String getWid() {
        return wid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
