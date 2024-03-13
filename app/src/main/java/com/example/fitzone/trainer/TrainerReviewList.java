package com.example.fitzone.trainer;

public class TrainerReviewList {
    private String rating, review,name;
    public TrainerReviewList(String review, String rating,String name) {
        this.review = review;
        this.rating =rating;
        this.name =name;

    }

    public String getReview() {return review;}
    public String getRating() {return rating;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
