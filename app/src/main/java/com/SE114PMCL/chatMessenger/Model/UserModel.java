package com.SE114PMCL.chatMessenger.Model;

public class UserModel {
    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String timkiem;

    public UserModel(String id, String username, String imageURL, String status, String timkiem) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.timkiem = timkiem;
    }

    public UserModel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimkiem() {
        return timkiem;
    }

    public void setTimkiem(String timkiem) {
        this.timkiem = timkiem;
    }
}
