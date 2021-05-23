package com.SE114PMCL.chatMessenger;

public class FriendData {
    private String UID;
    private String tenUser;
    private String lastMessage;
    private Boolean activeStatus;
    private int avatar;

    public FriendData(String UID,String tenUser, int avatar,String lastMessage,Boolean activeStatus) {
        this.UID=UID;
        this.tenUser = tenUser;
        this.lastMessage=lastMessage;
        this.activeStatus=activeStatus;
        this.avatar = avatar;
    }
    public String getUID() {
        return UID;
    }

    public String getTenUser() {
        return tenUser;
    }

    public void setTenUser(String tenUser) {
        this.tenUser = tenUser;
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

    public void setActiveStatus(Boolean status) {
        this.activeStatus= status;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
