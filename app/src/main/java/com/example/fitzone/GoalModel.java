package com.example.fitzone;

public class GoalModel {
    private String goal;

    public GoalModel() {
        // Default constructor required for Firestore
    }

    public GoalModel(String goal) {
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
