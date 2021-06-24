package com.SE114PMCL.chatMessenger.Model;

public class FriendData {
    private String UID;
    private String username;
    private String lastMessage;
    private Boolean activeStatus;
    private int avatar;
    private String imageURL;

    public FriendData(String UID,String username, int avatar,String lastMessage,Boolean activeStatus, String imageURL) {
        this.UID=UID;
        this.username = username;
        this.avatar = avatar;
        this.lastMessage=lastMessage;
        this.activeStatus=activeStatus;
        this.imageURL = imageURL;
    }

    public FriendData(){

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
