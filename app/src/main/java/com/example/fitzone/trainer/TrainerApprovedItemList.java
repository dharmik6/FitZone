package com.example.fitzone.trainer;

public class TrainerApprovedItemList {
    private String tname, timage,review;
    private String temail, tnumber, tgender, tboi, taddress,specialization, experience, tjoidate;

    public TrainerApprovedItemList(String tname, String experience, String timage,String specialization,String review) {
        this.tname = tname;
        this.experience =experience;
        this.timage=timage;
        this.review=review;
        this.specialization=specialization;
    }

    public String getTname() {return tname;}
    public String getReview() {return review;}
    public String getTimage() {return timage;}
    public String getSpecialization() {return specialization;}
    public String getTemail() {return temail;}
    public String getTnumber() {return tnumber;}
    public String getTgender() {return tgender;}
    public String getTboi() {return tboi;}
    public String getTaddress() {return taddress;}
    public String getExperience() {return experience;}
    public String getTjoidate() {return tjoidate;}

}
