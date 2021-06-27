package com.SE114PMCL.chatMessenger.Model;

public class UserModel {
    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String lastMessage;

    public UserModel(String id, String username, String imageURL, String status, String lastMessage) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.lastMessage = lastMessage;
    }

    public UserModel(){

    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
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

}
