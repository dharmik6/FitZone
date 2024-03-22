package com.example.fitzone;

public class ExercisesItemList {
    private String name;
    private String goal;
    private String image;
    private String wid;

    public ExercisesItemList() {
        // Empty constructor needed for Firestore
    }

    public ExercisesItemList(String name, String goal, String image,String wid) {
        this.name = name;
        this.goal = goal;
        this.image = image;
        this.wid = wid;
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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
