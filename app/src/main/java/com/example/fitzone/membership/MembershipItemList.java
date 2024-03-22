package com.example.fitzone.membership;

public class MembershipItemList {
    String name ,time , status ,id ;
    public MembershipItemList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MembershipItemList(String name, String time, String status, String id) {
        this.name = name;
        this.time = time;
        this.status = status;
        this.id = id;
    }
}
