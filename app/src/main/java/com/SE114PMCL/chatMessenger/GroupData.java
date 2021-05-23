package com.SE114PMCL.chatMessenger;

public class GroupData {
    private String tenGroup;
    private int avatar;

    public GroupData(String tenGroup, int avatar) {
        this.tenGroup = tenGroup;
        this.avatar = avatar;
    }

    public String getTenGroup() {
        return tenGroup;
    }

    public void setTenGroup(String tenGroup) {
        this.tenGroup = tenGroup;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
